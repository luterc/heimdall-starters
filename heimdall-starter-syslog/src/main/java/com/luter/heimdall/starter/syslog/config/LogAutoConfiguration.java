package com.luter.heimdall.starter.syslog.config;

import com.luter.heimdall.starter.syslog.aspect.SysLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
@Slf4j
@EnableConfigurationProperties(LogConfig.class)
@ConditionalOnProperty(name = "heimdall.syslog.enabled", havingValue = "true", matchIfMissing = true)
public class LogAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SysLogAspect sysLogAspect() {
        log.warn("初始化 系统日志");
        return new SysLogAspect();
    }
}
