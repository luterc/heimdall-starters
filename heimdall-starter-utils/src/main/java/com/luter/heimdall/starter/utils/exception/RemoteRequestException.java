package com.luter.heimdall.starter.utils.exception;


public class RemoteRequestException extends RuntimeException {


    public String message;
    public Integer status;

    public RemoteRequestException(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public RemoteRequestException(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
