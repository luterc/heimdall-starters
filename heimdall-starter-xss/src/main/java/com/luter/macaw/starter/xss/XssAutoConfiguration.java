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
