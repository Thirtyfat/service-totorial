package com.mglj.shards.service.domain.builder;

import com.mglj.shards.service.domain.SyncMessage;
import com.mglj.shards.service.domain.SyncMessageItem;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.mglj.shards.service.domain.SyncMessageItem.BIZ_TYPE_UNDEFINE;


/**
 * Created by zsp on 2019/3/20.
 */
public class SyncMessageBuilder {

    private SyncMessage syncMessage;

    public SyncMessageBuilder() {
        syncMessage = new SyncMessage();
    }

    public SyncMessageBuilder setAction(Integer action) {
        syncMessage.setAction(action);
        return this;
    }

    public SyncMessageBuilder setRemark(String remark) {
        syncMessage.setRemark(remark);
        return this;
    }

    public SyncMessageBuilder addItem(Long bizId) {
        return addItem(bizId, BIZ_TYPE_UNDEFINE);
    }

    public SyncMessageBuilder addItem(Long bizId, Integer bizType) {
        List<SyncMessageItem> itemList = syncMessage.getItemList();
        if(CollectionUtils.isEmpty(itemList)) {
            itemList = new ArrayList<>();
            syncMessage.setItemList(itemList);
        }
        SyncMessageItem item = new SyncMessageItem();
        item.setBizId(bizId);
        item.setBizType(bizType);
        itemList.add(item);
        return this;
    }

    public SyncMessage get() {
        return syncMessage;
    }

}
