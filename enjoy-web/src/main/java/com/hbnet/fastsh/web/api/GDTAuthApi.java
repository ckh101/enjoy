package com.hbnet.fastsh.web.api;

import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.redis.service.RedisService;
import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.web.service.impl.GdtLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@Controller
public class GDTAuthApi {
    private final Logger userActionsLogger = LoggerFactory.getLogger("user_action_data");
    private final Logger messageLogger = LoggerFactory.getLogger("message_receiver");

    @Autowired
    GdtLocationService gdtLocationService;

    @Autowired
    RedisService redisService;

    @RequestMapping("loc/tree")
    @ResponseBody
    public String getLocTree(){
        return gdtLocationService.getTreeJson();
    }

    @RequestMapping("ztree")
    public String ztree(Model model){
        model.addAttribute("treeData", gdtLocationService.ztreeData());
        return "ztree";
    }


    @RequestMapping("intf/user_actions/add")
    @ResponseBody
    public ApiResponse addUserActions(HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        String account_id = request.getParameter("account_id");
        String user_action_set_id = request.getParameter("user_action_set_id");
        String action_time = request.getParameter("action_time");
        String action_type = request.getParameter("action_type");
        String outer_action_id = request.getParameter("outer_action_id");
        String click_id = request.getParameter("click_id");
        String amount = request.getParameter("amount");
        String adGroupId = request.getParameter("adGroupId");
        userActionsLogger.info(action_time+"\t"+account_id+"\t"+user_action_set_id+"\t"+action_type+"\t"+outer_action_id+"\t"+click_id+"\t"+amount+"\t"+adGroupId);
        if("14770464".equals("account_id")){
            if("COMPLETE_ORDER".equals(action_type)){
                redisService.lset(account_id,user_action_set_id+"-"+action_time+"-PURCHASE-"+outer_action_id+"-"+click_id+"-"+amount+"-"+adGroupId);
                redisService.lset(account_id,user_action_set_id+"-"+action_time+"-COMPLETE_ORDER-"+outer_action_id+"-"+click_id+"-"+amount+"-"+adGroupId);
            }
        }else{
            if("PURCHASE".equals(action_type)){
                redisService.lset(account_id,user_action_set_id+"-"+action_time+"-PURCHASE-"+outer_action_id+"-"+click_id+"-"+amount+"-"+adGroupId);
                redisService.lset(account_id,user_action_set_id+"-"+action_time+"-COMPLETE_ORDER-"+outer_action_id+"-"+click_id+"-"+amount+"-"+adGroupId);

            /*RedisMessage msg = new RedisMessage(BusinessType.COMPLETE_ORDER.getValue(), new CompleteOrderBody(account_id, adGroupId, click_id));
            redisService.publish("cn.hbnet.smartad", msg);*/
            }
        }
        //redisService.lset(account_id,user_action_set_id+"-"+action_time+"-"+action_type+"-"+outer_action_id+"-"+click_id+"-"+amount);
        return response;
    }

    @RequestMapping(value="intf/message/receiver",produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public JSONObject messageReceiver(@RequestBody(required=false) JSONObject body, HttpServletRequest request){
        JSONObject response = new JSONObject();
        if(body != null){
            messageLogger.info(body.toJSONString());
            response.put("code",0);
            response.put("data", new HashMap<String, Object>(){{
                put("event_id",body.getString("event_id"));
            }});
        }else{
            messageLogger.info("empty");
        }
        return response;
    }

    @RequestMapping(value="/intf/gdt/authcallback",produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public JSONObject authcallback(@RequestBody(required=false) JSONObject body, HttpServletRequest request){
        JSONObject response = new JSONObject();
        if(body != null){
            messageLogger.info("authcallback-"+body.toJSONString());
            response.put("code",0);
            response.put("data", new HashMap<String, Object>(){{
                put("event_id",body.getString("event_id"));
            }});
        }else{
            messageLogger.info("empty");
        }
        return response;
    }

}
