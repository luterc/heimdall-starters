package com.luter.heimdall.starter.boot.validator.core;

@FunctionalInterface
public interface ParamValidator<T> {
    Boolean validate(T value);
}
