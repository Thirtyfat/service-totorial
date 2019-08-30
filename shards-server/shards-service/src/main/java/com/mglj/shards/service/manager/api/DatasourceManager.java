package com.mglj.shards.service.manager.api;

import com.mglj.shards.service.api.dto.DatasourceGroupHeadDTO;
import com.mglj.shards.service.api.dto.DatasourceHeadDTO;
import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;
import com.mglj.shards.service.domain.CriteriaConfig;
import com.mglj.shards.service.domain.Datasource;
import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.shards.service.domain.query.DatasourceGroupQuery;
import com.mglj.shards.service.domain.query.DatasourceQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by zsp on 2019/3/8.
 */
public interface DatasourceManager {

    /**
     *
     * @return
     */
    Map<String, Object> listGlobalProperty();

    /**
     *
     * @return
     */
    List<CriteriaConfig> listCriteriaConfig();

    /**
     * 保存
     *
     * @param datasource
     */
    int saveDatasource(Datasource datasource);

    /**
     * 更新
     *
     * @param datasource
     */
    int updateDatasource(Datasource datasource);

    /**
     * 删除
     *
     * @param datasourceId
     */
    int deleteDatasource(Long datasourceId);

    /**
     *
     * @param name
     * @param datasourceId
     * @return
     */
    boolean isDatasourceNameExisted(String name, Long datasourceId);

    /**
     *
     * @param datasourceIdCollection
     * @return
     */
    boolean areAllDatasourceExist(Collection<Long> datasourceIdCollection);

    /**
     * 查找一个
     *
     * @param datasourceId
     * @return
     */
    Datasource getDatasource(Long datasourceId);

    /**
     *
     * @param datasourceId
     * @return
     */
    Datasource getDatasourceHead(Long datasourceId);

    /**
     *
     * @param datasourceId
     * @return
     */
    Map<String, Object> getDatasourceProperties(Long datasourceId);

    /**
     * 查找多个
     *
     * @param datasourceIdCollection 标识集合
     * @return
     */
    List<Datasource> listDatasourceByIds(Collection<Long> datasourceIdCollection);

    /**
     * 查找多个
     *
     * @param query 查找条件
     * @return
     */
    List<Datasource> listDatasource(DatasourceQuery query);

    /**
     * 统计个数
     *
     * @param query 查找条件
     * @return
     */
    int countDatasource(DatasourceQuery query);

    /**
     *
     * @param query
     * @return
     */
    List<DatasourceHeadDTO> headDatasource(DatasourceQuery query);

    /**
     *
     * @param datasourceId
     * @return
     */
    boolean isDatasourceRelation(Long datasourceId);

    /**
     *
     * @param datasourceId
     * @return
     */
    List<Long> listDatasourceGroupId(Long datasourceId);

    /**
     *
     * @param datasourceGroup
     * @return
     */
    int saveDatasourceGroup(DatasourceGroup datasourceGroup);

    /**
     *
     * @param datasourceGroup
     * @return
     */
    int updateDatasourceGroup(DatasourceGroup datasourceGroup);

    /**
     *
     * @param datasourceGroupId
     * @param status
     * @return
     */
    int updateDatasourceGroupStatus(Long datasourceGroupId, DatasourceGroupStatusEnum status);

    /**
     *
     * @param datasourceGroupId
     * @return
     */
    int deleteDatasourceGroup(Long datasourceGroupId);

    /**
     *
     * @param name
     * @param datasourceGroupId
     * @return
     */
    boolean isDatasourceGroupNameExisted(String name, Long datasourceGroupId);

    /**
     *
     * @param datasourceGroupId
     * @return
     */
    DatasourceGroup getDatasourceGroup(Long datasourceGroupId);

    /**
     *
     * @param datasourceGroupId
     * @return
     */
    DatasourceGroup getDatasourceGroupHead(Long datasourceGroupId);

    /**
     *
     * @param collection
     * @param withDatasource
     * @return
     */
    List<DatasourceGroup> listDatasourceGroupByIds(Collection<Long> collection, boolean withDatasource);

    /**
     *
     * @param query
     * @return
     */
    List<DatasourceGroup> listDatasourceGroup(DatasourceGroupQuery query);

    /**
     *
     * @param query
     * @return
     */
    int countDatasourceGroup(DatasourceGroupQuery query);

    /**
     *
     * @param query
     * @return
     */
    List<DatasourceGroupHeadDTO> headDatasourceGroup(DatasourceGroupQuery query);

}
