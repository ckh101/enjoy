package com.hbnet.fastsh.web.enums;

public enum AccountTypeEnum {

    ADCCOUNT_TYPE_ADVERTISER("ADVERTISER", "广告主"),
    ADCCOUNT_TYPE_SYSTEM("SYSTEM", "系统");

    private String value;

    private String desc;

    AccountTypeEnum(String value, String desc){
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
