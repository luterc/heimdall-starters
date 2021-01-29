package com.luter.heimdall.starter.captcha.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.luter.heimdall.starter.captcha.config.CaptchaConfig;
import com.luter.heimdall.starter.captcha.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@ConditionalOnProperty(name = "heimdall.captcha.cache-type", havingValue = "local", matchIfMissing = true)
public class LocalCacheServiceImpl implements CacheService {
    private final CaptchaConfig config;
    private final TimedCache<String, String> timedCache;


    public LocalCacheServiceImpl(CaptchaConfig config) {
        log.warn("图形验证码服务初始化完毕:采用 本地 缓存");
        this.config = config;
        this.timedCache = CacheUtil.newTimedCache(config.getCacheExpire().toMillis());
    }

    @Override
    public void set(String key, String value) {
        set(key, value, config.getCacheExpire().getSeconds());
    }

    @Override
    public void set(String key, String value, Long expire) {
        //秒转换成毫秒
        timedCache.put(config.getKeyPrefix() + key, value, Duration.ofSeconds(expire).toMillis());
    }


    @Override
    public Boolean delete(String key) {
        timedCache.remove(config.getKeyPrefix() + key);
        return true;
    }

    @Override
    public Object get(String key) {
        return timedCache.get(config.getKeyPrefix() + key);
    }
}
