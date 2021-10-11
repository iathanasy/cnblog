package com.icss.cnblog.config;

import com.icss.cnblog.service.SysMenuService;
import com.icss.cnblog.shiro.ShiroRealm;
import com.icss.cnblog.shiro.filter.KickoutSessionControlFilter;
import com.icss.cnblog.shiro.session.ShiroSessionManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: Mr.Wang
 * @create: 2020-02-06 11:49
 * @description:
 **/
@Configuration
public class ShiroConfig {

    @Autowired
    private SysMenuService menuService;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    /**
     * 是否启用redis作为缓存
     */
    @Value("${shiro.cache.enable-redis}")
    private boolean enableRedis;

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/sys/index.html");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403.html");


        // 添加自己的过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("logout", logoutFilter());
        filterMap.put("kickout", kickoutSessionControlFilter());

        shiroFilterFactoryBean.setFilters(filterMap);

        /**
         * 配置访问权限
         * - anon:所有url都都可以匿名访问
         * - authc: 需要认证才能进行访问（此处指所有非匿名的路径都需要登录才能访问）
         * - user：如果使用rememberMe的功能可以直接访问
         * - perms:该资源必须得到资源权限可以访问
         * - role:该资源必须得到角色权限才能访问
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //退出将会使用自定义的过滤器
        filterChainDefinitionMap.put("/logout", "logout");

        filterChainDefinitionMap.put("/gifCode.html", "anon");
        filterChainDefinitionMap.put("/login.html", "anon");
        filterChainDefinitionMap.put("/403.html", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");

        /*//加载数据中配置的资源权限列表
        List<SysMenuEntity> menuList = menuService.selectUrlAndPermsMenuList();
        if (CollectionUtils.isEmpty(menuList)) {
            throw new GlobalException("未加载到SysMenu资源");
        }
        for (SysMenuEntity menu: menuList) {
            if (!StringUtils.isEmpty(menu.getUrl()) && !StringUtils.isEmpty(menu.getPerms())){
                //filterChainDefinitionMap.put("/add", "perms[权限添加]");
                String permission = "perms[" + menu.getPerms() + "]";
                filterChainDefinitionMap.put(menu.getUrl(), permission);
            }
        }*/
        //拦截全部 放在最后
        filterChainDefinitionMap.put("/sys/**", "authc,kickout");
//        filterChainDefinitionMap.put("/sys/**", "anon");
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 安全管理器
     * @param authRealm
     * @return
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager(@Qualifier("shiroRealm") ShiroRealm authRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(authRealm);
        if(enableRedis){
            // redis缓存实现
            securityManager.setCacheManager(redisCacheManager());
        }else{
            // 自定义缓存实现
            securityManager.setCacheManager(cacheManager());
        }

        // 自定义session管理
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
     *
     * @return
     */
    @Bean("shiroRealm")
    public ShiroRealm shiroRealm(){
        ShiroRealm shiroRealm = new ShiroRealm();
        return shiroRealm;
    }

    /**
     * 退出过滤器
     */
    public LogoutFilter logoutFilter(){
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl("/login.html");
        return logoutFilter;
    }

    /**
     *  开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 限制同一账号登录同时登录人数控制
     * @return
     */
    public KickoutSessionControlFilter kickoutSessionControlFilter(){
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的redisManager()实现的cacheManager()缓存管理
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        if(enableRedis) {
            kickoutSessionControlFilter.setCacheManager(redisCacheManager());
        }else{
            kickoutSessionControlFilter.setCacheManager(cacheManager());
        }
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        kickoutSessionControlFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionControlFilter.setMaxSession(-1);
        //被踢出后重定向到的地址；
        kickoutSessionControlFilter.setKickoutUrl("/login.html?kickout");
        return kickoutSessionControlFilter;
    }


    //////////////////////////////////////ehcache/////////////////////////////////////////

    /**
     * ehcache缓存管理
     * @return
     */
    @Bean
    public EhCacheManager cacheManager(){
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }

    /**
     * shiro sessionDao层的实现
     * AbstractSessionDAO提供了SessionDAO的基础实现，如生成会话ID等；
     * CachingSessionDAO提供了对开发者透明的会话缓存的功能，只需要设置相应的CacheManager即可；
     * MemorySessionDAO直接在内存中进行会话维护；
     * EnterpriseCacheSessionDAO提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     *
     */
    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        return sessionDAO;
    }

    //////////////////////////////////////redis/////////////////////////////////////////

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        //设置一小时超时  单位是秒
        redisManager.setExpire(1800);// 配置缓存过期时间
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setTimeout(timeout);
        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     * @return org.crazycake.shiro.RedisCacheManager
     */
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * Session Manager
     */
    @Bean
    public ShiroSessionManager sessionManager() {
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        if(enableRedis){
            // 加入缓存管理器
            sessionManager.setCacheManager(redisCacheManager());
            sessionManager.setSessionDAO(redisSessionDAO());
        }else {
            // 加入缓存管理器
            sessionManager.setCacheManager(cacheManager());
            sessionManager.setSessionDAO(sessionDAO());
        }
        // 删除过期的session
        sessionManager.setDeleteInvalidSessions(true);

        // 设置全局session超时时间 默认30分钟
        sessionManager.setGlobalSessionTimeout(30 * 60 * 1000);

        // 去掉 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        // 是否定时检查session
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置Cookie名字，默认为JSESSIONID
        sessionManager.setSessionIdCookie(new SimpleCookie("shiroUserid"));

        return sessionManager;
    }

}
