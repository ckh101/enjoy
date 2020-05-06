package com.hbnet.fastsh.web.constant;

/**
 * 拼多多店铺账户类型
 */
public enum PddStoreAccountType {
    BALANCE("余额账户", 1),
    BLOCKED_BALANCE("冻结账户", 2);

    private String name;
    private int code;

    private PddStoreAccountType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
