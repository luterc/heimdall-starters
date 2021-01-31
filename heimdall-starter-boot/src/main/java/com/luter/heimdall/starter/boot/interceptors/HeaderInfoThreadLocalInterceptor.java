package com.luter.heimdall.starter.boot.interceptors;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.luter.heimdall.starter.boot.util.WebUtil;
import com.luter.heimdall.starter.utils.constants.BaseConstants;
import com.luter.heimdall.starter.utils.context.BaseContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.luter.heimdall.starter.boot.util.WebUtil.getHeader;


@Slf4j
public class HeaderInfoThreadLocalInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        log.debug(WebUtil.getPrintHeaderString(request));
        log.debug("获取当前用户和Client信息，存入:BaseContextHolder");
        final String userJson = getHeader(request, BaseConstants.HEADER_CURRENTUSER_PARAM);
        BaseContextHolder.setUser(StrUtil.isEmpty(userJson) ? null : userJson);
        final String username = getHeader(request, BaseConstants.HEADER_USER_NAME_PARAM);
        BaseContextHolder.setUsername(StrUtil.isEmpty(username) ? null : username);
        final String userId = getHeader(request, BaseConstants.HEADER_USER_ID_PARAM);
        BaseContextHolder.setUserId(StrUtil.isEmpty(userId) ? null : NumberUtil.parseLong(userId));
        final String clientId = getHeader(request, BaseConstants.HEADER_CLIENTID_PARAM);
        BaseContextHolder.setClientId(StrUtil.isEmpty(clientId) ? null : clientId);
        String traceId = request.getHeader(BaseConstants.HEADER_TRACE_ID_PARAM);
        MDC.put(BaseConstants.HEADER_TRACE_ID_PARAM, StrUtil.isEmpty(traceId) ? StrUtil.EMPTY : traceId);
        MDC.put(BaseConstants.HEADER_USER_ID_PARAM, userId);
        MDC.put(BaseConstants.HEADER_CLIENTID_PARAM, clientId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.debug("清理:BaseContextHolder");
        BaseContextHolder.remove();
        MDC.remove(BaseConstants.HEADER_TRACE_ID_PARAM);
        MDC.remove(BaseConstants.HEADER_USER_ID_PARAM);
        MDC.remove(BaseConstants.HEADER_CLIENTID_PARAM);
    }
}
