package com.mglj.shards.service.api.facade.api;

import com.mglj.shards.service.api.request.SaveSyncMessageAckRequest;
import com.mglj.totorial.framework.common.lang.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by zsp on 2019/3/21.
 */
@FeignClient("${feign.scm-shards-service.name}")
public interface SyncFacade {

    /**
     * 反馈确认同步消息
     *
     * @param request
     * @return
     */
    @PostMapping("/shard/sync/ack")
    Result<?> ackSyncMessage(SaveSyncMessageAckRequest request);

}
