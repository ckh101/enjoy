package com.hbnet.fastsh.web.enums;

public enum ProductTypeEnum {
    PRODUCT_TYPE_LINK_WECHAT("PRODUCT_TYPE_LINK_WECHAT", "品牌活动页"),
    PRODUCT_TYPE_LBS_WECHAT("PRODUCT_TYPE_LBS_WECHAT", "本地门店推广"),
    PRODUCT_TYPE_ECOMMERCE("PRODUCT_TYPE_ECOMMERCE", "电商推广");
    private String value;
    private String desc;
    ProductTypeEnum(String value, String desc){
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
