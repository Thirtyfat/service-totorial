package com.mglj.shards.service.domain.po;

/**
 * Created by zsp on 2019/3/11.
 */
public class WmsShardPO {

    private Long warehouseId;
    private Long datasourceGroupId;

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
}
