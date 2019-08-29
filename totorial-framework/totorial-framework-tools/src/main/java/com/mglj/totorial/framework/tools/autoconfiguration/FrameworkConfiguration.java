package com.mglj.totorial.framework.tools.autoconfiguration;


import com.mglj.totorial.framework.tools.aop.DuplicateKeyCheckAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FrameworkConfiguration {

    public FrameworkConfiguration(){
        System.out.println("FrameworkConfiguration ==> " + this);
    }

    @Bean
    public DuplicateKeyCheckAspect duplicateKeyCheckAspect() {
        return new DuplicateKeyCheckAspect();
    }

}
