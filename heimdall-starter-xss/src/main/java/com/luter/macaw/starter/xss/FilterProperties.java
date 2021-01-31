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

package com.luter.macaw.starter.xss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Filter properties.
 *
 * @author Luter
 */
@Data
@ConfigurationProperties(prefix = "heimdall.xss")
public class FilterProperties {
    /**
     * 是否开启xss拦截,默认开启
     */
    private Boolean enabled = false;
    /**
     * 排除链接,哪些url不需要进行xss预处理。（多个用逗号分隔）,默认为空
     */
    private String excludes = "";
    /**
     * 哪些路径需要xss拦截,多个用逗号分隔，支持AntPath风格配置.默认为空
     */
    private String urlPatterns = "";

    /**
     * 过滤器顺序，越小越靠前
     */
    private Integer order = 1;
}
