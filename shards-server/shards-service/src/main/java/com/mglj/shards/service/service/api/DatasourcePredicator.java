package com.mglj.shards.service.service.api;


import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;
import com.mglj.shards.service.domain.Datasource;
import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.totorial.framework.common.lang.Result;

/**
 * 数据源及组相关操作是否允许执行的判断逻辑
 *
 * Created by zsp on 2019/3/19.
 */
public interface DatasourcePredicator {

    /**
     * 数据源的删除操作是否允许执行
     *
     * @param datasource
     * @return
     */
    Result<?> isDatasourceDeletionAllowed(Datasource datasource);

    /**
     * 数据源组的状态改变操作是否允许执行
     *
     * @param datasourceGroup
     * @param status
     * @return
     */
    Result<?> isDatasourceGroupStatusChangedAllowed(DatasourceGroup datasourceGroup,
                                                    DatasourceGroupStatusEnum status);

    /**
     * 数据源组的删除操作是否允许执行
     *
     * @param datasourceGroup
     * @return
     */
    Result<?> isDatasourceGroupDeletionAllowed(DatasourceGroup datasourceGroup);

}
