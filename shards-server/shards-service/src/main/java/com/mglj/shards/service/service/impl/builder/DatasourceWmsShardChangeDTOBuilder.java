package com.mglj.shards.service.service.impl.builder;


import com.mglj.shards.service.api.dto.WmsShardChangeDTO;
import com.mglj.shards.service.domain.Datasource;
import com.mglj.shards.service.domain.SyncMessage;
import com.mglj.shards.service.domain.converter.DatasourceConverter;
import com.mglj.shards.service.manager.api.DatasourceManager;

/**
 * Created by zsp on 2019/3/29.
 */
public class DatasourceWmsShardChangeDTOBuilder implements WmsShardChangeDTOBuilder {

    private DatasourceManager datasourceManager;

    public DatasourceWmsShardChangeDTOBuilder(DatasourceManager datasourceManager) {
        this.datasourceManager = datasourceManager;
    }

    @Override
    public WmsShardChangeDTO build(SyncMessage syncMessage) {
        WmsShardChangeDTO wmsShardChangeDTO = new WmsShardChangeDTO();
        wmsShardChangeDTO.setAction(syncMessage.getAction());
        Datasource datasource = datasourceManager.getDatasource(
                syncMessage.getItemList().get(0).getBizId());
        if(datasource != null) {
            wmsShardChangeDTO.setDatasource(DatasourceConverter.toDTO(datasource));
        }
        return wmsShardChangeDTO;
    }

}
