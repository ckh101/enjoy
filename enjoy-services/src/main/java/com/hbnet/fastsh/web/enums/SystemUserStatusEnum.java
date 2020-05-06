package com.hbnet.fastsh.web.enums;

public enum SystemUserStatusEnum {

    SYSTEM_USER_STATUS_FORBIDDEN("FORBIDDEN", "禁用"),
    SYSTEM_USER_STATUS_ALLOW("ALLOW", "正常"),
    SYSTEM_USER_STATUS_DEL("DEL", "删除");

    private String value;

    private String desc;

    SystemUserStatusEnum(String value, String desc){
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
