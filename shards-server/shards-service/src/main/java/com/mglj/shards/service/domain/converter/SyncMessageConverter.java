package com.mglj.shards.service.domain.converter;

import com.mglj.shards.service.api.DatasourceConstants;
import com.mglj.shards.service.api.WmsShardConstants;
import com.mglj.shards.service.api.dto.SyncMessageAckDTO;
import com.mglj.shards.service.api.dto.SyncMessageDTO;
import com.mglj.shards.service.api.dto.SyncMessageItemDTO;
import com.mglj.shards.service.domain.SyncMessage;
import com.mglj.shards.service.domain.SyncMessageItem;
import com.mglj.shards.service.domain.po.SyncMessageItemPO;
import com.mglj.shards.service.domain.po.SyncMessagePO;
import com.mglj.shards.service.domain.query.SyncMessageQuery;
import com.mglj.shards.service.domain.request.PageSyncMessageRequest;
import com.mglj.totorial.framework.common.lang.Ipv4;
import com.mglj.totorial.framework.tool.beans.BeanUtilsEx;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zsp on 2019/3/19.
 */
public class SyncMessageConverter {

    public static SyncMessagePO toPO(SyncMessage syncMessage) {
        return BeanUtilsEx.copyProperties(syncMessage, () -> new SyncMessagePO());
    }

    public static List<SyncMessageItemPO> itemToPO(Collection<SyncMessageItem> collection) {
        return BeanUtilsEx.copyPropertiesForNewList(collection, () -> new SyncMessageItemPO());
    }

    public static SyncMessage fromPO(SyncMessagePO syncMessage) {
        return BeanUtilsEx.copyProperties(syncMessage, () -> new SyncMessage());
    }

    public static List<SyncMessage> fromPO(Collection<SyncMessagePO> collection) {
        return BeanUtilsEx.copyPropertiesForNewList(collection, () -> new SyncMessage());
    }

    public static List<SyncMessageItem> itemFromPO(Collection<SyncMessageItemPO> collection) {
        return BeanUtilsEx.copyPropertiesForNewList(collection, () -> new SyncMessageItem());
    }

    public static SyncMessageDTO toDTO(SyncMessage syncMessage) {
        SyncMessageDTO syncMessageDTO = BeanUtilsEx.copyProperties(syncMessage, () -> new SyncMessageDTO());
        syncMessageDTO.setItemList(BeanUtilsEx.copyPropertiesForNewList(syncMessage.getItemList(),
                () -> new SyncMessageItemDTO()));
        syncMessageDTO.setAckList(BeanUtilsEx.copyPropertiesForNewList(syncMessage.getAckList(),
                () -> new SyncMessageAckDTO()));
        return syncMessageDTO;
    }

    public static List<SyncMessageDTO> toDTO(Collection<SyncMessage> collection) {
        List<SyncMessageDTO> syncMessageDTOList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(collection)) {
            SyncMessageDTO syncMessageDTO;
            Integer action;
            String actionRemark;
            List<SyncMessageAckDTO> ackList;
            for(SyncMessage syncMessage : collection) {
                syncMessageDTO = toDTO(syncMessage);
                syncMessageDTOList.add(syncMessageDTO);
                actionRemark = DatasourceConstants.getActionRemark(action = syncMessageDTO.getAction());
                if(actionRemark == null) {
                    actionRemark = WmsShardConstants.getActionRemark(action);
                }
                syncMessageDTO.setActionRemark(actionRemark);
                ackList = syncMessageDTO.getAckList();
                if(!CollectionUtils.isEmpty(ackList)) {
                    for(SyncMessageAckDTO ack : ackList) {
                        ack.setStringClientIp(Ipv4.parse(ack.getClientIp()).toString());
                    }
                }
            }
        }
        return syncMessageDTOList;
    }

    public static SyncMessageQuery requestToQuery(PageSyncMessageRequest request) {
        return request == null ? null : BeanUtilsEx.copyProperties(request, () -> new SyncMessageQuery());
    }

}
