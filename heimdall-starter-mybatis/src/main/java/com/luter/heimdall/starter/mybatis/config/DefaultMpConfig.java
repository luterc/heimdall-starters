package com.luter.heimdall.starter.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.luter.heimdall.starter.mybatis.base.entity.BaseObjectHandler;
import com.luter.heimdall.starter.mybatis.config.properties.MybatisConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 公共Mybatis plus配置，项目中继承即可
 *
 * @author Luter
 */
@RequiredArgsConstructor
@Slf4j
public abstract class DefaultMpConfig {
    /**
     * The Tenant properties.
     */
    @Autowired
    private MybatisConfigProperties tenantProperties;
    /**
     * The Tenant line inner interceptor.
     */
    @Autowired(required = false)
    private TenantLineInnerInterceptor tenantLineInnerInterceptor;

    /**
     * 公共配置
     *
     * @return the global config
     */
    protected GlobalConfig defaultGlobalConfiguration() {
        GlobalConfig globalConfiguration = new GlobalConfig();
        globalConfiguration.setMetaObjectHandler(new BaseObjectHandler());
        return globalConfiguration;
    }

    /**
     * 插件注册
     *
     * @return the mybatis plus interceptor
     */
    protected MybatisPlusInterceptor defaultbatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //多租户插件必须在分页之前
        if (tenantProperties.getTenant().getEnabled()) {
            interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        }
        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 数据库类型
        paginationInterceptor.setDbType(DbType.MYSQL);
        // 单页分页条数限制,默认无限制
        paginationInterceptor.setMaxLimit(1000L);
        // 溢出总页数后是否进行处理
        paginationInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(paginationInterceptor);
        //防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        //乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 开启返回map结果集的下划线转驼峰
     *
     * @return the configuration customizer
     */
    protected ConfigurationCustomizer defaultConfigurationCustomizer() {
        return i -> {
            i.setObjectWrapperFactory(new MybatisMapWrapperFactory());
            i.setUseGeneratedShortKey(true);
        };
    }

}
