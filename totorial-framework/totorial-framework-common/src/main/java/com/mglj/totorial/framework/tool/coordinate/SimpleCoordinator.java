package com.mglj.totorial.framework.tool.coordinate;

import com.mglj.totorial.framework.common.util.NetUtils;
import com.mglj.totorial.framework.tool.coordinate.domain.Server;
import com.mglj.totorial.framework.tool.coordinate.service.api.ServerService;
import com.mglj.totorial.framework.tool.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * Created by zsp on 2018/7/11.
 */
public class SimpleCoordinator implements Coordinator, InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(SimpleCoordinator.class);

    @Autowired
    private ServerService serverService;

    @Value("${server.port}")
    private Integer serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    private volatile int currentSequence;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(serverPort == null) {
            throw new IllegalStateException("未配置: server.port");
        }
        if(applicationName == null) {
            throw new IllegalStateException("未配置: spring.application.name");
        }
        currentSequence = getSequence(getServerPort());
    }

    @Override
    public int getSequence() {
        return currentSequence;
    }

    @Override
    public int getSequence(String ip) {
        Objects.requireNonNull(ip, "The ip cannot be null.");
        return getSequence(ip, getServerPort());
    }

    @Override
    public int getSequence(int port) {
        try {
            String ip = NetUtils.getHostAddress();
            return getSequence(ip, port);
        } catch (UnknownHostException e) {
            LogUtils.error(logger, "Fail to fetch ip.", e);
            int random = new SecureRandom().nextInt(Server.MAX_SERVER_SIZE);
            LogUtils.info(logger, "get sequence [" + random + "] by random.");
            return random;
        }
    }

    @Override
    public int getSequence(String ip, int port) {
        Objects.requireNonNull(ip, "The ip cannot be null.");
        if(port < 1) {
            port = getServerPort();
        }
        Integer sequence = serverService.getSequenceOrUpdateEmptyServer(applicationName, ip, port);
        if(sequence != null) {
            LogUtils.info(logger, "get sequence [" + sequence + "] by " + ip + ":" + port);
            return sequence.intValue();
        }
        LogUtils.warn(logger, "The sequence for " + ip + ":" + port + " is not defined.");
        int hash = ip.hashCode() & Server.MAX_SERVER_SIZE_MASK;
        LogUtils.info(logger, "get sequence [" + hash + "] by " + ip + " hash code.");
        return hash;
    }

    private int getServerPort() {
        if(serverPort != null) {
            return serverPort;
        }
        return 8080;
    }

}
