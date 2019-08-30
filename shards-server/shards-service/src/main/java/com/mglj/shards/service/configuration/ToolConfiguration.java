package com.mglj.shards.service.configuration;

import com.mglj.totorial.framework.tool.context.ApplicationContext;
import com.mglj.totorial.framework.tool.coordinate.Coordinator;
import com.mglj.totorial.framework.tool.coordinate.SimpleCoordinator;
import com.mglj.totorial.framework.tool.gid.GidGenerator;
import com.mglj.totorial.framework.tool.gid.SimpleGidGenerator;
import com.mglj.totorial.framework.tool.sequence.MillisSequenceGenerator;
import com.mglj.totorial.framework.tool.sequence.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by zsp on 2018/8/31.
 */
@Configuration
@Import(ContextConfiguration.class)
public class ToolConfiguration {

    @Autowired
    private ContextConfiguration contextConfiguration;

    @Bean
    public Coordinator coordinator() {
        SimpleCoordinator simpleCoordinator = new SimpleCoordinator();
        ApplicationContext applicationContext = contextConfiguration.applicationContext();
        applicationContext.buildDistributedId(simpleCoordinator.getSequence());
        return simpleCoordinator;
    }

    @Bean
    public GidGenerator gidGenerator() {
        return new SimpleGidGenerator();
    }

    @Bean
    public SequenceGenerator sequenceGenerator() {
        return new MillisSequenceGenerator();
    }

}
