
package com.luter.heimdall.starter.syslog.aspect;


import cn.hutool.core.util.StrUtil;
import com.luter.heimdall.starter.syslog.annotation.SysLog;
import com.luter.heimdall.starter.syslog.dto.SysLogEventDTO;
import com.luter.heimdall.starter.syslog.enums.BizStatus;
import com.luter.heimdall.starter.syslog.enums.BizType;
import com.luter.heimdall.starter.syslog.enums.TerminalType;
import com.luter.heimdall.starter.syslog.event.SysLogEvent;
import com.luter.heimdall.starter.utils.constants.BaseConstants;
import com.luter.heimdall.starter.utils.json.JacksonUtils;
import com.luter.heimdall.starter.utils.request.RequestIpUtil;
import com.luter.heimdall.starter.utils.request.RequestUserAgentUtils;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Map;

@Aspect
@Slf4j
public class SysLogAspect {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Environment environment;

    @Pointcut("execution(public * *(..)) " +
            "&& @annotation(com.luter.heimdall.starter.syslog.annotation.SysLog)")
    public void logPointCut() {
    }

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();


    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.debug("doBefore,方法:{}.{}", className, methodName);
    }

    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.debug("doAfterThrowing,方法:{}.{}", className, methodName);

    }

    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.debug("doAfterThrowing,方法:{}.{}", className, methodName);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        HttpServletRequest request = getRequest();
        if (null == request) {
            log.warn("it is not a http servlet request, ignored!");
            return;
        }
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        try {
            // 获得注解
            SysLog controllerLog = method.getAnnotation(SysLog.class);
            if (controllerLog == null) {
                return;
            }
            SysLogEventDTO opeLog = new SysLogEventDTO();
            opeLog.setTraceId(MDC.get(BaseConstants.HEADER_TRACE_ID_PARAM));
            opeLog.setStatus(BizStatus.SUCCESS.getValue());
            opeLog.setRequestIp(RequestIpUtil.getRealIP(request));
            opeLog.setRequestUrl(request.getRequestURI());
            opeLog.setRequestTime(LocalDateTime.now());
            opeLog.setResponseResult(StrUtil.sub(JacksonUtils.objectToJson(jsonResult), 0, 1000));
            if (e != null) {
                opeLog.setStatus(BizStatus.FAIL.getValue());
                opeLog.setExceptionMessage(StrUtil.sub(e.getMessage(), 0, 1000));
            }
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            opeLog.setMethod(className + "." + methodName + "()");
            opeLog.setRequestMethod(request.getMethod());
            final BizType type = controllerLog.type();
            opeLog.setBusinessType(type.getValue());
            String title = controllerLog.value();
            if (StrUtil.isEmpty(title)) {
                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                if (null != apiOperation) {
                    title = apiOperation.value();
                    if (StrUtil.isEmpty(title)) {
                        title = apiOperation.notes();
                        if (StrUtil.isEmpty(title)) {
                            title = type.getName();
                        }
                    }
                }
            }
            opeLog.setTitle(title);
            opeLog.setTerminalType(getTerminalType(request).getValue());
            //UserAgent
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            final OperatingSystem operatingSystem = userAgent.getOperatingSystem();
            if (null != operatingSystem) {
                opeLog.setTerminalOsName(operatingSystem.getName());
            }
            opeLog.setBrowserName(RequestUserAgentUtils.getBrowser(request).getName());
            final Version browserVersion = userAgent.getBrowserVersion();
            if (null != browserVersion) {
                opeLog.setBrowserVersion(userAgent.getBrowserVersion().getVersion());
            } else {
                opeLog.setBrowserVersion("UNKNOW");
            }
            if (controllerLog.isSaveRequestData()) {
                setRequestValue(joinPoint, opeLog);
            }
            String appName = environment.getProperty("spring.application.name");
            String host = "";
            try {
                host = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ignored) {
            }
            String port = environment.getProperty("local.server.port");
            opeLog.setAppHostIp(host);
            opeLog.setAppName(appName);
            opeLog.setAppPort(port);
            long endTime = System.currentTimeMillis();
            opeLog.setConsumingTime(endTime - startTime.get());
            startTime.remove();
            applicationContext.publishEvent(new SysLogEvent(opeLog));
        } catch (Exception exp) {
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
        }
    }


    private void setRequestValue(JoinPoint joinPoint, SysLogEventDTO operLog) {
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.DELETE.name().equals(requestMethod)
                || HttpMethod.PUT.name().equals(requestMethod)
                || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            operLog.setRequestParam(StrUtil.sub(params, 0, 255));
        } else if (HttpMethod.GET.name().equals(requestMethod)) {
            final HttpServletRequest request = getRequest();
            if (null != request) {
                operLog.setRequestParam(StrUtil.sub(request.getQueryString(), 0, 255));
            }
        } else {
            final HttpServletRequest request = getRequest();
            if (null != request) {
                request.getQueryString();
                Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                operLog.setRequestParam(StrUtil.sub(paramsMap.toString(), 0, 255));
            }

        }
    }

    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (!isFilterObject(o)) {
                    String jsonObj = JacksonUtils.toJson(o);
                    params.append(jsonObj).append(" ");
                }
            }
        }
        return params.toString().trim();
    }

    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }

    private static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            return attributes.getRequest();
        }
        log.debug("it is not a http servlet request,ignored!  ");
        return null;
    }

    public static TerminalType getTerminalType(HttpServletRequest request) {
        if (RequestUserAgentUtils.isComputer(request)) {
            return TerminalType.PC;
        }
        if (RequestUserAgentUtils.isMobile(request)) {
            return TerminalType.MOBILE;
        }
        if (RequestUserAgentUtils.isTablet(request)) {
            return TerminalType.TABLET;
        }
        return TerminalType.OTHER;
    }


}
