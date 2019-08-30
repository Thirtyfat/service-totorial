package com.mglj.shards.service.manager.api;


import com.mglj.shards.service.domain.SyncMessage;
import com.mglj.shards.service.domain.SyncMessageAck;
import com.mglj.shards.service.domain.query.SyncMessageQuery;

import java.util.List;

/**
 * Created by zsp on 2019/3/19.
 */
public interface SyncManager {

    /**
     *
     * @param syncMessage
     */
    void saveSyncMessage(SyncMessage syncMessage);

    /**
     *
     * @param syncMessageId
     * @return
     */
    SyncMessage getSyncMessage(Long syncMessageId);

    /**
     * 查找多个
     *
     * @param query 查找条件
     * @return
     */
    List<SyncMessage> listSyncMessage(SyncMessageQuery query);

    /**
     * 统计个数
     *
     * @param query 查找条件
     * @return
     */
    int countSyncMessage(SyncMessageQuery query);

    /**
     *
     * @param syncMessage
     */
    void sendSyncMessage(SyncMessage syncMessage);

    /**
     *
     * @param syncMessageAck
     */
    void ackSyncMessage(SyncMessageAck syncMessageAck);

}
