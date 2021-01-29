package com.luter.heimdall.starter.utils.exception;

public class LuterIdEntityNotFoundException extends RuntimeException {
    public String message;
    public Integer code;

    public LuterIdEntityNotFoundException(String message) {
        this.message = message;
    }

    public LuterIdEntityNotFoundException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }


    @Override
    public String getMessage() {
        return message;
    }

}
