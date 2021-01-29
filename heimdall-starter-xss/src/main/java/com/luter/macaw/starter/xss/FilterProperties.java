package com.luter.macaw.starter.xss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "luter.xss")
public class FilterProperties {
    private Boolean enabled = false;
    private String excludes = "";
    private String urlPatterns = "";

    private Integer order = 1;
}
