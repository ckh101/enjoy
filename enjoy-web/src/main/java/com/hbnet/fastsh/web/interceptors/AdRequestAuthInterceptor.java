package com.hbnet.fastsh.web.interceptors;
import com.hbnet.fastsh.utils.SessionUtil;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.annotations.AdRequestAuth;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.entity.User;
import com.hbnet.fastsh.web.enums.AccountTypeEnum;
import com.hbnet.fastsh.web.service.impl.AdvertiserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class AdRequestAuthInterceptor implements HandlerInterceptor {


    @Autowired
    AdvertiserService advertiserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            Subject currentSubject = SecurityUtils.getSubject();
            if (currentSubject.isAuthenticated()) {
                if (!(handler instanceof HandlerMethod)) {
                    return true;
                }

                AdRequestAuth auth = ((HandlerMethod) handler).getMethodAnnotation(AdRequestAuth.class);
                if(auth == null || !auth.isAuth()){
                    return true;
                }
                Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                long aId = Tools.longValue((String)pathVariables.get("aId"), 0);
                User user = SessionUtil.getCurUser();
                Advertiser adv = advertiserService.findById(aId);
                if(adv == null){
                    request.setAttribute("error","找不到广告主信息");
                    request.getRequestDispatcher("/admin/ad/autherror").forward(request, response);
                    return false;
                }
                if(user.getAccountType().equals(AccountTypeEnum.ADCCOUNT_TYPE_ADVERTISER.getValue())){
                    Advertiser a = advertiserService.findByUserIdAndId(user.getId(), adv.getId());
                    if(a == null){
                        request.setAttribute("error","当前用户没有操作权限");
                        request.getRequestDispatcher("/admin/ad/autherror").forward(request, response);
                        return false;
                    }
                }
                request.setAttribute("adv", adv);
            }
        } catch (AuthenticationException e) {

        } catch (ServletException e) {

        } catch (IOException e) {

        }
        return true;
    }
}
