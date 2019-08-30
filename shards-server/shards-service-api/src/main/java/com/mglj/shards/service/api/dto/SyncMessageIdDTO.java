package com.mglj.shards.service.api.dto;

/**
 * Created by zsp on 2019/3/20.
 */
public class SyncMessageIdDTO {

    /**
     * 消息Id
     */
    private Long syncMessageId;

    public SyncMessageIdDTO() {

    }

    public SyncMessageIdDTO(Long syncMessageId) {
        this.syncMessageId = syncMessageId;
    }

    @Override
    public String toString() {
        return "SyncMessageIdDTO{" +
                "syncMessageId=" + syncMessageId +
                '}';
    }

    public Long getSyncMessageId() {
        return syncMessageId;
    }

    public void setSyncMessageId(Long syncMessageId) {
        this.syncMessageId = syncMessageId;
    }
}
