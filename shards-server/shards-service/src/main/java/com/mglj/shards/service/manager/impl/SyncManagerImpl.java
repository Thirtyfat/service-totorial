package com.mglj.shards.service.manager.impl;

import com.mglj.shards.service.api.dto.SyncMessageIdDTO;
import com.mglj.shards.service.dao.api.SyncMessageAckDao;
import com.mglj.shards.service.dao.api.SyncMessageDao;
import com.mglj.shards.service.dao.api.SyncMessageItemDao;
import com.mglj.shards.service.domain.SyncMessage;
import com.mglj.shards.service.domain.SyncMessageAck;
import com.mglj.shards.service.domain.converter.SyncMessageAckConverter;
import com.mglj.shards.service.domain.converter.SyncMessageConverter;
import com.mglj.shards.service.domain.po.SyncMessageAckPO;
import com.mglj.shards.service.domain.po.SyncMessageItemPO;
import com.mglj.shards.service.domain.po.SyncMessagePO;
import com.mglj.shards.service.domain.query.SyncMessageQuery;
import com.mglj.shards.service.manager.api.SyncManager;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zsp on 2019/3/19.
 */
@Service
public class SyncManagerImpl implements SyncManager {

    @Autowired
    private SyncMessageDao syncMessageDao;
    @Autowired
    private SyncMessageItemDao syncMessageItemDao;
    @Autowired
    private SyncMessageAckDao syncMessageAckDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void saveSyncMessage(SyncMessage syncMessage) {
        SyncMessagePO syncMessagePO = SyncMessageConverter.toPO(syncMessage);
        syncMessageDao.saveSyncMessage(syncMessagePO);
        List<SyncMessageItemPO> syncMessageItemPOList = SyncMessageConverter.itemToPO(syncMessage.getItemList());
        if(!CollectionUtils.isEmpty(syncMessageItemPOList)) {
            for (SyncMessageItemPO itemPO : syncMessageItemPOList) {
                itemPO.setSyncMessageId(syncMessagePO.getSyncMessageId());
                itemPO.setCreateTime(syncMessagePO.getCreateTime());
            }
            syncMessageItemDao.saveAllSyncMessageItem(syncMessageItemPOList);
        }
    }

    @Override
    public SyncMessage getSyncMessage(Long syncMessageId) {
        SyncMessage syncMessage = SyncMessageConverter.fromPO(
                syncMessageDao.getSyncMessage(syncMessageId));
        if(syncMessage != null) {
            syncMessage.setItemList(SyncMessageConverter.itemFromPO(
                    syncMessageItemDao.listSyncMessageItemByIds(Arrays.asList(syncMessageId))));
        }
        return syncMessage;
    }

    @Override
    public List<SyncMessage> listSyncMessage(SyncMessageQuery query) {
        List<SyncMessage> syncMessageList = SyncMessageConverter.fromPO(
                syncMessageDao.listSyncMessage(query));
        if(!CollectionUtils.isEmpty(syncMessageList)) {
            Set<Long> syncMessageIdSet = syncMessageList.stream()
                    .map(SyncMessage::getSyncMessageId).collect(Collectors.toSet());
            List<SyncMessageItemPO> syncMessageItemPOList = syncMessageItemDao.listSyncMessageItemByIds(syncMessageIdSet);
            Map<Long, List<SyncMessageItemPO>> syncMessageItemPOMap = syncMessageItemPOList.stream()
                    .collect(Collectors.groupingBy(SyncMessageItemPO::getSyncMessageId));
            List<SyncMessageAckPO> syncMessageAckPOList = syncMessageAckDao.listSyncMessageAckByIds(syncMessageIdSet);
            Map<Long, List<SyncMessageAckPO>> syncMessageAckPOMap = syncMessageAckPOList.stream()
                    .collect(Collectors.groupingBy(SyncMessageAckPO::getSyncMessageId));
            for(SyncMessage syncMessage : syncMessageList) {
                syncMessage.setItemList(SyncMessageConverter.itemFromPO(
                        syncMessageItemPOMap.get(syncMessage.getSyncMessageId())));
                syncMessage.setAckList(SyncMessageAckConverter.fromPO(
                        syncMessageAckPOMap.get(syncMessage.getSyncMessageId())));
            }
        }
        return syncMessageList;
    }

    @Override
    public int countSyncMessage(SyncMessageQuery query) {
        return syncMessageDao.countSyncMessage(query);
    }

    @Override
    public void sendSyncMessage(SyncMessage syncMessage) {
        rabbitTemplate.convertAndSend(syncMessage.getExchangeName(),
                syncMessage.getRouteKey(),
                new SyncMessageIdDTO(syncMessage.getSyncMessageId()));
    }

    @Override
    public void ackSyncMessage(SyncMessageAck syncMessageAck) {
        syncMessageAckDao.saveSyncMessageAck(SyncMessageAckConverter.toPO(syncMessageAck));
    }

}
