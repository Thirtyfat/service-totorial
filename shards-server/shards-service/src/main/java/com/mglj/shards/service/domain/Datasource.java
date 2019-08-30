package com.mglj.shards.service.domain;

import com.mglj.totorial.framework.common.enums.DataSourceRoleEnum;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源信息
 * 
 * @author zsp
 * @date 2019-3-8
 */
public class Datasource {
	
	/**
	 * 数据源信息ID
	 */
	private Long datasourceId;
	/**
	 * 类型 0是写，1是读
	 */
	private DataSourceRoleEnum type;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 数据源属性
	 */
	private Map<String, Object> properties;

	public void addProperty(String key, Object value) {
		if(key == null || value == null) {
			return;
		}
		if(properties == null) {
			properties = new HashMap<>();
		}
		properties.put(key, value);
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
	 * 设置类型 1是写，2是读
	 */
	public void setType(DataSourceRoleEnum value) {
		this.type = value;
	}
	/**
	 * 获取类型 1是写，2是读
	 */
	public DataSourceRoleEnum getType() {
		return this.type;
	}
	/**
	 * 设置名称
	 */
	public void setName(String value) {
		this.name = value;
	}
	/**
	 * 获取名称
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * 设置
	 */
	public void setUpdateTime(Date value) {
		this.updateTime = value;
	}
	/**
	 * 获取
	 */
	public Date getUpdateTime() {
		return this.updateTime;
	}
	/**
	 * 设置备注
	 */
	public void setRemark(String value) {
		this.remark = value;
	}
	/**
	 * 获取备注
	 */
	public String getRemark() {
		return this.remark;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}

