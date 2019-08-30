package com.mglj.shards.service.domain.request;


import com.mglj.totorial.framework.common.validation.AlertMessage;

import javax.validation.constraints.NotNull;

/**
 * Created by zsp on 2019/3/22.
 */
public class ResendSyncMessageRequest {

    @NotNull(message = "syncMessageIdRequired")
    @AlertMessage(code = "syncMessageIdRequired", msg = "ID不能为空")
    private Long syncMessageId;

    private String clientIp;

    private Integer clientPort;

    public Long getSyncMessageId() {
        return syncMessageId;
    }

    public void setSyncMessageId(Long syncMessageId) {
        this.syncMessageId = syncMessageId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }
}
