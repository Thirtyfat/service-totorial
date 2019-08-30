package com.mglj.shards.service.manager.api;


import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.shards.service.domain.WmsShard;
import com.mglj.shards.service.domain.query.WmsShardQuery;

import java.util.List;

/**
 * Created by zsp on 2019/3/11.
 */
public interface WmsShardManager {

    int saveShard(Long warehouseId, Long datasourceGroupId);

    int updateShard(Long warehouseId, Long datasourceGroupId);

    int deleteShard(Long warehouseId);

    DatasourceGroup getShard(Long warehouseId);

    List<WmsShard> listShard(WmsShardQuery query);

    int countShard(WmsShardQuery query);

    List<WmsShard> listAllShard();

    List<Long> listWarehouseId(Long datasourceGroupId);

    List<Long> listAllWarehouseId();

}
