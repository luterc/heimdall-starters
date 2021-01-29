package com.luter.heimdall.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luter.heimdall.starter.utils.json.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Slf4j
public abstract class DefaultJacksonConfig {
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        log.info("注册: Jackson ObjectMapper ");
        return JacksonUtils.getObjectMapper();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        log.info("注册: Jackson MappingJackson2HttpMessageConverter ");
        //定义Json转换器
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(JacksonUtils.getObjectMapper());
        return jackson2HttpMessageConverter;
    }

}
