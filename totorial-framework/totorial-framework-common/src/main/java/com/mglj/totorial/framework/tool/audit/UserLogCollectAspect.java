package com.mglj.totorial.framework.tool.audit;

import com.mglj.totorial.framework.tool.audit.domain.builder.UserLogBuilder;
import com.mglj.totorial.framework.tool.audit.service.api.UserLogCollectService;
import com.mglj.totorial.framework.tool.metadata.domain.Operation;
import com.mglj.totorial.framework.tool.metadata.service.api.OperationSyncService;
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

import java.lang.reflect.Method;

/**
 * Created by zsp on 2018/8/30.
 */
@Aspect
public class UserLogCollectAspect implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(UserLogCollectAspect.class);

    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Pointcut("@annotation(com.mglj.totorial.framework.tool.audit.UserLogCollect)")
    public void collect() {
    }

    @Around("collect()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        Object bean = pjp.getTarget();
        Class<?> clazz = bean.getClass();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        if (logger.isDebugEnabled()) {
            logger.debug("user log collect class: " + clazz + ", method: " + method);
        }

        boolean present = method.isAnnotationPresent(UserLogCollect.class);
        if (!present) {
            return pjp.proceed();
        }

        UserLogContextHolder.clear();

        Object bizResult = null;
        try {
            long start = System.currentTimeMillis();
            bizResult = pjp.proceed();
            long cost = System.currentTimeMillis() - start;

            String className = clazz.getName();
            String methodName = method.getName();
            OperationSyncService operationSyncService = applicationContext.getBean(OperationSyncService.class);
            UserLogCollect userLogCollect = method.getAnnotation(UserLogCollect.class);
            Operation operation = operationSyncService.getOrSaveOperation(className, methodName,
                    userLogCollect.title(), userLogCollect.group());
            UserLogBuilder userLogBuilder = UserLogContextHolder.get();
            userLogBuilder.setOperation(operation).setCost(cost);
            UserLogCollectService userLogCollectService = applicationContext.getBean(UserLogCollectService.class);
            userLogCollectService.collectUserLog(userLogBuilder);
        } finally {
            UserLogContextHolder.clear();
        }

        return bizResult;
    }

}
