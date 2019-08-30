package com.mglj.totorial.framework.mintor;

import javax.sql.DataSource;

/**
 * 数据源的创建、更新、销毁逻辑
 *
 * Created by zsp on 2019/3/13.
 */
public interface DataSourceFactory {

    /**
     * 创建一个数据源
     *
     * @param config            数据源及连接池的配置
     * @return
     */
    DataSource create(Object config);

    /**
     * 更新数据源属性
     *
     * @param dataSource        数据源
     * @param config            属性
     */
    void update(DataSource dataSource, Object config);

    /**
     * 关闭数据源
     *
     * @param dataSource        数据源
     */
    void close(DataSource dataSource);

}
