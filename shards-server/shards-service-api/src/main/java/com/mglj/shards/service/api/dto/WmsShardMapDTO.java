package com.mglj.shards.service.api.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsp on 2019/3/13.
 */
public class WmsShardMapDTO {

    private List<Long> warehouseIdList;
    private DatasourceGroupDTO datasourceGroup;

    public void addWarehouseId(Long warehouseId) {
        if(warehouseId == null) {
            return;
        }
        if(warehouseIdList == null) {
            warehouseIdList = new ArrayList<>();
        }
        warehouseIdList.add(warehouseId);
    }

    public List<Long> getWarehouseIdList() {
        return warehouseIdList;
    }

    public void setWarehouseIdList(List<Long> warehouseIdList) {
        this.warehouseIdList = warehouseIdList;
    }

    public DatasourceGroupDTO getDatasourceGroup() {
        return datasourceGroup;
    }

    public void setDatasourceGroup(DatasourceGroupDTO datasourceGroup) {
        this.datasourceGroup = datasourceGroup;
    }
}
