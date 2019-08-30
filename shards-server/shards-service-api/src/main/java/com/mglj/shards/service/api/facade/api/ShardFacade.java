package com.mglj.shards.service.api.facade.api;

import com.mglj.shards.service.api.dto.DatasourceDTO;
import com.mglj.shards.service.api.dto.DatasourceGroupDTO;
import com.mglj.totorial.framework.common.enums.DataSourceRoleEnum;
import com.mglj.totorial.framework.mintor.DataSourceException;
import com.mglj.totorial.framework.mintor.DataSourceShard;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by zsp on 2019/3/14.
 */
public interface ShardFacade {

    /**
     * 创建数据源分片
     *
     * @param datasourceGroupDTO
     * @param dataSourceFactory
     * @return
     */
    default DataSourceShard createDataSourceShard(DatasourceGroupDTO datasourceGroupDTO,
                                                  Function<DatasourceDTO, DataSource> dataSourceFactory) {
        Objects.requireNonNull(dataSourceFactory);
        String shardName = datasourceGroupDTO.getName();
        List<DatasourceDTO> datasourceDTOList = datasourceGroupDTO.getDatasourceList();
        if(CollectionUtils.isEmpty(datasourceDTOList)) {
            throw new DataSourceException("分片" + shardName + "未定义数据源");
        }
        DataSourceShard dataSourceShard = new DataSourceShard(shardName);
        for(DatasourceDTO datasourceDTO : datasourceDTOList) {
            if(Objects.equals(DataSourceRoleEnum.READ_ONLY, datasourceDTO.getType())) {
                dataSourceShard.addSlaveDataSource(createDataSource(datasourceDTO, dataSourceFactory));
            } else {
                dataSourceShard.setMasterDataSource(createDataSource(datasourceDTO, dataSourceFactory));
            }
        }
        return dataSourceShard;
    }

    default DataSourceShard.DataSourceWrapper createDataSource(DatasourceDTO datasourceDTO,
                                                               Function<DatasourceDTO, DataSource> dataSourceFactory) {
        Objects.requireNonNull(dataSourceFactory);
        return new DataSourceShard.DataSourceWrapper(datasourceDTO.getName(),
                dataSourceFactory.apply(datasourceDTO));
    }

}
