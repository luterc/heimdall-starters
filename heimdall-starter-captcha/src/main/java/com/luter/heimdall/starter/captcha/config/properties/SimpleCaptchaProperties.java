package com.luter.heimdall.starter.captcha.config.properties;

import lombok.Data;

import java.awt.*;

@Data
public class SimpleCaptchaProperties {

    private int width = 140;
    private int height = 40;
    private int digit = 4;
    private int lines = 5;

    private int fontSize = 20;
    private Color bgColor = Color.LIGHT_GRAY;
}
