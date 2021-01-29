package com.luter.heimdall.starter.boot.validator.annotations;


import com.luter.heimdall.starter.boot.validator.core.ValidatorAspect;
import com.luter.heimdall.starter.boot.validator.enums.Validater;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ValidatorAspect.class})
@Repeatable(Validator.List.class)
public @interface Validator {

    boolean required() default false;

    String message() default "参数验证错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Validater func();

    String express() default "";

    @Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Validator[] value();
    }


}
