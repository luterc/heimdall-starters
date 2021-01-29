/*
 *
 *  *
 *  *
 *  *      Copyright 2020-2021 Luter.me
 *  *
 *  *      Licensed under the Apache License, Version 2.0 (the "License");
 *  *      you may not use this file except in compliance with the License.
 *  *      You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *      Unless required by applicable law or agreed to in writing, software
 *  *      distributed under the License is distributed on an "AS IS" BASIS,
 *  *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *      See the License for the specific language governing permissions and
 *  *      limitations under the License.
 *  *
 *  *
 *
 */

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