package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.utils.JsonUtils;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.AdGroup;
import com.hbnet.fastsh.web.entity.AdGroupTargeting;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.repositories.AdGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
public class AdGroupService {

    @Autowired
    AdGroupRepository adGroupRepository;

    public AdGroup findById(Long id){
        Optional<AdGroup> optional = adGroupRepository.findById(id);
        return optional.isPresent()?optional.get():null;
    }
    public AdGroup saveOrUpdate(AdGroup adGroup){
        return adGroupRepository.saveAndFlush(adGroup);
    }

    public AdGroup findByAdGroupId(Integer adGroupId){
        return adGroupRepository.findByAdGroupId(adGroupId);
    }

    public AdGroup findByAdGroupIdAndAccountId(Integer adGroupId, Integer accountId){
        return adGroupRepository.findByAdGroupIdAndAccountId(adGroupId,accountId);
    }
    public AdGroup findByIdAndAccountId(Long id, Integer accountId){
        return adGroupRepository.findByIdAndAccountId(id,accountId);
    }

    public AdGroup findByPrivateCampaignId(Long privateCampaignId){
        return adGroupRepository.findByPrivateCampaignId(privateCampaignId);
    }

    @Transactional
    public JSONObject addAdGroupToGDT(AdGroup adGroup, Advertiser adv){
        JSONObject result = null;
        Map<String, String> params = adgroupToParams(adGroup);
        params.put("account_id", String.valueOf(adv.getAccountId()));
        params.put("campaign_id", String.valueOf(adGroup.getCampaignId()));
        result = GDTApiUtils.addAdGroups(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result == null?"http error":result.toJSONString());
        }else{
            adGroup.setAdGroupId(result.getJSONObject("data").getIntValue("adgroup_id"));
            saveOrUpdate(adGroup);
        }
        return result;
    }

    @Transactional
    public JSONObject updateAdGroupToGDT(AdGroup adGroup, Advertiser adv){
        saveOrUpdate(adGroup);
        Map<String, String> params = adgroupToParams(adGroup);
        params.remove("optimization_goal");
        params.put("account_id", String.valueOf(adv.getAccountId()));
        params.put("adgroup_id", String.valueOf(adGroup.getAdGroupId()));
        JSONObject result = GDTApiUtils.updateAdGroups(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }
        return result;
    }

    @Transactional
    public JSONObject updateAdGroupNameToGDT(AdGroup adGroup, Advertiser adv){
        saveOrUpdate(adGroup);
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(adv.getAccountId()));
        params.put("adgroup_id", String.valueOf(adGroup.getAdGroupId()));
        params.put("adgroup_name", adGroup.getAdGroupName());
        JSONObject result = GDTApiUtils.updateAdGroups(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }
        return result;
    }


    @Transactional
    public JSONObject delAdGroupByGDT(AdGroup adGroup, Advertiser adv){
        saveOrUpdate(adGroup);
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(adv.getAccountId()));
        params.put("adgroup_id", String.valueOf(adGroup.getAdGroupId()));
        JSONObject result = GDTApiUtils.deleteAdGroups(adv.getAccessToken(), params);
        if(result == null||result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }
        return result;
    }



    public JSONObject getAdGroupsByGDT(JSONArray filtering, Advertiser adv, int page, int pageSize){
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(adv.getAccountId()));
        JSONArray fields = new JSONArray();
        fields.add("campaign_id");
        fields.add("adgroup_id");
        fields.add("adgroup_name");
        fields.add("site_set");
        fields.add("optimization_goal");
        fields.add("billing_event");
        fields.add("bid_amount");
        fields.add("daily_budget");
        fields.add("promoted_object_type");
        fields.add("promoted_object_id");
        fields.add("targeting_id");
        fields.add("is_include_unsupported_targeting");
        fields.add("begin_date");
        fields.add("end_date");
        fields.add("time_series");
        fields.add("configured_status");
        fields.add("customized_category");
        fields.add("is_deleted");
        fields.add("user_action_sets");
        fields.add("targeting");
        params.put("fields", fields.toJSONString());
        if(filtering != null){
            params.put("filtering", filtering.toJSONString());
        }
        if(page > 0){
            params.put("page", String.valueOf(page));
        }
        if(pageSize > 0){
            params.put("page_size", String.valueOf(pageSize));
        }
        return GDTApiUtils.getAdGroups(adv.getAccessToken(), params);
    }
    public Map<String, String> adgroupToParams(AdGroup adGroup){
        Map<String, String> params = new HashMap<>();
        if(!Tools.isBlank(adGroup.getAdGroupName())){
            params.put("adgroup_name", adGroup.getId()+"-"+adGroup.getAdGroupName()+"-"+System.currentTimeMillis());
        }
        if(!Tools.isBlank(adGroup.getSiteSet())){
            params.put("site_set", adGroup.getSiteSet());
        }
        if(!Tools.isBlank(adGroup.getPromotedObjectType())){
            params.put("promoted_object_type", adGroup.getPromotedObjectType());
        }
        if(!Tools.isBlank(adGroup.getBeginDate())){
            params.put("begin_date", adGroup.getBeginDate());
        }
        if(!Tools.isBlank(adGroup.getEndDate())){
            params.put("end_date", adGroup.getEndDate());
        }
        params.put("billing_event", adGroup.getBillingEvent());
        params.put("bid_amount", String.valueOf(adGroup.getBidAmount()));
        params.put("optimization_goal", adGroup.getOptimizationGoal());
        params.put("time_series", adGroup.getTimeSeries());
        if(adGroup.getDailyBudget() != null && adGroup.getDailyBudget() > 0){
            params.put("daily_budget", String.valueOf(adGroup.getDailyBudget()));
        }
        if(!Tools.isBlank(adGroup.getPromotedObjectId())){
            params.put("promoted_object_id", adGroup.getPromotedObjectId());
        }
        if(!Tools.isBlank(adGroup.getConfiguredStatus())){
            params.put("configured_status", adGroup.getConfiguredStatus());
        }
        if(!Tools.isBlank(adGroup.getCustomizedCategory())){
            params.put("customized_category", adGroup.getCustomizedCategory());
        }
        if(!Tools.isBlank(adGroup.getUserActionSets())){
            params.put("user_action_sets", adGroup.getUserActionSets());
        }
        JSONObject targetingJson = new JSONObject();
        AdGroupTargeting targeting = adGroup.getTargeting();
        if(!Tools.isBlank(targeting.getAge())){
            targetingJson.put("age", targeting.getAge());
        }
        if(!Tools.isBlank(targeting.getGender())){
            targetingJson.put("gender", targeting.getGender());
        }
        if(!Tools.isBlank(targeting.getEducation())){
            targetingJson.put("education", targeting.getEducation());
        }
        if(!Tools.isBlank(targeting.getMaritalStatus())){
            targetingJson.put("marital_status", targeting.getMaritalStatus());
        }
        if(!Tools.isBlank(targeting.getWorkingStatus())){
            targetingJson.put("working_status", targeting.getWorkingStatus());
        }
        if(!Tools.isBlank(targeting.getGeoLocation())){
            targetingJson.put("geo_location", targeting.getGeoLocation());
        }
        if(!Tools.isBlank(targeting.getUserOs())){
            targetingJson.put("user_os", targeting.getUserOs());
        }
        if(!Tools.isBlank(targeting.getNewDevice())){
            targetingJson.put("new_device", targeting.getNewDevice());
        }
        if(!Tools.isBlank(targeting.getDevicePrice())){
            targetingJson.put("device_price", targeting.getDevicePrice());
        }
        if(!Tools.isBlank(targeting.getNetworkType())){
            targetingJson.put("network_type", targeting.getNetworkType());
        }
        if(!Tools.isBlank(targeting.getNetworkOperator())){
            targetingJson.put("network_operator", targeting.getNetworkOperator());
        }
        if(!Tools.isBlank(targeting.getNetworkScene())){
            targetingJson.put("network_scene", targeting.getNetworkScene());
        }
        if(!Tools.isBlank(targeting.getConsumptionStatus())){
            targetingJson.put("consumption_status", targeting.getConsumptionStatus());
        }
        if(!Tools.isBlank(targeting.getGamerConsumptionAbility())){
            targetingJson.put("gamer_consumption_ability", targeting.getGamerConsumptionAbility());
        }
        if(!Tools.isBlank(targeting.getResidentialCommunityPrice())){
            targetingJson.put("residential_community_price", targeting.getResidentialCommunityPrice());
        }
        if(!Tools.isBlank(targeting.getWechatAdBehavior())){
            targetingJson.put("wechat_ad_behavior", targeting.getWechatAdBehavior());
        }
        if(!Tools.isBlank(targeting.getCustomAudience())){
            targetingJson.put("custom_audience", targeting.getCustomAudience());
        }
        if(!Tools.isBlank(targeting.getExcludedCustomAudience())){
            targetingJson.put("excluded_custom_audience", targeting.getExcludedCustomAudience());
        }
        if(!Tools.isBlank(targeting.getBehaviorOrInterest())){
            targetingJson.put("behavior_or_interest", targeting.getBehaviorOrInterest());
        }
        if(!Tools.isBlank(targeting.getWechatOfficialAccountCategory())){
            targetingJson.put("wechat_official_account_category", targeting.getWechatOfficialAccountCategory());
        }
        if(!Tools.isBlank(targeting.getFinancialSituation())){
            targetingJson.put("financial_situation", targeting.getFinancialSituation());
        }
        if(!Tools.isBlank(targeting.getConsumptionType())){
            targetingJson.put("consumption_type", targeting.getConsumptionType());
        }
        if(!Tools.isBlank(targeting.getUvIndex())){
            targetingJson.put("uv_index", targeting.getUvIndex());
        }
        if(!Tools.isBlank(targeting.getMakeupIndex())){
            targetingJson.put("makeup_index", targeting.getMakeupIndex());
        }
        if(!Tools.isBlank(targeting.getClimate())){
            targetingJson.put("climate", targeting.getClimate());
        }
        if(!Tools.isBlank(targeting.getDressingIndex())){
            targetingJson.put("dressing_index", targeting.getDressingIndex());
        }
        if(!Tools.isBlank(targeting.getAirQualityIndex())){
            targetingJson.put("air_quality_index", targeting.getAirQualityIndex());
        }
        if(!Tools.isBlank(targeting.getTemperature())){
            targetingJson.put("temperature", targeting.getTemperature());
        }
        params.put("targeting", targetingJson.toJSONString());
        if(!Tools.isBlank(adGroup.getOcpaExpandEnabled())&&"true".equals(adGroup.getOcpaExpandEnabled())){
            params.put("expand_enabled", adGroup.getOcpaExpandEnabled());
            JSONObject ocpa_expand_targeting = new JSONObject();
            String expandTargeting = adGroup.getExpandTargeting();
            if(!Tools.isBlank(expandTargeting)){
                if(!Tools.isBlank(targeting.getAge())&&expandTargeting.contains("age")){
                    ocpa_expand_targeting.put("age", targeting.getAge());
                }
                if(!Tools.isBlank(targeting.getGender())&&expandTargeting.contains("gender")){
                    ocpa_expand_targeting.put("gender", targeting.getGender());
                }
                if(!Tools.isBlank(targeting.getGeoLocation())&&expandTargeting.contains("geo_location")){
                    ocpa_expand_targeting.put("geo_location", targeting.getGeoLocation());
                }
            }

            params.put("expand_targeting", ocpa_expand_targeting.toJSONString());
        }else if(!Tools.isBlank(adGroup.getCpcExpandEnabled())&&"true".equals(adGroup.getCpcExpandEnabled())){
            String expandTargeting = adGroup.getExpandTargeting();
            params.put("expand_enabled", adGroup.getOcpaExpandEnabled());
            JSONObject ocpa_expand_targeting = new JSONObject();
            if(!Tools.isBlank(targeting.getAge())&&expandTargeting.contains("age")){
                ocpa_expand_targeting.put("age", targeting.getAge());
            }
            if(!Tools.isBlank(targeting.getGender())&&expandTargeting.contains("gender")){
                ocpa_expand_targeting.put("gender", targeting.getGender());
            }
            if(!Tools.isBlank(targeting.getGeoLocation())&&expandTargeting.contains("gender")){
                ocpa_expand_targeting.put("geo_location", targeting.getGeoLocation());
            }
            params.put("expand_targeting", ocpa_expand_targeting.toJSONString());
        }
        return params;
    }


    public JSONArray getCustomAudiences(Advertiser adv){
        JSONArray list = null;
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(adv.getAccountId()));
        JSONArray fields = new JSONArray();
        fields.add("audience_id");
        fields.add("name");
        fields.add("status");
        fields.add("type");
        params.put("fields", fields.toJSONString());
        params.put("page", "1");
        params.put("page_size", "100");
        JSONObject json = GDTApiUtils.getCustomAudiences(adv.getAccessToken(), params);
        if(json != null && json.getIntValue("code") == 0){
            list = json.getJSONObject("data").getJSONArray("list");
        }
        System.out.println(list);
        return list;
    }


}
