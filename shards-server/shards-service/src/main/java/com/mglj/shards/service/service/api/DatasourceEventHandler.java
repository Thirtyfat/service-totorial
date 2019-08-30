package com.mglj.shards.service.service.api;


import com.mglj.shards.service.domain.Datasource;
import com.mglj.shards.service.domain.DatasourceGroup;

import java.util.List;
import java.util.Map;

/**
 * 数据源及组相关操作引发的事件处理逻辑
 *
 * Created by zsp on 2019/3/15.
 */
public interface DatasourceEventHandler {

    /**
     * 处理数据源属性更新事件引发的逻辑
     *
     * @param datasource
     */
    void handleDatasourcePropertyModification(Datasource datasource,
                                              Map<String, Object> addOrModifyProperties,
                                              Map<String, Object> removeProperties);

    /**
     * 处理数据源组的数据源集合增、删更新事件引发的逻辑
     *
     * @param datasourceGroup
     * @param addDatasourceList
     * @param removeDatasourceList
     */
    void handleDatasourceGroupDatasourceModification(DatasourceGroup datasourceGroup,
                                                     List<Datasource> addDatasourceList,
                                                     List<Datasource> removeDatasourceList);

}
