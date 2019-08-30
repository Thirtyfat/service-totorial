package com.mglj.totorial.framework.mintor;

import javax.sql.DataSource;

/**
 * 数据源路由
 *
 * Created by zsp on 2019/3/7.
 */
public interface DataSourceRouter {

    /**
     * 根据分片键路由，选择数据源
     *
     * @param shardingKey           分片键
     * @return
     */
    DataSource route(Object shardingKey);

}
