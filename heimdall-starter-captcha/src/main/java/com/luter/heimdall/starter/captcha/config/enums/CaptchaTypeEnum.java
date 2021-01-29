package com.luter.heimdall.starter.captcha.config.enums;

public enum CaptchaTypeEnum {
    EASY("easy"),
    SIMPLE("simple"),
    GOOGLE("google");

    private final String value;

    CaptchaTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
