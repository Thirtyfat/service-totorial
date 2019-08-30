package com.mglj.shards.service.service.api;


import com.mglj.shards.service.domain.SyncMessage;
import com.mglj.shards.service.domain.SyncMessageAck;
import com.mglj.shards.service.domain.query.SyncMessageQuery;
import com.mglj.shards.service.domain.request.ResendSyncMessageRequest;
import com.mglj.shards.service.domain.request.TraceRequest;

import java.util.List;

/**
 * 同步逻辑
 *
 * Created by zsp on 2019/3/19.
 */
public interface SyncService {

    /**
     * 发布同步消息
     *
     * @param message           消息
     */
    void publishSyncMessage(SyncMessage message);

    /**
     * 在业务的事务提交后，发送消息
     */
    void sendSyncMessageAfterTransactionCommited();

    /**
     * 获取同步消息
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
     * 反馈确认消息
     *
     * @param syncMessageAck
     */
    void ackSyncMessage(SyncMessageAck syncMessageAck);

    /**
     * 重发消息
     *
     * @param request
     */
    void resendSyncMessage(ResendSyncMessageRequest request);

    /**
     * 追踪
     */
    void trace(TraceRequest request);

}
