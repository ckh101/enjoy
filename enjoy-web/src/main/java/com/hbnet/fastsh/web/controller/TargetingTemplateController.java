package com.hbnet.fastsh.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.TargetingTemplate;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@RequestMapping(value="admin/targetingTemplate")
public class TargetingTemplateController {

    @Autowired
    TargetingTemplateService targetingTemplateService;

    @Autowired
    GdtLocationService gdtLocationService;

    @Autowired
    GdtBehaviorService gdtBehaviorService;

    @Autowired
    GdtInterestService gdtInterestService;

    @Autowired
    WechatOfficalAccountGactegoryService wechatOfficalAccountGactegoryService;

    @RequestMapping()
    public String list(TargetingTemplate targetingTemplate, PageInfo<TargetingTemplate> pageInfo, ModelMap model){
        targetingTemplateService.page(targetingTemplate, pageInfo);
        model.addAttribute("page", pageInfo);
        model.addAttribute("targetingTemplate", targetingTemplate);
        return "/targetingtemplate/list";
    }

    @RequestMapping("add")
    public String add(ModelMap model) {
        JSONObject data = new JSONObject();
        model.addAttribute("locData", gdtLocationService.ztreeData());
        model.addAttribute("behaviorData", gdtBehaviorService.ztreeData());
        model.addAttribute("interestData", gdtInterestService.ztreeData());
        model.addAttribute("wechatCateData", wechatOfficalAccountGactegoryService.getList());
        model.addAttribute("data",data);
        model.addAttribute("method","add");
        return "/targetingtemplate/edit";
    }

    @RequestMapping("edit/{tid}")
    public String edit(ModelMap model, @PathVariable Long tid) {
        TargetingTemplate targetingTemplate = targetingTemplateService.findById(tid);
        JSONObject data = new JSONObject();
        JSONObject params = targetingToParams(targetingTemplate);
        data.put("params", params);
        model.addAttribute("locData", gdtLocationService.ztreeData());
        model.addAttribute("behaviorData", gdtBehaviorService.ztreeData());
        model.addAttribute("interestData", gdtInterestService.ztreeData());
        model.addAttribute("wechatCateData", wechatOfficalAccountGactegoryService.getList());
        model.addAttribute("data",data);
        model.addAttribute("method", "edit");
        return "/targetingtemplate/edit";
    }

