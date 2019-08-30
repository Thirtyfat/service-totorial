package com.mglj.shards.service.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsp on 2019/3/21.
 */
public class WmsShardConstants {

    /**
     * 数据操作 - WMS分片添加
     */
    public final static int ACTION_WMS_SHARD_ADDITION = 30;
    /**
     * 数据操作 - WMS分片更新
     */
    public final static int ACTION_WMS_SHARD_MODIFICATION = 31;
    /**
     * 数据操作 - WMS分片删除
     */
    public final static int ACTION_WMS_SHARD_DELETION = 32;

    /**
     * 数据操作 - WMS分片追踪
     */
    public final static int ACTION_WMS_SHARD_TRACE = 39;

    private final static Map<Integer, String> actionRemarkMap = new HashMap<>();

    static {
        actionRemarkMap.put(ACTION_WMS_SHARD_ADDITION, "WMS分片添加");
        actionRemarkMap.put(ACTION_WMS_SHARD_MODIFICATION, "WMS分片更新");
        actionRemarkMap.put(ACTION_WMS_SHARD_DELETION, "WMS分片删除");
        actionRemarkMap.put(ACTION_WMS_SHARD_TRACE, "WMS分片追踪");
    }

    public static String getActionRemark(Integer action) {
        return actionRemarkMap.get(action);
    }

}
