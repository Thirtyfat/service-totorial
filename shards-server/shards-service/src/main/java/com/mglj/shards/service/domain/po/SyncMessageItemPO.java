package com.mglj.shards.service.domain.po;

import java.util.Date;

/**
 * 同步消息项
 * 
 * @author zsp
 * @date 2019-3-19
 */
public class SyncMessageItemPO {
	
	/**
	 * 消息Id
	 */
	private Long syncMessageId;
	/**
	 * 数据Id
	 */
	private Long bizId;
	/**
	 * 数据类型
	 */
	private Integer bizType;
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
	 * 设置数据Id
	 */
	public void setBizId(Long value) {
		this.bizId = value;
	}
	/**
	 * 获取数据Id
	 */
	public Long getBizId() {
		return this.bizId;
	}
	/**
	 * 设置数据类型
	 */
	public void setBizType(Integer value) {
		this.bizType = value;
	}
	/**
	 * 获取数据类型
	 */
	public Integer getBizType() {
		return this.bizType;
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

