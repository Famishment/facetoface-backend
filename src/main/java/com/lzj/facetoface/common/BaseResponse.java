package com.lzj.facetoface.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 基本返回类（基本响应）
 *
 * @Auther: lzj
 * @Date: 2022/11/7-11-07-14:36
 * @description: com.lzj.facetoface.common
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data,String message) {
        this(code,data,message,"");
    }

    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
}
