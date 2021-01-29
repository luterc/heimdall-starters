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

package com.luter.macaw.starter.xss.xss;

import cn.hutool.core.util.StrUtil;
import com.luter.macaw.starter.xss.FilterProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class XssFilter implements Filter {
    private final FilterProperties properties;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeUrl(req, resp)) {
            log.debug("url:{}不在xss处理列表中,不做处理", req.getRequestURI());
            chain.doFilter(request, response);
            return;
        }
        log.debug("开始xss处理,url:{}", req.getRequestURI());
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    private boolean handleExcludeUrl(HttpServletRequest request, HttpServletResponse response) {
        //false代表不启用
        if (!properties.getEnabled()) {
            return true;
        }
        List<String> tempExcludes = new ArrayList<>();
        if (StrUtil.isNotEmpty(properties.getExcludes())) {
            String[] url = properties.getExcludes().split(",");
            tempExcludes.addAll(Arrays.asList(url));
        }
        //如果excludes为空，则没有例外uri，全部过滤
        if (tempExcludes.isEmpty()) {
            return false;
        }
        //获取到当前请求的url
        String url = request.getRequestURI();
        //遍历，如果匹配上，则认为是需要排除的uri
        for (String pattern : tempExcludes) {
            AntPathMatcher matcher = new AntPathMatcher();
            boolean match = matcher.match(pattern, url);
            if (match) {
                return true;
            }
        }
        //否则进行xss过滤
        return false;
    }

    @Override
    public void destroy() {

    }
}