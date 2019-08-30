package com.mglj.shards.service.domain;

import java.util.Date;
import java.util.List;

/**
 * 同步消息
 * 
 * @author zsp
 * @date 2019-3-19
 */
public class SyncMessage {
	
	/**
	 * 消息Id
	 */
	private Long syncMessageId;
	/**
	 * 数据操作
	 */
	private Integer action;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 消息项
	 */
	private List<SyncMessageItem> itemList;
	/**
	 * 消息确认
	 */
	private List<SyncMessageAck> ackList;

	/**
	 *
	 */
	private String exchangeName;
	/**
	 *
	 */
	private String routeKey;

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
	 * 设置数据操作
	 */
	public void setAction(Integer value) {
		this.action = value;
	}
	/**
	 * 获取数据操作
	 */
	public Integer getAction() {
		return this.action;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<SyncMessageItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SyncMessageItem> itemList) {
		this.itemList = itemList;
	}

	public List<SyncMessageAck> getAckList() {
		return ackList;
	}

	public void setAckList(List<SyncMessageAck> ackList) {
		this.ackList = ackList;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getRouteKey() {
		return routeKey;
	}

	public void setRouteKey(String routeKey) {
		this.routeKey = routeKey;
	}
}

