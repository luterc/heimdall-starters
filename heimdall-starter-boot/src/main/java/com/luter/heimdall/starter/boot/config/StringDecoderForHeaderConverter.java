package com.luter.heimdall.starter.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

@Slf4j
public class StringDecoderForHeaderConverter implements GenericConverter {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String NO_NAME = "NO_NAME";

    private Charset charset;

    public StringDecoderForHeaderConverter(Charset charset) {
        this.charset = charset;
        if (this.charset == null) {
            this.charset = DEFAULT_CHARSET;
        }
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;

        if (this.charset == null) {
            this.charset = DEFAULT_CHARSET;
        }
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, String.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (ObjectUtils.isEmpty(source)) {
            return source;
        }
        String name = needDecoder(source, targetType);
        if (name != null) {
            return convert(source.toString(), name);
        }
        return source;
    }

    private String needDecoder(Object source, TypeDescriptor targetType) {
        RequestHeader requestHeader = targetType.getAnnotation(RequestHeader.class);
        Class<?> type = targetType.getType();
        if (requestHeader != null && type == String.class) {
            if (source.toString().contains("%")) {
                String name = requestHeader.name();
                if ("".equals(name)) {
                    name = requestHeader.value();
                }
                if ("".equals(name)) {
                    name = NO_NAME;
                }

                return name;
            }
        }

        return null;
    }

    private String convert(final String source, final String name) {
        log.debug("Begin convert[" + source + "] for RequestHeader[" + name + "].");
        String result;
        try {
            result = URLDecoder.decode(source, this.charset.name());
            log.debug("Success convert[" + source + ", " + result + "] for RequestHeader[" + name + "].");

            return result;
        } catch (Exception e) {
            log.warn("Fail convert[" + source + "] for RequestHeader[" + name + "]!", e);
        }

        return source;
    }
}