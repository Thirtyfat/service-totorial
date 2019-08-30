package com.mglj.shards.service.aop;

import com.mglj.shards.service.service.api.SyncService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * 执行顺序在@Transactional拦截处理之前，在事务提交之后再发送消息
 *
 * Created by zsp on 2019/3/20.
 */
@Order(0)
@Aspect
public class SyncAspect{

    private final Logger logger = LoggerFactory.getLogger(SyncAspect.class);

    private SyncService syncService;

    public SyncAspect(SyncService syncService) {
        this.syncService = syncService;
    }

    @Pointcut("@annotation(com.mglj.shards.service.aop.Sync)")
    public void access() {

    }

    @Around("access()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        SyncMessageHolder.clear();
        Object result;
        try {
            result = pjp.proceed();
            if(logger.isDebugEnabled()) {
                logger.debug("事务提交，发送消息：" + SyncMessageHolder.get());
            }
            //本地数据库事务成功提交之后，真正广播下发消息
            syncService.sendSyncMessageAfterTransactionCommited();

            return result;
        } finally {
            SyncMessageHolder.clear();
        }
    }

}
