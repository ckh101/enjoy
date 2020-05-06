package com.hbnet.fastsh.web.enums;

public enum SystemStatusEnum {
    CUSTOMER_STATUS_NORMAL("CUSTOMER_STATUS_NORMAL", "有效"),
    CUSTOMER_STATUS_PENDING("CUSTOMER_STATUS_PENDING", "待审核"),
    CUSTOMER_STATUS_DENIED("CUSTOMER_STATUS_DENIED", "审核不通过"),
    CUSTOMER_STATUS_FROZEN("CUSTOMER_STATUS_FROZEN", "封停"),
    CUSTOMER_STATUS_FROZEN_TEMPORARILY("CUSTOMER_STATUS_FROZEN_TEMPORARILY", "临时冻结");
    private String value;
    private String desc;

    SystemStatusEnum(String value, String desc){
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
