package com.mglj.shards.service.service.api;


import com.mglj.shards.service.api.dto.WmsShardChangeDTO;
import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.shards.service.domain.WmsShard;
import com.mglj.shards.service.domain.query.WmsShardQuery;
import com.mglj.totorial.framework.common.lang.Result;

import java.util.List;

/**
 * Created by zsp on 2019/3/11.
 */
public interface WmsShardService {

    /**
     * 保存分片
     *
     * @param warehouseId
     * @param warehouseName
     * @param datasourceGroupId
     * @return
     */
    Result<?> saveShard(Long warehouseId, String warehouseName, Long datasourceGroupId);

    /**
     * 更新分片
     *
     * @param warehouseId
     * @param datasourceGroupId
     * @return
     */
    Result<?> updateShard(Long warehouseId, Long datasourceGroupId);

    /**
     * 删除分片
     *
     * @param warehouseId
     */
    void deleteShard(Long warehouseId);

    /**
     * 获取分片
     *
     * @param warehouseId
     * @return
     */
    DatasourceGroup getShard(Long warehouseId);

    /**
     * 查询分片
     *
     * @param query
     * @return
     */
    List<WmsShard> listShard(WmsShardQuery query);

    /**
     * 统计分片
     *
     * @param query
     * @return
     */
    int countShard(WmsShardQuery query);

    /**
     * 查询所有分片
     *
     * @return
     */
    List<WmsShard> listAllShard();

    /**
     * 根据同步消息Id获取分片
     *
     * @param syncMessageId
     * @return
     */
    WmsShardChangeDTO getLatestWmsShard(Long syncMessageId);

    /**
     *
     * @return
     */
    List<Long> listAllWarehouseId();

}
