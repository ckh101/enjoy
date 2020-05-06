package com.ckh.enjoy.redis.consumer;

public enum BusinessType {
    COMPLETE_ORDER("enjoy.complete.order"),
    DAILY_REPORT_QUERY("enjoy.daily.report.qry");

    private String value;

    public String getValue() {
        return value;
    }

    private BusinessType(String value) {
        this.value = value;
    }

    public static BusinessType get(String value) {
        for (BusinessType bt : values()) {
            if (bt.value.equals(value)) {
                return bt;
            }
        }

        return null;
    }
}
