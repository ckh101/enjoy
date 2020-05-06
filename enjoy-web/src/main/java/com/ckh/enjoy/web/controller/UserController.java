package com.ckh.enjoy.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.ckh.enjoy.constants.WebConstants;
import com.ckh.enjoy.utils.ApiResponse;
import com.ckh.enjoy.utils.SessionUtil;
import com.ckh.enjoy.utils.Tools;
import com.ckh.enjoy.web.entity.*;
import com.ckh.enjoy.web.enums.SystemUserStatusEnum;
import com.ckh.enjoy.web.service.base.PageInfo;
import com.ckh.enjoy.web.service.impl.*;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/** 
 * @description: 模块控制器
 */  
@Controller
@RequestMapping(value = "admin/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;

	
	@Autowired
	WxUserService wus;


	
	/** md5密钥 */
	private String secretKey;
	
	private static final String navId = WebConstants.USER_LIST;
	
	@RequestMapping()
	public String list(User user, PageInfo<User> pageInfo, ModelMap model){
		userService.page(user, pageInfo);
		model.addAttribute("page", pageInfo);
		List<Role> rolelist = roleService.getAllList();
		model.addAttribute("rlist", rolelist);
		model.addAttribute("user",user);
		return "/user/list";
	}
	
	@RequestMapping("verifyAccount")
	@ResponseBody
	public JSONObject verifyAccount(String account) {
		JSONObject result = new JSONObject();
		result.put("valid", !userService.isExist(account));
		return result;
	}
	
	@RequestMapping("verifyPhone")
	@ResponseBody
	public JSONObject verifyPhone(String phone) {
		JSONObject result = new JSONObject();
		result.put("valid", !userService.isExistPhone(phone));
		return result;
	}
	
	@RequestMapping("add")
	public String add(ModelMap model) {
		List<Role> rolelist = roleService.getAllList();

		model.addAttribute("rlist", rolelist);
		model.addAttribute("method","add");
		return "/user/edit";
	}
	
	@RequestMapping("edit/{uid}")
	public String edit(ModelMap model, @PathVariable Long uid) {
		User user = userService.findById(uid);
		List<Role> rolelist = roleService.getAllList();

		model.addAttribute("user",user);
		model.addAttribute("rlist", rolelist);
		model.addAttribute("method", "edit");
		return "/user/edit";
	}
	
	@RequestMapping(value="save", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse  save(User user, HttpServletRequest req){
			ApiResponse result = new ApiResponse();

			String method = req.getParameter("method");
			String advs = Tools.stringValue(req.getParameter("advs"), "");
			String wxadvs = Tools.stringValue(req.getParameter("wxadvs"), "");
			long rid = Tools.longValue(req.getParameter("roleId"), 0);
            Role r = new Role();
            r.setId(rid);
			if("edit".equals(method)){
				User u = userService.findById(user.getId());
				if(!Tools.isBlank(user.getUserName())){
					u.setUserName(user.getUserName());
				}
				if(!Tools.isBlank(user.getPhone())) {
					if(!user.getPhone().equals(u.getPhone())){
						if(userService.isExist(user.getPhone())){
							result.setMsg("手机号码已经存在");
							return result;
						}
					}
					u.setPhone(user.getPhone());
					u.getWxUser().setPhone(user.getPhone());
				}
				if(!Tools.isBlank(user.getCompany())) {
					u.setCompany(user.getCompany());
				}

				u.setAccountType(user.getAccountType());
				u.setSystemStatus(user.getSystemStatus());
                u.setRole(r);
                userService.saveOrUpdate(u);
                result.setStatus(1);
			}else{
				user.setIsRoot(0);
				user.setOperator(SessionUtil.getCurUser());
                user = userService.saveOrUpdate(user);
                user.setRole(r);
                WxUser wxUser = new WxUser();
                wxUser.setPhone(user.getPhone());
                wxUser.setUser(user);
                user.setWxUser(wxUser);
                userService.saveOrUpdate(user);
				result.setStatus(1);
			}
			return result;
	}
	@RequestMapping(value = "delete/{uid}")
	@ResponseBody
	public ApiResponse del(@PathVariable Long uid, HttpServletResponse resp){
		ApiResponse result = new ApiResponse();
		try {
			if(uid > 0){
				User user = userService.findById(uid);
				if(user != null){
					user.setSystemStatus(SystemUserStatusEnum.SYSTEM_USER_STATUS_DEL.getValue());
					userService.saveOrUpdate(user);
				}
				result.setStatus(1);
				result.setMsg("操作成功");
			}else{
				result.setMsg("缺少ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@RequestMapping("password/{uid}")
	public String password(ModelMap model, @PathVariable Long uid){
		model.addAttribute("uid", uid);
		return "/user/password";
	}
	@RequestMapping("resetpassword")
	@ResponseBody
	public ApiResponse reset(String password, Long uid, HttpServletResponse resp) throws Exception{
		ApiResponse result = new ApiResponse();
		User user = SessionUtil.getCurUser();
		String md5Password = DigestUtils.md5Hex(password + secretKey);
		if(user.getId() == uid){
			user.setPassword(md5Password);
			userService.saveOrUpdate(user);
			Subject currentSubject = SecurityUtils.getSubject();
			currentSubject.logout();
			result.setStatus(1);
		}else if(user.getIsRoot() == 1){
			User u = userService.findById(uid);
			if(u == null){
				result.setStatus(0);
				result.setMsg("用户不存在");
				return result;
			}
			u.setPassword(md5Password);
			userService.saveOrUpdate(u);
			result.setStatus(1);
		}
		return result;
	}
}
