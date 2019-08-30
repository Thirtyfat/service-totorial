package com.mglj.shards.service.domain.po;

import java.util.Date;

/**
 * 同步确认
 * 
 * @author zsp
 * @date 2019-3-19
 */
public class SyncMessageAckPO {
	
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

}

