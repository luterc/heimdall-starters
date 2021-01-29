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
import com.luter.heimdall.starter.captcha.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@ConditionalOnProperty(name = "heimdall.captcha.cache-type", havingValue = "redis")
public class RedisCacheServiceImpl implements CacheService {
    private final CaptchaConfig config;
    private final StringRedisTemplate redisTemplate;

    public RedisCacheServiceImpl(CaptchaConfig config, StringRedisTemplate redisTemplate) {
        log.warn("图形验证码服务初始化完毕:采用 redis 缓存");
        this.config = config;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void set(String key, String value) {
        set(key, value, config.getCacheExpire().getSeconds());
    }

    @Override
    public void set(String key, String value, Long expire) {
        log.debug("缓存数据=key:{},value:{}", key, value);
        redisTemplate.opsForValue().set(config.getKeyPrefix() + key, value, config.getCacheExpire().getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(config.getKeyPrefix() + key);
    }

    @Override
    public Object get(String key) {
        final Object o = redisTemplate.opsForValue().get(config.getKeyPrefix() + key);
        log.debug("从redis缓存获取key:{},value:{}", key, o);
        return o;
    }
}
