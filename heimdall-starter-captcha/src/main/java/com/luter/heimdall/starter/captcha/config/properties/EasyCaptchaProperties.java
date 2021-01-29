package com.luter.heimdall.starter.captcha.config.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class EasyCaptchaProperties {
    private int cType;

    private int width = 120;
    private int height = 40;
    private int digit = 4;
    @Range(min = 1, max = 6, message = "验证码类型：{min}-{max}，必须数字")
    private int fType = 1;
    @Range(min = 1, max = 10, message = "验证码字体：{min}-{max}，必须数字")
    private int font = 1;

    private int mathLength = 2;
}
