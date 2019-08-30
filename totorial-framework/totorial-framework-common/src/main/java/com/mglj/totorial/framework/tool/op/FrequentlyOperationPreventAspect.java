package com.mglj.totorial.framework.tool.op;

import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.tool.context.RequestContextHolder;
import com.mglj.totorial.framework.tool.context.ServletContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by zsp on 2018/11/8.
 */
@Aspect
public class FrequentlyOperationPreventAspect implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(FrequentlyOperationPreventAspect.class);

    private ApplicationContext container;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.container = applicationContext;
    }

    @Pointcut("@annotation(com.mglj.totorial.framework.tool.op.FrequentlyOperationPrevent)")
    public void validate() {
    }

    @Around("validate()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        Object bean = pjp.getTarget();
        Class<?> clazz = bean.getClass();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        if(logger.isDebugEnabled()) {
            logger.debug("validate class: " + clazz + ", method: " + method);
        }

        boolean present = method.isAnnotationPresent(FrequentlyOperationPrevent.class);
        if(!present) {
            return pjp.proceed();
        }

        com.mglj.totorial.framework.tool.context.ApplicationContext applicationContext =
                container.getBean(com.mglj.totorial.framework.tool.context.ApplicationContext.class);
        if(!applicationContext.isEnableFrequentlyOperationPrevent()) {
            return pjp.proceed();
        }

        int frequentlyOperationPreventSeconds  = applicationContext.getFrequentlyOperationPreventSeconds();
        HttpServletRequest request = ServletContextHolder.getRequest();
        String requestId = request.getRequestURI() + RequestContextHolder.getUserContext().getUserId();
        FrequentlyOperationPreventHandler frequentlyOperationPreventHandler
                = container.getBean(FrequentlyOperationPreventHandler.class);
        if(frequentlyOperationPreventHandler.validate(requestId, frequentlyOperationPreventSeconds)) {
            return pjp.proceed();
        }

        return Result.errorWithMsg(Result.ERROR_STATUS, frequentlyOperationPreventSeconds + "秒内禁止频繁刷新访问，请稍后尝试！");
    }

}
