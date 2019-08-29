package com.mglj.totorial.framework.gid.service.impl;

import com.mglj.totorial.framework.gid.service.api.GidServerService;
import com.mglj.totorial.framework.gid.service.api.GidService;
import com.mglj.totorial.framework.tools.moduling.BasicModule;
import com.mglj.totorial.framework.tools.util.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yj on 2019/8/29.
 **/
@Service
public class GidServiceImpl extends BasicModule implements GidService {

    private static final Logger logger = LoggerFactory.getLogger(GidServiceImpl.class);

    private volatile long machineNo = -1;
    private volatile long currentMillis = 0;
    private final static AtomicInteger incr
            = new AtomicInteger(new SecureRandom().nextInt(0x1000));
    private final Object lockObj = new Object();
    private final long offset = 1501516800000L; //2017-08-01

    @Autowired
    private GidServerService gidServerService;

    @Value("${server.port}")
    private Integer serverPort;
    private String  host = "10.80.85.22";

    public GidServiceImpl() {
        super("全局ID: " + GidServiceImpl.class);
    }

    /**
     * 生成一个ID。
     *
     * @return 返回一个全局唯一的ID
     */
    @Override
    public long generate() {
        if(machineNo == -1) {
            fetchMachineNo();
        }
        synchronized (lockObj) {
            /*
             * 上锁防止单个节点每毫秒的自增数被耗尽而超用
             */
            long millis = System.currentTimeMillis();
            if(currentMillis < millis) {
                /*
                 * 更新当前毫秒，并且自增数归零
                 */
                currentMillis = millis;
                incr.set(0);
            }
            if(incr.compareAndSet(0x0FFF, 0)) {
                /*
                 * 如果当前自增数已达最大值，则归零；判断最新毫秒数，是否已流逝到下一毫秒，否则线程自旋等待时间的流逝
                 */
                long latestMillis = System.currentTimeMillis();
                final long m = currentMillis;
                while(latestMillis <= m){
                    Thread.yield();
                    latestMillis = System.currentTimeMillis();
                }
                millis = latestMillis;
                currentMillis = millis;
            }
            //41位时间戳 + 10位机器序号 + 12位自增序列
            return ((millis - offset) << 22) | (machineNo << 12) | incr.incrementAndGet();
        }
    }

    private void fetchMachineNo() {
        if(machineNo == -1) {
            synchronized(this) {
                if(machineNo == -1) {
                    try {
                        // 为防止一台机器部署多个不同端口号的应用,需要把IP字段加上端口号(ip:port)
                        String ips = NetUtils.getHostAddress() + ":" + serverPort;
                        // junit
                        String ip = host + ":" + serverPort;
                        Integer sequence = gidServerService.getSequenceOrUpdateEmptyGidServer(host);
                        if(sequence != null) {
                            machineNo = sequence;
                        } else {
                            machineNo = ip.hashCode() & 0x03FF;
                            if(logger.isWarnEnabled()) {
                                logger.warn("未配置机器\"" + ip + "\"的序号");
                            }
                        }
                    } catch (UnknownHostException e) {
                        machineNo = new SecureRandom().nextInt(0x0400);
                        logger.error("获取机器ip异常", e);
                    } finally {
                        if(logger.isInfoEnabled()) {
                            logger.info("当前机器的序号: " + machineNo);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void doInitialize() {
        if(serverPort == null) {
            throw new IllegalStateException("未配置: server.port");
        }
        fetchMachineNo();
    }
}
