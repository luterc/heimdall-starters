package com.luter.heimdall.starter.captcha.config.enums;

public enum CaptchaCacheTypeEnum {
    LOCAL("local"),
    CAFFEINE("caffeine"),
    REDIS("redis");

    private final String value;

    CaptchaCacheTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
