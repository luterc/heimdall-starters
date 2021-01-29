package com.luter.heimdall.starter.boot.validator.core;



import com.luter.heimdall.starter.boot.validator.enums.Validater;
import com.luter.heimdall.starter.boot.validator.annotations.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatorAspect implements ConstraintValidator<Validator, Object> {

    private static final String DEFAULT_MSG = "参数验证错误";


    private boolean required = false;
    private Validater func;
    private String express;
    private String msg;


    public ValidatorAspect() {
    }

    @Override
    public void initialize(Validator constraintAnnotation) {
        required = constraintAnnotation.required();
        func = constraintAnnotation.func();
        express = constraintAnnotation.express();
        msg = constraintAnnotation.message();
        if (DEFAULT_MSG.equals(msg)) {
            msg = func.msg + express;
        }
        //有验证函数，则 required=true
        if (null != func) {
            required = true;
        }
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (required) {
            String tmpMsg = msg;
            Boolean res = false;
            try {
                res = func.func.apply(value, express);
            } catch (Exception e) {
                // handle exception
                String errorMessage;
                if (e.getCause() != null && e.getCause().getMessage() != null) {
                    errorMessage = e.getCause().getMessage();
                } else {
                    errorMessage = e.getMessage();
                }
                tmpMsg = msg + "; raw exception occured, info: " + errorMessage;
            }
            if (!res) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(tmpMsg)
                        .addConstraintViolation();
            }
            return res;
        } else {
            return true;
        }
    }

}