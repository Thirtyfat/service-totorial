package com.mglj.shards.service.dao.api;


import com.mglj.shards.service.domain.po.DatasourcePropertyPO;

import java.util.Collection;
import java.util.List;

/**
 * Created by zsp on 2019/3/8.
 */
public interface DatasourcePropertyDao {

    /**
     * 保存多个
     *
     * @param col
     */
    int saveAllDatasourceProperty(Collection<DatasourcePropertyPO> col);

    /**
     * 更新
     *
     * @param col
     */
    int updateAllDatasourceProperty(Collection<DatasourcePropertyPO> col);

    /**
     * 删除
     *
     * @param datasourceId
     */
    int deleteDatasourceProperty(Long datasourceId);

//    /**
//     * 删除
//     *
//     * @param datasourceId
//     * @param keys
//     */
//    int deleteSomeDatasourceProperty(Long datasourceId, Collection<String> keys);

    /**
     * 查找多个
     *
     * @param datasourceId
     * @return
     */
    List<DatasourcePropertyPO> listDatasourceProperty(Long datasourceId);

    /**
     * 查找多个
     *
     * @param col 标识集合
     * @return
     */
    List<DatasourcePropertyPO> listDatasourcePropertyByIds(Collection<Long> col);

    /**
     * 查找多个
     *
     * @return
     */
    List<DatasourcePropertyPO> listAllDatasourceProperty();

}
