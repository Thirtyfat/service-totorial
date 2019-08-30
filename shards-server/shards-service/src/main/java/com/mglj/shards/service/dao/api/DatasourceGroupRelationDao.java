package com.mglj.shards.service.dao.api;


import com.mglj.shards.service.domain.po.DatasourceGroupRelationPO;

import java.util.Collection;
import java.util.List;

/**
 * Created by zsp on 2019/3/11.
 */
public interface DatasourceGroupRelationDao {

    int saveDatasourceGroupRelation(Long datasourceId,
                                    Collection<Long> datasourceGroupIdCollection);

    int saveDatasourceGroupRelationByGroup(Long datasourceGroupId,
                                           Collection<Long> datasourceIdCollection);

    int deleteDatasourceGroupRelation(Long datasourceId);

    int deleteDatasourceGroupRelationByGroup(Long datasourceGroupId);

    List<Long> listDatasourceGroupId(Long datasourceId);

    List<Long> listDatasourceId(Long datasourceGroupId);

    List<DatasourceGroupRelationPO> listDatasourceGroupRelationByGroup(Collection<Long> datasourceGroupIdCollection);

}
