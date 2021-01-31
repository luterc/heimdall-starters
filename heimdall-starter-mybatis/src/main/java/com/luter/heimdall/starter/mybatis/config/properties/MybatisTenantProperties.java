package com.luter.heimdall.starter.mybatis.config.properties;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 行级多租户设置
 *
 * @author Luter
 */
@Data
public class MybatisTenantProperties {
    /**
     * 是否开启租户模式
     */
    private Boolean enabled = false;
    /**
     * 多租户字段名称,表里面多租户的标识字段
     */
    private String column = "tenant_id";
    /**
     * 不需要进行多租户拦截的表
     */
    private List<String> excludeTables = new ArrayList<>();


}
