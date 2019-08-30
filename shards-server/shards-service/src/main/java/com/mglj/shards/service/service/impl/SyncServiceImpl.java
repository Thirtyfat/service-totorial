package com.mglj.shards.service.service.impl;

import com.mglj.shards.service.aop.Sync;
import com.mglj.shards.service.aop.SyncMessageHolder;
import com.mglj.shards.service.domain.SyncMessage;
import com.mglj.shards.service.domain.SyncMessageAck;
import com.mglj.shards.service.domain.builder.SyncMessageBuilder;
import com.mglj.shards.service.domain.query.SyncMessageQuery;
import com.mglj.shards.service.domain.request.ResendSyncMessageRequest;
import com.mglj.shards.service.domain.request.TraceRequest;
import com.mglj.shards.service.manager.api.SyncManager;
import com.mglj.shards.service.service.api.SyncService;
import com.mglj.totorial.framework.tool.gid.GidGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by zsp on 2019/3/20.
 */
@Service
public class SyncServiceImpl implements SyncService, InitializingBean {

    @Autowired
    private SyncManager syncManager;
    @Autowired
    private GidGenerator gidGenerator;

    @Value("${myconf.sharding.sync.exchangeName}")
    private String shardingSyncExchangeName;

    @Value("${myconf.sharding.sync.queueName}")
    private String shardingSyncQueueName;

    private String faoutExchangeName;
    private String directExchangeName;

    @Override
    public void afterPropertiesSet() throws Exception {
        Objects.requireNonNull(shardingSyncExchangeName, "The shardingSyncExchangeName is not set.");
        Objects.requireNonNull(shardingSyncQueueName, "The shardingSyncQueueName is not set.");
        faoutExchangeName = shardingSyncExchangeName + ".fanout";
        directExchangeName = shardingSyncExchangeName + ".direct";
    }

    @Override
    public void publishSyncMessage(SyncMessage message) {
        message.setSyncMessageId(gidGenerator.generate());
        message.setExchangeName(faoutExchangeName);
        message.setCreateTime(new Date());

        syncManager.saveSyncMessage(message);

        SyncMessageHolder.set(message);
    }

    @Override
    public void sendSyncMessageAfterTransactionCommited() {
        SyncMessage message = SyncMessageHolder.get();
        if(message == null) {
            return;
        }
        syncManager.sendSyncMessage(message);
    }

    @Override
    public SyncMessage getSyncMessage(Long syncMessageId) {
        return syncManager.getSyncMessage(syncMessageId);
    }

    @Override
    public List<SyncMessage> listSyncMessage(SyncMessageQuery query) {
        return syncManager.listSyncMessage(query);
    }

    @Override
    public int countSyncMessage(SyncMessageQuery query) {
        return syncManager.countSyncMessage(query);
    }

    @Override
    public void ackSyncMessage(SyncMessageAck syncMessageAck) {
        syncMessageAck.setSyncMessageAckId(gidGenerator.generate());
        syncMessageAck.setCreateTime(new Date());
        syncManager.ackSyncMessage(syncMessageAck);
    }

    @Sync
    @Override
    public void resendSyncMessage(ResendSyncMessageRequest request) {
        SyncMessage syncMessage = syncManager.getSyncMessage(request.getSyncMessageId());
        if(syncMessage == null) {
            return;
        }
        String clientIp;
        if(StringUtils.hasText(clientIp = request.getClientIp())) {
            syncMessage.setExchangeName(directExchangeName);
            syncMessage.setRouteKey(shardingSyncQueueName
                    + "." + clientIp + "." + request.getClientPort());
        } else {
            syncMessage.setExchangeName(faoutExchangeName);
        }
        SyncMessageHolder.set(syncMessage);
    }

    @Sync
    @Override
    public void trace(TraceRequest request) {
        SyncMessage message = new SyncMessageBuilder()
                .setAction(request.getAction()).get();
        publishSyncMessage(message);
    }

}
