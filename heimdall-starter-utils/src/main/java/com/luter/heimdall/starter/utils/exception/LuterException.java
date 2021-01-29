package com.luter.heimdall.starter.utils.exception;


public class LuterException extends RuntimeException {


    public String message;
    public Integer code;

    public LuterException(String message, Throwable cause) {
        super(message, cause);
    }

    public LuterException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public LuterException(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
