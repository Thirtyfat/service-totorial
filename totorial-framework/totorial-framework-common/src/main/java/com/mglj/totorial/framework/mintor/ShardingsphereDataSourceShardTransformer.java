package com.mglj.totorial.framework.mintor;

import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by zsp on 2019/3/13.
 */
public class ShardingsphereDataSourceShardTransformer implements DataSourceShardTransformer {

    @Override
    public DataSource toDataSource(DataSourceShard dataSourceShard) {
        Objects.requireNonNull(dataSourceShard, "数据源分片不能为空");
        String shardName = dataSourceShard.getName();
        Objects.requireNonNull(shardName, "数据源分片名称不能为空");
        DataSourceShard.DataSourceWrapper masterDataSource = dataSourceShard.getMasterDataSource();
        Objects.requireNonNull(masterDataSource, "数据源分片" + shardName + "未定义写库");

        Map<String, DataSource> dataSourceMap = new HashMap<>();
        String dataSourceName, masterDataSourceName;
        DataSource dataSource;
        dataSourceName = masterDataSourceName = masterDataSource.getName();
        Objects.requireNonNull(dataSourceName, "数据源分片写库未定义名称");
        dataSource = masterDataSource.getDataSource();
        Objects.requireNonNull(dataSource, "数据源分片写库" + dataSourceName + "未定义DataSource实例");
        dataSourceMap.put(dataSourceName, dataSource);
        List<DataSourceShard.DataSourceWrapper> slaveDataSourceList = dataSourceShard.getSlaveDataSourceList();
        List<String> slaveNames = new ArrayList<>();
        if(CollectionUtils.isEmpty(slaveDataSourceList)) {
            slaveNames.add(dataSourceName);
        } else {
            for (DataSourceShard.DataSourceWrapper slaveDataSource : slaveDataSourceList) {
                dataSourceName = slaveDataSource.getName();
                Objects.requireNonNull(dataSourceName, "数据源分片读库未定义名称");
                slaveNames.add(dataSourceName);
                dataSource = slaveDataSource.getDataSource();
                Objects.requireNonNull(dataSource, "数据源分片读库" + dataSourceName + "未定义DataSource实例");
                dataSourceMap.put(dataSourceName, dataSource);
            }
        }
        try {
            return MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,
                    new MasterSlaveRuleConfiguration(shardName, masterDataSourceName, slaveNames, null),
                    new HashMap<>(),
                    new Properties());
        } catch (SQLException e) {
            throw new DataSourceException("根据数据源分片创建DataSource对象异常", e);
        }
    }

}
