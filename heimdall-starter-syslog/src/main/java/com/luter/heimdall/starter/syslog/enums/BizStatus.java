package com.luter.heimdall.starter.syslog.enums;

public enum BizStatus {
    SUCCESS(1, "成功"),

    FAIL(0, "失败");


    private final int value;


    private final String name;

    BizStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}