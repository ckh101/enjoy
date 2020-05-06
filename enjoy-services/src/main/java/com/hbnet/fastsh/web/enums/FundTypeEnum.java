package com.hbnet.fastsh.web.enums;

public enum FundTypeEnum {
    FUND_TYPE_CASH("FUND_TYPE_CASH", "现金账户"),
    FUND_TYPE_GIFT("FUND_TYPE_GIFT", "赠送账户"),
    FUND_TYPE_SHARED("FUND_TYPE_SHARED", "分成账户"),
    FUND_TYPE_BANK("FUND_TYPE_BANK", "银证账户"),
    FUND_TYPE_UNION_GIFT("FUND_TYPE_UNION_GIFT", "移动联盟赠送金账户，仅对部分客户开放"),
    FUND_TYPE_CREDIT_ROLL("FUND_TYPE_CREDIT_ROLL", "信用账户-滚动，仅对部分客户开放"),
    FUND_TYPE_CREDIT_TEMPORARY("FUND_TYPE_CREDIT_TEMPORARY", "预授权-临时，仅对部分客户开放");


    private String value;

    private String desc;

    FundTypeEnum(String value, String desc){
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
