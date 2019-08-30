package com.mglj.totorial.framework.tool.web;

import com.mglj.totorial.framework.common.exceptions.BaseSysException;
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
 * Created by zsp on 2018/11/15.
 */
@ControllerAdvice
@Order(7)
public class UncaughtBaseSysExceptionControllerAdvice {

    private final static Logger logger = LoggerFactory.getLogger(UncaughtBaseSysExceptionControllerAdvice.class);

    @ExceptionHandler(BaseSysException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<?> handleException(BaseSysException e) {
        logger.error("统一系统运行时异常处理:" ,  e);
        return Result.errorWithMsg(Result.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}
