package com.mglj.shards.service.api.dto;


import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;

/**
 * Created by zsp on 2019/3/15.
 */
public class DatasourceGroupHeadDTO {

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
     * 备注
     */
    private String remark;

    public Long getDatasourceGroupId() {
        return datasourceGroupId;
    }

    public void setDatasourceGroupId(Long datasourceGroupId) {
        this.datasourceGroupId = datasourceGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DatasourceGroupStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DatasourceGroupStatusEnum status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
