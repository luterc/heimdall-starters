package com.luter.heimdall.starter.boot.config;

import com.luter.heimdall.starter.boot.controller.BaseServletErrorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@Slf4j
public class BasicErrorControllerConfig {
    @Bean
    public BasicErrorController basicErrorController(ErrorAttributes errorAttributes,
                                                     ServerProperties serverProperties,
                                                     List<ErrorViewResolver> errorViewResolvers) {
        log.warn("初始化 全局 Servlet ErrorController ");
        return new BaseServletErrorController(errorAttributes, serverProperties, errorViewResolvers);
    }
}
