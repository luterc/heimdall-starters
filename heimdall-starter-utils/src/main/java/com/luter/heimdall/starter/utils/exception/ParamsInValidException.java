package com.luter.heimdall.starter.utils.exception;

public class ParamsInValidException extends RuntimeException {

    public ParamsInValidException() {
    }

    public ParamsInValidException(String message) {
        super(message);
    }

    public ParamsInValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamsInValidException(Throwable cause) {
        super(cause);
    }

    public ParamsInValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
