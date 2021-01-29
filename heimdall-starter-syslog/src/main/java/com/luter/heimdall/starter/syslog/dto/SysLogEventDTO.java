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

package com.luter.heimdall.starter.syslog.dto;


import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SysLogEventDTO implements Serializable {
    private String traceId;
    private String title;

    private Integer businessType;

    private String method;

    private String requestMethod;
/////
    private Integer terminalType;
    private String terminalOsName;
    private String browserName;
    private String browserVersion;

    //////
    private String requestUrl;

    private String requestIp;
    private String userLocation;

    private String requestParam;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;
    private Integer status;
    private String exceptionMessage;

    private String responseResult;
    private String appName;
    private String appHostIp;
    private String appPort;

    private Long consumingTime;
}
