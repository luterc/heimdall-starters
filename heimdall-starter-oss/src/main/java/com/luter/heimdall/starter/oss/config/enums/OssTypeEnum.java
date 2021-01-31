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

package com.luter.heimdall.starter.oss.config.enums;

/**
 * 文件上传实现类型枚举
 */
public enum OssTypeEnum {
    /**
     * Minio 存储
     */
    MINIO("minio"),

    /**
     * 本地服务存储
     */
    LOCAL("local");
    /**
     * The Value.
     */
    private final String value;

    /**
     * Instantiates a new Type enum.
     *
     * @param value the value
     */
    OssTypeEnum(String value) {
        this.value = value;
    }

    /**
     * Value string.
     *
     * @return the string
     */
    public String value() {
        return value;
    }
}
