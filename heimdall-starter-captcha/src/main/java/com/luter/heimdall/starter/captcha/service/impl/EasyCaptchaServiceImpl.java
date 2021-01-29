package com.luter.heimdall.starter.captcha.service.impl;

import cn.hutool.core.util.IdUtil;
import com.luter.heimdall.starter.captcha.config.CaptchaConfig;
import com.luter.heimdall.starter.captcha.dto.CaptchaDTO;
import com.luter.heimdall.starter.captcha.service.CacheService;
import com.luter.heimdall.starter.captcha.service.CaptchaService;
import com.wf.captcha.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class EasyCaptchaServiceImpl extends AbstractCaptchaServiceImpl implements CaptchaService {


    public EasyCaptchaServiceImpl(CaptchaConfig config, CacheService cacheService) {
        super(config, cacheService);
    }

    @Override
    public CaptchaDTO genCaptcha() {
        return genSomeCaptcha(config.getCacheExpire().getSeconds());
    }

    @Override
    public CaptchaDTO genCaptcha(long expire) {
        return genSomeCaptcha(config.getCacheExpire().getSeconds());
    }

    public CaptchaDTO genSomeCaptcha(long expire) {
        SpecCaptcha specCaptcha = new SpecCaptcha(config.getEasyCaptcha().getWidth(),
                config.getEasyCaptcha().getHeight(), config.getEasyCaptcha().getDigit());
        specCaptcha.setCharType(config.getEasyCaptcha().getFType());
        CaptchaDTO captchaDTO = gen1Png();
        switch (config.getEasyCaptcha().getCType()) {
            case 2:
                captchaDTO = gen2Gif();
                break;
            case 3:
                captchaDTO = gen3Chinese();
                break;
            case 4:
                captchaDTO = gen4ChineseGif();
                break;
            case 5:
                captchaDTO = gen5Math();
                break;
            default:
                break;
        }
        // 缓存
        cacheService.set(captchaDTO.getUuid(),
                captchaDTO.getCode(), expire);
        return captchaDTO;
    }

    private CaptchaDTO gen1Png() {
        SpecCaptcha specCaptcha = new SpecCaptcha(config.getEasyCaptcha().getWidth(),
                config.getEasyCaptcha().getHeight(), config.getEasyCaptcha().getDigit());
        specCaptcha.setCharType(config.getEasyCaptcha().getFType());
        return new CaptchaDTO().setCode(specCaptcha.text())
                .setUuid(IdUtil.fastSimpleUUID())
                .setContent(specCaptcha.toBase64());
    }

    private CaptchaDTO gen2Gif() {
        GifCaptcha specCaptcha = new GifCaptcha(config.getEasyCaptcha().getWidth(),
                config.getEasyCaptcha().getHeight(), config.getEasyCaptcha().getDigit());
        specCaptcha.setCharType(config.getEasyCaptcha().getFType());
        return new CaptchaDTO()
                .setCode(specCaptcha.text())
                .setUuid(IdUtil.fastSimpleUUID()).setContent(specCaptcha.toBase64());
    }

    private CaptchaDTO gen3Chinese() {
        ChineseCaptcha specCaptcha = new ChineseCaptcha(config.getEasyCaptcha().getWidth(),
                config.getEasyCaptcha().getHeight(), config.getEasyCaptcha().getDigit());
        specCaptcha.setCharType(config.getEasyCaptcha().getFType());
        return new CaptchaDTO()
                .setCode(specCaptcha.text())
                .setUuid(IdUtil.fastSimpleUUID()).setContent(specCaptcha.toBase64());
    }

    private CaptchaDTO gen4ChineseGif() {
        ChineseGifCaptcha specCaptcha = new ChineseGifCaptcha(config.getEasyCaptcha().getWidth(),
                config.getEasyCaptcha().getHeight(), config.getEasyCaptcha().getDigit());
        specCaptcha.setCharType(config.getEasyCaptcha().getFType());
        return new CaptchaDTO()
                .setCode(specCaptcha.text())
                .setUuid(IdUtil.fastSimpleUUID()).setContent(specCaptcha.toBase64());
    }

    private CaptchaDTO gen5Math() {
        ArithmeticCaptcha specCaptcha = new ArithmeticCaptcha(config.getEasyCaptcha().getWidth(),
                config.getEasyCaptcha().getHeight(), config.getEasyCaptcha().getMathLength());
        specCaptcha.setCharType(config.getEasyCaptcha().getFType());
        return new CaptchaDTO()
                .setCode(specCaptcha.text())
                .setUuid(IdUtil.fastSimpleUUID()).setContent(specCaptcha.toBase64());
    }
}
