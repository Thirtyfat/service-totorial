package com.mglj.shards.service.service.impl.builder;


import com.mglj.shards.service.api.dto.WmsShardChangeDTO;
import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.shards.service.domain.SyncMessage;
import com.mglj.shards.service.domain.converter.DatasourceGroupConverter;
import com.mglj.shards.service.manager.api.DatasourceManager;

/**
 * Created by zsp on 2019/3/26.
 */
public class DatasourceGroupWmsShardChangeDTOBuilder implements WmsShardChangeDTOBuilder {

    private DatasourceManager datasourceManager;

    public DatasourceGroupWmsShardChangeDTOBuilder(DatasourceManager datasourceManager) {
        this.datasourceManager = datasourceManager;
    }

    @Override
    public WmsShardChangeDTO build(SyncMessage syncMessage) {
        WmsShardChangeDTO wmsShardChangeDTO = new WmsShardChangeDTO();
        wmsShardChangeDTO.setAction(syncMessage.getAction());
        DatasourceGroup datasourceGroup = datasourceManager.getDatasourceGroup(
                syncMessage.getItemList().get(0).getBizId());
        if(datasourceGroup != null) {
            wmsShardChangeDTO.setDatasourceGroup(DatasourceGroupConverter.toDTO(datasourceGroup));
        }
        return wmsShardChangeDTO;
    }
}
