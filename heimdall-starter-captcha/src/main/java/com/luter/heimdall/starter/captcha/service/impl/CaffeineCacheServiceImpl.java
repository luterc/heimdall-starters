package com.luter.heimdall.starter.captcha.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.luter.heimdall.starter.captcha.config.CaptchaConfig;
import com.luter.heimdall.starter.captcha.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "heimdall.captcha.cache-type", havingValue = "caffeine")
public class CaffeineCacheServiceImpl implements CacheService {
    private final CaptchaConfig config;
    private Cache<String, String> cache;

    public CaffeineCacheServiceImpl(CaptchaConfig config) {
        log.warn("图形验证码服务初始化完毕:采用 redis 缓存");
        this.config = config;
        this.cache = Caffeine.newBuilder()
                .expireAfterAccess(config.getCacheExpire()).build();
    }


    @Override
    public void set(String key, String value) {
        set(key, value, config.getCacheExpire().getSeconds());
    }

    @Override
    public void set(String key, String value, Long expire) {
        log.debug("缓存数据=key:{},value:{}", key, value);
        cache.put(key, value);
    }

    @Override
    public Boolean delete(String key) {
        cache.invalidate(key);
        return true;
    }

    @Override
    public Object get(String key) {
        final String o = cache.getIfPresent(key);
        log.debug("从redis缓存获取key:{},value:{}", key, o);
        return o;
    }

    public Cache<String, String> getCache() {
        return cache;
    }

    public void setCache(Cache<String, String> cache) {
        this.cache = cache;
    }
}
