package com.luter.heimdall.starter.jpa.base.service.impl;

import com.luter.heimdall.starter.jpa.base.service.AuditorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultAuditorServiceImpl implements AuditorService {
    @Override
    public Long getCurrentUserId() {
        log.warn("没有实现JPA实体类审计服务，采用默认实现，null");
        return null;
    }
}
