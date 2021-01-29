package com.luter.heimdall.starter.utils;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageUtils {

    private final MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String msgKey, String defaultText) {
        return messageSource.getMessage(msgKey, null, defaultText, LocaleContextHolder.getLocale());
    }

    public String get(String msgKey) {
        return messageSource.getMessage(msgKey, null, msgKey, LocaleContextHolder.getLocale());
    }

    public String get(String msgKey, Object... args) {
        return messageSource.getMessage(msgKey, args, msgKey, LocaleContextHolder.getLocale());
    }
}