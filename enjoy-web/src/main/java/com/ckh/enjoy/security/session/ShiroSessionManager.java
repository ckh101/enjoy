package com.ckh.enjoy.security.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;
@Slf4j
public class ShiroSessionManager extends DefaultWebSessionManager {

    /**
     * 获取session
     * @param sessionKey
     * @return
     * @throws UnknownSessionException
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey){
        Serializable sessionId = getSessionId(sessionKey);

        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }

        if (request != null && null != sessionId) {
            Object sessionObj = request.getAttribute(sessionId.toString());
            if (sessionObj != null) {
                log.debug("read session from request");
                return (Session) sessionObj;
            }
        }

        Session session = null;
		try {
			session = super.retrieveSession(sessionKey);
			if (request != null && null != sessionId) {
			    request.setAttribute(sessionId.toString(), session);
			}
		} catch (UnknownSessionException e) {
		}
        return session;
    }
}