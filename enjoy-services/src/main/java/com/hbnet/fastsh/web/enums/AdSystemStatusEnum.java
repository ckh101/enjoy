package com.hbnet.fastsh.web.enums;

public enum AdSystemStatusEnum {

    AD_STATUS_NORMAL("AD_STATUS_NORMAL", "有效"),
    AD_STATUS_PENDING("AD_STATUS_PENDING", "待审核"),
    AD_STATUS_DENIED("AD_STATUS_DENIED", "审核不通过"),
    AD_STATUS_DELETED("AD_STATUS_DELETED", "已删除");
    private String value;
    private String desc;

    AdSystemStatusEnum(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
