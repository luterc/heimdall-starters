package com.luter.heimdall.starter.captcha.config.properties;

import lombok.Data;

import java.awt.*;

/**
 * The type Simple captcha properties.
 *
 * @author Luter
 */
@Data
public class SimpleCaptchaProperties {

    /**
     * 宽度
     */
    private int width = 140;
    /**
     * 高度
     */
    private int height = 40;
    /**
     * 字符个数
     */
    private int digit = 4;
    /**
     * 产生干扰线条的数量
     */
    private int lines = 5;

    /**
     * 字体大小
     */
    private int fontSize = 20;
    /**
     * 背景色
     */
    private Color bgColor = Color.LIGHT_GRAY;
}
