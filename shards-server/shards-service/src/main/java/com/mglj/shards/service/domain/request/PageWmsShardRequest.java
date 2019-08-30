package com.mglj.shards.service.domain.request;


import com.mglj.shards.service.domain.query.WmsShardQuery;

/**
 * Created by zsp on 2019/3/15.
 */
public class PageWmsShardRequest extends WmsShardQuery {

    private Long warehouseId;

    @Override
    public Long getWarehouseId() {
        return warehouseId;
    }

    @Override
    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

}
