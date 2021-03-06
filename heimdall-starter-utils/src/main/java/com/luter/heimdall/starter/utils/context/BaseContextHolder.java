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

package com.luter.heimdall.starter.utils.context;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.luter.heimdall.starter.utils.constants.BaseConstants;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor
public final class BaseContextHolder {
    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, String> map = getMap();
        log.debug("设置 ThreadLocal 数据，key:{},value:{}", key, value);
        map.put(key, value == null ? StrUtil.EMPTY : value.toString());
    }

    public static <T> T get(String key, Class<T> type) {
        Map<String, String> map = getMap();
        log.debug("获取 ThreadLocal 数据，key:{},type:{}", key, type.getName());
        return Convert.convert(type, map.get(key));
    }

    public static <T> T get(String key, Class<T> type, Object defaultValue) {
        Map<String, String> map = getMap();
        log.debug("获取 ThreadLocal 数据，key:{},type:{},defaultValue:{}", key, type.getName(),defaultValue);
        return Convert.convert(type, map.getOrDefault(key, String.valueOf(defaultValue == null ? StrUtil.EMPTY : defaultValue)));
    }

    public static String get(String key) {
        Map<String, String> map = getMap();
        log.debug("获取 ThreadLocal 数据，key:{}", key);
        return map.getOrDefault(key, StrUtil.EMPTY);
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>(100);
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setMap(Map<String, String> localMap) {
        THREAD_LOCAL.set(localMap);
    }

    public static void remove() {
        log.debug("清理 ThreadLocal 数据");
        THREAD_LOCAL.remove();
    }
//    ///////业务部分

    public static void setUserId(Long userId) {
        set(BaseConstants.HEADER_USER_ID_PARAM, userId);
    }

    public static Long getUserId(Long defaultValue) {
        return get(BaseConstants.HEADER_USER_ID_PARAM, Long.class, defaultValue);
    }

    public static void setUser(String userJson) {
        set(BaseConstants.HEADER_CURRENTUSER_PARAM, userJson);
    }

    public static String getUser() {
        return get(BaseConstants.HEADER_CURRENTUSER_PARAM);
    }

    public static void setClientId(String clientId) {
        set(BaseConstants.HEADER_CLIENTID_PARAM, clientId);
    }

    public static String getClientId() {
        return get(BaseConstants.HEADER_CLIENTID_PARAM);
    }

    public static void setUsername(String username) {
        set(BaseConstants.HEADER_USER_NAME_PARAM, username);
    }

    public static String getUsername() {
        return get(BaseConstants.HEADER_USER_NAME_PARAM);
    }

    public static void setTraceId(String traceId) {
        set(BaseConstants.HEADER_TRACE_ID_PARAM, traceId);
    }

    public static String getTraceId() {
        return get(BaseConstants.HEADER_TRACE_ID_PARAM);
    }
}
