package com.luter.heimdall.starter.mybatis.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.luter.heimdall.starter.mybatis.config.properties.MybatisConfigProperties;
import com.luter.heimdall.starter.utils.context.BaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Default tenant configuration.
 *
 * @author Luter
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(name = "luter.mybatis.tenant.enabled", havingValue = "true")
public class DefaultTenantConfiguration {
    /**
     * The Config.
     */
    private final MybatisConfigProperties config;

    /**
     * 新多租户插件配置,一缓和二缓遵循mybatis的规则,
     * 避免缓存万一出现问题
     *
     * @return the tenant line inner interceptor
     */
    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        log.warn("开启行级多租户拦截器...");
        return new TenantLineInnerInterceptor(new TenantLineHandler() {
            /**
             * 获取租户ID
             */
            @Override
            public Expression getTenantId() {
                String tenant = BaseContextHolder.getClientId();
                if (StrUtil.isNotEmpty(tenant)) {
                    return new StringValue(BaseContextHolder.getClientId());
                }
                return new NullValue();
            }

            /**
             * 获取多租户的字段名
             * @return String
             */
            @Override
            public String getTenantIdColumn() {
                return config.getTenant().getColumn();
            }

            /**
             * 过滤不需要根据租户隔离的表
             * 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
             * @param tableName 表名
             */
            @Override
            public boolean ignoreTable(String tableName) {
                return config.getTenant().getExcludeTables().stream().anyMatch(
                        (t) -> t.equalsIgnoreCase(tableName)
                );
            }
        });
    }
}
