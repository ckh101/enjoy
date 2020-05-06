package com.ckh.enjoy.utils;


import org.apache.shiro.SecurityUtils;

import com.ckh.enjoy.constants.WebConstants;
import com.ckh.enjoy.web.entity.User;

/** 
 * @description: 会话相关的工具类
 */  
public class SessionUtil {
	public SessionUtil(){
		
	}
	public static void setCurUser(User user) {
		SecurityUtils.getSubject().getSession()
				.setAttribute(WebConstants.KEY_USER, user);
	}

	public static User getCurUser() {
		return (User) SecurityUtils.getSubject().getSession(true)
				.getAttribute(WebConstants.KEY_USER);
	}


	public static boolean isAdmin() {
		User curUser = getCurUser();
		if (curUser == null) {
			return false;
		}

		return curUser.getIsRoot() == 1 || "admin".equals(curUser.getRole().getFlagStr());
	}
	/**
	 * 是否有权限
	 * 
	 * @param url
	 * @return
	 */
	public static boolean hasPermission(String url) {
		return SecurityUtils.getSubject().isPermitted(url);
	}


}
