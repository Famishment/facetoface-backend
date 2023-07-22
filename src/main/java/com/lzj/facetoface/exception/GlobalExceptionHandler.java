package com.lzj.facetoface.exception;

import com.lzj.facetoface.common.BaseResponse;
import com.lzj.facetoface.common.ErrorCode;
import com.lzj.facetoface.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @Auther: lzj
 * @Date: 2022/11/7-11-07-17:29
 * @description: com.lzj.facetoface.exception
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 此方法只捕获BusinessException异常
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException" + e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeException(RuntimeException e){
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
