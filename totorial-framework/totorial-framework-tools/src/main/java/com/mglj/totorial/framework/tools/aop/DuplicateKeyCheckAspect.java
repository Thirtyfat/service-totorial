package com.mglj.totorial.framework.tools.aop;


import com.mglj.totorial.framework.tools.model.CommonConstants;
import com.mglj.totorial.framework.tools.model.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.dao.DuplicateKeyException;

import java.lang.reflect.Method;


@Aspect
public class DuplicateKeyCheckAspect {

    @Pointcut("@annotation(com.mglj.totorial.framework.tools.aop.DuplicateKeyCheck)")
    public void check() {

    }

    @Around("check()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (DuplicateKeyException e) {
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            Class<?> returnType = method.getReturnType();
            if(Void.TYPE.equals(returnType) || method.getAnnotation(DuplicateKeyCheck.class).forceThrowException()) {
                throw e;
            }
            if(Number.class.isAssignableFrom(returnType)
                    || int.class.isAssignableFrom(returnType)
                    || long.class.isAssignableFrom(returnType)) {
                return CommonConstants.DUPLICATE_KEY_AFFECTED_ROWS;
            }
            if(Result.class.isAssignableFrom(returnType)) {
                return Result.conflict();
            }
            throw e;
        }
    }

}
