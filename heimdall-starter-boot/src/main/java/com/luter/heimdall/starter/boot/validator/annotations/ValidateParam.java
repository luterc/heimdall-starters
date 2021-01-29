package com.luter.heimdall.starter.boot.validator.annotations;



import com.luter.heimdall.starter.boot.validator.enums.Validater;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ValidateParam.List.class)
public @interface ValidateParam {


    boolean required() default true;


    Validater value() default Validater.NotNull;

    String express() default "";

    String argName();


    String msg() default "";


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ValidateParam[] value();
    }


}