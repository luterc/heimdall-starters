
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

package com.luter.heimdall.starter.utils.request;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class RequestIpUtil {
    static final String[] mobileAgents = {"iphone", "android", "phone", "mobile", "wap",
            "netfront", "java", "opera mobi", "opera mini", "ucweb",
            "windows ce", "symbian", "series", "webos", "sony",
            "blackberry", "dopod", "nokia", "samsung", "palmsource",
            "xda", "pieplus", "meizu", "midp", "cldc", "motorola",
            "foma", "docomo", "up.browser", "up.link", "blazer",
            "helio", "hosin", "huawei", "novarra", "coolpad",
            "webos", "techfaith", "palmsource", "alcatel", "amoi",
            "ktouch", "nexian", "ericsson", "philips", "sagem",
            "wellcom", "bunjalloo", "maui", "smartphone", "iemobile",
            "spice", "bird", "zte-", "longcos", "pantech", "gionee",
            "portalmmm", "jig browser", "hiptop", "benq", "haier",
            "^lct", "320x320", "240x320", "176x220", "w3c ", "acs-",
            "alav", "alca", "amoi", "audi", "avan", "benq", "bird",
            "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang",
            "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
            "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-",
            "maui", "maxo", "midp", "mits", "mmef", "mobi", "mot-",
            "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm",
            "pana", "pant", "phil", "play", "port", "prox", "qwap",
            "sage", "sams", "sany", "sch-", "sec-", "send", "seri",
            "sgh-", "shar", "sie-", "siem", "smal", "smar", "sony",
            "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-",
            "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi",
            "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
            "Googlebot-Mobile"};

    public boolean requestIsMoblie(HttpServletRequest request) {
        boolean isMoblie = false;
        if (request.getHeader("User-Agent") != null) {
            for (String mobileAgent : mobileAgents) {
                if (request.getHeader("User-Agent").toLowerCase().contains(mobileAgent)) {
                    isMoblie = true;
                    break;
                }
            }
        }
        return isMoblie;
    }

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getRealIP(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static boolean internalIp(String ip) {
        byte[] addr = textToNumericFormatV4(ip);
        return internalIp(addr) || "127.0.0.1".equals(ip);
    }

    private static boolean internalIp(byte[] addr) {
        if (null == addr || addr.length < 2) {
            return true;
        }
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        // 10.x.x.x/8
        final byte sECTION1 = 0x0A;
        // 172.16.x.x/12
        final byte sECTION2 = (byte) 0xAC;
        final byte sECTION3 = (byte) 0x10;
        final byte sECTION4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte sECTION5 = (byte) 0xC0;
        final byte sECTION6 = (byte) 0xA8;
        switch (b0) {
            case sECTION1:
                return true;
            case sECTION2:
                if (b1 >= sECTION3 && b1 <= sECTION4) {
                    return true;
                }
            case sECTION5:
                if (b1 == sECTION6) {
                    return true;
                }
            default:
                return false;
        }
    }

    public static byte[] textToNumericFormatV4(String text) {
        if (text.length() == 0) {
            return null;
        }

        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return null;
                    }
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return null;
                    }
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return bytes;
    }

    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
        }
        return "127.0.0.1";
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
        }
        return "未知";
    }
}
