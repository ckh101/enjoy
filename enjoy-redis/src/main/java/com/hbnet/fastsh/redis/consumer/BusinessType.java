package com.hbnet.fastsh.redis.consumer;

public enum BusinessType {
    COMPLETE_ORDER("smartad.complete.order"),
    DAILY_REPORT_QUERY("smartad.daily.report.qry");

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
