package com.mglj.shards.service.domain;


import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据源组
 * 
 * @author zsp
 * @date 2019-3-8
 */
public class DatasourceGroup {
	
	/**
	 * 数据源groupid
	 */
	private Long datasourceGroupId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 0是关闭，1是开启
	 */
	private DatasourceGroupStatusEnum status;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 数据源ID集合
	 */
	private List<Long> datasourceIdList;
	/**
	 * 数据源集合
	 */
	private List<Datasource> datasourceList;

	public void addDatasource(Datasource datasource) {
		if(datasource == null) {
			return;
		}
		if(datasourceList == null) {
			datasourceList = new ArrayList<>();
		}
		datasourceList.add(datasource);
	}

	/**
	 * 设置数据源groupid
	 */
	public void setDatasourceGroupId(Long value) {
		this.datasourceGroupId = value;
	}
	/**
	 * 获取数据源groupid
	 */
	public Long getDatasourceGroupId() {
		return this.datasourceGroupId;
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
	 * 设置0是关闭，1是开启
	 */
	public void setStatus(DatasourceGroupStatusEnum value) {
		this.status = value;
	}
	/**
	 * 获取0是关闭，1是开启
	 */
	public DatasourceGroupStatusEnum getStatus() {
		return this.status;
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

	public List<Long> getDatasourceIdList() {
		return datasourceIdList;
	}

	public void setDatasourceIdList(List<Long> datasourceIdList) {
		this.datasourceIdList = datasourceIdList;
	}

	public List<Datasource> getDatasourceList() {
		return datasourceList;
	}

	public void setDatasourceList(List<Datasource> datasourceList) {
		this.datasourceList = datasourceList;
	}
}

