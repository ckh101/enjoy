package com.hbnet.fastsh.web.service.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSONObject;

@Component()
public class ApiArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.getParameterType().equals(Apper.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory){
		Apper apper = new Apper();

		// Body
		JSONObject body = (JSONObject) webRequest.getAttribute("body", RequestAttributes.SCOPE_REQUEST);
		apper.setBody(body);

		HttpServletRequest request = (HttpServletRequest) webRequest.getAttribute("request",
				RequestAttributes.SCOPE_REQUEST);
		apper.setRequest(request);

		return apper;
	}
}
