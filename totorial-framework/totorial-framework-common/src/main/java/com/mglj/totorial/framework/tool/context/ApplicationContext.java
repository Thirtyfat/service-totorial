package com.mglj.totorial.framework.tool.context;

import org.springframework.stereotype.Component;

/**
 * Created by zsp on 2018/8/10.
 */
@Component
public class ApplicationContext {

    /**
     * 分布式ID，识别（机器 + 进程）
     */
    private String distributedId;

    /**
     * 应用程序标识
     */
    private Integer applicationId;

    /**
     * 应用程序名称
     */
    private String applicationName;

    /**
     * 应用程序端口
     */
    private Integer serverPort;

    /**
     * 应用命名空间
     */
    private String namespace;

    /**
     * Spring的当前profile
     */
    private String profile;

    /**
     * 分布式ID的时间戳偏移值（毫秒）
     */
    private long gidTimestampOffset;

    /**
     * 是否开启缓存命中率的访问
     */
    private boolean enableCacheStatHitRate;

    /**
     * 是否开启追溯redis的访问
     */
    private boolean enableTraceRedisAccess;

    /**
     * 是否开启监控redis访问的慢日志
     */
    private boolean enableMonitorSlowRedisAccess;

    /**
     * redis访问记录慢日志的阈值（毫秒），即访问消耗的时间大于阈值则记录慢日志
     */
    private int logSlowRedisAccessThreshold = 100;

    /**
     * 批量操作记录数
     */
    private int multiOperationBatchSize = 100;

    /**
     * 缓存失效时间（秒）
     */
    private int expiredSeconds;

    /**
     * 是否开启防止缓存击穿
     */
    private boolean enableBreakdownPrevent;

    /**
     * 防止缓存击穿的空值的缓存失效时间（秒）
     */
    private int breakdownPreventExpiredSeconds = 1;

    /**
     * 是否开启追溯rabbitMQ的访问
     */
    private boolean enableTraceRabbitMqAccess;

    /**
     * 是否开启防止频繁操作
     */
    private boolean enableFrequentlyOperationPrevent = true;

    /**
     * 防止频繁操作的时间（秒）
     */
    private int frequentlyOperationPreventSeconds = 3;

    public String getDistributedId() {
        return distributedId;
    }

    public void buildDistributedId(int sequence) {
        this.distributedId = this.applicationName + sequence;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public long getGidTimestampOffset() {
        return gidTimestampOffset;
    }

    public void setGidTimestampOffset(long gidTimestampOffset) {
        this.gidTimestampOffset = gidTimestampOffset;
    }

    public boolean isEnableCacheStatHitRate() {
        return enableCacheStatHitRate;
    }

    public void setEnableCacheStatHitRate(boolean enableCacheStatHitRate) {
        this.enableCacheStatHitRate = enableCacheStatHitRate;
    }

    public boolean isEnableTraceRedisAccess() {
        return enableTraceRedisAccess;
    }

    public void setEnableTraceRedisAccess(boolean enableTraceRedisAccess) {
        this.enableTraceRedisAccess = enableTraceRedisAccess;
    }

    public boolean isEnableMonitorSlowRedisAccess() {
        return enableMonitorSlowRedisAccess;
    }

    public void setEnableMonitorSlowRedisAccess(boolean enableMonitorSlowRedisAccess) {
        this.enableMonitorSlowRedisAccess = enableMonitorSlowRedisAccess;
    }

    public int getLogSlowRedisAccessThreshold() {
        return logSlowRedisAccessThreshold;
    }

    public void setLogSlowRedisAccessThreshold(int logSlowRedisAccessThreshold) {
        this.logSlowRedisAccessThreshold = logSlowRedisAccessThreshold;
    }

    public int getMultiOperationBatchSize() {
        return multiOperationBatchSize;
    }

    public void setMultiOperationBatchSize(int multiOperationBatchSize) {
        this.multiOperationBatchSize = multiOperationBatchSize;
    }

    public final int getExpiredSeconds() {
        return expiredSeconds;
    }

    public final void setExpiredSeconds(int expiredSeconds) {
        this.expiredSeconds = expiredSeconds;
    }

    public boolean isEnableBreakdownPrevent() {
        return enableBreakdownPrevent;
    }

    public void setEnableBreakdownPrevent(boolean enableBreakdownPrevent) {
        this.enableBreakdownPrevent = enableBreakdownPrevent;
    }

    public int getBreakdownPreventExpiredSeconds() {
        return breakdownPreventExpiredSeconds;
    }

    public void setBreakdownPreventExpiredSeconds(int breakdownPreventExpiredSeconds) {
        if(breakdownPreventExpiredSeconds < 1) {
            throw new IllegalArgumentException("The value should be larger than 0.");
        }
        this.breakdownPreventExpiredSeconds = breakdownPreventExpiredSeconds;
    }

    public boolean isEnableTraceRabbitMqAccess() {
        return enableTraceRabbitMqAccess;
    }

    public void setEnableTraceRabbitMqAccess(boolean enableTraceRabbitMqAccess) {
        this.enableTraceRabbitMqAccess = enableTraceRabbitMqAccess;
    }

    public boolean isEnableFrequentlyOperationPrevent() {
        return enableFrequentlyOperationPrevent;
    }

    public void setEnableFrequentlyOperationPrevent(boolean enableFrequentlyOperationPrevent) {
        this.enableFrequentlyOperationPrevent = enableFrequentlyOperationPrevent;
    }

    public int getFrequentlyOperationPreventSeconds() {
        return frequentlyOperationPreventSeconds;
    }

    public void setFrequentlyOperationPreventSeconds(int frequentlyOperationPreventSeconds) {
        this.frequentlyOperationPreventSeconds = frequentlyOperationPreventSeconds;
    }
}
