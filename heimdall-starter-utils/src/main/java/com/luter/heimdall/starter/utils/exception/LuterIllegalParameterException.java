package com.luter.heimdall.starter.utils.exception;


public class LuterIllegalParameterException extends RuntimeException {

    public LuterIllegalParameterException(String message) {

        super(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code = 400;

    public LuterIllegalParameterException(int code) {
        this.code = code;
    }

    public LuterIllegalParameterException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public LuterIllegalParameterException(int code, String message) {
        super(message);
        this.code = code;
    }

    public LuterIllegalParameterException(Throwable throwable) {
        super(throwable);
    }

    public LuterIllegalParameterException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
