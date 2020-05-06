package com.hbnet.fastsh.web.enums;

public enum CampaignsTypeEnum {
    CAMPAIGN_TYPE_NORMAL("CAMPAIGN_TYPE_NORMAL","普通广告/微信公众号广告"),
    CAMPAIGN_TYPE_WECHAT_MOMENTS("CAMPAIGN_TYPE_WECHAT_MOMENTS","微信朋友圈广告");

    private String value;
    private String desc;

    CampaignsTypeEnum(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public String getValue(){
        return this.value;
    }

    public String getDesc(){
        return this.getDesc();
    }
}
