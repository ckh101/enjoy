package com.hbnet.fastsh.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.hbnet.fastsh.constants.WebConstants;
import com.hbnet.fastsh.utils.*;
import com.hbnet.fastsh.web.annotations.AdRequestAuth;
import com.hbnet.fastsh.web.entity.*;
import com.hbnet.fastsh.web.enums.AdSystemStatusEnum;
import com.hbnet.fastsh.web.enums.ConfiguredStatusEnum;
import com.hbnet.fastsh.web.service.alarm.AlarmService;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import com.hbnet.fastsh.web.vo.req.AdMonitorConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import static com.hbnet.fastsh.web.service.impl.AlarmFlagService.FLAG_ROI_THRESHOLD_ALARM;

@Controller
@RequestMapping("admin/ad/admanager")
@Slf4j
public class AdManagerController {
    @Autowired
    AdService adService;

    @Autowired
    CampaignService campaignService;

    @Autowired
    AdGroupService adGroupService;

    @Autowired
    AdCreativeService adCreativeService;

    @Autowired
    GdtLocationService gdtLocationService;

    @Autowired
    GdtBehaviorService gdtBehaviorService;

    @Autowired
    GdtInterestService gdtInterestService;

    @Autowired
    WechatOfficalAccountGactegoryService wechatOfficalAccountGactegoryService;

    @Autowired
    MiniProgramService miniProgramService;

    @Autowired
    TargetingTemplateService targetingTemplateService;

    @Autowired
    private AdMonitorConfigService adMonitorConfigService;

    @Autowired
    private AlarmFlagService alarmFlagService;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    AdvertiserService advertiserService;

    @RequestMapping("adplanlist/{aId}")
    @AdRequestAuth
    public String adplanList(Model model, @PathVariable long aId, PageInfo<Campaign> pageInfo, HttpServletRequest request){
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        Campaign campaign = new Campaign();
        String campaignName = request.getParameter("campaignName");
        int campaignId = Tools.integerValue(request.getParameter("campaignId"), 0);
        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("end_date");
        if(!Tools.isBlank(campaignName)){
            campaign.setCampaignName(campaignName);
            model.addAttribute("campaignName", campaignName);
        }
        if(campaignId > 0){
            campaign.setCampaignId(Tools.integerValue(request.getParameter("campaignId"), 0));
            model.addAttribute("campaignId", campaignId);
        }
        if(!Tools.isBlank(startDate)){
            pageInfo.setStartDate(Tools.dateValue(startDate, "yyyy/MM/dd"));
            model.addAttribute("startDate", startDate);
        }
        if(!Tools.isBlank(endDate)){
            pageInfo.setEndDate(Tools.dateValue(endDate, "yyyy/MM/dd"));
            model.addAttribute("endDate", endDate);
        }
        campaign.setAccountId(adv.getAccountId());
        campaignService.page(campaign, pageInfo);
        model.addAttribute("page", pageInfo);
        model.addAttribute("adv", adv);
        JSONObject adsense = new JSONObject();
        for (Map.Entry<String, Object> entry :WebConstants.ADSENSES.entrySet()) {
            adsense.put(entry.getKey(),((JSONObject)entry.getValue()).getString("name"));
        }
        model.addAttribute("adsense", adsense);
        return "ad/admanager/adplanlist";
    }

    @RequestMapping("adlist/{aId}")
    @AdRequestAuth
    public String adList(Model model, PageInfo<Ad> pageInfo, HttpServletRequest request){
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        Ad ad = new Ad();
        String adName = request.getParameter("adName");
        int adGroupId = Tools.intValue(request.getParameter("adGroupId"), 0);
        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("end_date");
        long cId = Tools.longValue(request.getParameter("cId"), 0);
        Integer campaignId = Tools.integerValue(request.getParameter("campaignId"),null);
        String campaignName = request.getParameter("campaignName");
        if(!Tools.isBlank(adName)){
            ad.setAdName(adName);
            model.addAttribute("adName", adName);
        }
        if(adGroupId > 0){
            AdGroup adGroup = new AdGroup();
            adGroup.setAdGroupId(adGroupId);
            ad.setAdGroup(adGroup);
            model.addAttribute("adGroupId", adGroupId);
        }
        if(cId > 0||campaignId != null||!Tools.isBlank(campaignName)){
            Campaign campaign = new Campaign();
            campaign.setId(cId);
            campaign.setCampaignId(campaignId);
            model.addAttribute("campaignId", campaignId);
            campaign.setCampaignName(campaignName);
            model.addAttribute("campaignName", campaignName);
            ad.setCampaign(campaign);
        }
        if(!Tools.isBlank(startDate)){
            pageInfo.setStartDate(Tools.dateValue(startDate, "yyyy/MM/dd"));
            model.addAttribute("startDate", startDate);
        }
        if(!Tools.isBlank(endDate)){
            pageInfo.setEndDate(Tools.dateValue(endDate, "yyyy/MM/dd"));
            model.addAttribute("endDate", endDate);
        }
        ad.setAccountId(adv.getAccountId());
        adService.page(ad, pageInfo);

        model.addAttribute("page", pageInfo);
        model.addAttribute("adv", adv);
        JSONObject adsense = new JSONObject();
        for (Map.Entry<String, Object> entry :WebConstants.ADSENSES.entrySet()) {
            adsense.put(entry.getKey(),((JSONObject)entry.getValue()).getString("name"));
        }
        model.addAttribute("adsense", adsense);
        return "ad/admanager/adlist";
    }

    @RequestMapping("adplan/{aId}")
    @AdRequestAuth
    public String adPlan(Model model, HttpServletRequest request){
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        JSONObject data = new JSONObject();
        data.put("campaigns", campaignService.findAllByAccountId(adv.getAccountId()));
        data.put("adsenses", WebConstants.ADSENSES);
        model.addAttribute("method", "add");
        model.addAttribute("locData", gdtLocationService.ztreeData());
        model.addAttribute("behaviorData", gdtBehaviorService.ztreeData());
        model.addAttribute("interestData", gdtInterestService.ztreeData());
        model.addAttribute("wechatCateData", wechatOfficalAccountGactegoryService.getList());
        model.addAttribute("miniPrograms", miniProgramService.findAll());
        model.addAttribute("targetings", targetingTemplateService.findList());
        model.addAttribute("audiences", adGroupService.getCustomAudiences((Advertiser) request.getAttribute("adv")));
        model.addAttribute("data", data);

        return "ad/adplan/adplan";
    }


