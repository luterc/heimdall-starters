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

package com.luter.macaw.starter.xss.converter;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.luter.macaw.starter.xss.utils.XssUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XssStringJsonSerializer extends JsonSerializer<String> {

    public XssStringJsonSerializer() {
    }

    @Override
    public Class<String> handledType() {
        return String.class;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        if (value != null) {
            try {
                String encodedValue = XssUtils.xssClean(value, null);
                jsonGenerator.writeString(encodedValue);
            } catch (Exception var5) {
                log.error("序列化失败:[{}]", value, var5);
            }
        }

    }
}
