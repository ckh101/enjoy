package com.hbnet.fastsh.security.filter;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/** 
 * @description: URL过滤器
 */  
public class ShiroUrlBasedFilter extends AuthorizationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object mappedValue) throws Exception {
		HttpServletRequest hreq = (HttpServletRequest)req;
		Subject subject = SecurityUtils.getSubject();
		boolean isPermitted = false;
		
		if (subject.isAuthenticated()) {
			String url = hreq.getServletPath();
			if (StringUtils.isBlank(url)) {
				url = hreq.getPathInfo();
			}
			
			isPermitted = subject.isPermitted(url);
		}
		
		return isPermitted;
	}
}
