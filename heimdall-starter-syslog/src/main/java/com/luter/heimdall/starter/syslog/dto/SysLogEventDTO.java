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
