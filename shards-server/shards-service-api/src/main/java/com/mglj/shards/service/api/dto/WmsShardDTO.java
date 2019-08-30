package com.mglj.shards.service.api.dto;

/**
 * Created by zsp on 2019/3/11.
 */
public class WmsShardDTO {

    private Long warehouseId;

    private Long datasourceGroupId;

    private DatasourceGroupDTO datasourceGroup;

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getDatasourceGroupId() {
        return datasourceGroupId;
    }

    public void setDatasourceGroupId(Long datasourceGroupId) {
        this.datasourceGroupId = datasourceGroupId;
    }

    public DatasourceGroupDTO getDatasourceGroup() {
        return datasourceGroup;
    }

    public void setDatasourceGroup(DatasourceGroupDTO datasourceGroup) {
        this.datasourceGroup = datasourceGroup;
    }

}
