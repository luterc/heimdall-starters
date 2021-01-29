package com.luter.heimdall.starter.captcha.service;


public interface CacheService {

    void set(String key, String value);

    void set(String key, String value, Long expire);

    Boolean delete(String key);

    Object get(String key);


}
