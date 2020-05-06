package com.hbnet.fastsh.web.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="smartad_adcreative")
@Getter
@Setter
public class AdCreative extends BaseEntity{


    private Integer adCreativeId;

    private Integer accountId;

    private Integer campaignId;

    private Long privateCampaignId;

    private String adCreativeName;

    private Integer adCreativeTemplateId;

    private String adCreativeElements;

    private String siteSet;

    private String promotedObjectType;

    private String pageType;

    private String pageSpec;

    private String promotedObjectId;

    private String shareContentSpec;

    private String dynamicAdCreativeSpec;

    private String multiShareOptimizationEnabled;

    private Boolean isDeleted;

    private String materialUrls;

    private String adcreativeType;


    public void jsonToObject(JSONObject json){
        this.setCampaignId(json.getIntValue("campaign_id"));
        this.setAdCreativeId(json.getIntValue("adcreative_id"));
        this.setAdCreativeName(json.getString("adcreative_name"));
        this.setAdCreativeTemplateId(json.getIntValue("adcreative_template_id"));
        this.setAdCreativeElements(json.getString("adcreative_elements"));
        this.setPageSpec(json.getString("page_spec"));
        this.setPageType(json.getString("page_type"));
        this.setSiteSet(json.getString("site_set"));
        this.setPromotedObjectType(json.getString("promoted_object_type"));
        this.setPromotedObjectId(json.getString("promoted_object_id"));
        this.setShareContentSpec(json.getString("share_content_spec"));
        this.setDynamicAdCreativeSpec(json.getString("ynamic_adcreative_spec"));
        this.setIsDeleted(json.getBoolean("is_deleted"));
        this.setMultiShareOptimizationEnabled(json.getString("multi_share_optimization_enabled"));
    }
}