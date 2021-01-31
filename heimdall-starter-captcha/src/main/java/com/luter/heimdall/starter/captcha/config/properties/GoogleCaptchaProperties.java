package com.luter.heimdall.starter.captcha.config.properties;

import lombok.Data;

/**
 * The type Google captcha properties.
 *
 * @author Luter
 */
@Data
public class GoogleCaptchaProperties {
    /**
     * 验证码类型。1：数学计算验证码，其他数字都默认字符验证码
     */
    private int cType;
    /**
     * 宽度
     */
    private int width = 140;
    /**
     * 高度
     */
    private int height = 40;
    /**
     * 位数
     */
    private int digit = 4;
    /**
     * 是否开启边框,yes: 开启，no:关闭
     */
    private String border = "yes";
    /**
     * 边框颜色，标准css颜色
     */
    private String borderColor = "105,179,90";
    /**
     * 字体，多个字体英文逗号分隔
     */
    private String fonts = "Arial,Courier,宋体,楷体,微软雅黑";
    /**
     * 字体颜色，标准css颜色
     */
    private String fontColor = "black";
    /**
     * 字体大小
     */
    private int fontSize = 32;

}
