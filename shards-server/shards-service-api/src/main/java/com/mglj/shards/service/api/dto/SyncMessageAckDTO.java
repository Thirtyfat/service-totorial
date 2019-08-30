package com.mglj.shards.service.api.dto;

import java.util.Date;

/**
 * Created by zsp on 2019/3/21.
 */
public class SyncMessageAckDTO {

    /**
     * 同步确认Id
     */
    private Long syncMessageAckId;
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
     * 创建时间
     */
    private Date createTime;

    private String stringClientIp;

    /**
     * 设置同步确认Id
     */
    public void setSyncMessageAckId(Long value) {
        this.syncMessageAckId = value;
    }
    /**
     * 获取同步确认Id
     */
    public Long getSyncMessageAckId() {
        return this.syncMessageAckId;
    }
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
    /**
     * 设置创建时间
     */
    public void setCreateTime(Date value) {
        this.createTime = value;
    }
    /**
     * 获取创建时间
     */
    public Date getCreateTime() {
        return this.createTime;
    }

    public String getStringClientIp() {
        return stringClientIp;
    }

    public void setStringClientIp(String stringClientIp) {
        this.stringClientIp = stringClientIp;
    }
}
