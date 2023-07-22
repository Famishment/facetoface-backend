package com.lzj.facetoface.common;

/**
 * 返回工具类，有成功和失败的方法，返回对象是BaseRespose
 *
 * @Auther: lzj
 * @Date: 2022/11/7-11-07-14:44
 * @description: com.lzj.facetoface.common
 */

public class ResultUtils {
    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static<T> BaseResponse<T> success(T data ){
        return new BaseResponse<>(0,data,"ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code,String message,String description){
        return new BaseResponse<>(code,null,message,description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse<>(errorCode.getCode(),errorCode.getMessage(),description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String message,String description){
        return new BaseResponse<>(errorCode.getCode(),null,message,description);
    }
}
