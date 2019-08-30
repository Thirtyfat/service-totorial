package com.mglj.shards.service.service.impl.builder;


import com.mglj.shards.service.api.dto.WmsShardChangeDTO;
import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.shards.service.domain.SyncMessage;
import com.mglj.shards.service.domain.converter.DatasourceGroupConverter;
import com.mglj.shards.service.manager.api.WmsShardManager;

/**
 * Created by zsp on 2019/3/26.
 */
public class GenericWmsShardChangeDTOBuilder implements WmsShardChangeDTOBuilder {

    private WmsShardManager wmsShardManager;

    public GenericWmsShardChangeDTOBuilder(WmsShardManager wmsShardManager) {
        this.wmsShardManager = wmsShardManager;
    }

    @Override
    public WmsShardChangeDTO build(SyncMessage syncMessage) {
        WmsShardChangeDTO wmsShardChangeDTO = new WmsShardChangeDTO();
        wmsShardChangeDTO.setAction(syncMessage.getAction());
        Long warehouseId = syncMessage.getItemList().get(0).getBizId();
        wmsShardChangeDTO.setWarehouseId(warehouseId);
        DatasourceGroup datasourceGroup = wmsShardManager.getShard(warehouseId);
        if(datasourceGroup != null) {
            wmsShardChangeDTO.setDatasourceGroup(DatasourceGroupConverter.toDTO(datasourceGroup));
        }
        return wmsShardChangeDTO;
    }

}
