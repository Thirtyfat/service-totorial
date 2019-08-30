package com.mglj.shards.service.facade.impl;

import com.mglj.shards.service.api.dto.SyncMessageDTO;
import com.mglj.shards.service.domain.converter.SyncMessageConverter;
import com.mglj.shards.service.domain.query.SyncMessageQuery;
import com.mglj.shards.service.domain.request.PageSyncMessageRequest;
import com.mglj.shards.service.domain.request.ResendSyncMessageRequest;
import com.mglj.shards.service.domain.request.TraceRequest;
import com.mglj.shards.service.service.api.SyncService;
import com.mglj.totorial.framework.common.lang.Page;
import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.common.validation.Validatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zsp on 2019/3/21.
 */
@RestController
@RequestMapping("/shard/sync/manage")
public class SyncManageControllerImpl {

    @Autowired
    private SyncService syncService;

    @Validatable
    @PostMapping("resend")
    public Result<?> resendSyncMessage(@RequestBody ResendSyncMessageRequest request){
        syncService.resendSyncMessage(request);
        return Result.result();
    }

    @Validatable
    @PostMapping("trace")
    public Result<?> trace(@RequestBody TraceRequest request){
        syncService.trace(request);
        return Result.result();
    }

    @PostMapping("page")
    public Result<Page<SyncMessageDTO>> pageSyncMessage(@RequestBody PageSyncMessageRequest request){
        SyncMessageQuery syncMessageQuery = SyncMessageConverter.requestToQuery(request);
        List<SyncMessageDTO> syncMessageDTOList = SyncMessageConverter.toDTO(
                syncService.listSyncMessage(syncMessageQuery));
        int count = syncService.countSyncMessage(syncMessageQuery);
        return Result.result(new Page(syncMessageDTOList, count));
    }

}
