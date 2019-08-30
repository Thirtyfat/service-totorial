package com.mglj.shards.service.service.impl.builder;


import com.mglj.shards.service.api.dto.WmsShardChangeDTO;
import com.mglj.shards.service.domain.SyncMessage;

/**
 * Created by zsp on 2019/3/26.
 */
public interface WmsShardChangeDTOBuilder {

    WmsShardChangeDTO build(SyncMessage syncMessage);

}
