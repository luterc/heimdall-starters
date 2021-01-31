/*
 *
 *  *
 *  *
 *  *      Copyright 2020-2021 Luter.me
 *  *
 *  *      Licensed under the Apache License, Version 2.0 (the "License");
 *  *      you may not use this file except in compliance with the License.
 *  *      You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *      Unless required by applicable law or agreed to in writing, software
 *  *      distributed under the License is distributed on an "AS IS" BASIS,
 *  *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *      See the License for the specific language governing permissions and
 *  *      limitations under the License.
 *  *
 *  *
 *
 */

package com.luter.heimdall.starter.oss.config;

import cn.hutool.json.JSONUtil;
import com.luter.heimdall.starter.oss.service.OssService;
import com.luter.heimdall.starter.oss.service.impl.LocalOssServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * oss配置
 * 需要配置本地MVC资源映射，以便服务器代理静态资源访问
 * * 扩展：DefaultWebMvcConfig
 * //    @Override
 * //    public void addResourceHandlers(ResourceHandlerRegistry registry) {
 * //        if (StrUtil.isBlank(com.luter.macaw.petstore.config.getLocal().getUrlPrefix()) || StrUtil.isBlank(com.luter.macaw.petstore.config.getLocal().getSavePath())) {
 * //            throw new RuntimeException("文件上传参数配置错误,url-prefix或者path错误，上传功能将会不可用");
 * //        }
 * //        String savePath = com.luter.macaw.petstore.config.getLocal().getSavePath().endsWith("/") ? com.luter.macaw.petstore.config.getLocal().getSavePath() : com.luter.macaw.petstore.config.getLocal().getSavePath() + "/";
 * //        String urlPrefix = com.luter.macaw.petstore.config.getLocal().getUrlPrefix().startsWith("/") ? com.luter.macaw.petstore.config.getLocal().getUrlPrefix() : com.luter.macaw.petstore.config.getLocal().getUrlPrefix() + "/";
 * //        registry.addResourceHandler(urlPrefix + "/**")
 * //                .addResourceLocations("file:" + savePath);
 * //    }
 */
@ConditionalOnWebApplication
@Slf4j
@EnableConfigurationProperties({OSSConfig.class})
@ConditionalOnProperty(name = "heimdall.oss.type", havingValue = "local", matchIfMissing = true)
public class LocalAutoConfigutration {
    @Bean
    @ConditionalOnMissingBean(OssService.class)
    public OssService ossService(OSSConfig ossConfig) {
        log.warn("初始化  local文件服务,配置参数:\n{}", JSONUtil.toJsonStr(ossConfig));
        return new LocalOssServiceImpl(ossConfig);
    }
}
