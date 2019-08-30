package com.mglj.shards.service.api.request;

/**
 * Created by zsp on 2019/3/21.
 */
public class SaveSyncMessageAckRequest {

    /**
     * 消息Id
     */
    private Long syncMessageId;
    /**
     * 客户端IP
     */
    private Integer clientIp;
    /**
     * 客户端端口
     */
    private Integer clientPort;

    /**
     * 设置消息Id
     */
    public void setSyncMessageId(Long value) {
        this.syncMessageId = value;
    }
    /**
     * 获取消息Id
     */
    public Long getSyncMessageId() {
        return this.syncMessageId;
    }
    /**
     * 设置客户端IP
     */
    public void setClientIp(Integer value) {
        this.clientIp = value;
    }
    /**
     * 获取客户端IP
     */
    public Integer getClientIp() {
        return this.clientIp;
    }
    /**
     * 设置客户端端口
     */
    public void setClientPort(Integer value) {
        this.clientPort = value;
    }
    /**
     * 获取客户端端口
     */
    public Integer getClientPort() {
        return this.clientPort;
    }

}
