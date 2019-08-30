package com.mglj.shards.service.configuration;

import com.mglj.totorial.framework.tool.context.RequestContextHolder;
import com.mglj.totorial.framework.tool.context.ServletContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by zsp on 2018/9/14.
 */
@Configuration
public class FeginInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, String> headers = ServletContextHolder.getHeaders();
        if(headers!=null&&headers.size()<1){
            headers= RequestContextHolder.getRequestHeaders();
        }
        if(headers!=null){
            for(String headerName : headers.keySet()){
                requestTemplate.header(headerName, headers.get(headerName));
            }
        }
    }

}
