package com.mglj.shards.service.dao.api;


import com.mglj.shards.service.domain.po.WmsShardPO;
import com.mglj.shards.service.domain.query.WmsShardQuery;

import java.util.List;

/**
 * Created by zsp on 2019/3/11.
 */
public interface WmsShardDao {

    int saveShard(Long warehouseId, Long datasourceGroupId);

    int updateShard(Long warehouseId, Long datasourceGroupId);

    int deleteShard(Long warehouseId);

    Long getShard(Long warehouseId);

    List<WmsShardPO> listShard(WmsShardQuery query);

    int countShard(WmsShardQuery query);

    List<WmsShardPO> listAllShard();

    List<Long> listWarehouseId(Long datasourceGroupId);

    List<Long> listAllWarehouseId();

}
