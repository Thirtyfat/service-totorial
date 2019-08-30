package com.mglj.totorial.framework.mintor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 动态数据源
 */
public class DynamicDataSource extends AbstractDataSource implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    private DataSource defaultDataSource;
    private DataSourceRouter dataSourceRouter;

    private DynamicDataSource() {

    }

    /**
     * 构建一个动态数据源
     *
     * @param dataSourceRouter      数据源路由
     */
    public DynamicDataSource(DataSourceRouter dataSourceRouter) {
        Objects.requireNonNull(dataSourceRouter);
        this.dataSourceRouter = dataSourceRouter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Objects.requireNonNull(this.defaultDataSource, "The defaultDataSource is not set.");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.determineTargetDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return this.determineTargetDataSource().getConnection(username, password);
    }

    private DataSource determineTargetDataSource() {
        Object shardingKey = ShardingKeyHolder.get();
        if (shardingKey == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("未设置shardingKey，返回默认数据源");
            }
            return defaultDataSource;
        }
        DataSource dataSource = dataSourceRouter.route(shardingKey);
        if (dataSource == null) {
            throw new DataSourceException("未对分片键[" + shardingKey + "]定义数据源");
        }
        return dataSource;
    }

    public void setDefaultDataSource(DataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

}
