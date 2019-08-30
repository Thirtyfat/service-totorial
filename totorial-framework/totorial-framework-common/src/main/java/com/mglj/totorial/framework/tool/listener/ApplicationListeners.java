package com.mglj.totorial.framework.tool.listener;

import com.mglj.totorial.framework.common.util.NetUtils;
import com.mglj.totorial.framework.tool.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

/**
 * @author dalaoyang
 * SpringBoot启动类监听器，当SpringBoot环境准备完毕执行。
 *
 * 在引用处配置如下直接使用
 * context:
 *    listener:
 *      classes: com.yhdx.baseframework.tool.listener.ApplicationListeners
 */
@Component
public class ApplicationListeners implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationListeners.class);

    private final static String KEY_APP_NAME = "appName";

    private final static String KEY_IP = "ip";


    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event)
    {
        ConfigurableEnvironment configurableEnvironment=event.getEnvironment();
        String applicationName = configurableEnvironment.getProperty("spring.application.name");
        MDC.put(KEY_APP_NAME, applicationName);
        try {
            MDC.put(KEY_IP, NetUtils.getHostAddress());
        }catch (UnknownHostException uhe){
            LogUtils.error(logger,"获取本机IP失败："+uhe.getMessage());
        }
    }

}
