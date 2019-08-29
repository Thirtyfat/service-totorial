package com.mglj.order.server.startup;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {
        "com.mglj.totorial.framework.gid",
        "com.mglj.totorial.framework.tools"
})
@ComponentScan("com.mglj")
@MapperScan(basePackages = {
        "com.mglj.totorial.framework.gid.dao.api",
        "com.mglj.order.server.dao.mapper"
})
public class OrderServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServerApplication.class, args);
    }

}
