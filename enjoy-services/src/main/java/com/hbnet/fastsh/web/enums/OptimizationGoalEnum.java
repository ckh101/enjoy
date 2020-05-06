package com.hbnet.fastsh.web.enums;

public enum OptimizationGoalEnum {
    OPTIMIZATIONGOAL_CLICK("OPTIMIZATIONGOAL_CLICK", "根据点击量优化,公众号广告使用"),
    OPTIMIZATIONGOAL_IMPRESSION("OPTIMIZATIONGOAL_IMPRESSION", "根据曝光量优化，朋友圈广告使用");

    private String value;

    private String desc;

    OptimizationGoalEnum(String value, String desc){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public String getDesc(){
        return this.getDesc();
    }
}
