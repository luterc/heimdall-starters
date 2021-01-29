package com.luter.heimdall.starter.syslog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "luter.syslog")
public class LogConfig {
    private Boolean enabled = true;
}
