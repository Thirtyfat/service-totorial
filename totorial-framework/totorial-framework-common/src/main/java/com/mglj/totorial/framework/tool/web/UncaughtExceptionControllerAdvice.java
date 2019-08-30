package com.mglj.totorial.framework.tool.web;

import com.mglj.totorial.framework.common.lang.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by zsp on 2018/7/12.
 */
@ControllerAdvice
@Order(10)
public class UncaughtExceptionControllerAdvice {

    private final static Logger logger = LoggerFactory.getLogger(UncaughtExceptionControllerAdvice.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<?> handleException(Throwable t) {
        logger.error("统一服务内部异常处理:" ,  t);
        return Result.errorWithMsg(Result.INTERNAL_SERVER_ERROR, Result.INTERNAL_SERVER_ERROR_MSG);
    }

}
