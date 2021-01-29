package com.luter.heimdall.starter.syslog.enums;

public enum TerminalType {
    OTHER(-1, "其他"),

    PC(1, "PC端"),

    MOBILE(2, "手机端"),

    TABLET(3, "平板设备");
    private final int value;


    private final String name;

    TerminalType(int value, String name) {
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
