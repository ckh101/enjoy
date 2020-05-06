package com.hbnet.fastsh.web.service.config;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public class Apper {

	// body 参数
	private JSONObject body;

	private HttpServletRequest request;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public JSONObject getBody() {
		return body;
	}

	public void setBody(JSONObject body) {
		this.body = body;
	}

}
