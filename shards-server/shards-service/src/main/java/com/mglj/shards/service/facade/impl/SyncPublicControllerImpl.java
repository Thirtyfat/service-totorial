package com.mglj.shards.service.facade.impl;

import com.mglj.shards.service.api.facade.api.SyncFacade;
import com.mglj.shards.service.api.request.SaveSyncMessageAckRequest;
import com.mglj.shards.service.domain.SyncMessageAck;
import com.mglj.shards.service.service.api.SyncService;
import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.tool.beans.BeanUtilsEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zsp on 2019/3/21.
 */
@RestController
public class SyncPublicControllerImpl implements SyncFacade {

    @Autowired
    private SyncService syncService;

    @Override
    public Result<?> ackSyncMessage(@RequestBody SaveSyncMessageAckRequest request) {
        syncService.ackSyncMessage(BeanUtilsEx.copyProperties(request,
                () -> new SyncMessageAck()));
        return Result.result();
    }

}
