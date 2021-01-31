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
import com.luter.heimdall.starter.oss.minio.MinioHelper;
import com.luter.heimdall.starter.oss.service.OssService;
import com.luter.heimdall.starter.oss.service.impl.MinIoOssServiceImpl;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * oss配置 minio自动配置
 */
@ConditionalOnWebApplication
@Slf4j
@EnableConfigurationProperties({OSSConfig.class})
@ConditionalOnProperty(name = "heimdall.oss.type", havingValue = "minio")
public class MinioAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MinioClient minioClient(OSSConfig ossConfig) {
        //创建minio client
        MinioClient client = MinioClient.builder()
                .endpoint(ossConfig.getMinio().getEndpoint())
                .credentials(ossConfig.getMinio().getAccessKey(), ossConfig.getMinio().getSecretKey())
                .build();
        //设置超时
        client.setTimeout(ossConfig.getMinio().getConnectTimeout().toMillis()
                , ossConfig.getMinio().getWriteTimeout().toMillis()
                , ossConfig.getMinio().getReadTimeout().toMillis());
        if (ossConfig.getMinio().isCheckBucket()) {
            try {
                log.debug("开始检测bukect:[{}]", ossConfig.getMinio().getBucket());
                //判断是否存在bucket,不存在就新建
                if (!client.bucketExists(BucketExistsArgs.builder().bucket(ossConfig.getMinio().getBucket()).build())) {
                    log.warn("Bucket不存在！创建新的Bucket:{}", ossConfig.getMinio().getBucket());
                    client.makeBucket(MakeBucketArgs.builder()
                            .bucket(ossConfig.getMinio().getBucket())
                            .build());
                    //新建bucket 并且设置访问策略 为read-only
                    String json = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\"],\"Resource\":[\"arn:aws:s3:::{bucketName}\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::{bucketName}/*\"]}]}";
                    client.setBucketPolicy(SetBucketPolicyArgs.builder()
                            .bucket(ossConfig.getMinio().getBucket())
                            .config(json.replace("{bucketName}", ossConfig.getMinio().getBucket())).build());
                }
            } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidBucketNameException | InvalidResponseException | IOException | NoSuchAlgorithmException | RegionConflictException | ServerException | XmlParserException e) {
                log.error("初始化minio错误:创建Bucket失败");
                e.printStackTrace();
            }
        }

        log.debug("Bukect:[{}]初始化完毕", ossConfig.getMinio().getBucket());
        return client;
    }

    /**
     * 注册oss服务
     */
    @Bean
    @ConditionalOnMissingBean(OssService.class)
    public OssService ossService(MinioClient minioClient, OSSConfig ossConfig) {
        log.warn("初始化 Minio 文件服务,配置参数:\n{}", JSONUtil.toJsonStr(ossConfig));
        return new MinIoOssServiceImpl(ossConfig, minioClient);
    }


    /**
     * 注册minio帮助类
     */
    @Bean
    @ConditionalOnMissingBean
    public MinioHelper minioHelper(MinioClient minioClient, OSSConfig ossConfig) {
        log.warn(" Minio 帮助类初始化完毕");
        return new MinioHelper(ossConfig, minioClient);
    }
}
