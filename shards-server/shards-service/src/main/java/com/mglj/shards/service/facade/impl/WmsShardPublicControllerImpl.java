package com.mglj.shards.service.facade.impl;

import com.mglj.shards.service.api.dto.SyncMessageIdDTO;
import com.mglj.shards.service.api.dto.WmsShardChangeDTO;
import com.mglj.shards.service.api.dto.WmsShardMapDTO;
import com.mglj.shards.service.api.facade.api.WmsShardFacade;
import com.mglj.shards.service.domain.converter.WmsShardConverter;
import com.mglj.shards.service.service.api.WmsShardService;
import com.mglj.totorial.framework.common.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zsp on 2019/3/8.
 */
@RestController
public class WmsShardPublicControllerImpl implements WmsShardFacade {

    @Autowired
    private WmsShardService wmsShardService;

    @Override
    public Result<List<WmsShardMapDTO>> listWmsShard() {
        return Result.result(WmsShardConverter.toMapDTO(wmsShardService.listAllShard()));
    }

    @Override
    public Result<WmsShardChangeDTO> getLatestWmsShard(@RequestBody SyncMessageIdDTO syncMessageIdDTO) {
        return Result.result(wmsShardService.getLatestWmsShard(syncMessageIdDTO.getSyncMessageId()));
    }

}
