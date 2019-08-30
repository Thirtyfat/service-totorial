package com.mglj.shards.service.api.dto;


import com.mglj.totorial.framework.common.enums.DataSourceRoleEnum;

/**
 * Created by zsp on 2019/3/15.
 */
public class DatasourceHeadDTO {

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
     * 备注
     */
    private String remark;

    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

    public DataSourceRoleEnum getType() {
        return type;
    }

    public void setType(DataSourceRoleEnum type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
