package com.mglj.totorial.framework.mintor;

import javax.sql.DataSource;

/**
 * 数据源分片的转换逻辑
 *
 * Created by zsp on 2019/3/13.
 */
public interface DataSourceShardTransformer {

    /**
     * 把数据源分片信息转换为一个数据源
     *
     * @param dataSourceShard           数据源分片信息
     * @return
     */
    DataSource toDataSource(DataSourceShard dataSourceShard);

}
