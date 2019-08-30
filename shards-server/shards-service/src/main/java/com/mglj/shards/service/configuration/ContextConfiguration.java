package com.mglj.shards.service.configuration;

import com.mglj.totorial.framework.tool.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zsp on 2018/8/13.
 */
@Configuration
public class ContextConfiguration {

    @Value("${myconf.applicationId}")
    private Integer applicationId;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private Integer serverPort;

    @Value("${myconf.namespace}")
    private String namespace;

    @Value("${myconf.gid.timestamp.offset}")
    private long gidTimestampOffset;

    @Bean
    public ApplicationContext applicationContext() {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.setApplicationId(applicationId);
        applicationContext.setApplicationName(applicationName);
        applicationContext.setServerPort(serverPort);
        applicationContext.setNamespace(namespace);
        applicationContext.setGidTimestampOffset(gidTimestampOffset);
        applicationContext.setEnableCacheStatHitRate(true);
//        applicationContext.setEnableTraceRedisAccess(true);
        applicationContext.setEnableMonitorSlowRedisAccess(true);
        applicationContext.setEnableTraceRabbitMqAccess(true);

        return applicationContext;
    }

}
