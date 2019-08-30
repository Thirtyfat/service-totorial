package com.mglj.shards.service.domain.po;

import java.util.Date;

/**
 * 同步消息
 * 
 * @author zsp
 * @date 2019-3-19
 */
public class SyncMessagePO {
	
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

}

