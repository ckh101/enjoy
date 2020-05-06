package com.ckh.enjoy.utils;

import com.alibaba.fastjson.JSONObject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class WxAppUtil {

	public static final String LOGIN_APPID = "wx7822f5784bb7ece0";
	public static final String LOGIN_SECRET = "f26da71599685b2f685f56f47d7f0794";
	public static final String COMPONENT_APPID = "wxa6b55d67539a6fd5";
	public static final String COMPONENT_APPSECRET = "5c8fb1b219734a66b2c1d927d3b541a0";
	private static final String LOGIN_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	private static final String LOGIN_REFRESH_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	private static final String WX_APICOMPONENT_TOKEN = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
	private static final String PRE_AUTH_CODE_URL = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=";
	private static final String API_QUERY_AUTH_URL = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=";
	private static final String API_AUTHORIZER_TOKEN = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=";
	/**
	* 处理xml请求信息
	*/
	public static String getWeiXinResponse(HttpServletRequest request){
		BufferedReader bis=null;
		StringBuilder result = new StringBuilder();
		try{
			bis=new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line;
			while((line=bis.readLine())!=null){
				result.append(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(bis!=null){
				try{
					bis.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return result.toString();
	}
	
	/**
	* 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	*/
	public static Map parseXMLToMap(String strxml) throws JDOMException, IOException {
		strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");   	 
		if(null == strxml || "".equals(strxml))
			return null;
		Map m = new HashMap();
		InputStream in = new ByteArrayInputStream(strxml.getBytes(StandardCharsets.UTF_8));
		SAXBuilder builder = new SAXBuilder();
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
		builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
		builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v;
			List children = e.getChildren();
			if(children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v =getChildrenText(children);
			}
			m.put(k, v);
		}
		//关闭流
		in.close();
		return m;
	}

    /**
     * 将需要传递给微信的参数转成xml格式
     * @param parameters
     * @return
     */
    public static String assembParamToXml(Map<String,String> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set<String> es = parameters.keySet();
        List<Object> list=new ArrayList<>(es);
        Object[] ary =list.toArray();
        Arrays.sort(ary);
        list=Arrays.asList(ary);
        Iterator<Object> it = list.iterator();
        while(it.hasNext()) {
            String key =  (String) it.next();
            String val=(String) parameters.get(key);
            if ("attach".equalsIgnoreCase(key)||"body".equalsIgnoreCase(key)||"sign".equalsIgnoreCase(key)) {
                sb.append("<"+key+">"+"<![CDATA["+val+"]]></"+key+">");
            }else {
                sb.append("<"+key+">"+val+"</"+key+">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }
	/**
	* 获取子结点的xml
	* @param children
	* @return String
	*/
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()) {
			Iterator it = children.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if(!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		return sb.toString();
	}
	
	public static String getApiComponentAccessToken(String component_appid, String component_appsecret, String component_verify_ticket) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("component_appid", component_appid);
		params.put("component_appsecret", component_appsecret);
		params.put("component_verify_ticket", component_verify_ticket);
		return OkHttpUtils.getInstance().postJson(WX_APICOMPONENT_TOKEN, JsonUtils.toJson(params));
	}
	
	public static String getPreAuthCode(String component_appid, String component_access_token) {
		JSONObject params = new JSONObject();
		params.put("component_appid", component_appid);
		String url = PRE_AUTH_CODE_URL + component_access_token;
		return OkHttpUtils.getInstance().postJson(url, params.toJSONString());
	}
	
	public static String queryAuth(String component_appid, String component_access_token, String auth_code) {
		JSONObject params = new JSONObject();
		params.put("component_appid", component_appid);
		params.put("authorization_code", auth_code);
		String url = API_QUERY_AUTH_URL + component_access_token;
		return OkHttpUtils.getInstance().postJson(url, params.toJSONString());
	}
	
	public static String userAccessToken(String code, String appId, String secret) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		return OkHttpUtils.getInstance().get(url, null);
	}
	
	public static String userRefreshToken(String appId, String refreshToken) {
		String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+appId+"&grant_type=refresh_token&refresh_token="+refreshToken;
		return OkHttpUtils.getInstance().get(url, null);
	}

	public static String refreshAppAccessToken(String component_access_token, String component_appid, String  authorizer_appid, String authorizer_refresh_token){
		String url = API_AUTHORIZER_TOKEN + component_access_token;
		JSONObject params = new JSONObject();
		params.put("component_appid", component_appid);
		params.put("authorizer_appid",authorizer_appid);
		params.put("authorizer_refresh_token", authorizer_refresh_token);
        return OkHttpUtils.getInstance().postJson(url, params.toJSONString());
	}
	
	public static String getOpenWxUserInfo(String accessToken, String openId) {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openId;
		return OkHttpUtils.getInstance().get(url, null);
	}
	
	public static String checkToken(String accessToken, String openId) {
		String url = "https://api.weixin.qq.com/sns/auth?access_token="+accessToken+"&openid="+openId;
		return OkHttpUtils.getInstance().get(url, null);
	}
}
