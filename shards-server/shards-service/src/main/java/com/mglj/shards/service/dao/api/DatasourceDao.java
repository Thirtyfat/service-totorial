package com.mglj.shards.service.dao.api;


import com.mglj.shards.service.api.dto.DatasourceHeadDTO;
import com.mglj.shards.service.domain.po.DatasourcePO;
import com.mglj.shards.service.domain.query.DatasourceQuery;

import java.util.Collection;
import java.util.List;

/**
 * 的Dao
 * 
 * @author cxf
 * @date 2019-2-27
 */
public interface DatasourceDao {
	
	/**
	 * 保存
	 *
	 * @param datasourcePO
	 */
	int saveDatasource(DatasourcePO datasourcePO);
	
	/**
	 * 更新
	 *
	 * @param datasourcePO
	 */
	int updateDatasource(DatasourcePO datasourcePO);
	
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
	boolean isNameExisted(String name, Long datasourceId);

	/**
	 *
	 * @param col
	 * @return
	 */
	int countDatasourceExist(Collection<Long> col);
	
	/**
	 * 查找一个
	 *
	 * @param datasourceId
	 * @return
	 */
	DatasourcePO getDatasource(Long datasourceId);
	
	/**
	 * 查找多个
	 *
	 * @param col 标识集合
	 * @return
	 */
	List<DatasourcePO> listDatasourceByIds(Collection<Long> col);
	
	/**
	 * 查找多个
	 *
	 * @param query 查找条件
	 * @return
	 */
	List<DatasourcePO> listDatasource(DatasourceQuery query);
	
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

}
