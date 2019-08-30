package com.mglj.shards.service.configuration;

import com.mglj.shards.service.aop.SyncAspect;
import com.mglj.shards.service.service.api.SyncService;
import com.mglj.totorial.framework.tool.op.AccessLogAspect;
import com.mglj.totorial.framework.tool.validation.ValidationAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zsp on 2019/3/20.
 */
@Configuration
public class AopConfiguration {

    @Bean
    public ValidationAspect validationAspect() {
        return new ValidationAspect();
    }

    @Bean
    public AccessLogAspect accessLogAspect() {
        return new AccessLogAspect();
    }

    @Bean
    public SyncAspect syncAspect(SyncService syncService) {
        return new SyncAspect(syncService);
    }

}
