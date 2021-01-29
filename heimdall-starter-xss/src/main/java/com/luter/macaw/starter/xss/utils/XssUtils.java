package com.luter.macaw.starter.xss.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.owasp.validator.html.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Slf4j
public final class XssUtils {
    private static final String ANTISAMY_SLASHDOT_XML = "antisamy-slashdot-1.4.4.xml";
    private static Policy policy = null;

    static {
        log.debug(" start read XSS configfile [{}]", ANTISAMY_SLASHDOT_XML);
        InputStream inputStream = XssUtils.class.getClassLoader().getResourceAsStream(ANTISAMY_SLASHDOT_XML);

        try {
            policy = Policy.getInstance(inputStream);
            log.debug("read XSS configfile [antisamy-slashdot-1.4.4.xml] success");
        } catch (PolicyException var10) {
            log.error("read XSS configfile [antisamy-slashdot-1.4.4.xml] fail , reason:", var10);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("close XSS configfile [antisamy-slashdot-1.4.4.xml] fail , reason:{}", e.getMessage());
                }

            }

        }
    }


    public static String xssClean(String paramValue) {
        return xssClean(paramValue, null);

    }

    public static String xssClean(String paramValue, List<String> ignoreParamValueList) {
        AntiSamy antiSamy = new AntiSamy();

        try {
            log.debug("raw value before xssClean: " + paramValue);
            if (isIgnoreParamValue(paramValue, ignoreParamValueList)) {
                log.debug("ignore the xssClean,keep the raw paramValue: " + paramValue);
                return paramValue;
            }

            CleanResults cr = antiSamy.scan(paramValue, policy);
            List<String> var10000 = cr.getErrorMessages();
            var10000.forEach(log::debug);
            String str = cr.getCleanHTML();
            str = str.replaceAll("&quot;", "\"");
            str = str.replaceAll("&amp;", "&");
            str = str.replaceAll("'", "'");
            str = str.replaceAll("'", "＇");
            str = str.replaceAll("&lt;", "<");
            str = str.replaceAll("&gt;", ">");
            log.debug("xssfilter value after xssClean" + str);
            return str;
        } catch (ScanException var5) {
            log.error("scan failed armter is [" + paramValue + "]", var5);
        } catch (PolicyException var6) {
            log.error("antisamy convert failed  armter is [" + paramValue + "]", var6);
        }

        return paramValue;
    }

    private static boolean isIgnoreParamValue(String paramValue, List<String> ignoreParamValueList) {
        if (StrUtil.isBlank(paramValue)) {
            return true;
        } else if (CollectionUtil.isEmpty(ignoreParamValueList)) {
            return false;
        } else {
            Iterator<String> var2 = ignoreParamValueList.iterator();
            String ignoreParamValue;
            do {
                if (!var2.hasNext()) {
                    return false;
                }
                ignoreParamValue = var2.next();
            } while (!paramValue.contains(ignoreParamValue));

            return true;
        }
    }


}
