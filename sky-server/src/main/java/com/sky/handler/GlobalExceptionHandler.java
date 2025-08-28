package com.sky.handler;

import com.sky.exception.BaseException;
import com.sky.exception.PasswordEditFailedException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result<Void> exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }



    /**
     * 修改密码时捕获的异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result<Void> exceptionHandler(NullPointerException ex){
        log.error("空指针异常:{}", ex.getMessage(),ex);
        return Result.error("空指针异常");
    }

}
