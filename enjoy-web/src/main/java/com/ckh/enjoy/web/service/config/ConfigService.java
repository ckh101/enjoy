package com.ckh.enjoy.web.service.config;

import com.danga.MemCached.MemCachedClient;
import com.ckh.enjoy.security.cache.ShiroMcCacheManager;
import com.ckh.enjoy.security.realms.ShiroDbRealm;
import com.ckh.enjoy.security.session.MemcachedSessionDAO;
import com.ckh.enjoy.security.session.ShiroSessionFactory;
import com.ckh.enjoy.security.session.ShiroSessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Configuration
public class ConfigService {


	@Autowired
    ShiroDbRealm shiroDbRealm;

	@Autowired
    MemCachedClient mcc;

	
	@Bean
    public FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean() {  
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistration = new FilterRegistrationBean<>();
        DelegatingFilterProxy shiroFilter = new DelegatingFilterProxy("shiroFilter");
        shiroFilter.setTargetFilterLifecycle(true);
        filterRegistration.setFilter(shiroFilter);   
        filterRegistration.setEnabled(true);
        Set<String> urls = new HashSet<>();
        urls.add("/login");
        urls.add("/");
        urls.add("/images/captcha.jpg");
        urls.add("/logincheck");
        urls.add("/verifyLogin");
        urls.add("/loginbind");
        urls.add("/logout");
        urls.add("/autherror");
        urls.add("/index");
        urls.add("/admin/*");
        filterRegistration.setUrlPatterns(urls);
        filterRegistration.setName("shiroFilter");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;  
    }


 

	


	/*@Bean
	public ShiroUrlBasedFilter shiroUrlBasedFilter() {
		ShiroUrlBasedFilter shiroUrlBasedFilter = new ShiroUrlBasedFilter();
		return shiroUrlBasedFilter;
	}*/
	
	
	@Bean
	public  ShiroMcCacheManager shiroMcCacheManager() {
		ShiroMcCacheManager shiroMcCacheManager = new ShiroMcCacheManager("fastsh",mcc);
		return shiroMcCacheManager;
	}
	
	@Bean
	public MemcachedSessionDAO memcachedSessionDAO() {
		MemcachedSessionDAO memcachedSessionDAO = new MemcachedSessionDAO();
		return memcachedSessionDAO;
	}
	
	@Bean("sessionFactory")
	public ShiroSessionFactory sessionFactory(){
	    ShiroSessionFactory sessionFactory = new ShiroSessionFactory();
	    return sessionFactory;
	}
	@Bean
	public DefaultWebSessionManager sessionManager() {
		ShiroSessionManager sessionManager = new ShiroSessionManager();
		sessionManager.setSessionDAO(memcachedSessionDAO());
		sessionManager.setSessionFactory(sessionFactory());
		sessionManager.setGlobalSessionTimeout(24*3600*1000L);
		return sessionManager;
	}
	@Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroDbRealm);
        securityManager.setSessionManager(sessionManager());
        securityManager.setCacheManager(shiroMcCacheManager());
        return securityManager;
    }
	
	@Bean("shiroFilter") 
    public ShiroFilterFactoryBean shirFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //配置不会被拦截的链接 顺序判断
        
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/verifyLogin", "anon");
        filterChainDefinitionMap.put("/logincheck", "anon");
        filterChainDefinitionMap.put("/loginbind", "anon");
        filterChainDefinitionMap.put("/autherror", "anon");
        filterChainDefinitionMap.put("/logout", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

	@Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}