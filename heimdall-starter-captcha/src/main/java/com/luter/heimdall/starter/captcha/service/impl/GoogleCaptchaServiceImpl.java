/*
 *
 *  *
 *  *  *    Copyright 2020-2021 Luter.me
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *
 */

package com.luter.heimdall.starter.captcha.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.luter.heimdall.starter.captcha.config.CaptchaConfig;
import com.luter.heimdall.starter.captcha.dto.CaptchaDTO;
import com.luter.heimdall.starter.captcha.producer.GoogleCaptchaBeanUtil;
import com.luter.heimdall.starter.captcha.service.CacheService;
import com.luter.heimdall.starter.captcha.service.CaptchaService;
import com.luter.heimdall.starter.utils.exception.LuterException;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class GoogleCaptchaServiceImpl extends AbstractCaptchaServiceImpl implements CaptchaService {
    public static DefaultKaptcha getDefaultKaptcha(CaptchaConfig config) {
        if (1 == config.getGoogleCaptcha().getCType()) {
            return GoogleCaptchaBeanUtil.getMathKaptcha(config);
        }
        return GoogleCaptchaBeanUtil.getDefaultKaptcha(config);
    }

    public GoogleCaptchaServiceImpl(CaptchaConfig config, CacheService cacheService) {
        super(config, cacheService);
    }


    @Override
    public CaptchaDTO genCaptcha() {
        return genCaptcha(config.getCacheExpire().getSeconds());
    }

    @Override
    public CaptchaDTO genCaptcha(long expire) {
        DefaultKaptcha defaultKaptcha = getDefaultKaptcha(config);
        String text = defaultKaptcha.createText();
        log.debug("text:{}", text);
        String capStr, code;
        if (1 == config.getGoogleCaptcha().getCType()) {
            capStr = text.substring(0, text.lastIndexOf("@"));
            code = text.substring(text.lastIndexOf("@") + 1);
        } else {
            capStr = text;
            code = text;
        }

        log.debug("计算:{},code:{}", capStr, code);
        String uuid = IdUtil.fastSimpleUUID();
        //生成图片
        BufferedImage bufferedImage = defaultKaptcha.createImage(capStr);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            String base64 = Base64.encode(outputStream.toByteArray());
            String captchaBase64 = "data:image/jpg;base64,"
                    + base64.replaceAll("\n", "").replaceAll("\r", "");
            CaptchaDTO captchaDTO = new CaptchaDTO();
            captchaDTO.setContent(captchaBase64);
            captchaDTO.setUuid(uuid);
            captchaDTO.setCode(code);
            // 缓存
            cacheService.set(uuid, code, expire);
            return captchaDTO;
        } catch (IOException e) {
            throw new LuterException(e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }


}
