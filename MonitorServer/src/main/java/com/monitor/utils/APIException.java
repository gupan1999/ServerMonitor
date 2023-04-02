package com.monitor.utils;

import lombok.Getter;

@Getter //只要getter方法，无需setter
public class APIException extends RuntimeException {
    private final ResultCode resultCode;

    public APIException() {
        this(ResultCode.ERROR);
    }


    public APIException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }
}