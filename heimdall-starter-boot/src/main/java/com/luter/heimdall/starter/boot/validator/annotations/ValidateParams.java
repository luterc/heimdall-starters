package com.luter.heimdall.starter.boot.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateParams {

    ValidateParam[] value();

    boolean shortPath() default false;

    boolean anded() default true;


}
