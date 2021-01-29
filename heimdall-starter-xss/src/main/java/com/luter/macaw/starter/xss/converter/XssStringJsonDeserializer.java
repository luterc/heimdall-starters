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

package com.luter.macaw.starter.xss.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.luter.macaw.starter.xss.utils.XssUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XssStringJsonDeserializer extends JsonDeserializer<String> {
    public XssStringJsonDeserializer() {
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext dc) throws IOException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            String value = p.getValueAsString();
            if (value != null && !"".equals(value)) {
                List<String> list = new ArrayList<>();
                list.add("<script>");
                list.add("</script>");
                list.add("<iframe>");
                list.add("</iframe>");
                list.add("<noscript>");
                list.add("</noscript>");
                list.add("<frameset>");
                list.add("</frameset>");
                list.add("<frame>");
                list.add("</frame>");
                list.add("<noframes>");
                list.add("</noframes>");
                final boolean flag = list.stream().anyMatch(value::contains);
                return flag ? XssUtils.xssClean(value, null) : value;
            } else {
                return value;
            }
        } else {
            return null;
        }
    }
}
