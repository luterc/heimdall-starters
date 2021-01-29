package com.luter.heimdall.starter.captcha.service.impl;


import cn.hutool.core.util.StrUtil;
import com.luter.heimdall.starter.captcha.config.CaptchaConfig;
import com.luter.heimdall.starter.captcha.dto.CaptchaDTO;
import com.luter.heimdall.starter.captcha.service.CacheService;
import com.luter.heimdall.starter.captcha.service.CaptchaService;
import com.luter.heimdall.starter.utils.exception.LuterIllegalParameterException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCaptchaServiceImpl implements CaptchaService {
    final CaptchaConfig config;
    final CacheService cacheService;

    public AbstractCaptchaServiceImpl(CaptchaConfig config, CacheService cacheService) {
        this.config = config;
        this.cacheService = cacheService;
    }

    @Override
    public boolean checkCaptcha(String uuid, String code) {
        if (StrUtil.isEmpty(uuid) || StrUtil.isEmpty(code)) {
            throw new LuterIllegalParameterException("请输入验证码");
        }
        //验证结果
        boolean result = false;
        log.debug("从缓存中查询key:{}", uuid);
        final Object cacheValue = cacheService.get(uuid);
        log.debug("输入的uuid:{},验证码:{},缓存中的验证码:{}", uuid, code, cacheValue);
        if (null == cacheValue) {
            throw new LuterIllegalParameterException("验证码已经失效，请重新获取");
        }
        if (StrUtil.equalsIgnoreCase(cacheValue.toString(), code)) {
            //验证成功后,作废验证码,可能会造成暴力破解
//            redisTemplate.delete(uuid);
            log.debug("图形验证码校验成功:uuid:{},captcha:{},缓存的验证码:{},清理缓存", uuid, code, cacheValue.toString());
            result = true;
        }
        log.debug("图形验证码校验失败:uuid:{},captcha:{},清理缓存", uuid, code);
        //只要验证,不管成功还是失败,都作废此验证码
        cacheService.delete(uuid);
        return result;
    }

    @Override
    public boolean checkCaptcha(CaptchaDTO param) {
        return checkCaptcha(param.getUuid(), param.getCaptcha());
    }
}
