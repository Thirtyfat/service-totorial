package com.mglj.shards.service.domain.converter;


import com.mglj.shards.service.domain.SyncMessageAck;
import com.mglj.shards.service.domain.po.SyncMessageAckPO;
import com.mglj.totorial.framework.tool.beans.BeanUtilsEx;

import java.util.Collection;
import java.util.List;

/**
 * Created by zsp on 2019/3/21.
 */
public class SyncMessageAckConverter {

    public static SyncMessageAckPO toPO(SyncMessageAck syncMessageAck) {
        return BeanUtilsEx.copyProperties(syncMessageAck, () -> new SyncMessageAckPO());
    }

    public static List<SyncMessageAck> fromPO(Collection<SyncMessageAckPO> collection) {
        return BeanUtilsEx.copyPropertiesForNewList(collection, () -> new SyncMessageAck());
    }

}
