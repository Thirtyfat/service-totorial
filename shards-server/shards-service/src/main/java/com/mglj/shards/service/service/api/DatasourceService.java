package com.mglj.shards.service.service.api;


import com.mglj.shards.service.api.dto.DatasourceGroupHeadDTO;
import com.mglj.shards.service.api.dto.DatasourceHeadDTO;
import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;
import com.mglj.shards.service.domain.CriteriaConfig;
import com.mglj.shards.service.domain.Datasource;
import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.shards.service.domain.query.DatasourceGroupQuery;
import com.mglj.shards.service.domain.query.DatasourceQuery;
import com.mglj.totorial.framework.common.lang.Result;

import java.util.List;
import java.util.Map;

/**
 * Created by zsp on 2019/3/8.
 */
public interface DatasourceService {

    /**
     * 查询标准配置
     *
     * @return
     */
    List<CriteriaConfig> listCriteriaConfig();

    /**
     * 保存数据源
     * @param datasource
     * @return
     */
    Result<?> saveDatasource(Datasource datasource);

    /**
     * 更新数据源
     *
     * @param datasource
     * @return
     */
    Result<?> updateDatasource(Datasource datasource);

    /**
     * 删除数据源
     *
     * @param datasourceId
     * @return
     */
    Result<?> deleteDatasource(Long datasourceId);

    /**
     * 获取数据源属性
     *
     * @param datasourceId
     * @return
     */
    Map<String, Object> getDatasourceProperties(Long datasourceId);

    /**
     * 查询数据源
     *
     * @param query
     * @return
     */
    List<Datasource> listDatasource(DatasourceQuery query);

    /**
     * 统计数据源
     *
     * @param query
     * @return
     */
    int countDatasource(DatasourceQuery query);

    /**
     * 查询数据源标题
     *
     * @param query
     * @return
     */
    List<DatasourceHeadDTO> headDatasource(DatasourceQuery query);

    /**
     * 测试数据源
     *
     * @param datasourceId
     * @return
     */
    Result<?> testDatasource(Long datasourceId);

    /**
     * 保存组
     *
     * @param datasourceGroup
     * @return
     */
    Result<?> saveDatasourceGroup(DatasourceGroup datasourceGroup);

    /**
     * 更新组
     *
     * @param datasourceGroup
     * @return
     */
    Result<?> updateDatasourceGroup(DatasourceGroup datasourceGroup);

    /**
     * 更新组状态
     *
     * @param datasourceGroupId
     * @param status
     * @return
     */
    Result<?> updateDatasourceGroupStatus(Long datasourceGroupId, DatasourceGroupStatusEnum status);

    /**
     * 删除组
     *
     * @param datasourceGroupId
     * @return
     */
    Result<?> deleteDatasourceGroup(Long datasourceGroupId);

    /**
     * 查询组
     *
     * @param query
     * @return
     */
    List<DatasourceGroup> listDatasourceGroup(DatasourceGroupQuery query);

    /**
     * 统计组
     *
     * @param query
     * @return
     */
    int countDatasourceGroup(DatasourceGroupQuery query);

    /**
     * 查询组标题
     *
     * @return
     */
    List<DatasourceGroupHeadDTO> headEnabledDatasourceGroup();

}
