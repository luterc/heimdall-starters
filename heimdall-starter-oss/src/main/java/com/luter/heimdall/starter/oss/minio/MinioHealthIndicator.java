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

package com.luter.heimdall.starter.oss.minio;

import com.luter.heimdall.starter.oss.config.OSSConfig;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementContextAutoConfiguration;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * minio 健康状态上报,可以通过spring actuator或者admin监控Minio桶的健康情况
 */
@ConditionalOnClass(ManagementContextAutoConfiguration.class)
@RequiredArgsConstructor
@Slf4j
public class MinioHealthIndicator implements HealthIndicator {
    private final OSSConfig config;
    private final MinioClient minioClient;

    @Override
    public Health health() {
        if (minioClient == null) {
            return Health.down().build();
        }

        try {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(config.getMinio().getBucket()).build())) {
                return Health.up()
                        .withDetail("bucketName", config.getMinio().getBucket())
                        .build();
            } else {
                return Health.down()
                        .withDetail("bucketName", config.getMinio().getBucket())
                        .build();
            }
        } catch (InvalidBucketNameException | ServerException | IOException | NoSuchAlgorithmException | InsufficientDataException | InvalidKeyException | XmlParserException | ErrorResponseException | InternalException | InvalidResponseException e) {
            return Health.down(e)
                    .withDetail("bucketName", config.getMinio().getBucket())
                    .build();
        }
    }
}
