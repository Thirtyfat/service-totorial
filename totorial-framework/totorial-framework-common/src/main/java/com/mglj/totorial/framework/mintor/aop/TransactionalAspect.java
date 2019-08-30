package com.mglj.totorial.framework.mintor.aop;

import io.shardingsphere.api.HintManager;
import io.shardingsphere.core.hint.HintManagerHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * 在DataSourceRouteRuleAspect拦截之后执行，即使DataSourceRouteRuleAspect设置了数据源的读写特性，
 * 但是只要方法上有Transactional注解，则强制数据源走写(主)库。
 */
@Order(0)
@Aspect
public class TransactionalAspect {

    private final Logger logger = LoggerFactory.getLogger(TransactionalAspect.class);

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void access() {

    }

    @Around("access()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        if(logger.isDebugEnabled()) {
            logger.debug("@Transactional: setMasterRouteOnly");
        }
        HintManagerHolder.clear();
        try (HintManager hintManager = HintManager.getInstance()) {
            hintManager.setMasterRouteOnly();
            return pjp.proceed();
        }
    }

}
