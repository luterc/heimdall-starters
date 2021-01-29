package com.luter.macaw.starter.xss;

import cn.hutool.json.JSONUtil;
import com.luter.macaw.starter.xss.converter.XssStringJsonDeserializer;
import com.luter.macaw.starter.xss.xss.XssFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.DispatcherType;

@ConditionalOnWebApplication
@Slf4j
@EnableConfigurationProperties(FilterProperties.class)
@RequiredArgsConstructor
public class XssAutoConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer2() {
        return (builder) -> builder.deserializerByType(String.class, new XssStringJsonDeserializer());
    }

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean(FilterProperties properties) {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter(properties));
        registration.isAsyncSupported();
        registration.addUrlPatterns(properties.getUrlPatterns());
        registration.setName("xssFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        log.warn("初始化 XSS Filter .配置 :{}", JSONUtil.toJsonStr(properties));
        return registration;
    }
}
