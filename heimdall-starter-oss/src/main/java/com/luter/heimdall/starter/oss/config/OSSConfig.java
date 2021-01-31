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

import com.luter.heimdall.starter.oss.config.enums.OssTypeEnum;
import com.luter.heimdall.starter.oss.config.properties.LocalFileProperties;
import com.luter.heimdall.starter.oss.config.properties.MinioProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "heimdall.oss")
@Data
public class OSSConfig {
    /**
     * 文件服务实现类型，local、minio,默认Local
     */
    private OssTypeEnum type = OssTypeEnum.LOCAL;
    /**
     * 图片最大上传大小，默认:3MB
     */
    private long imageMaxSize = 1024 * 1024 * 3;
    /**
     * 文件上传大小，默认:5MB
     */
    private long fileMaxSize = 1024 * 1024 * 5;
    /**
     * 本项目存储
     */
    @NestedConfigurationProperty
    private LocalFileProperties local = new LocalFileProperties();

    /**
     * MinIO存储
     */
    @NestedConfigurationProperty
    private MinioProperties minio = new MinioProperties();
}
