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
