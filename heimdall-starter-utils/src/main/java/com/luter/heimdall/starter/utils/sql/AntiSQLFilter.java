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

package com.luter.heimdall.starter.utils.sql;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AntiSQLFilter {

    private static final String SQL_PATTERN = "[a-zA-Z0-9_ ,.]+";

    private static final String[] keyWords = {";", "\"", "'", "/*", "*/", "--", "exec",
            "select", "update", "delete", "insert",
            "alter", "drop", "create", "shutdown"};

    public static Map<String, String[]> getSafeParameterMap(Map<String, String[]> parameterMap) {
        Maps.MapBuilder<String, String[]> builder = Maps.builder(HashMap::new);
        Iterator<String> iter = parameterMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String[] oldValues = parameterMap.get(key);
            builder.put(key, getSafeValues(oldValues));
        }
        return builder.unmodifiable().build();
    }

    public static String[] getSafeValues(String[] oldValues) {
        if (ArrayUtil.isNotEmpty(oldValues)) {
            String[] newValues = new String[oldValues.length];
            for (int i = 0; i < oldValues.length; i++) {
                newValues[i] = getSafeValue(oldValues[i]);
            }
            return newValues;
        }
        return null;
    }

    public static String getSafeValue(String oldValue) {
        String value = escapeOrderBySql(oldValue);
        if (StrUtil.EMPTY.equals(value)) {
            return value;
        }
        StringBuilder sb = new StringBuilder(value);
        String lowerCase = value.toLowerCase();
        for (String keyWord : keyWords) {
            int x;
            while ((x = lowerCase.indexOf(keyWord)) >= 0) {
                if (keyWord.length() == 1) {
                    sb.replace(x, x + 1, " ");
                    lowerCase = sb.toString().toLowerCase();
                    continue;
                }
                sb.deleteCharAt(x + 1);
                lowerCase = sb.toString().toLowerCase();
            }
        }
        return sb.toString();
    }

    private static String escapeOrderBySql(String value) {
        if (StrUtil.isNotEmpty(value) && !isValidOrderBySql(value)) {
            return StrUtil.EMPTY;
        }
        return value;
    }

    private static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

}
