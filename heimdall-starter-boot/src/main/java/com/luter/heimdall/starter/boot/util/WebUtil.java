package com.luter.heimdall.starter.boot.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The type Web util.
 */
@Slf4j
public final class WebUtil {
    private final static AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    /**
     * 从上下文获取请求对象
     *
     * @return the http servlet request
     */
    public static HttpServletRequest request() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取请求头信息并且转码
     *
     * @param request the request
     * @param name    the name
     * @return the header
     */
    public static String getHeader(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        if (StrUtil.isEmpty(value)) {
            return StrUtil.EMPTY;
        }
        return URLUtil.decode(value);
    }


    /**
     * 获取header参数字符串，用作打印
     */
    public static String getPrintHeaderString(HttpServletRequest request) {
        final Enumeration<String> names = request.getHeaderNames();
        StringBuilder sb = new StringBuilder("\n================headers\n");
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Enumeration<String> headers = request.getHeaders(name);
            sb.append(name).append(":");
            while (headers.hasMoreElements()) {
                sb.append(headers.nextElement()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    public static String getPrintQueryParamsString(HttpServletRequest request) {
        StringBuilder prams = new StringBuilder();
        if (null != request.getParameterMap()) {
            int index = 0;
            Set<String> paramsStirs = request.getParameterMap().keySet();
            for (String param : paramsStirs) {
                prams.append(index++ == 0 ? "" : "&").append(param).append("=");
                prams.append(request.getParameter(param));
            }
        }
        return prams.toString();
    }

    /**
     * 判断url是否在排除列表中
     *
     * @param excludeUrls 排除的url列表
     * @param request     请求
     * @return the boolean true:是，false,否
     */
    public static boolean isExcludeUrl(List<String> excludeUrls, HttpServletRequest request) {
        if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            return true;
        }
        return isExcludeUrl(excludeUrls, request.getRequestURI());
    }

    /**
     * 判断url是否在排除列表中
     *
     * @param excludeUrls 排除的url列表
     * @param url         the url 当前访问的url
     * @return the boolean true:是，false,否
     */
    public static boolean isExcludeUrl(List<String> excludeUrls, String url) {
        //url空，或者白名单空，需要鉴权
        if (StrUtil.isEmpty(url) || excludeUrls.isEmpty()) {
            return false;
        }
        for (String s : excludeUrls) {
            //url匹配到白名单了，直接放行
            if (ANT_PATH_MATCHER.match(s, url)) {
                return true;
            }
        }
        return false;

    }
}
