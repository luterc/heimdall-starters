/*
 *
 *  *
 *  *  *    Copyright 2020-2021 Luter.me
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *
 */

package com.luter.heimdall.starter.jpa.base.entity;

import com.luter.heimdall.starter.jpa.base.service.AuditorService;
import com.luter.heimdall.starter.jpa.base.service.impl.DefaultAuditorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Slf4j
@Configuration
@EnableJpaAuditing
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Autowired
    private AuditorService auditorService;

    @Bean
    @ConditionalOnMissingBean(AuditorService.class)
    public DefaultAuditorServiceImpl auditorService() {
        log.warn("当前没有自定义JPA审计服务实现，将无法自动带入创建者、修改者信息。" +
                "请自定义JPA审计实现，实现:AuditorService服务接口");
        return new DefaultAuditorServiceImpl();
    }

    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {
        final Long currentUserId = auditorService.getCurrentUserId();
        if (null != currentUserId) {
            return Optional.of(currentUserId);
        }
        return Optional.empty();
    }
}
