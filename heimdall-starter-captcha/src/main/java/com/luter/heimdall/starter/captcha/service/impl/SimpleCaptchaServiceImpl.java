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

package com.luter.heimdall.starter.captcha.service.impl;


import com.luter.heimdall.starter.captcha.config.CaptchaConfig;
import com.luter.heimdall.starter.captcha.dto.CaptchaDTO;
import com.luter.heimdall.starter.captcha.service.CacheService;
import com.luter.heimdall.starter.captcha.service.CaptchaService;
import com.luter.heimdall.starter.captcha.producer.SimpleCaptchaProducer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SimpleCaptchaServiceImpl extends AbstractCaptchaServiceImpl implements CaptchaService {


    public SimpleCaptchaServiceImpl(CaptchaConfig config, CacheService cacheService) {
        super(config, cacheService);
    }

    @Override
    public CaptchaDTO genCaptcha() {
        return genCaptcha(config.getCacheExpire().getSeconds());
    }


    @Override
    public CaptchaDTO genCaptcha(long expire) {
        try {
            final CaptchaDTO captchaDTO = SimpleCaptchaProducer.newBuilder()
                    //设置图片的宽度
                    .setWidth(config.getSimpleCaptcha().getWidth())
                    //设置图片的高度
                    .setHeight(config.getSimpleCaptcha().getHeight())
                    //设置字符的个数
                    .setSize(config.getSimpleCaptcha().getDigit())
                    //设置干扰线的条数
                    .setLines(config.getSimpleCaptcha().getLines())
                    //设置字体的大小
                    .setFontSize(config.getSimpleCaptcha().getFontSize())
                    //设置是否需要倾斜
                    .setTilt(true)
                    //设置验证码的背景颜色
                    .setBackgroundColor(config.getSimpleCaptcha().getBgColor())
                    .build()
                    .createImage();
            // 存入redis
            cacheService.set(captchaDTO.getUuid(), captchaDTO.getCode(), expire);
            return captchaDTO;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }


}
