/*
 *
 *  *
 *  *
 *  *      Copyright 2020-2021 Luter.me
 *  *
 *  *      Licensed under the Apache License, Version 2.0 (the "License");
 *  *      you may not use this file except in compliance with the License.
 *  *      You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *      Unless required by applicable law or agreed to in writing, software
 *  *      distributed under the License is distributed on an "AS IS" BASIS,
 *  *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *      See the License for the specific language governing permissions and
 *  *      limitations under the License.
 *  *
 *  *
 *
 */

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
    /**
     * 是否开启登录图形验证码，默认:true
     */
    private boolean enabled = true;
    /**
     * 图形验证码缓存方式,local、redis ,默认local
     */
    private CaptchaCacheTypeEnum cacheType = CaptchaCacheTypeEnum.LOCAL;
    /**
     * 图形验证码的实现方式，simple\google\easy,默认easy方式
     */
    private CaptchaTypeEnum type = CaptchaTypeEnum.EASY;
    /**
     * 验证码在缓存中保存的时长。 默认180s
     */
    private Duration cacheExpire = Duration.ofSeconds(180);
    /**
     * 缓存中的key前缀，默认:captcha:
     */
    private String keyPrefix = "captcha:";
    /**
     * simple简单版实现的图形验证码配置参数
     */
    @NestedConfigurationProperty
    private SimpleCaptchaProperties simpleCaptcha = new SimpleCaptchaProperties();
    /**
     * easy captcha 实现的图形验证码配置参数
     */
    @NestedConfigurationProperty
    private EasyCaptchaProperties easyCaptcha = new EasyCaptchaProperties();
    /**
     * google captcha 实现的图形验证码配置参数
     */
    @NestedConfigurationProperty
    private GoogleCaptchaProperties googleCaptcha = new GoogleCaptchaProperties();
}
