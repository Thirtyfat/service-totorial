package com.mglj.shards.service.api.facade.api;

import com.mglj.shards.service.api.dto.DatasourceDTO;
import com.mglj.shards.service.api.dto.SyncMessageIdDTO;
import com.mglj.shards.service.api.dto.WmsShardChangeDTO;
import com.mglj.shards.service.api.dto.WmsShardMapDTO;
import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.mintor.DataSourceShard;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@FeignClient("${feign.scm-shards-service.name}")
public interface WmsShardFacade extends ShardFacade {

    /**
     * 获取所有仓库-数据源分片的映射关系集合
     *
     * @return
     */
    @PostMapping("/shard/wms/list")
    Result<List<WmsShardMapDTO>> listWmsShard();

    /**
     * 根据同步消息Id获取数据源的更新数据
     *
     * @param syncMessageIdDTO      同步消息Id
     * @return
     */
    @PostMapping("/shard/wms/get-latest")
    Result<WmsShardChangeDTO> getLatestWmsShard(SyncMessageIdDTO syncMessageIdDTO);

    /**
     * 返回数据源组名 - 仓库ID集合的映射表
     *
     * @param wmsShardMapDTOCollection
     * @return
     */
    default Map<String, List<Long>> getGroupNameToWarehouseMap(Collection<WmsShardMapDTO> wmsShardMapDTOCollection) {
        return wmsShardMapDTOCollection.stream().collect(
                Collectors.toMap(e -> e.getDatasourceGroup().getName(), e -> e.getWarehouseIdList()));

    }

    /**
     * 返回数据源分片
     *
     * @param wmsShardMapDTOCollection
     * @param dataSourceFactory
     * @return
     */
    default List<DataSourceShard> listDataSourceShard(Collection<WmsShardMapDTO> wmsShardMapDTOCollection,
                                                      Function<DatasourceDTO, DataSource> dataSourceFactory) {
        Objects.requireNonNull(dataSourceFactory);
        List<DataSourceShard> dataSourceShardList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(wmsShardMapDTOCollection)) {
            for(WmsShardMapDTO wmsShardMapDTO : wmsShardMapDTOCollection) {
                dataSourceShardList.add(createDataSourceShard(wmsShardMapDTO, dataSourceFactory));
            }
        }
        return dataSourceShardList;
    }

    /**
     * 创建数据源分片
     *
     * @param wmsShardMapDTO
     * @param dataSourceFactory
     * @return
     */
    default DataSourceShard createDataSourceShard(WmsShardMapDTO wmsShardMapDTO,
                                                  Function<DatasourceDTO, DataSource> dataSourceFactory) {
        Objects.requireNonNull(dataSourceFactory);
        return createDataSourceShard(wmsShardMapDTO.getDatasourceGroup(), dataSourceFactory);
    }

}
