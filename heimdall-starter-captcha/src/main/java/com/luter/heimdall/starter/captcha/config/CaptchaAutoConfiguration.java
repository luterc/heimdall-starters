package com.luter.heimdall.starter.captcha.config;

import cn.hutool.json.JSONUtil;
import com.luter.heimdall.starter.captcha.service.CacheService;
import com.luter.heimdall.starter.captcha.service.CaptchaService;
import com.luter.heimdall.starter.captcha.service.impl.EasyCaptchaServiceImpl;
import com.luter.heimdall.starter.captcha.service.impl.GoogleCaptchaServiceImpl;
import com.luter.heimdall.starter.captcha.service.impl.SimpleCaptchaServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ConditionalOnWebApplication
@Slf4j
@EnableConfigurationProperties(CaptchaConfig.class)
@ConditionalOnProperty(name = "heimdall.captcha.enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan("com.luter.heimdall.starter.captcha.service")
public class CaptchaAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(CaptchaService.class)
    public CaptchaService captchaService(CaptchaConfig captchaConfig, CacheService cacheService) {
        switch (captchaConfig.getType()) {

            case SIMPLE:
                log.info("注册 simple captcha 图形验证码实现，配置:\n{}", JSONUtil.toJsonStr(captchaConfig.getSimpleCaptcha()));
                return new SimpleCaptchaServiceImpl(captchaConfig, cacheService);
            case GOOGLE:
                log.info("注册 google captcha 图形验证码实现，配置:\n{}", JSONUtil.toJsonStr(captchaConfig.getGoogleCaptcha()));
                return new GoogleCaptchaServiceImpl(captchaConfig, cacheService);
            default:
                log.info("注册 easy captcha 图形验证码实现，配置:\n{}", JSONUtil.toJsonStr(captchaConfig.getEasyCaptcha()));
                return new EasyCaptchaServiceImpl(captchaConfig, cacheService);
        }
    }

}
