package com.mglj.shards.service.domain.po;

/**
 * 数据源配置属性
 * 
 * @author zsp
 * @date 2019-3-8
 */
public class DatasourcePropertyPO {
	
	/**
	 * 
	 */
	private Long datasourceId;
	/**
	 * 键
	 */
	private String key;
	/**
	 * 值
	 */
	private String value;

	public DatasourcePropertyPO() {

	}

	public DatasourcePropertyPO(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * 设置
	 */
	public void setDatasourceId(Long value) {
		this.datasourceId = value;
	}
	/**
	 * 获取
	 */
	public Long getDatasourceId() {
		return this.datasourceId;
	}
	/**
	 * 设置键
	 */
	public void setKey(String value) {
		this.key = value;
	}
	/**
	 * 获取键
	 */
	public String getKey() {
		return this.key;
	}
	/**
	 * 设置值
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 获取值
	 */
	public String getValue() {
		return this.value;
	}

}

