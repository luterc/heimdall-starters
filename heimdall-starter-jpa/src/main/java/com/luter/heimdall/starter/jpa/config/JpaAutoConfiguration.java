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

package com.luter.heimdall.starter.jpa.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.luter.heimdall.starter.jpa.converters.IBaseEnum;
import com.luter.heimdall.starter.jpa.helper.JpaPaginationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@ConditionalOnWebApplication
@Slf4j
@EnableConfigurationProperties({SpringDataWebProperties.class, JpaProperties.class})
public class JpaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(JpaPaginationHelper.class)
    public JpaPaginationHelper jpaQueryHelper(SpringDataWebProperties properties) {
        log.debug("注册 JpaQueryHelper 工具");
        return new JpaPaginationHelper(properties);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer enumCustomizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.serializerByType(IBaseEnum.class, new JsonSerializer<IBaseEnum>() {
            @Override
            public void serialize(IBaseEnum e, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                //当做对象输出
//                gen.writeStartObject();
//                gen.writeNumberField("value", e.value());
//                gen.writeStringField("label", e.label());
//                gen.writeEndObject();
                //只输出文字
                gen.writeString(e.label());
            }
        });
    }

}
