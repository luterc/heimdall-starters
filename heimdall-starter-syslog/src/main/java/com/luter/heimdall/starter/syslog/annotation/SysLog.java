package com.luter.heimdall.starter.syslog.annotation;


import com.luter.heimdall.starter.syslog.enums.BizType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";

    BizType type() default BizType.OTHER;

    boolean isSaveRequestData() default true;
}
