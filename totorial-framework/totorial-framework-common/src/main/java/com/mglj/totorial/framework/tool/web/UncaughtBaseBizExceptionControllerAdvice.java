package com.mglj.totorial.framework.tool.web;

import com.mglj.totorial.framework.common.exceptions.BaseBizException;
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
@Order(5)
public class UncaughtBaseBizExceptionControllerAdvice {

    private final static Logger logger = LoggerFactory.getLogger(UncaughtBaseBizExceptionControllerAdvice.class);

    @ExceptionHandler(BaseBizException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<?> handleException(BaseBizException e) {
        logger.error("统一业务异常处理:" ,  e);
        return Result.errorWithMsg(Result.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}
