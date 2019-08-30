package com.mglj.shards.service.domain.query;


import com.mglj.totorial.framework.common.lang.PageRequest;

/**
 * Created by zsp on 2019/3/15.
 */
public class WmsShardQuery extends PageRequest {

    private Long warehouseId;

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
}
