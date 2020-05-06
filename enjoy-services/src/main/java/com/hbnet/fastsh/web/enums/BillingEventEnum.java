package com.hbnet.fastsh.web.enums;

public enum BillingEventEnum {
    BILLINGEVENT_CLICK("BILLINGEVENT_CLICK", "按 CPC 出价，优化目标为根据点击量优化使用"),
    BILLINGEVENT_IMPRESSION("BILLINGEVENT_IMPRESSION", "按 CPM 出价，优化目标为根据曝光量优化使用");

    private String value;

    private String desc;

    BillingEventEnum(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public String getValue(){
        return this.value;
    }

    public String getDesc(){
        return this.desc;
    }
}
