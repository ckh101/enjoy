package com.hbnet.fastsh.web.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name="smartad_adgroup")
@Getter
@Setter
public class AdGroup extends BaseEntity {


    private Integer adGroupId;

    private Integer accountId;

    private Integer campaignId;

    private Long privateCampaignId;

    private String adGroupName;

    private String siteSet;

    private String promotedObjectType;

    private String beginDate;

    private String endDate;

    private String billingEvent;

    private Integer bidAmount;

    private String optimizationGoal;

    private String timeSeries;

    private Integer dailyBudget;

    private String promotedObjectId;

    private Integer gdtTargetingId;

    private Integer targetingId;

    private String configuredStatus;

    private String customizedCategory;

    private Integer frequencyCapping;

    private String ocpaExpandEnabled;

    private String userActionSets;

    private String cpcExpandEnabled;

    private String ExpandTargeting;

    private Boolean isDeleted;

    private String bidMethod;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="privateTargetingId", referencedColumnName = "id")
    @NotFound(action= NotFoundAction.IGNORE)
    AdGroupTargeting targeting;


    public void jsonToObject(JSONObject json){
        this.setCampaignId(json.getIntValue("campaign_id"));
        this.setAdGroupId(json.getIntValue("adgroup_id"));
        this.setAdGroupName(json.getString("adgroup_name"));
        this.setSiteSet(json.getString("site_set"));
        this.setOptimizationGoal(json.getString("optimization_goal"));
        this.setBillingEvent(json.getString("billing_event"));
        this.setBidAmount(json.getIntValue("bid_amount"));
        this.setDailyBudget(json.getIntValue("daily_budget"));
        this.setPromotedObjectType(json.getString("promoted_object_type"));
        this.setPromotedObjectId(json.getString("promoted_object_id"));
        this.setTargetingId(json.getIntValue("targeting_id"));
        this.setBeginDate(json.getString("begin_date"));
        this.setEndDate(json.getString("end_date"));
        this.setTimeSeries(json.getString("time_series"));
        this.setConfiguredStatus(json.getString("configured_status"));
        this.setCustomizedCategory(json.getString("customized_category"));
        this.setUserActionSets(json.getString("user_action_sets"));
        this.setIsDeleted(json.getBoolean("is_deleted"));
        JSONObject targetingJson = json.getJSONObject("targeting");
        this.targeting.setAge(targetingJson.getString("age"));
        this.targeting.setGender(targetingJson.getString("gender"));
        this.targeting.setEducation(targetingJson.getString("education"));
        this.targeting.setMaritalStatus(targetingJson.getString("marital_status"));
        this.targeting.setWorkingStatus(targetingJson.getString("working_status"));
        this.targeting.setGeoLocation(targetingJson.getString("geo_location"));
        this.targeting.setUserOs(targetingJson.getString("user_os"));
        this.targeting.setNewDevice(targetingJson.getString("new_device"));
        this.targeting.setDevicePrice(targetingJson.getString("device_price"));
        this.targeting.setNetworkType(targetingJson.getString("network_type"));
        this.targeting.setNetworkOperator(targetingJson.getString("network_operator"));
        this.targeting.setNetworkScene(targetingJson.getString("network_scene"));
        this.targeting.setConsumptionStatus(targetingJson.getString("consumption_status"));
        this.targeting.setGamerConsumptionAbility(targetingJson.getString("gamer_consumption_ability"));
        this.targeting.setResidentialCommunityPrice(targetingJson.getString("residential_community_price"));
        this.targeting.setWechatAdBehavior(targetingJson.getString("wechat_ad_behavior"));
        this.targeting.setCustomAudience(targetingJson.getString("custom_audience"));
        this.targeting.setExcludedCustomAudience(targetingJson.getString("excluded_custom_audience"));
        this.targeting.setBehaviorOrInterest(targetingJson.getString("behavior_or_interest"));
        this.targeting.setWechatOfficialAccountCategory(targetingJson.getString("wechat_official_account_category"));
        this.targeting.setFinancialSituation(targetingJson.getString("financial_situation"));
        this.targeting.setConsumptionType(targetingJson.getString("consumption_type"));
    }
}
