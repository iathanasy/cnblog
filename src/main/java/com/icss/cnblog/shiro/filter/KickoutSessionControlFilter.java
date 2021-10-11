package com.icss.cnblog.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.utils.RequestUtil;
import com.icss.cnblog.utils.Result;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 1.读取当前登录用户名，获取在缓存中的sessionId队列
 * 2.判断队列的长度，大于最大登录限制的时候，按踢出规则
 *  将之前的sessionId中的session域中存入kickout：true，并更新队列缓存
 * 3.判断当前登录的session域中的kickout如果为true，
 * 想将其做退出登录处理，然后再重定向到踢出登录提示页面
 *
 * @description: 自定义filter 实现 并发登录控制
 * @author: Mr.Wang
 * @create: 2020-02-12 11:28
 **/
@Slf4j
@Data
public class KickoutSessionControlFilter extends AccessControlFilter {

    /** 踢出后到的地址 */
    private String kickoutUrl;

    /**  踢出之前登录的/之后登录的用户 默认踢出之前登录的用户 */
    private boolean kickoutAfter = false;

    private String kickoutAttrName = CommonConst.SESSION_ATTR_KICKOUT;

    /**  同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；*/
    private int maxSession = 1;
    /** 用于根据会话ID，获取会话进行踢出操作的**/
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    /**
     * 	设置Cache的key的前缀
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-kickout-session");
    }

    /**
     * 是否允许访问，返回true表示允许；
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {

        return false;
    }

    /**
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered() || maxSession == -1) {
            //如果没有登录 或用户最大会话数为-1，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        // /当前登录用户
        SysUserEntity user = (SysUserEntity) subject.getPrincipal();
        String username = user.getUserName();
        Serializable sessionId = session.getId();

        log.info("进入KickoutControl, sessionId:{}", sessionId);
        //TODO 同步控制
        Deque<Serializable> deque = cache.get(username);
        if(deque == null) {
            // 初始化队列
            deque = new LinkedList<Serializable>();
        }
        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId) && session.getAttribute(kickoutAttrName) == null) {
            // 将sessionId存入队列
            deque.push(sessionId);
            // 将用户的sessionId队列缓存
            cache.put(username, deque);
        }

        log.info("deque.size:{}",deque.size());
        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            // 是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
            if(kickoutAfter) {
                //如果踢出后者
                kickoutSessionId = deque.removeFirst();
            } else {
                //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            // 踢出后再更新下缓存队列
            cache.put(username, deque);

            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute(kickoutAttrName, true);
                }
            } catch (Exception e) {//ignore exception
                log.error(e.getMessage());
            }
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute(kickoutAttrName) != null
                && (Boolean)session.getAttribute(kickoutAttrName) == true) {
            //会话被踢出了
            try {
                subject.logout();
            } catch (Exception e) { //ignore
                log.warn(e.getMessage());
                e.printStackTrace();
            }
            saveRequest(request);
            if(RequestUtil.isAjax((HttpServletRequest) request)) {
                Result result = Result.error("您已在别处登录，请您修改密码或重新登录");
                RequestUtil.renderString((HttpServletResponse) response, JSON.toJSONString(result));
            }else {
                //重定向
                log.info("用户登录人数超过限制, 重定向到{}", kickoutUrl);
                WebUtils.issueRedirect(request, response, kickoutUrl);
            }
            return false;
        }
        return true;
    }
}
