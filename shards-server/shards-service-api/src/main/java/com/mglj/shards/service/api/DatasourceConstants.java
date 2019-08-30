package com.mglj.shards.service.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsp on 2019/3/21.
 */
public class DatasourceConstants {

    /**
     * 数据操作 - 数据源追踪
     */
    public final static int ACTION_DATASOURCE_TRACE = 0;

    /**
     * 数据操作 - 数据源连接池属性更新
     */
    public final static int ACTION_DATASOURCE_PROPERTY_MODIFICATION = 11;

    /**
     * 数据操作 - 数据源组数据源集合更新（添加、删除）
     */
    public final static int ACTION_DATASOURCE_GROUP_MEMBER_MODIFICATION = 21;

    private final static Map<Integer, String> actionRemarkMap = new HashMap<>();

    static {
        actionRemarkMap.put(ACTION_DATASOURCE_TRACE, "数据源追踪");
        actionRemarkMap.put(ACTION_DATASOURCE_PROPERTY_MODIFICATION, "数据源连接池属性更新");
        actionRemarkMap.put(ACTION_DATASOURCE_GROUP_MEMBER_MODIFICATION, "数据源组数据源集合更新");
    }

    public static String getActionRemark(Integer action) {
        return actionRemarkMap.get(action);
    }

}