    @RequestMapping("campaign/save/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse saveCampaign(Campaign campaign, HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        if(Tools.isBlank(campaign.getCampaignName())){
            response.setStatus(0);
            response.setMsg("参数出错");
        }else{
            Advertiser adv = (Advertiser) request.getAttribute("adv");
            campaign.setAccountId(adv.getAccountId());
            campaign.setSpeedMode("SPEED_MODE_STANDARD");
            campaign.setConfiguredStatus(ConfiguredStatusEnum.AD_STATUS_PENDING.getValue());
            campaign.setIsDeleted(false);
            campaign.setCreateTime(new Date());
            long id = campaignService.saveOrUpdate(campaign).getId();
            if(id == 0){
                response.setStatus(0);
                response.setMsg("服务器出错，联系管理员");
            }else{
                response.setStatus(1);
                response.setData(id);
            }
        }
        return response;
    }


    @RequestMapping("campaign/del/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse delCampaign(HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        String cIds = request.getParameter("cIds");
        if(!Tools.isBlank(cIds)){
            String[] ids = cIds.split(",");
            Arrays.asList(ids).forEach(idStr->{
                long id = Tools.longValue(idStr,0);
                if(id > 0){
                    Campaign campaign = campaignService.findByIdAndAccountId(id, adv.getAccountId());
                    if(campaign != null){
                        campaign.setIsDeleted(true);
                        campaign.setUpdateTime(new Date());
                        if(campaign.getConfiguredStatus().equals(ConfiguredStatusEnum.AD_STATUS_PENDING.getValue())){
                            campaignService.saveOrUpdate(campaign);
                        }else{
                            campaignService.delCampaignByGDT(campaign, adv);
                        }
                    }
                }
            });
            response.setStatus(1);
        }
        return response;
    }


    @RequestMapping("campaign/list_by_account/{aId}/{accountId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse getCampaignsByAccountId(@PathVariable Integer accountId){
        ApiResponse apiResponse = new ApiResponse();
        List<Campaign> campaigns = campaignService.findAllByAccountId(accountId);
        apiResponse.setData(campaigns);
        apiResponse.setStatus(1);
        return apiResponse;
    }


    @RequestMapping("adgroup/save/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse saveAdGroup(AdGroup adGroup, HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        adGroup.setAccountId(adv.getAccountId());
        Campaign campaign = campaignService.findByIdAndAccountId(adGroup.getPrivateCampaignId(), adv.getAccountId());
        if(campaign == null){
            response.setMsg("推广计划不存在！");
        }else{

            adGroup.setPromotedObjectType(campaign.getPromotedObjectType());
            StringBuilder timeSeries = new StringBuilder();
            if(!Tools.isBlank(adGroup.getTimeSeries())){
                String[] times = adGroup.getTimeSeries().split(",");
                for(int i=0;i < 48;i++){
                    timeSeries.append("0000000");
                }
                for(String time:times){
                    String[] timeSlice = time.split("-");
                    timeSeries = GDTApiUtils.getTimeSeries(timeSeries, timeSlice[0], timeSlice[1]);
                }
            }else{
                for(int i=0;i < 48;i++){
                    timeSeries.append("1111111");
                }
            }
            adGroup.setUserActionSets("[{\"type\":\"USER_ACTION_SET_TYPE_WEB\",\"id\":"+adv.getWebUserActionSetId()+"}]");
            adGroup.setTimeSeries(timeSeries.toString());
            adGroup.setIsDeleted(false);
            adGroup.setCreateTime(new Date());
            Long id = adGroupService.saveOrUpdate(adGroup).getId();
            response.setStatus(1);
            response.setData(id);
        }
        return response;
    }


    @RequestMapping("estimation/get/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse getEstimation(AdGroup adGroup, HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        adGroup.setAccountId(adv.getAccountId());
        Campaign campaign = campaignService.findByIdAndAccountId(adGroup.getPrivateCampaignId(), adv.getAccountId());
        if(campaign == null){
            response.setMsg("推广计划不存在！");
        }else{
            Map<String, String> params = new HashMap<>();
            params.put("account_id", String.valueOf(adv.getAccountId()));
            adGroup.setPromotedObjectType(campaign.getPromotedObjectType());
            StringBuilder timeSeries = new StringBuilder();
            if(!Tools.isBlank(adGroup.getTimeSeries())){
                String[] times = adGroup.getTimeSeries().split(",");
                for(int i=0;i < 48;i++){
                    timeSeries.append("0000000");
                }
                for(String time:times){
                    String[] timeSlice = time.split("-");
                    timeSeries = GDTApiUtils.getTimeSeries(timeSeries, timeSlice[0], timeSlice[1]);
                }
            }else{
                for(int i=0;i < 48;i++){
                    timeSeries.append("1111111");
                }
            }
            adGroup.setTimeSeries(timeSeries.toString());
            Map<String, String> adgroupParams = new HashMap<>();
            adgroupParams.put("site_set", adGroup.getSiteSet());
            adgroupParams.put("promoted_object_type", adGroup.getPromotedObjectType());
            adgroupParams.put("billing_event", adGroup.getBillingEvent());
            if(adGroup.getBidAmount() != null && adGroup.getBidAmount() > 0){
                adgroupParams.put("bid_amount", String.valueOf(adGroup.getBidAmount()));
            }

            adgroupParams.put("optimization_goal", adGroup.getOptimizationGoal());
            adgroupParams.put("time_series", adGroup.getTimeSeries());
            if(!Tools.isBlank(adGroup.getPromotedObjectId())){
                adgroupParams.put("promoted_object_id", adGroup.getPromotedObjectId());
            }
            params.put("adgroup", JsonUtils.toJson(adgroupParams));
            Map<String, String> adcreativeParams = new HashMap<>();
            adcreativeParams.put("adcreative_template_id", String.valueOf(WebConstants.ADSENSES.getJSONObject(campaign.getAdsense()).getInteger("id")));
            params.put("adcreative", "["+JsonUtils.toJson(adcreativeParams)+"]");
            Map<String, String> targetingParams = new HashMap<>();
            AdGroupTargeting targeting = adGroup.getTargeting();
            if(targeting != null){
                if(!Tools.isBlank(adGroup.getCustomizedCategory())){
                    targetingParams.put("keyword", "{\"words\":"+adGroup.getCustomizedCategory()+"}");
                }
                if(!Tools.isBlank(targeting.getAge())){
                    targetingParams.put("age", targeting.getAge());
                }
                if(!Tools.isBlank(targeting.getGender())){
                    targetingParams.put("gender", targeting.getGender());
                }
                if(!Tools.isBlank(targeting.getEducation())){
                    targetingParams.put("education", targeting.getEducation());
                }
                if(!Tools.isBlank(targeting.getMaritalStatus())){
                    targetingParams.put("marital_status", targeting.getMaritalStatus());
                }
                if(!Tools.isBlank(targeting.getWorkingStatus())){
                    targetingParams.put("working_status", targeting.getWorkingStatus());
                }
                if(!Tools.isBlank(targeting.getGeoLocation())){
                    targetingParams.put("geo_location", targeting.getGeoLocation());
                }
                if(!Tools.isBlank(targeting.getUserOs())){
                    targetingParams.put("user_os", targeting.getUserOs());
                }
                if(!Tools.isBlank(targeting.getNewDevice())){
                    targetingParams.put("new_device", targeting.getNewDevice());
                }
                if(!Tools.isBlank(targeting.getDevicePrice())){
                    targetingParams.put("device_price", targeting.getDevicePrice());
                }
                if(!Tools.isBlank(targeting.getNetworkType())){
                    targetingParams.put("network_type", targeting.getNetworkType());
                }
                if(!Tools.isBlank(targeting.getNetworkOperator())){
                    targetingParams.put("network_operator", targeting.getNetworkOperator());
                }
                if(!Tools.isBlank(targeting.getNetworkScene())){
                    targetingParams.put("network_scene", targeting.getNetworkScene());
                }
                if(!Tools.isBlank(targeting.getConsumptionStatus())){
                    targetingParams.put("consumption_status", targeting.getConsumptionStatus());
                }
                if(!Tools.isBlank(targeting.getGamerConsumptionAbility())){
                    targetingParams.put("gamer_consumption_ability", targeting.getGamerConsumptionAbility());
                }
                if(!Tools.isBlank(targeting.getResidentialCommunityPrice())){
                    targetingParams.put("residential_community_price", targeting.getResidentialCommunityPrice());
                }
                if(!Tools.isBlank(targeting.getWechatAdBehavior())){
                    targetingParams.put("wechat_ad_behavior", targeting.getWechatAdBehavior());
                }
                if(!Tools.isBlank(targeting.getCustomAudience())){
                    targetingParams.put("custom_audience", targeting.getCustomAudience());
                }
                if(!Tools.isBlank(targeting.getExcludedCustomAudience())){
                    targetingParams.put("excluded_custom_audience", targeting.getExcludedCustomAudience());
                }
                if(!Tools.isBlank(targeting.getBehaviorOrInterest())){
                    targetingParams.put("behavior_or_interest", targeting.getBehaviorOrInterest());
                }
                if(!Tools.isBlank(targeting.getFinancialSituation())){
                    targetingParams.put("financial_situation", targeting.getFinancialSituation());
                }
                if(!Tools.isBlank(targeting.getConsumptionType())){
                    targetingParams.put("consumption_type", targeting.getConsumptionType());
                }
                if(!targetingParams.isEmpty()){
                    params.put("targeting", JsonUtils.toJson(targetingParams));
                }
            }
            Map<String, String> campaignParams = new HashMap<>();
            campaignParams.put("campaign_type", campaign.getCampaignType());
            campaignParams.put("daily_budget", String.valueOf(campaign.getDailyBudget()));
            params.put("campaign_spec", JsonUtils.toJson(campaignParams));
            JSONObject result = GDTApiUtils.getEstimation(adv.getAccessToken(), params);
            if(result == null || result.getIntValue("code") != 0){
                response.setMsg(result == null?"":result.toJSONString());
            }else{
                response.setStatus(1);
                response.setData(result.getJSONObject("data"));
            }


        }
        return response;
    }

    @RequestMapping("adcreative/save/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse saveAdCreative(AdCreative adCreative, HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        Campaign campaign =  campaignService.findByIdAndAccountId(adCreative.getPrivateCampaignId(), adv.getAccountId());
        if(campaign != null){
            adCreative.setCampaignId(campaign.getCampaignId());
            adCreative.setAccountId(adv.getAccountId());
            adCreative.setPromotedObjectType(campaign.getPromotedObjectType());
            adCreative.setIsDeleted(false);
            adCreative.setCreateTime(new Date());
            Long id = adCreativeService.saveOrUpdate(adCreative).getId();
            response.setStatus(1);
            response.setData(id);
        }else{
            response.setMsg("推广计划不存在！");
        }

        return response;
    }



    @RequestMapping("ad/save/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse saveAd(Ad ad, HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        ad.setAccountId(adv.getAccountId());
        ad.setConfiguredStatus(ConfiguredStatusEnum.AD_STATUS_NORMAL.getValue());
        ad.setSystemStatus(AdSystemStatusEnum.AD_STATUS_PENDING.getValue());
        Campaign campaign = campaignService.findById(ad.getCampaign().getId());
        JSONObject campaignResult = null;
        if(campaign.getCampaignId() == null){
            campaign.setConfiguredStatus(ad.getCampaign().getConfiguredStatus());
            campaignResult = campaignService.addCampaignToGDT(campaign, adv);
        }
        if(campaign.getCampaignId() != null && campaign.getCampaignId() > 0){
            AdGroup adGroup = adGroupService.findById(ad.getAdGroup().getId());
            adGroup.setCampaignId(campaign.getCampaignId());
            if(adGroup.getAdGroupId() != null){
                adGroupService.delAdGroupByGDT(adGroup, adv);
            }
            JSONObject adGroupResult = adGroupService.addAdGroupToGDT(adGroup, adv);
            if(adGroup.getAdGroupId()!= null && adGroup.getAdGroupId() > 0){
                AdCreative adCreative = adCreativeService.findById(ad.getAdCreative().getId());
                adCreative.setCampaignId(campaign.getCampaignId());
                if(adCreative.getAdCreativeId() != null){
                    adCreativeService.delAdCreativeByGDT(adCreative, adv);
                }
                JSONObject adCreativeResult = adCreativeService.addAdCreativeToGDT(adCreative,adv);
                if(adCreative.getAdCreativeId() != null && adCreative.getAdCreativeId() > 0){
                    ad.setAdName(adGroup.getAdGroupName());
                    ad.setCampaign(campaign);
                    ad.setAdGroup(adGroup);
                    ad.setIsDeleted(false);
                    ad.setAdCreative(adCreative);
                    ad.setCreateUserId(SessionUtil.getCurUser().getId());
                    JSONObject adResult = adService.addAdToGDT(ad, adv);
                    if(ad.getAdId() != null && ad.getAdId() > 0){
                        adService.saveOrUpdate(ad);
                        response.setStatus(1);
                    }else{
                        response.setMsg("提交广告错误,联系管理员！错误原因：\n"+adResult.toJSONString());
                    }
                }else{
                    response.setMsg("提交创意错误,联系管理员！错误原因：\n"+adCreativeResult.toJSONString());
                }
            }else{
                response.setMsg("提交广告组错误,联系管理员！错误原因：\n"+adGroupResult.toJSONString());
            }

        }else{
            response.setMsg("提交计划错误,联系管理员！错误原因：\n"+campaignResult.toJSONString());
        }
        return response;
    }

    @RequestMapping("ad/qrcode")
    @ResponseBody
    public ApiResponse getQrcode(String path){
        ApiResponse response = new ApiResponse();
        Map<String, String> params = new HashMap<>();
        params.put("path", path);
        params.put("width", "50");
        params.put("auto_color", "false");
        params.put("line_color", "{ \"r\": \"0\", \"g\": \"0\", \"b\": \"0\" }");
        String result = OkHttpUtils.getInstance().post("https://test.8guess.com/mbuy/intf/wx/getQrCode.jsp", params);
        if(result != null){
            JSONObject json = JSONObject.parseObject(result);
            if(json != null && json.getIntValue("code") == 0){
                response.setStatus(1);
                response.setData(json.getString("qrcode"));
            }else{
                response.setMsg(json+"");
            }
        }
        return response;
    }

    @RequestMapping("ad/adplan/continue/{aId}/{cId}")
    @AdRequestAuth
    public String continueAdd(@PathVariable Long cId, Model model, HttpServletRequest request){
        Campaign campaign = campaignService.findById(cId);
        JSONObject data = new JSONObject();
        String method = Tools.stringValue(request.getParameter("method"),"continue");
        model.addAttribute("method", method);
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        data.put("campaigns", campaignService.findAllByAccountId(adv.getAccountId()));
        if(campaign != null){
            data.put("campaign", JSONObject.parseObject(JsonUtils.toJson(campaign)));
            AdGroup adGroup = null;
            AdCreative adCreative = null;
            if("copy".equals(method)){
                long adId = Tools.longValue(request.getParameter("adId"), 0);
                Ad ad = adService.findById(adId);
                if(ad != null){
                    adGroup = ad.getAdGroup();
                    adCreative = ad.getAdCreative();
                }
            }else if("continue".equals(method)){
                adGroup = adGroupService.findByPrivateCampaignId(campaign.getId());
                adCreative = adCreativeService.findByPrivateCampaignId(campaign.getId());

            }
            if(adGroup != null){
                JSONObject params = adGroupToParams(adGroup);
                data.put("params", params);
            }
            if(adCreative != null){
                String materialUrls = adCreative.getMaterialUrls();
                JSONObject adCreativeElements = JSONObject.parseObject(adCreative.getAdCreativeElements());
                adCreativeElements.put("materialUrls", materialUrls);
                adCreativeElements.put("id", adCreative.getId());
                adCreativeElements.put("adCreativeTemplateId", adCreative.getAdCreativeTemplateId());
                adCreativeElements.put("adCreativeType", adCreative.getAdcreativeType());
                if(adCreative.getPageType().equals("PAGE_TYPE_MINI_PROGRAM_WECHAT")){
                    JSONObject mini_program_spec = JSONObject.parseObject(adCreative.getPageSpec()).getJSONObject("mini_program_spec");
                    adCreativeElements.put("mini_program_id", mini_program_spec.getString("mini_program_id"));
                    adCreativeElements.put("mini_program_path", mini_program_spec.getString("mini_program_path"));
                }else{
                    adCreativeElements.put("page_url", JSONObject.parseObject(adCreative.getPageSpec()).getString("page_url"));
                }
                data.put("adCreativeElements", adCreativeElements);
            }
        }
        data.put("adsenses", WebConstants.ADSENSES);
        model.addAttribute("data",data);
        model.addAttribute("locData", gdtLocationService.ztreeData());
        model.addAttribute("behaviorData", gdtBehaviorService.ztreeData());
        model.addAttribute("interestData", gdtInterestService.ztreeData());
        model.addAttribute("wechatCateData", wechatOfficalAccountGactegoryService.getList());
        model.addAttribute("miniPrograms", miniProgramService.findAll());
        model.addAttribute("targetings", targetingTemplateService.findList());
        model.addAttribute("audiences", adGroupService.getCustomAudiences((Advertiser) request.getAttribute("adv")));
        return "ad/adplan/adplan";
    }

    @RequestMapping("ad/adplan/newad/{aId}/{cId}")
    @AdRequestAuth
    public String newAd(@PathVariable Long cId, Model model, HttpServletRequest request){
        Campaign campaign = campaignService.findById(cId);
        JSONObject data = new JSONObject();
        model.addAttribute("method", "newad");
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        data.put("campaigns", campaignService.findAllByAccountId(adv.getAccountId()));
        if(campaign != null){
            data.put("campaign", JSONObject.parseObject(JsonUtils.toJson(campaign)));
        }
        data.put("adsenses", WebConstants.ADSENSES);
        model.addAttribute("data",data);
        model.addAttribute("locData", gdtLocationService.ztreeData());
        model.addAttribute("behaviorData", gdtBehaviorService.ztreeData());
        model.addAttribute("interestData", gdtInterestService.ztreeData());
        model.addAttribute("wechatCateData", wechatOfficalAccountGactegoryService.getList());
        model.addAttribute("miniPrograms", miniProgramService.findAll());
        model.addAttribute("targetings", targetingTemplateService.findList());
        model.addAttribute("audiences", adGroupService.getCustomAudiences((Advertiser) request.getAttribute("adv")));
        return "ad/adplan/adplan";
    }

    @RequestMapping("ad/adplan/batchadd/{aId}/{adId}")
    @AdRequestAuth
    public String batchAdd(@PathVariable Long adId, Model model, HttpServletRequest request){
        Ad ad = adService.findById(adId);
        model.addAttribute("ad", ad);
        return "ad/adplan/batchadd";
    }

    @RequestMapping("ad/adplan/batchsave/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse batchSave(HttpServletRequest request){
        ApiResponse apiResponse = new ApiResponse();
        long adId = Tools.longValue(request.getParameter("adId"), 0);
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        Ad ad = adService.findById(adId);
        if(ad == null){
            apiResponse.setMsg("广告不存在!");
        }else{
            String ads = request.getParameter("ads");
            JSONArray array = JSONArray.parseArray(ads);
            String addType = request.getParameter("addType");
            int success = 0;
            for(int i = 0; i < array.size();i++){
                JSONObject obj = array.getJSONObject(i);
                String beginDate = obj.getString("beginDate");
                String endDate = obj.getString("endDate");
                Ad newAd = copyAd(ad, addType);
                newAd.getAdGroup().setBeginDate(beginDate);
                newAd.getAdGroup().setEndDate(endDate);
                if(addAd(newAd, adv)){
                    success++;
                }
            }
            apiResponse.setStatus(1);
            apiResponse.setMsg("成功创建广告数："+success);
        }

        return apiResponse;
    }

    @RequestMapping("ad/adplan/batchcopy/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse batchCopy(Model model, HttpServletRequest request) throws IOException {
        ApiResponse apiResponse = new ApiResponse();
        int accountId = Tools.intValue(request.getParameter("accountId"), 0);
        Advertiser adv = advertiserService.findByAccountId(accountId);
        if(adv == null){
            apiResponse.setMsg("广告主不存在!");
        }else{
            String campaignType = request.getParameter("campaignType");
            JSONArray ids = JSONArray.parseArray(request.getParameter("ids"));
            JSONObject msg = new JSONObject();
            int success = 0;
            long campaignId = Tools.longValue(request.getParameter("campaignId"), 0);
            Campaign campaign = null;
            if(campaignId > 0){
                campaign = campaignService.findById(campaignId);
                if(campaign == null){
                    apiResponse.setMsg("计划不存在!");
                    return apiResponse;
                }
            }
            for(int i = 0;i < ids.size();i++){
                long id = Tools.longValue(ids.getString(i), 0);
                Ad ad = adService.findById(id);
                if(ad != null){
                    String date = Tools.format(new Date(), "yyyy-MM-dd");
                    if(!"1970-01-01".equals(ad.getAdGroup().getEndDate()) && date.compareTo(ad.getAdGroup().getEndDate()) > 0){
                        msg.put(ad.getAdName(), "广告已已过期，无法复制！");
                    }else{
                        if(date.compareTo(ad.getAdGroup().getBeginDate()) > 0){
                            ad.getAdGroup().setBeginDate(date);
                        }
                        if("1970-01-01".equals(ad.getAdGroup().getEndDate()) ){
                            ad.getAdGroup().setEndDate("");
                        }
                        AdCreative adCreative = ad.getAdCreative();
                        JSONObject adCreativeElements = JSONObject.parseObject(adCreative.getAdCreativeElements());
                        byte[] fileBytes = OkHttpUtils.getInstance().getRemoteImage(adCreative.getMaterialUrls());
                        String signature = Tools.md5(fileBytes);
                        JSONObject image = null;
                        if("image".equals(ad.getAdCreative().getAdcreativeType())){
                            image = GDTApiUtils.addImages(adv.getAccessToken(), String.valueOf(adv.getAccountId()), fileBytes, signature);
                        }else if("video".equals(ad.getAdCreative().getAdcreativeType())){
                            image = GDTApiUtils.addVideos(adv.getAccessToken(), String.valueOf(adv.getAccountId()), fileBytes, signature, "朋友圈视频");
                            if(image != null && image.getIntValue("code") == 0){
                                int isvalid = 0;
                                Integer videoId = image.getJSONObject("data").getInteger("video_id");
                                Map<String, String> params = new HashMap<>();
                                params.put("account_id", adv.getAccountId().toString());
                                params.put("filtering","[{\"field\":\"media_id\",\"operator\":\"EQUALS\",\"values\":["+videoId+"]}]");
                                params.put("fields", "[\"preview_url\",\"system_status\"]");
                                while(isvalid == 0){
                                    JSONObject video = GDTApiUtils.getVideos(adv.getAccessToken(), params);
                                    if(video != null && video.getIntValue("code") == 0){
                                        String systemStatus = video.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("system_status");
                                        if("MEDIA_STATUS_VALID".equals(systemStatus)){
                                            isvalid = 1;
                                        }else if("MEDIA_STATUS_ERROR".equals(systemStatus)){
                                            isvalid = 1;
                                            image = null;
                                        }
                                    }else{
                                        isvalid = 1;
                                        image = null;
                                    }
                                }
                            }
                        }
                        if(image != null && image.getIntValue("code") == 0){
                            String imageId = image.getJSONObject("data").getString("image_id");
                            if("CAMPAIGN_TYPE_WECHAT_MOMENTS".equals(ad.getCampaign().getCampaignType())){
                                if("image".equals(ad.getAdCreative().getAdcreativeType())){
                                    adCreativeElements.put("image_list", "["+imageId+"]");
                                }else if("video".equals(ad.getAdCreative().getAdcreativeType())){
                                    Integer videoId = image.getJSONObject("data").getInteger("video_id");
                                    adCreativeElements.put("short_video_struct","{\"short_video2\":"+imageId+"}");
                                }
                            }else{
                                adCreativeElements.put("image", imageId);
                            }
                            Ad newAd = null;
                            if("old_campaign".equals(campaignType)){
                                newAd = copyAd(ad,"old_campaign");
                                newAd.setCampaign(campaign);
                            }else{
                                newAd = copyAd(ad,"new_campaign");
                            }
                            newAd.getAdCreative().setAdCreativeElements(adCreativeElements.toJSONString());
                            newAd.getAdGroup().setBeginDate(date);
                            newAd.setAccountId(adv.getAccountId());
                            newAd.getAdGroup().setAccountId(adv.getAccountId());
                            newAd.getAdGroup().setUserActionSets("[{\"type\":\"USER_ACTION_SET_TYPE_WEB\",\"id\":"+adv.getWebUserActionSetId()+"}]");
                            newAd.getAdGroup().getTargeting().setCustomAudience(null);
                            newAd.getAdGroup().getTargeting().setExcludedCustomAudience(null);
                            newAd.getCampaign().setAccountId(adv.getAccountId());
                            newAd.getAdCreative().setAccountId(adv.getAccountId());
                            if(!addAd(newAd, adv)){
                                msg.put(ad.getAdName(), "复制错误!");
                            }else{
                                success++;
                            }
                        }else{
                            msg.put(ad.getAdName(), "上传素材出错!");
                        }
                    }
                }else{
                    msg.put("广告不存在", id);
                }
                msg.put("广告复制成功数", success);
                apiResponse.setStatus(1);
                apiResponse.setMsg(msg.toJSONString());
            }
        }
        return apiResponse;
    }

    @RequestMapping("ad/adplan/batchdel/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse batchDel(HttpServletRequest request){
        ApiResponse apiResponse = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        JSONArray ids = JSONArray.parseArray(request.getParameter("ids"));
        for(int i = 0;i < ids.size();i++) {
            long id = ids.getLong(i);
            Ad ad = adService.findById(id);
            if(ad != null){
                ad.setConfiguredStatus(ConfiguredStatusEnum.AD_STATUS_SUSPEND.getValue());
                adService.updateAdToGDT(ad, adv);
                ad.setIsDeleted(true);
                ad.getAdCreative().setIsDeleted(true);
                ad.getAdGroup().setIsDeleted(true);
                adService.delAdByGDT(ad,adv);
                adCreativeService.delAdCreativeByGDT(ad.getAdCreative(),adv);
                adGroupService.delAdGroupByGDT(ad.getAdGroup(),adv);
            }
        }
        apiResponse.setStatus(1);
        return apiResponse;
    }

    @RequestMapping("ad/adplan/batchsub/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse batchSub(HttpServletRequest request){
        ApiResponse apiResponse = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        JSONArray ids = JSONArray.parseArray(request.getParameter("ids"));
        for(int i = 0;i < ids.size();i++) {
            long id = ids.getLong(i);
            Ad ad = adService.findById(id);
            if(ad != null) {
                ad.setConfiguredStatus(ConfiguredStatusEnum.AD_STATUS_SUSPEND.getValue());
                adService.updateAdToGDT(ad, adv);
            }
        }
        apiResponse.setStatus(1);
        return apiResponse;
    }

    @RequestMapping("ad/adplan/batchon/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse batchOn(HttpServletRequest request){
        ApiResponse apiResponse = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        JSONArray ids = JSONArray.parseArray(request.getParameter("ids"));
        for(int i = 0;i < ids.size();i++) {
            long id = ids.getLong(i);
            Campaign campaign = campaignService.findById(id);
            if(campaign != null) {
                campaign.setConfiguredStatus(ConfiguredStatusEnum.AD_STATUS_NORMAL.getValue());
                campaignService.setCampaignConfiguredStatusToGDT(campaign, adv);
            }
        }
        apiResponse.setStatus(1);
        return apiResponse;
    }

    @RequestMapping("adplan/batchdel/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse campaignBatchDel(HttpServletRequest request){
        ApiResponse apiResponse = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        JSONArray ids = JSONArray.parseArray(request.getParameter("ids"));
        for(int i = 0;i < ids.size();i++) {
            long id = ids.getLong(i);
            Campaign campaign = campaignService.findById(id);
            if(campaign != null){
                campaign.setConfiguredStatus(ConfiguredStatusEnum.AD_STATUS_SUSPEND.getValue());
                campaignService.setCampaignConfiguredStatusToGDT(campaign, adv);
                campaign.setIsDeleted(true);
                if(campaign.getCampaignId() != null){
                    campaignService.delCampaignByGDT(campaign, adv);
                }else{
                    campaignService.saveOrUpdate(campaign);
                }

            }
        }
        apiResponse.setStatus(1);
        return apiResponse;
    }

    @RequestMapping("adplan/batchsub/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse campaignBatchSub(HttpServletRequest request){
        ApiResponse apiResponse = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        JSONArray ids = JSONArray.parseArray(request.getParameter("ids"));
        for(int i = 0;i < ids.size();i++) {
            long id = ids.getLong(i);
            Campaign campaign = campaignService.findById(id);
            if(campaign != null) {
                campaign.setConfiguredStatus(ConfiguredStatusEnum.AD_STATUS_SUSPEND.getValue());
                campaignService.setCampaignConfiguredStatusToGDT(campaign, adv);
            }
        }
        apiResponse.setStatus(1);
        return apiResponse;
    }

    @RequestMapping("adplan/batchon/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse campaignBatchOn(HttpServletRequest request){
        ApiResponse apiResponse = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        JSONArray ids = JSONArray.parseArray(request.getParameter("ids"));
        for(int i = 0;i < ids.size();i++) {
            long id = ids.getLong(i);
            Campaign campaign = campaignService.findById(id);
            if(campaign != null) {
                campaign.setConfiguredStatus(ConfiguredStatusEnum.AD_STATUS_NORMAL.getValue());
                campaignService.setCampaignConfiguredStatusToGDT(campaign, adv);
            }
        }
        apiResponse.setStatus(1);
        return apiResponse;
    }

    @RequestMapping("ad/editpage/{aId}/{adId}")
    @AdRequestAuth
    public String editPage(@PathVariable Long adId, Model model, HttpServletRequest request){
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        Ad ad = adService.findById(adId);
        AdGroup adGroup = ad.getAdGroup();
        JSONObject params = adGroupToParams(adGroup);
        model.addAttribute("params", params);
        model.addAttribute("campaignName", ad.getCampaign().getCampaignName());
        model.addAttribute("campaignId", ad.getCampaign().getId());
        model.addAttribute("adv", adv);
        model.addAttribute("locData", gdtLocationService.ztreeData());
        model.addAttribute("behaviorData", gdtBehaviorService.ztreeData());
        model.addAttribute("interestData", gdtInterestService.ztreeData());
        model.addAttribute("wechatCateData", wechatOfficalAccountGactegoryService.getList());
        model.addAttribute("audiences", adGroupService.getCustomAudiences(adv));
        return "ad/adplan/adplan_edit";
    }

    @RequestMapping("ad/editadcreative/{aId}/{adId}")
    @AdRequestAuth
    public String editAdcreative(@PathVariable Long adId, Model model, HttpServletRequest request){
        Ad ad = adService.findById(adId);
        model.addAttribute("adname", ad.getAdName());
        model.addAttribute("adId", ad.getId());
        model.addAttribute("adcreativeId", ad.getAdCreative().getId());
        model.addAttribute("adsense",ad.getCampaign().getAdsense());
        model.addAttribute("imgUrl", ad.getAdCreative().getMaterialUrls());
        model.addAttribute("adsenses", WebConstants.ADSENSES);
        model.addAttribute("adCreativeType", ad.getAdCreative().getAdcreativeType());
        return "ad/adplan/adcreative_edit";
    }


    @RequestMapping("ad/updatecreative/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse updateAdcreativeToGdt(HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        long id = Tools.longValue(request.getParameter("id"), 0);
        AdCreative adCreative = adCreativeService.findById(id);
        if(adCreative != null){
            Campaign campaign = campaignService.findById(adCreative.getPrivateCampaignId());
            JSONObject adCreativeElements = JSONObject.parseObject(adCreative.getAdCreativeElements());
            String imageId = request.getParameter("img");
            String imageUrl = request.getParameter("imageUrl");
            String adcreativeType = request.getParameter("adcreativeType");
            if("CAMPAIGN_TYPE_WECHAT_MOMENTS".equals(campaign.getCampaignType())){
                if("image".equals(adcreativeType)){
                    adCreativeElements.put("image_list", "["+imageId+"]");
                }else if ("video".equals(adcreativeType)){
                    adCreativeElements.put("short_video_struct","{\"short_video2\":"+imageId+"}");
                }
            }else{
                adCreativeElements.put("image", imageId);
            }
            if(adCreative.getPageType().equals("PAGE_TYPE_DEFAULT")){
                if(adCreativeElements.containsKey("mini_program_path")){
                    String path = adCreativeElements.getString("mini_program_path");
                    String mini_program_id = adCreativeElements.getString("mini_program_id");
                    adCreative.setPageSpec("{\"mini_program_spec\":{\"mini_program_id\":\""+mini_program_id+"\",\"mini_program_path\":\""+path+"\"}}");
                    adCreative.setPageType("PAGE_TYPE_MINI_PROGRAM_WECHAT");
                    adCreativeElements.remove("mini_program_path");
                    adCreativeElements.remove("mini_program_id");
                    adCreative.setAdCreativeElements(adCreativeElements.toJSONString());
                }
            }
            adCreative.setMaterialUrls(imageUrl);
            adCreative.setAdCreativeElements(adCreativeElements.toJSONString());
            JSONObject jsonObject = adCreativeService.updateAdCreativeToGDT(adCreative, adv);
            if(jsonObject != null && jsonObject.getIntValue("code") == 0){
                response.setStatus(1);
            }else{
                response.setMsg(jsonObject!=null?jsonObject.toJSONString():"请求出错");
            }
        }else{
            response.setMsg("找不到创意!");
        }
        return response;
    }


    @RequestMapping("adgroup/updateToGdt/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse updateToGdt(AdGroup adGroup, HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        adGroup.setAccountId(adv.getAccountId());
        Campaign campaign = campaignService.findByIdAndAccountId(adGroup.getPrivateCampaignId(), adv.getAccountId());
        if(campaign == null){
            response.setMsg("推广计划不存在！");
        }else{
            AdGroup ag = adGroupService.findById(adGroup.getId());
            if(ag == null){
                response.setMsg("广告不存在！");
                return response;
            }else{
                ag.setCreateTime(ag.getCreateTime());
                ag.setAdGroupId(ag.getAdGroupId());
            }
            ag.setPromotedObjectType(campaign.getPromotedObjectType());
            StringBuilder timeSeries = new StringBuilder();
            if(!Tools.isBlank(adGroup.getTimeSeries())){
                String[] times = adGroup.getTimeSeries().split(",");
                for(int i=0;i < 48;i++){
                    timeSeries.append("0000000");
                }
                for(String time:times){
                    String[] timeSlice = time.split("-");
                    timeSeries = GDTApiUtils.getTimeSeries(timeSeries, timeSlice[0], timeSlice[1]);
                }
            }else{
                for(int i=0;i < 48;i++){
                    timeSeries.append("1111111");
                }
            }
            ag.setUserActionSets("[{\"type\":\"USER_ACTION_SET_TYPE_WEB\",\"id\":"+adv.getWebUserActionSetId()+"}]");
            ag.setTimeSeries(timeSeries.toString());
            AdGroupTargeting targeting = adGroup.getTargeting();
            targeting.setId(ag.getTargeting().getId());
            ag.setTargeting(targeting);
            ag.setCampaignId(campaign.getCampaignId());
            ag.setBeginDate(adGroup.getBeginDate());
            ag.setEndDate(adGroup.getEndDate());
            ag.setBidMethod(adGroup.getBidMethod());
            ag.setBillingEvent(adGroup.getBillingEvent());
            ag.setCpcExpandEnabled(adGroup.getCpcExpandEnabled());
            ag.setCustomizedCategory(adGroup.getCustomizedCategory());
            ag.setCustomizedCategory(adGroup.getCustomizedCategory());
            ag.setExpandTargeting(adGroup.getExpandTargeting());
            ag.setFrequencyCapping(adGroup.getFrequencyCapping());
            ag.setOcpaExpandEnabled(adGroup.getOcpaExpandEnabled());
            ag.setPromotedObjectId(adGroup.getPromotedObjectId());
            JSONObject json = adGroupService.updateAdGroupToGDT(ag, adv);
            if(json != null&&json.getIntValue("code") == 0){
                response.setStatus(1);
            }else{
                response.setMsg(json.toJSONString());
            }
        }
        return response;
    }


    private JSONObject adGroupToParams(AdGroup adGroup){
        JSONObject params = new JSONObject();
        params.put("id", adGroup.getId());
        params.put("adGroupName",adGroup.getAdGroupName());
        params.put("beginDate", adGroup.getBeginDate().replaceAll("-", "/"));
        params.put("endDate", adGroup.getEndDate().replaceAll("-", "/"));
        if(!Tools.isBlank(adGroup.getTimeSeries())&&!GDTApiUtils.ALL_TIEM_SERIES.equals(adGroup.getTimeSeries())){
            Map<String, Stack<Map<String, String>>> timeMap = GDTApiUtils.transTimeSeries(adGroup.getTimeSeries());
            Stack<Map<String, String>> timeSeries = timeMap.get("0");
            for(Map<String, String> time:timeSeries){
                String startTime = time.get("start");
                if(startTime.length()==4){
                    time.put("start", "0" + startTime);
                }
                String endTime = time.get("end");
                if(endTime.length() == 4){
                    time.put("end", "0" + endTime);
                }
            }
            params.put("timeSeries", timeSeries);
            params.put("custom_time", 1);
        }
        params.put("bidMethod", adGroup.getBidMethod());
        params.put("bidAmount", adGroup.getBidAmount()/100);
        if(!Tools.isBlank(adGroup.getCustomizedCategory())){
            params.put("customized_category", adGroup.getCustomizedCategory());
        }
        if("true".equals(adGroup.getOcpaExpandEnabled())||"true".equals(adGroup.getCpcExpandEnabled())){
            params.put("expand_enabled", "true");
            if(!Tools.isBlank(adGroup.getExpandTargeting())){
                params.put("expand_targeting", adGroup.getExpandTargeting());
            }
        }
        AdGroupTargeting targeting = adGroup.getTargeting();
        if(targeting != null){
            params.put("targetingId", targeting.getId());
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

        }

        return params;
    }

    @GetMapping("admonitor/autoCreate/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse autoCreate(){
        List<Ad> adList = adService.findAll();
        for (Ad ad : adList) {
            adMonitorConfigService.createDefaultConfig(ad);
        }

        return ApiResponse.ok();
    }

    @GetMapping("admonitor/smstest/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse smstest(){
        String phone = "15918729412";
        int adId = 123456;
        String adName = "测试广告";
        AliSMSUtil.adFirstOrderNotify(phone, adId, adName);
        AliSMSUtil.adOrderIntervalNotify(phone, adId, adName, 100);
        AliSMSUtil.adCostThresholdExceedNotify(phone, adId, adName, 12300, 145000);
        AliSMSUtil.adBudgetThresholdExceedNotify(phone, adId, adName, 0.455);
        AliSMSUtil.adRoiThresholdExceedNotify(phone, adId, adName, 0.9, 1.2);
        AliSMSUtil.adRoiCauseAutoPauseNotify(phone, adId, adName, 0.9, 1.2);

        return ApiResponse.ok();
    }

    @GetMapping("admonitor/get/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse getAdMonotorConfig(int adId, HttpServletRequest request){
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        Ad ad = adService.findByAdIdAndAccountId(adId, adv.getAccountId());
        if (ad == null) {
            return ApiResponse.error("广告不存在！");
        }

        AdMonitorConfig conf = adMonitorConfigService.findByAdId(adId);
        if (conf == null) {
            return ApiResponse.error("预警配置不存在！");
        }

        return ApiResponse.ok(conf);
    }

    @PostMapping("admonitor/save/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse saveAdMonotorConfig(@Validated AdMonitorConfigVo param, BindingResult br, HttpServletRequest request){
        if (br.hasErrors()) {
            return ApiResponse.error(br.getFieldError().getDefaultMessage());
        }

        Advertiser adv = (Advertiser) request.getAttribute("adv");
        AdMonitorConfig conf = adMonitorConfigService.findByAdId(param.getAdId());
        if (!adv.getAccountId().equals(conf.getAccountId())) {
            return ApiResponse.error("广告计划ID参数有误！");
        }

        Date now = new Date();
        DecimalFormat df = new DecimalFormat("#.00");

        String budgetStr = df.format(param.getDailyBudgetThreshold() * 100D);
        param.setDailyBudgetThreshold(Double.valueOf(budgetStr) / 100D);

        String roiStr = df.format(param.getRoiThreshold());
        param.setRoiThreshold(Double.valueOf(roiStr));

        if (!param.getDailyBudgetThreshold().equals(conf.getDailyBudgetThreshold())) {
            alarmService.clearFlagBudgetThresholdExceed(now, param.getAdId()); // 清除当天的预算剩余预警标识
        }

        if (!param.getDailyCostThreshold().equals(conf.getDailyCostThreshold())) {
            alarmService.clearFlagCostThresholdExceed(now, param.getAdId()); // 清除预算消耗预警标识
        }

        if (!param.getRoiThreshold().equals(conf.getRoiThreshold())) { // 修改后清除预警标识
            String flag = FLAG_ROI_THRESHOLD_ALARM + "-" + param.getAdId();
            alarmFlagService.clearFlag(flag);
        }

        BeanUtils.copyProperties(param, conf);
        conf.setUpdateTime(now);
        adMonitorConfigService.saveOrUpdate(conf);

        return ApiResponse.ok(conf);
    }

    private Ad copyAd(Ad ad, String addType){
        Ad newAd = new Ad();
        BeanUtils.copyProperties(ad, newAd);
        newAd.setSystemStatus(AdSystemStatusEnum.AD_STATUS_PENDING.getValue());
        newAd.setRejectMessage("");
        AdGroup adGroup = new AdGroup();
        BeanUtils.copyProperties(ad.getAdGroup(), adGroup);
        AdGroupTargeting targeting = new AdGroupTargeting();
        BeanUtils.copyProperties(ad.getAdGroup().getTargeting(), targeting);
        targeting.setAdGroup(null);
        targeting.setId(null);
        targeting.setCreateTime(new Date());
        targeting.setUpdateTime(new Date());
        adGroup.setTargetingId(null);
        adGroup.setTargeting(targeting);
        adGroup.setPrivateCampaignId(null);
        adGroup.setCampaignId(null);
        adGroup.setId(null);
        adGroup.setAdGroupId(null);
        adGroup.setCreateTime(new Date());
        adGroup.setUpdateTime(new Date());
        adGroup.setIsDeleted(false);
        AdCreative adCreative = new AdCreative();
        BeanUtils.copyProperties(ad.getAdCreative(), adCreative);
        adCreative.setId(null);
        adCreative.setAdCreativeId(null);
        adCreative.setCreateTime(new Date());
        adCreative.setUpdateTime(new Date());
        adCreative.setCampaignId(null);
        adCreative.setPrivateCampaignId(null);
        adCreative.setIsDeleted(false);
        if("new_campaign".equals(addType)){
            Campaign campaign = new Campaign();
            BeanUtils.copyProperties(ad.getCampaign(), campaign);
            campaign.setId(null);
            campaign.setCampaignId(null);
            campaign.setCreateTime(new Date());
            campaign.setUpdateTime(new Date());
            campaign.setIsDeleted(false);
            newAd.setCampaign(campaign);
        }
        newAd.setId(null);
        newAd.setAdId(null);
        newAd.setUpdateTime(new Date());
        newAd.setCreateTime(new Date());

        newAd.setAdCreative(adCreative);
        newAd.setAdGroup(adGroup);
        newAd.setIsDeleted(false);
        return newAd;
    }

    @RequestMapping("ad/editname/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse editAdName(String adName, long adId, HttpServletRequest request){
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        ApiResponse response = new ApiResponse();
        Ad ad = adService.findById(adId);
        if(ad != null && !Tools.isBlank(adName)){
            AdGroup adGroup = ad.getAdGroup();
            adGroup.setAdGroupName(adName);
            JSONObject json = adGroupService.updateAdGroupNameToGDT(adGroup,adv);
            if(json == null || json.getIntValue("code") != 0){
                response.setMsg(json == null?"请求出错":json.toJSONString());
                return response;
            }
        }
        response.setStatus(1);
        return response;

    }


    private boolean addAd(Ad ad, Advertiser adv){
        Campaign  campaign = null;
        if(ad.getCampaign().getId() == null){
            campaign = campaignService.saveOrUpdate(ad.getCampaign());
            JSONObject campaignResult = campaignService.addCampaignToGDT(campaign, adv);
        }else{
            campaign = ad.getCampaign();
        }
        if(campaign.getCampaignId() != null && campaign.getCampaignId() > 0){
            ad.getAdGroup().setCampaignId(campaign.getCampaignId());
            ad.getAdGroup().setPrivateCampaignId(campaign.getId());
            AdGroup adGroup = adGroupService.saveOrUpdate(ad.getAdGroup());
            JSONObject adGroupResult = adGroupService.addAdGroupToGDT(adGroup, adv);
            if(adGroup.getAdGroupId()!= null && adGroup.getAdGroupId() > 0){
                AdCreative adCreative = ad.getAdCreative();
                if(adCreative.getPageType().equals("PAGE_TYPE_DEFAULT")){
                    JSONObject adCreativeElements = JSONObject.parseObject(adCreative.getAdCreativeElements());
                    if(adCreativeElements.containsKey("mini_program_path")){
                        String path = adCreativeElements.getString("mini_program_path");
                        String mini_program_id = adCreativeElements.getString("mini_program_id");
                        path = path.substring(0, path.indexOf("&from=gdt"))+"&from=gdt&account_id="+adv.getAccountId()+"&user_action_set_id="+adv.getWebUserActionSetId()+"&adGroupId="+adGroup.getId();
                        adCreative.setPageSpec("{\"mini_program_spec\":{\"mini_program_id\":\""+mini_program_id+"\",\"mini_program_path\":\""+path+"\"}}");
                        adCreative.setPageType("PAGE_TYPE_MINI_PROGRAM_WECHAT");
                        adCreativeElements.remove("mini_program_path");
                        adCreativeElements.remove("mini_program_id");
                        adCreative.setAdCreativeElements(adCreativeElements.toJSONString());
                    }
                }else if(adCreative.getPageType().equals("PAGE_TYPE_MINI_PROGRAM_WECHAT")){
                    JSONObject pageSpec = JSONObject.parseObject(adCreative.getPageSpec());
                    String path = pageSpec.getJSONObject("mini_program_spec").getString("mini_program_path");
                    path = path.substring(0, path.indexOf("&from=gdt"))+"&from=gdt&account_id="+adv.getAccountId()+"&user_action_set_id="+adv.getWebUserActionSetId()+"&adGroupId="+adGroup.getId();
                    pageSpec.getJSONObject("mini_program_spec").put("mini_program_path", path);
                    adCreative.setPageSpec(pageSpec.toJSONString());
                }
                adCreative.setCampaignId(campaign.getCampaignId());
                adCreative.setPrivateCampaignId(campaign.getId());
                adCreative = adCreativeService.saveOrUpdate(adCreative);
                JSONObject adCreativeResult = adCreativeService.addAdCreativeToGDT(adCreative,adv);
                if(adCreative.getAdCreativeId() != null && adCreative.getAdCreativeId() > 0){
                    ad.setAdName(adGroup.getAdGroupName());
                    ad.setCampaign(campaign);
                    ad.setAdGroup(adGroup);
                    ad.setAdCreative(adCreative);
                    ad.setCreateUserId(SessionUtil.getCurUser().getId());
                    JSONObject adResult = adService.addAdToGDT(ad, adv);
                    if(ad.getAdId() != null && ad.getAdId() > 0){
                        adService.saveOrUpdate(ad);
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
        return true;
    }


    @RequestMapping("target_tags/behavior_interest/get/{aId}")
    @AdRequestAuth
    @ResponseBody
    public ApiResponse  getBehaviorOrInterest(String type, String keyword, HttpServletRequest request){
        ApiResponse response = new ApiResponse();
        Advertiser adv = (Advertiser) request.getAttribute("adv");
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        if("BEHAVIOR".equals(type)){
            params.put("tag_spec", "{\"behavior_spec\":{\"query_mode\":\"TARGETING_TAG_QUERY_MODE_SEARCH\",\"query_spec\":{\"query\":\""+keyword+"\",\"max_result_number\":50,\"advanced_recommend_type\":\"ADVANCED_RECOMMEND_TYPE_INDUSTRY_HOT\"}}}");
        }else if("INTEREST".equals(type)){
            params.put("tag_spec", "{\"interest_spec\":{\"query_mode\":\"TARGETING_TAG_QUERY_MODE_SEARCH\",\"query_spec\":{\"query\":\""+keyword+"\",\"max_result_number\":50,\"advanced_recommend_type\":\"ADVANCED_RECOMMEND_TYPE_INDUSTRY_HOT\"}}}");
        }
        JSONObject targetTags =  GDTApiUtils.getTargetingTags(adv.getAccessToken(), params);
        if(targetTags != null && targetTags.getIntValue("code") == 0){
            JSONArray array = targetTags.getJSONObject("data").getJSONArray("list");
            response.setData(array);
            response.setStatus(1);
        }

        return response;
    }
}
