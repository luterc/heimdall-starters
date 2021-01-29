package com.luter.heimdall.starter.jpa.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "heimdall.showcase")
public class JpaProperties {

    private Boolean enabled = false;

    private List<String> excludeTables = new ArrayList<>();
}
