package com.hbnet.fastsh.web.enums;

public enum ConfiguredStatusEnum {
    AD_STATUS_NORMAL("AD_STATUS_NORMAL", "启动"),
    AD_STATUS_SUSPEND("AD_STATUS_SUSPEND", "暂停"),
    AD_STATUS_PENDING("AD_STATUS_PENDING", "创建中");

    private String value;
    private String desc;
    ConfiguredStatusEnum(String value, String desc){
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
