package com.mglj.shards.service.domain.request;


import com.mglj.totorial.framework.common.validation.AlertMessage;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by zsp on 2019/3/15.
 */
public class SaveWmsShardRequest {

    @NotEmpty(message = "warehouseIdRequired")
    @AlertMessage(code = "warehouseIdRequired", msg = "仓库ID不能为空====")
    private List<Long> warehouseIdList;

    private List<String> warehouseNameList;

    @NotNull(message = "datasourceGroupIdRequired")
    @AlertMessage(code = "datasourceGroupIdRequired", msg = "数据源组ID不能为空")
    private Long datasourceGroupId;

    public List<Long> getWarehouseIdList() {
        return warehouseIdList;
    }

    public void setWarehouseIdList(List<Long> warehouseIdList) {
        this.warehouseIdList = warehouseIdList;
    }

    public Long getDatasourceGroupId() {
        return datasourceGroupId;
    }

    public void setDatasourceGroupId(Long datasourceGroupId) {
        this.datasourceGroupId = datasourceGroupId;
    }

    public List<String> getWarehouseNameList() {
        return warehouseNameList;
    }

    public void setWarehouseNameList(List<String> warehouseNameList) {
        this.warehouseNameList = warehouseNameList;
    }
}
