package com.mglj.shards.service.dao.api;


import com.mglj.shards.service.domain.po.SyncMessageItemPO;

import java.util.Collection;
import java.util.List;

/**
 * 的Dao
 * 
 * @author zsp
 * @date 2019-3-19
 */
public interface SyncMessageItemDao {
	
	/**
	 * 保存多个
	 *
	 * @param col
	 */
	void saveAllSyncMessageItem(Collection<SyncMessageItemPO> col);
	
	/**
	 * 查找多个
	 *
	 * @param col 标识集合
	 * @return
	 */
	List<SyncMessageItemPO> listSyncMessageItemByIds(Collection<Long> col);

}
