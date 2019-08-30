package com.mglj.shards.service.dao.api;


import com.mglj.shards.service.domain.po.CriteriaConfigPO;

import java.util.List;

/**
 * 的Dao
 * 
 * @author zsp
 * @date 2019-2-27
 */
public interface CriteriaConfigDao {

	/**
	 * 查找多个
	 *
	 * @return
	 */
	List<CriteriaConfigPO> listCriteriaConfig();
	
}
