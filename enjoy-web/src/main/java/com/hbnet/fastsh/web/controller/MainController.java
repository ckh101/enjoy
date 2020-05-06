package com.hbnet.fastsh.web.controller;

import com.hbnet.fastsh.utils.SessionUtil;
import com.hbnet.fastsh.web.entity.User;
import com.hbnet.fastsh.web.service.impl.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hbnet.fastsh.constants.WebConstants;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController{
	@Autowired
	private UserService userService;

	/**
	 * 主页
	 * @return
	 */
	@RequestMapping(WebConstants.INDEX)
	public String index(HttpServletRequest req){
		try {
			Subject currentSubject = SecurityUtils.getSubject();

			if (currentSubject.isAuthenticated()) {
				User user = userService.findById(SessionUtil.getCurUser().getId());
				req.setAttribute("role", user.getRole());

				return "index";
			}
		} catch (AuthenticationException e) {
			return "redirect:/login";
		}
		return "redirect:/login";
	}
	
}
