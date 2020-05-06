package com.hbnet.fastsh.web.entity;

import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.mongodb.model.AdDailyReport;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;


@Entity
@Table(name = "smartad_ad")
@Getter
@Setter
public class Ad extends BaseEntity{
   
    private Integer adId;

    private Integer accountId;

    private String adName;

    private String configuredStatus;

    private String systemStatus;

    private String impressionTrackingUrl;

    private String clickTrackingUrl;

    private String feedsIntegereractionEnabled;

    private Boolean isDeleted;

    private String rejectMessage;

    private Long createUserId;

    @ManyToOne
    @JoinColumn(name="adCreativeId", referencedColumnName = "id")
    @NotFound(action=NotFoundAction.IGNORE)
    private AdCreative adCreative;

    @ManyToOne
    @JoinColumn(name="adgroupId", referencedColumnName = "id")
    @NotFound(action= NotFoundAction.IGNORE)
    private AdGroup adGroup;

    @ManyToOne
    @JoinColumn(name="campaignId", referencedColumnName = "id")
    @NotFound(action=NotFoundAction.IGNORE)
    private Campaign campaign;

    @Transient
    private AdDailyReport adDailyReport;

    public void jsonToObject(JSONObject json){
        this.setAdId(json.getInteger("ad_id"));
        this.setAdName(json.getString("ad_name"));
        this.setConfiguredStatus(json.getString("configured_status"));
        this.setSystemStatus(json.getString("system_status"));
        this.setImpressionTrackingUrl(json.getString("impression_tracking_url"));
        this.setClickTrackingUrl(json.getString("click_tracking_url"));
        this.setFeedsIntegereractionEnabled(json.getString("feeds_Integereraction_enabled"));
        this.setRejectMessage(json.getString("reject_message"));
        this.setIsDeleted(json.getBoolean("is_deleted"));

        if(this.adGroup == null){
            adGroup = new AdGroup();
        }
        if(this.adCreative == null){
            adCreative = new AdCreative();
        }
        this.adGroup.setAdGroupId(json.getInteger("adgroup_id"));
        this.adCreative.setAdCreativeId(json.getJSONObject("adcreative").getInteger("adcreative_id"));
    }
}