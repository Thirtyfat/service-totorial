package com.mglj.shards.service.domain;

/**
 * 同步消息项
 * 
 * @author zsp
 * @date 2019-3-19
 */
public class SyncMessageItem {

	public final static int BIZ_TYPE_UNDEFINE = 0;

	/**
	 * 数据Id
	 */
	private Long bizId;
	/**
	 * 数据类型
	 */
	private Integer bizType = BIZ_TYPE_UNDEFINE;

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

}

