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

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public final class RequestUserAgentUtils {
    public static UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

    public static DeviceType getDeviceType(HttpServletRequest request) {
        return getUserAgent(request).getOperatingSystem().getDeviceType();
    }

    public static boolean isComputer(HttpServletRequest request) {
        return DeviceType.COMPUTER.equals(getDeviceType(request));
    }

    public static boolean isMobile(HttpServletRequest request) {
        return DeviceType.MOBILE.equals(getDeviceType(request));
    }

    public static boolean isTablet(HttpServletRequest request) {
        return DeviceType.TABLET.equals(getDeviceType(request));
    }

    public static boolean isMobileOrTablet(HttpServletRequest request) {
        DeviceType deviceType = getDeviceType(request);
        return DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType);
    }

    public static Browser getBrowser(HttpServletRequest request) {
        return getUserAgent(request).getBrowser();
    }

    public static boolean isLteIe8(HttpServletRequest request) {
        Browser browser = getBrowser(request);
        return Browser.IE5.equals(browser) || Browser.IE6.equals(browser)
                || Browser.IE7.equals(browser) || Browser.IE8.equals(browser);
    }
}
