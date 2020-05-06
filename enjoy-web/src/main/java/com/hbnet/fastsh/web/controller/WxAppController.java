package com.hbnet.fastsh.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.utils.WxAppUtil;
import com.hbnet.fastsh.web.entity.AccessToken;
import com.hbnet.fastsh.web.entity.WxApp;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.impl.AccessTokenService;
import com.hbnet.fastsh.web.service.impl.WxAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/** 
 * @description: 模块控制器
 */  
@Controller
@RequestMapping(value = "admin/wxapp")
public class WxAppController {
	
	
	@Autowired
	WxAppService was;
	
	@Autowired
	AccessTokenService ats;
	
	
	@RequestMapping()
	public String list(WxApp app, PageInfo<WxApp> pageInfo, ModelMap model){
		was.page(app, pageInfo);
		model.addAttribute("page", pageInfo);
		model.addAttribute("app",app);
		return "/wxapp/list";
	}
	
	
	@RequestMapping("add")
	public String add(ModelMap model) {
		model.addAttribute("method","add");
		return "/wxapp/edit";
	}
	
	@RequestMapping("edit/{aid}")
	public String edit(ModelMap model, @PathVariable Long aid) {
		WxApp app = was.findById(aid);
		model.addAttribute("app",app);
		model.addAttribute("method", "edit");
		return "/wxapp/edit";
	}
	
	@RequestMapping(value="save", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse  save(WxApp app, String method){
			ApiResponse result = new ApiResponse();
			if("edit".equals(method)){
				WxApp wxapp = was.findById(app.getId());
				if(wxapp != null) {
					if(!Tools.isBlank(app.getAppId())) {
						wxapp.setAppId(app.getAppId());
					}
					if(!Tools.isBlank(app.getAppName())) {
						wxapp.setAppName(app.getAppName());
					}
					if(!Tools.isBlank(app.getCompany())) {
						wxapp.setCompany(app.getCompany());
					}
					wxapp.setTypeId(app.getTypeId());
					was.saveOrUpate(app);
				}
				
				result.setStatus(1);
			}else{
				was.saveOrUpate(app);
				result.setStatus(1);
			}
			return result;
	}
	@RequestMapping(value = "delete/{aid}")
	@ResponseBody
	public ApiResponse del(@PathVariable Long aid, HttpServletResponse resp){
		ApiResponse result = new ApiResponse();
		try {
			if(aid > 0){
				WxApp app = new WxApp();
				app.setId(aid);
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
	
	@RequestMapping("verifyAppName")
	@ResponseBody
	public JSONObject verifyAppName(String appName) {
		JSONObject result = new JSONObject();
		result.put("valid", !was.isExist(appName));
		return result;
	}
	
	@RequestMapping(value="mpauth/auth/{aId}")
	public String auth(HttpServletRequest req,HttpServletResponse resp, @PathVariable Long aId) throws IOException {
		WxApp app = was.findById(aId);
		if(app == null) {
			return "404";
		}
		String baseUrl = "https://" + req.getHeader("host") + req.getContextPath();
		AccessToken token = ats.getByAppId(WxAppUtil.COMPONENT_APPID);
		if(token == null) {
			return "404";
		}
		String pre_auth_code = WxAppUtil.getPreAuthCode(WxAppUtil.COMPONENT_APPID, token.getToken());
		JSONObject json = JSONObject.parseObject(pre_auth_code);
		String redirect_uri = baseUrl + "/intf/mpauth/"+app.getAppId()+"/authredirect";
		String url = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid="+WxAppUtil.COMPONENT_APPID+"&pre_auth_code="+json.getString("pre_auth_code")+"&biz_appid="+app.getAppId()+"&redirect_uri=" + URLEncoder.encode(redirect_uri, "utf-8");
		req.setAttribute("auth_url", url);
		return "wxauth";
	}
}
