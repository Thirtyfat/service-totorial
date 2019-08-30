package com.mglj.shards.service.domain;

/**
 * Created by zsp on 2019/3/11.
 */
public class WmsShard {

    private Long warehouseId;
    private Long datasourceGroupId;

    private DatasourceGroup datasourceGroup;

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

    public DatasourceGroup getDatasourceGroup() {
        return datasourceGroup;
    }

    public void setDatasourceGroup(DatasourceGroup datasourceGroup) {
        this.datasourceGroup = datasourceGroup;
    }
}
