package com.luter.heimdall.starter.mybatis.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * mybatis配置
 *
 * @author Luter
 */
@ConfigurationProperties(prefix = "luter.mybatis")
@Data
@Component
public class MybatisConfigProperties {
    /**
     * 行级多租户配置
     */
    @NestedConfigurationProperty
    private MybatisTenantProperties tenant = new MybatisTenantProperties();
    /**
     * 演示模式设置
     */
    private MybatisShowCaseProperties showcase = new MybatisShowCaseProperties();
}
