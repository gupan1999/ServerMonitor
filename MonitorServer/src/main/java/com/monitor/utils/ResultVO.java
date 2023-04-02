package com.monitor.utils;

import com.monitor.utils.ResultCode;
import lombok.Getter;

import java.util.Date;

@Getter
public class ResultVO<T> {
    /**
     * 状态码，比如1000代表响应成功
     */
    private final int code;
    /**
     * 响应信息，用来说明响应情况
     */
    private final String message;
    /**
     * 响应的具体数据
     */
    private T data;

    private Date timestamp;

    public ResultVO(T data) {
        this(ResultCode.SUCCESS,  data);
    }

    public ResultVO(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
        this.timestamp = new Date(System.currentTimeMillis());
    }
}