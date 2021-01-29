package com.luter.heimdall.starter.captcha.config.properties;

import lombok.Data;

@Data
public class GoogleCaptchaProperties {
    private int cType;
    private int width = 140;
    private int height = 40;
    private int digit = 4;
    private String border = "yes";
    private String borderColor = "105,179,90";
    private String fonts = "Arial,Courier,宋体,楷体,微软雅黑";
    private String fontColor = "black";
    private int fontSize = 32;

}
