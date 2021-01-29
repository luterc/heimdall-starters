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
