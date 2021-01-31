package com.luter.heimdall.starter.mybatis.config;

import com.luter.heimdall.starter.mybatis.config.properties.MybatisConfigProperties;
import com.luter.heimdall.starter.mybatis.helper.MybatisPaginationHelper;
import com.luter.heimdall.starter.mybatis.interceptor.ShowCaseInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Mybatis auto configuration.
 *
 * @author Luter
 */
@ConditionalOnWebApplication
@Slf4j
@Configuration
@EnableConfigurationProperties(MybatisConfigProperties.class)
public class MybatisAutoConfiguration {
    /**
     * 注册MybatisQueryHelper 工具
     *
     * @return the mybatis pagination helper
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPaginationHelper.class)
    public MybatisPaginationHelper mybatisQueryHelper() {
        log.warn("注册MybatisQueryHelper工具");
        return new MybatisPaginationHelper();
    }

    /**
     * 注册 演示模式拦截器
     *
     * @param configProperties the config properties
     * @return the show case interceptor
     */
    @Bean
    @ConditionalOnProperty(name = "luter.mybatis.showcase.enabled", havingValue = "true")
    public ShowCaseInterceptor showCaseInterceptor(MybatisConfigProperties configProperties) {
        log.warn("开启演示模式拦截器");
        return new ShowCaseInterceptor(configProperties);
    }

}
