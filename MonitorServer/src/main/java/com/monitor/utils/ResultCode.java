package com.monitor.utils;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(2000, "操作成功"),

    FAILED(4000, "响应失败"),

    VALIDATE_FAILED(3000, "参数校验失败"),

    AlREADY_EXISTS(3001,"用户已存在"),

    NO_AUTHORITIES(3002,"无相关权限"),

    ERROR(5000, "未知错误"),

    PASSWORD_WRONG(5001,"用户或密码错误"),

    ILLEGAL_TOKEN(5008, "非法token"),

    OTHER_CLIENT(5012, "其他客户端登录"),

    TOKEN_EXPIRED(5014,"无效token"),

    NO_DATA(3002,"暂无数据");


    private final int code;
    private final String message;

    ResultCode(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

}