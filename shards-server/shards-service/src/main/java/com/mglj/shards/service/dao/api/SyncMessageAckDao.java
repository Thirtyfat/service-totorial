package com.mglj.shards.service.dao.api;


import com.mglj.shards.service.domain.po.SyncMessageAckPO;

import java.util.Collection;
import java.util.List;

/**
 * 的Dao
 * 
 * @author zsp
 * @date 2019-3-20
 */
public interface SyncMessageAckDao {
	
	/**
	 * 保存
	 *
	 * @param syncMessageAck
	 */
	void saveSyncMessageAck(SyncMessageAckPO syncMessageAck);

	/**
	 * 查找多个
	 *
	 * @param col 标识集合
	 * @return
	 */
	List<SyncMessageAckPO> listSyncMessageAckByIds(Collection<Long> col);

	
}
