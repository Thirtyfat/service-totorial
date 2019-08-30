package com.mglj.shards.service.configuration;

import com.mglj.shards.service.manager.api.DatasourceManager;
import com.mglj.shards.service.manager.api.WmsShardManager;
import com.mglj.shards.service.service.impl.builder.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.mglj.shards.service.api.DatasourceConstants.*;
import static com.mglj.shards.service.api.WmsShardConstants.*;


/**
 * Created by zsp on 2019/3/26.
 */
@Configuration
public class ShardingConfiguration {

    @Bean
    public Map<Integer, WmsShardChangeDTOBuilder> wmsShardChangeDTOBuilderMap(
            WmsShardManager wmsShardManager,
            DatasourceManager datasourceManager) {
        TraceWmsShardChangeDTOBuilder traceWmsShardChangeDTOBuilder
                = new TraceWmsShardChangeDTOBuilder();
        GenericWmsShardChangeDTOBuilder genericWmsShardChangeDTOBuilder
                = new GenericWmsShardChangeDTOBuilder(wmsShardManager);
        DeletionWmsShardChangeDTOBuilder deletionWmsShardChangeDTOBuilder
                = new DeletionWmsShardChangeDTOBuilder();
        DatasourceGroupWmsShardChangeDTOBuilder datasourceGroupWmsShardChangeDTOBuilder
                = new DatasourceGroupWmsShardChangeDTOBuilder(datasourceManager);
        DatasourceWmsShardChangeDTOBuilder datasourceWmsShardChangeDTOBuilder
                = new DatasourceWmsShardChangeDTOBuilder(datasourceManager);

        Map<Integer, WmsShardChangeDTOBuilder> map = new HashMap<>();
        map.put(ACTION_DATASOURCE_TRACE, traceWmsShardChangeDTOBuilder);
        map.put(ACTION_WMS_SHARD_TRACE, traceWmsShardChangeDTOBuilder);
        map.put(ACTION_WMS_SHARD_ADDITION, genericWmsShardChangeDTOBuilder);
        map.put(ACTION_WMS_SHARD_MODIFICATION, genericWmsShardChangeDTOBuilder);
        map.put(ACTION_WMS_SHARD_DELETION, deletionWmsShardChangeDTOBuilder);
        map.put(ACTION_DATASOURCE_GROUP_MEMBER_MODIFICATION, datasourceGroupWmsShardChangeDTOBuilder);
        map.put(ACTION_DATASOURCE_PROPERTY_MODIFICATION, datasourceWmsShardChangeDTOBuilder);
        return map;
    }

}
