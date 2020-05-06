package com.hbnet.fastsh.web.constant;

/**
 * 拼多多店铺账户类型
 */
public enum PddAdProductStatus {
    WAIT_POST("待提交", 1),
    WAIT_PLACE("待投放", 2),
    PLACING("投放中", 3),
    COMPLETE("投放完成", 4);

    private String name;
    private int code;

    private PddAdProductStatus(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static PddAdProductStatus getByCode(int code) {
        for (PddAdProductStatus st : values()) {
            if (st.code == code) {
                return st;
            }
        }

        return null;
    }
}
