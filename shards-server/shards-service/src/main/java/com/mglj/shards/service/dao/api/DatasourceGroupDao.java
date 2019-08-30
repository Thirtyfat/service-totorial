package com.mglj.shards.service.dao.api;


import com.mglj.shards.service.api.dto.DatasourceGroupHeadDTO;
import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;
import com.mglj.shards.service.domain.po.DatasourceGroupPO;
import com.mglj.shards.service.domain.query.DatasourceGroupQuery;

import java.util.Collection;
import java.util.List;

/**
 * 的Dao
 * 
 * @author zsp
 * @date 2019-3-8
 */
public interface DatasourceGroupDao {
	
	/**
	 * 保存
	 *
	 * @param datasourceGroupPO
	 */
	int saveDatasourceGroup(DatasourceGroupPO datasourceGroupPO);
	
	/**
	 * 更新
	 *
	 * @param datasourceGroupPO
	 */
	int updateDatasourceGroup(DatasourceGroupPO datasourceGroupPO);

	/**
	 *
	 * @param datasourceGroupId
	 * @param status
	 * @return
	 */
	int updateDatasourceGroupStatus(Long datasourceGroupId, DatasourceGroupStatusEnum status);
	
	/**
	 * 删除
	 *
	 * @param id
	 */
	int deleteDatasourceGroup(Long id);

	/**
	 *
	 * @param name
	 * @param datasourceGroupId
	 * @return
	 */
	boolean isNameExisted(String name, Long datasourceGroupId);
	
	/**
	 * 查找一个
	 *
	 * @param id
	 * @return
	 */
	DatasourceGroupPO getDatasourceGroup(Long id);
	
	/**
	 * 查找多个
	 *
	 * @param col 标识集合
	 * @return
	 */
	List<DatasourceGroupPO> listDatasourceGroupByIds(Collection<Long> col);

	/**
	 *
	 * @param query
	 * @return
	 */
	List<DatasourceGroupPO> listDatasourceGroup(DatasourceGroupQuery query);

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
