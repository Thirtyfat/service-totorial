package com.mglj.shards.service.dao.api;


import com.mglj.shards.service.domain.po.SyncMessagePO;
import com.mglj.shards.service.domain.query.SyncMessageQuery;

import java.util.List;

/**
 * 的Dao
 * 
 * @author zsp
 * @date 2019-3-19
 */
public interface SyncMessageDao {
	
	/**
	 * 保存
	 *
	 * @param syncMessage
	 */
	void saveSyncMessage(SyncMessagePO syncMessage);
	
	/**
	 * 查找一个
	 *
	 * @param syncMessageId
	 * @return
	 */
	SyncMessagePO getSyncMessage(Long syncMessageId);
	
	/**
	 * 查找多个
	 *
	 * @param query 查找条件
	 * @return
	 */
	List<SyncMessagePO> listSyncMessage(SyncMessageQuery query);
	
	/**
	 * 统计个数
	 *
	 * @param query 查找条件
	 * @return
	 */
	int countSyncMessage(SyncMessageQuery query);
	
}
