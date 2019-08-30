package com.mglj.shards.service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by zsp on 2018/7/13.
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                //.exposedHeaders(HttpHeaders.SET_COOKIE)
                .maxAge(3600L);
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//       /* registry.addInterceptor(requestInterceptor()).order(1);
//        registry.addInterceptor(authenticationInterceptor()).order(2);*/
//       // registry.addInterceptor(new DataSourceInterceptor());
//    }

}
