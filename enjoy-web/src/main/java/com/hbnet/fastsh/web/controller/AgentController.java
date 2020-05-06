package com.hbnet.fastsh.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.utils.SessionUtil;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.Agent;
import com.hbnet.fastsh.web.entity.User;
import com.hbnet.fastsh.web.service.impl.AgentService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("admin/agent")
public class AgentController {

    @Autowired
    AgentService agentService;

    @RequestMapping("refreshToken/{agentId}")
    @ResponseBody
    public JSONObject refreshAccessToken(@PathVariable Long agentId){
        Subject currentSubject = SecurityUtils.getSubject();
        if(currentSubject.isAuthenticated()&&"admin".equals(SessionUtil.getCurUser().getAccount())) {
            return agentService.refreshToken(agentId);
        }else{
            return null;
        }
    }

    @RequestMapping(value="authcallback/{accountId}")
    public String authCallBack(HttpServletRequest req, @PathVariable Long accountId){
        String authorization_code = req.getParameter("authorization_code");
        Agent agent = agentService.findById(accountId);
        JSONObject json = GDTApiUtils.getAccessToken(authorization_code, agent.getCallbackUrl());
        if(json != null){
            if(json.getIntValue("code") == 0){
                String token = json.getJSONObject("data").getString("access_token");
                int expiresIn = json.getJSONObject("data").getIntValue("access_token_expires_in");
                String refreshtoken = json.getJSONObject("data").getString("access_token");
                int refreshExpiresIn = json.getJSONObject("data").getIntValue("refresh_token_expires_in");
                agent.setAccessToken(token);
                agent.setExpiresTime(Tools.addSeconds(new Date(), expiresIn - 600));
                agent.setRefreshAccessToken(refreshtoken);
                agent.setRefreshExpiresTime(Tools.addSeconds(new Date(), refreshExpiresIn - 600));
                agent.setUpdateTime(new Date());
                agentService.saveOrUpdate(agent);
            }else{
                req.setAttribute("error", "GDT获取Token失败,请联系服务商");
                return "/binderror";
            }
        }else{
            req.setAttribute("error", "请求GDT获取token接口失败,请联系服务商");
            return "/binderror";
        }


        return "redirect:/index";
    }

    @RequestMapping("downLoadGdtOrder")
    @ResponseBody
    public ApiResponse downLoadGdtOrder(String startDate, String endDate){
        Subject currentSubject = SecurityUtils.getSubject();
        if(currentSubject.isAuthenticated()&&"admin".equals(SessionUtil.getCurUser().getAccount())) {
            String eL = "(19|20)[0-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[01])";
            Pattern p = Pattern.compile(eL);
            if(!Tools.isBlank(startDate)&&!Tools.isBlank(endDate)&&p.matcher(startDate).matches()&&p.matcher(endDate).matches()){
                agentService.downLoadGdtOrder(startDate, endDate);
                return new ApiResponse();
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
}
