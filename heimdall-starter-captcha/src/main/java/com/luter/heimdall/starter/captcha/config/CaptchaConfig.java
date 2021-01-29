package com.luter.heimdall.starter.captcha.config;


import com.luter.heimdall.starter.captcha.config.enums.CaptchaCacheTypeEnum;
import com.luter.heimdall.starter.captcha.config.enums.CaptchaTypeEnum;
import com.luter.heimdall.starter.captcha.config.properties.EasyCaptchaProperties;
import com.luter.heimdall.starter.captcha.config.properties.GoogleCaptchaProperties;
import com.luter.heimdall.starter.captcha.config.properties.SimpleCaptchaProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;

@ConfigurationProperties(prefix = "heimdall.captcha")
@Data
public class CaptchaConfig {
    private boolean enabled = true;
    private CaptchaCacheTypeEnum cacheType = CaptchaCacheTypeEnum.LOCAL;
    private CaptchaTypeEnum type = CaptchaTypeEnum.EASY;
    private Duration cacheExpire = Duration.ofSeconds(180);
    private String keyPrefix = "captcha:";
    @NestedConfigurationProperty
    private SimpleCaptchaProperties simpleCaptcha = new SimpleCaptchaProperties();
    @NestedConfigurationProperty
    private EasyCaptchaProperties easyCaptcha = new EasyCaptchaProperties();
    @NestedConfigurationProperty
    private GoogleCaptchaProperties googleCaptcha = new GoogleCaptchaProperties();

}
