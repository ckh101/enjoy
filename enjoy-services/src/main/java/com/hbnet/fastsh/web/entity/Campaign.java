package com.hbnet.fastsh.web.entity;

import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.mongodb.model.CampaignDailyReport;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "smartad_campaign")
@Getter
@Setter
public class Campaign extends BaseEntity{


    private Integer campaignId;

    private Integer accountId;

    private String campaignName;

    private String campaignType;

    private String promotedObjectType;

    private String adsense;

    private Integer dailyBudget;

    private String configuredStatus;

    private String speedMode;

    private String budgetReachDate;

    private Boolean isDeleted;

    @Transient
    private CampaignDailyReport campaignDailyReport;

    public void jsonToObject(JSONObject json){
        this.setCampaignId(json.getIntValue("campaign_id"));
        this.setCampaignName(json.getString("campaign_name"));
        this.setConfiguredStatus(json.getString("configured_status"));
        this.setCampaignType(json.getString("campaign_type"));
        this.setPromotedObjectType(json.getString("promoted_object_type"));
        this.setDailyBudget(json.getIntValue("daily_budget"));
        this.setBudgetReachDate(json.getString("budget_reach_date"));
        this.setSpeedMode(json.getString("speed_mode"));
        this.setIsDeleted(json.getBoolean("is_deleted"));
    }
}
