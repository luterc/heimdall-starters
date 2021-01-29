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