    @RequestMapping(value="save", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse  save(TargetingTemplate targetingTemplate){
        ApiResponse result = new ApiResponse();
        targetingTemplate.setCreateTime(new Date());
        targetingTemplateService.saveOrUpdate(targetingTemplate);
        result.setStatus(1);

        return result;
    }
    @RequestMapping(value = "delete/{tid}")
    @ResponseBody
    public ApiResponse del(@PathVariable Long tid, HttpServletResponse resp){
        ApiResponse result = new ApiResponse();
        try {
            if(tid > 0){
                targetingTemplateService.delById(tid);
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

    @RequestMapping("getTargetingById/{tid}")
    @ResponseBody
    public ApiResponse getTargetingById(@PathVariable Long tid){
        ApiResponse result = new ApiResponse();
        TargetingTemplate targetingTemplate = targetingTemplateService.findById(tid);
        if(targetingTemplate != null){
            result.setData(targetingToParams(targetingTemplate));
        }
        return result;
    }

    private JSONObject targetingToParams(TargetingTemplate targeting){
        JSONObject params = new JSONObject();
        params.put("id", targeting.getId());
        params.put("templateName", targeting.getTemplateName());
        if(!Tools.isBlank(targeting.getAge())){
            JSONArray age = JSON.parseArray(targeting.getAge());
            params.put("age_min", age.getJSONObject(0).get("min"));
            params.put("age_max", age.getJSONObject(0).get("max"));
        }
        if(!Tools.isBlank(targeting.getGender())){
            params.put("gender", targeting.getGender());
        }
        if(!Tools.isBlank(targeting.getEducation())){
            params.put("education", targeting.getEducation());
        }
        if(!Tools.isBlank(targeting.getMaritalStatus())){
            params.put("marital_status", JSONArray.parseArray(targeting.getMaritalStatus()));
        }
        if(!Tools.isBlank(targeting.getWorkingStatus())){
            params.put("workingStatus", targeting.getWorkingStatus());
        }
        if(!Tools.isBlank(targeting.getGeoLocation())){
            JSONObject loc = JSONObject.parseObject(targeting.getGeoLocation());
            JSONArray regions = loc.getJSONArray("regions");
            JSONArray business_districts = loc.getJSONArray("business_districts");
            if(regions == null){
                regions = new JSONArray();

            }
            if(business_districts != null){
                regions.addAll(business_districts);
            }
            params.put("locs", regions);
        }
        if(!Tools.isBlank(targeting.getUserOs())){
            params.put("userOs", targeting.getUserOs());
        }
        if(!Tools.isBlank(targeting.getNewDevice())){
            params.put("newDevice", targeting.getNewDevice());
        }
        if(!Tools.isBlank(targeting.getDevicePrice())){
            params.put("devicePrice", targeting.getDevicePrice());
        }
        if(!Tools.isBlank(targeting.getNetworkType())){
            params.put("networkType", targeting.getNetworkType());
        }
        if(!Tools.isBlank(targeting.getNetworkOperator())){
            params.put("networkOperator", targeting.getNetworkOperator());
        }
        if(!Tools.isBlank(targeting.getNetworkScene())){
            params.put("networkScene", targeting.getNetworkScene());
        }
        if(!Tools.isBlank(targeting.getConsumptionStatus())){
            params.put("consumptionStatus", targeting.getConsumptionStatus());
        }
        if(!Tools.isBlank(targeting.getDressingIndex())){
            params.put("dressingIndex", targeting.getDressingIndex());
        }
        if(!Tools.isBlank(targeting.getMakeupIndex())){
            params.put("makeupIndex", targeting.getMakeupIndex());
        }
        if(!Tools.isBlank(targeting.getUvIndex())){
            params.put("uvIndex", targeting.getUvIndex());
        }
        if(!Tools.isBlank(targeting.getAirQualityIndex())){
            params.put("airQualityIndex", targeting.getAirQualityIndex());
        }
        if(!Tools.isBlank(targeting.getClimate())){
            params.put("climate", targeting.getClimate());
        }
        if(!Tools.isBlank(targeting.getResidentialCommunityPrice())){
            JSONArray residentialCommunityPrice = JSONArray.parseArray(targeting.getResidentialCommunityPrice());
            String min = residentialCommunityPrice.getJSONObject(0).getString("min");
            String max = residentialCommunityPrice.getJSONObject(0).getString("max");
            params.put("residentialCommunityPrice_min", min);
            params.put("residentialCommunityPrice_max", max);
        }
        if(!Tools.isBlank(targeting.getTemperature())){
            JSONArray temperature = JSONArray.parseArray(targeting.getTemperature());
            String min = temperature.getJSONObject(0).getString("min");
            String max = temperature.getJSONObject(0).getString("max");
            params.put("temperature_min", min);
            params.put("temperature_max", max);
        }
        if(!Tools.isBlank(targeting.getWechatAdBehavior())){
            JSONObject wechat_ad_behavior = JSONObject.parseObject(targeting.getWechatAdBehavior());
            params.put("actions", wechat_ad_behavior.getJSONArray("actions"));
            params.put("excludedActions", wechat_ad_behavior.getJSONArray("excluded_actions"));
        }
        if(!Tools.isBlank(targeting.getCustomAudience())){
            params.put("custom_audience", Joiner.on(",").join(JSONArray.parseArray(targeting.getCustomAudience()).toJavaList(String.class)));
        }
        if(!Tools.isBlank(targeting.getExcludedCustomAudience())){
            params.put("excluded_custom_audience", Joiner.on(",").join(JSONArray.parseArray(targeting.getExcludedCustomAudience()).toJavaList(String.class)));

        }
        if(!Tools.isBlank(targeting.getBehaviorOrInterest())){
            JSONObject bi = JSONObject.parseObject(targeting.getBehaviorOrInterest());
            params.put("interest", bi.getJSONObject("interest").getJSONArray("targeting_tags"));
            params.put("behavior",bi.getJSONArray("behavior").getJSONObject(0).getJSONArray("targeting_tags"));
            params.put("scene", bi.getJSONArray("behavior").getJSONObject(0).getJSONArray("scene").toJSONString());
            params.put("time_window", bi.getJSONArray("behavior").getJSONObject(0).getString("time_window"));
            params.put("intensity", bi.getJSONArray("behavior").getJSONObject(0).getJSONArray("intensity").toJSONString());
        }
        if(!Tools.isBlank(targeting.getWechatOfficialAccountCategory())){
            params.put("wechat_official_account_category", JSONArray.parseArray(targeting.getWechatOfficialAccountCategory()));
        }
        if(!Tools.isBlank(targeting.getFinancialSituation())){
            params.put("financialSituation", targeting.getFinancialSituation());
        }
        if(!Tools.isBlank(targeting.getConsumptionType())){
            params.put("consumptionType", targeting.getConsumptionType());
        }

        return params;
    }
}
