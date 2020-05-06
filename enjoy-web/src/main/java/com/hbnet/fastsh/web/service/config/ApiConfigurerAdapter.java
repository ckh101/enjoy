package com.hbnet.fastsh.web.service.config;



import com.hbnet.fastsh.web.interceptors.AdRequestAuthInterceptor;
import com.hbnet.fastsh.web.interceptors.SystemLogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ApiConfigurerAdapter implements WebMvcConfigurer{
	
	@Bean
	public SystemLogInterceptor getSystemLogInterceptor() {
		return new SystemLogInterceptor();
	}

	@Bean
	public AdRequestAuthInterceptor getAdRequestAuthInterceptor(){
		return new AdRequestAuthInterceptor();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new ApiArgumentResolver());
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**/*").addResourceLocations("classpath:/static/");
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getSystemLogInterceptor()).excludePathPatterns("/static/**/*").addPathPatterns("/admin/**/*");
		registry.addInterceptor(getAdRequestAuthInterceptor()).excludePathPatterns("/static/**/*").addPathPatterns("/admin/ad/**/*");
	}
	
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false);
	}
}