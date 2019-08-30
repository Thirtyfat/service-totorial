package com.mglj.shards.service.api.dto;

/**
 * Created by zsp on 2019/3/21.
 */
public class WmsShardChangeDTO {

    private Integer action;

    private Long warehouseId;

    private DatasourceGroupDTO datasourceGroup;

    private DatasourceDTO datasource;

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public DatasourceGroupDTO getDatasourceGroup() {
        return datasourceGroup;
    }

    public void setDatasourceGroup(DatasourceGroupDTO datasourceGroup) {
        this.datasourceGroup = datasourceGroup;
    }

    public DatasourceDTO getDatasource() {
        return datasource;
    }

    public void setDatasource(DatasourceDTO datasource) {
        this.datasource = datasource;
    }
}
