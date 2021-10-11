package com.icss.cnblog.shiro.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-13 19:41
 **/
@Slf4j
public class ShiroSessionManager extends DefaultWebSessionManager {
    /**
     * 获取session
     * 优化单次请求需要多次访问redis的问题
     * @param sessionKey
     * @return
     * @throws UnknownSessionException
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);

        if (sessionId == null) {
            log.debug("Unable to resolve session ID from SessionKey [{}].  Returning null to indicate a "
                    + "session could not be found.", sessionKey);
            return null;
        }

        ServletRequest request = null;

        if (sessionKey instanceof WebSessionKey){
            request = ((WebSessionKey)sessionKey).getServletRequest();
        }
        if(request != null && sessionId != null){
            //从request中获取session，如果有，则直接返回。
            Session session = (Session)request.getAttribute(sessionId.toString());
            if(session != null ){
                return session;
            }
        }

        Session session = super.retrieveSession(sessionKey);
        if(request != null && sessionId != null){
            //将从redis中获取到的session存入request中，方便下次获取
            request.setAttribute(sessionId.toString(), session);
        }

        return session;
    }
}
