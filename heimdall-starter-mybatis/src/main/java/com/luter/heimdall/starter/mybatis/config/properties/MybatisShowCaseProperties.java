package com.luter.heimdall.starter.mybatis.config.properties;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示模式配置
 *
 * @author Luter
 */
@Data
public class MybatisShowCaseProperties {
    /**
     * 是否开启租户模式
     */
    private Boolean enabled = false;

    /**
     * 演示模式下，要排除的操作
     */
    private List<String> excludeActions = new ArrayList<>();
}
