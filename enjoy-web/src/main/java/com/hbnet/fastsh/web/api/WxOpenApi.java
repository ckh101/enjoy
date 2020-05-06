package com.hbnet.fastsh.web.api;

import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.OkHttpUtils;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.utils.WxAdApiUtil;
import com.hbnet.fastsh.utils.WxAppUtil;
import com.hbnet.fastsh.web.cache.CacheClient;
import com.hbnet.fastsh.web.entity.AccessToken;
import com.hbnet.fastsh.web.entity.WxApp;
import com.hbnet.fastsh.web.service.impl.AccessTokenService;
import com.hbnet.fastsh.web.service.impl.WxAppService;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


@Controller
@Slf4j
public class WxOpenApi {
	@Autowired
	CacheClient cc;
	
	@Autowired
	AccessTokenService ats;
	
	@Autowired
	WxAppService was;
	
	@RequestMapping(value="intf/mpauth/authcallback")
	@ResponseBody
	public Object authEvent(HttpServletRequest req) {
		String msgSignature = req.getParameter("msgSignature");
		String timeStamp = req.getParameter("timeStamp");
		String nonce = req.getParameter("nonce");
		String xml_receive_result = WxAppUtil.getWeiXinResponse(req);
		try {
			Map<String, String> result_map = WxAppUtil.parseXMLToMap(xml_receive_result);
			if(result_map != null) {
				if(result_map.containsKey("AppId")&&result_map.containsKey("Encrypt")) {
					WXBizMsgCrypt wxcrypt = new WXBizMsgCrypt("hy123987hy123987", "hy123987hy123987hy123987hy123987hy123987888", WxAppUtil.COMPONENT_APPID);
					String msg = wxcrypt.decrypt(result_map.get("Encrypt"));
					String appId = result_map.get("AppId");
					Map<String, String> msg_map = WxAppUtil.parseXMLToMap(msg);
					if(msg_map.containsKey("ComponentVerifyTicket")&&"component_verify_ticket".equals(msg_map.get("InfoType"))) {
						String ticket = msg_map.get("ComponentVerifyTicket");
						cc.set("mp_"+appId, ticket);
						AccessToken token = ats.getByAppId(appId);
						if(token == null||token.getExpiresTime().getTime() <= new Date().getTime()) {
							
							String api_component_token = WxAppUtil.getApiComponentAccessToken(WxAppUtil.COMPONENT_APPID,WxAppUtil.COMPONENT_APPSECRET, ticket);
							JSONObject json = JSONObject.parseObject(api_component_token);
							if(json != null&&json.containsKey("component_access_token")) {
								if(token == null) {
									token = new AccessToken();
									token.setAppId(appId);
									token.setCreateTime(new Date());
									token.setUpdateTime(new Date());
									token.setToken(json.getString("component_access_token"));
									token.setExpiresTime(Tools.addMinute(new Date(), 90));
									ats.saveOrUpdate(token);
								}else {
									token.setUpdateTime(new Date());
									token.setToken(json.getString("component_access_token"));
									token.setExpiresTime(Tools.addMinute(new Date(), 90));
									ats.saveOrUpdate(token);
								}
							}
						}
					}else if("authorized".equals(msg_map.get("InfoType"))) {
						
					}else if("unauthorized".equals(msg_map.get("InfoType"))) {
						String authorizerAppid = msg_map.get("AuthorizerAppid");
						WxApp app = was.getByAppId(authorizerAppid);
						if(app != null) {
							app.setAuthStatus(0);
							was.saveOrUpate(app);
						}
					}else if("updateauthorized".equals(msg_map.get("InfoType"))) {
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "success";
	}
	
	@RequestMapping(value="intf/mpmsg/{appId}/callback")
	public void authEvent(HttpServletRequest req, @PathVariable String appId, HttpServletResponse response) throws AesException, JDOMException, IOException {
	    String result = "";
        String xml_receive_result = WxAppUtil.getWeiXinResponse(req);
        Map<String, String> result_map = WxAppUtil.parseXMLToMap(xml_receive_result);
        WXBizMsgCrypt wxcrypt = new WXBizMsgCrypt("hy123987hy123987", "hy123987hy123987hy123987hy123987hy123987888", WxAppUtil.COMPONENT_APPID);
        String msg = wxcrypt.decrypt(result_map.get("Encrypt"));
        Map<String, String> msg_map = WxAppUtil.parseXMLToMap(msg);
        String user = msg_map.get("FromUserName");
        String from = msg_map.get("ToUserName");
        String content = msg_map.get("Content");
        if(!Tools.isBlank(content)){
            if("TESTCOMPONENT_MSG_TYPE_TEXT".equalsIgnoreCase(content)){
                Map<String, String> res_msg = new LinkedHashMap<>();
                res_msg.put("ToUserName", user);
                res_msg.put("FromUserName", from);
                res_msg.put("CreateTime", String.valueOf(new Date().getTime()/1000));
                res_msg.put("MsgType", "text");
                res_msg.put("Content", "TESTCOMPONENT_MSG_TYPE_TEXT_callback");
                log.error(WxAppUtil.assembParamToXml(res_msg));
                result = wxcrypt.encryptMsg(WxAppUtil.assembParamToXml(res_msg), String.valueOf(new Date().getTime()), Tools.getRandomStr(6));
                log.error(result);
                response.getWriter().write(result);
            }else if("QUERY_AUTH_CODE".startsWith(content)){
                response.getWriter().write(result);
                String query_auth_code = content.split(":")[1];
                JSONObject json = new JSONObject();
                json.put("touser", user);
                json.put("msgtype","text");
                JSONObject text = new JSONObject();
                json.put("text", text);
                text.put("content", query_auth_code+"_from_api");
                AccessToken token = ats.getByAppId(WxAppUtil.COMPONENT_APPID);
                JSONObject tokenJson = JSONObject.parseObject(WxAppUtil.queryAuth(WxAppUtil.COMPONENT_APPID, token.getToken(), query_auth_code));
                String accessToken = tokenJson.getJSONObject("authorization_info").getString("authorizer_access_token");
                OkHttpUtils.getInstance().postJson("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken, json.toJSONString());
            }
        }
        log.error(msg);
	}
	
	
	@RequestMapping(value="intf/mpauth/{appId}/authredirect")
	public String authredirect(HttpServletRequest req,HttpServletResponse resp, @PathVariable String appId) {
		String auth_code = req.getParameter("auth_code");
		AccessToken token = ats.getByAppId(WxAppUtil.COMPONENT_APPID);
		JSONObject json = JSONObject.parseObject(WxAppUtil.queryAuth(WxAppUtil.COMPONENT_APPID, token.getToken(), auth_code));
		if(json.containsKey("authorization_info")) {
			JSONObject infoJson = json.getJSONObject("authorization_info");
			String accessToken = infoJson.getString("authorizer_access_token");
			String refreshToken = infoJson.getString("authorizer_refresh_token");
			String funcInfo = infoJson.getJSONArray("func_info").toJSONString();
			WxApp app = was.getByAppId(appId);
			if(app != null) {
				app.setAuthStatus(1);
				app.setRefreshToken(refreshToken);
				app.setAccessToken(accessToken);
				app.setFuncInfo(funcInfo);
				app.setUpdateTime(new Date());
				app.setExpiresTime(Tools.addMinute(new Date(), 90));
				was.saveOrUpate(app);
			}
		}
		return "index";
	}

	@RequestMapping("/upload")
	@ResponseBody
	public JSONObject upload(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws IOException {

		JSONObject result = new JSONObject();
		String appId = req.getParameter("appId");
		String accessToken = getAccessToken(appId);
		if(!Tools.isBlank(accessToken)){
			byte[] fileBytes = file.getBytes();
			String signature = Tools.md5(fileBytes);
			JSONObject image = WxAdApiUtil.addImages(accessToken, fileBytes, signature);
			return image;
		}
		return result;
	}
	private String getAccessToken(String appId){
		String accessToken = null;
		WxApp app = was.getByAppId(appId);
		if(app != null) {
			if (app.getExpiresTime().getTime() <= LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()) {
				AccessToken token = ats.getByAppId(WxAppUtil.COMPONENT_APPID);
				JSONObject tokenJson = JSONObject.parseObject(WxAppUtil.refreshAppAccessToken(token.getToken(), WxAppUtil.COMPONENT_APPID, app.getAppId(), app.getRefreshToken()));
				app.setAccessToken(tokenJson.getString("authorizer_access_token"));
				app.setRefreshToken(tokenJson.getString("authorizer_refresh_token"));
				app.setExpiresTime(Tools.addMinute(new Date(), 90));
				was.saveOrUpate(app);
			}
			accessToken = app.getAccessToken();
		}
		return accessToken;
	}
}
