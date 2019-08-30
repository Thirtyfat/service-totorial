package com.mglj.shards.service.service.impl.builder;


import com.mglj.shards.service.api.dto.WmsShardChangeDTO;
import com.mglj.shards.service.domain.SyncMessage;

/**
 * Created by zsp on 2019/3/29.
 */
public class TraceWmsShardChangeDTOBuilder implements WmsShardChangeDTOBuilder {

    @Override
    public WmsShardChangeDTO build(SyncMessage syncMessage) {
        WmsShardChangeDTO wmsShardChangeDTO = new WmsShardChangeDTO();
        wmsShardChangeDTO.setAction(syncMessage.getAction());
        return wmsShardChangeDTO;
    }

}
