package com.ckh.enjoy.web.interceptors;

import com.ckh.enjoy.utils.SessionUtil;
import com.ckh.enjoy.utils.Tools;
import com.ckh.enjoy.web.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
public class SystemLogInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		try {
			Subject currentSubject = SecurityUtils.getSubject();
			String url = request.getServletPath();
			String ip = Tools.getIp(request);
			String accessTime = Tools.format(new Date(), "yyyyMMddHHmmss");
			if (currentSubject.isAuthenticated()) {
				User user = SessionUtil.getCurUser();
				log.info(accessTime+"\t"+user.getAccount()+"\t"+ip+"\t"+url);
			}else{
				log.info(accessTime+"\t"+null+"\t"+ip+"\t"+url);
			}
		} catch (AuthenticationException e) {
			
		}
		return true;
	}
}
