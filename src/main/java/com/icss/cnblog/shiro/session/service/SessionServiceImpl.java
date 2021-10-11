package com.icss.cnblog.shiro.session.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.shiro.session.UserOnline;
import com.icss.cnblog.utils.AddressUtils;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import com.icss.cnblog.utils.RequestUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-13 14:43
 **/
@Service
public class SessionServiceImpl implements SessionService{

    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private RedisSessionDAO redisSessionDAO;

    /**
     * 是否启用redis作为缓存
     */
    @Value("${shiro.cache.enable-redis}")
    private boolean enableRedis;

    private String kickoutAttrName = CommonConst.SESSION_ATTR_KICKOUT;


    @Override
    public PageUtils selectPageList(Query query) {
        return new PageUtils(selectList(query));
    }

    @Override
    public void kickout(Serializable sessionId) {
        Session session = getSession(sessionId);
        if(enableRedis) {
            //根据sessionId执行强制退出
            //session.setAttribute(kickoutAttrName, true);
            redisSessionDAO.delete(session);
        }else{
            //删除session
            sessionDAO.delete(session);
        }
    }


    /**
     * 查询数据列表
     * @param query
     * @return
     */
    private Page<UserOnline> selectList(Query query){
        // 因为我们是用redis实现了shiro的session的Dao,而且是采用了shiro+redis这个插件
        // 所以从spring容器中获取redisSessionDAO
        // 来获取session列表.
        Collection<Session> sessions = getSessions();
        Iterator<Session> it = sessions.iterator();
        List<UserOnline> onlineUserList = new ArrayList<UserOnline>();
        Page<UserOnline> pageList = query.getPagePlus();
        // 遍历session
        while (it.hasNext()) {
            // 这是shiro已经存入session的
            // 现在直接取就是了
            Session session = it.next();
            //标记为已提出的不加入在线列表
            if(session.getAttribute(kickoutAttrName)==null?false:true)continue;
            UserOnline onlineUser = getUserOnLinSession(session);
            if(onlineUser!=null){
                onlineUserList.add(onlineUser);
            }
        }
        // 再将List<UserOnlineBo>转换成mybatisPlus封装的page对象
        int size = onlineUserList.size();
        pageList.setRecords(onlineUserList);
        pageList.setTotal(size);
        return pageList;
    }

    /**
     * 获取在线Session列表
     * @return
     */
    private  Collection<Session> getSessions(){
        Collection<Session> sessions ;
        if(enableRedis) {
            sessions = redisSessionDAO.getActiveSessions();
        }else{
            sessions = sessionDAO.getActiveSessions();
        }
        return sessions;
    }

    /**
     * 获取Session
     * @param sessionId
     * @return
     */
    private Session getSession(Serializable sessionId){
        Session session ;
        if(enableRedis) {
            //redis
            session = redisSessionDAO.readSession(sessionId);
        }else{
            session = sessionDAO.readSession(sessionId);
        }
        return session;
    }

    /**
     * 从session 获取在线UserOnline
     * @param session
     * @return
     */
    private UserOnline getUserOnLinSession(Session session){
        //获取session登录信息。
        Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if(null == obj){
            return null;
        }
        UserAgent userAgent = UserAgent.parseUserAgentString(RequestUtil.getRequest().getHeader("User-Agent"));
        String ip = RequestUtil.getIp();
        String address = AddressUtils.getRealAddressByIP(ip);
        //确保是 SimplePrincipalCollection对象。
        if(obj instanceof SimplePrincipalCollection) {
            SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
            /**
             * 获取用户登录的，@link SampleRealm.doGetAuthenticationInfo(...)方法中
             * return new SimpleAuthenticationInfo(sysUser, password, getName());的user 对象。
             */
            obj = spc.getPrimaryPrincipal();
            if(null != obj && obj instanceof SysUserEntity) {
                //存储session + user 综合信息
                UserOnline online = new UserOnline((SysUserEntity) obj);
                //最后一次和系统交互的时间
                online.setLoginDate(session.getLastAccessTime());
                //主机的ip地址
                online.setHost(ip);
                //session ID
                online.setSessionId(session.getId().toString());
                //session最后一次与系统交互的时间
                online.setLastAccessTime(session.getLastAccessTime());
                //回话到期 ttl(ms)
                online.setTimeout(session.getTimeout());
                //session创建时间
                online.setStartTimestamp(session.getStartTimestamp());
                //是否踢出
                online.setSessionStatus(false);

                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                online.setOs(os);
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                online.setBrowser(browser);
                online.setLoginLocation(address);

                Session currSession = SecurityUtils.getSubject().getSession();
                if(session.getId().equals(currSession.getId())) {
                    online.setUserStatus("0");
                }else{
                    online.setUserStatus("1");
                }
                return online;
            }
        }
        return null;
    }
}
