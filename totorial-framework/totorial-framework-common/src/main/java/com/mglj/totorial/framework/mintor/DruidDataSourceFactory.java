package com.mglj.totorial.framework.mintor;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mglj.totorial.framework.mintor.PropertyNames.*;


/**
 * Created by zsp on 2019/3/13.
 */
public class DruidDataSourceFactory implements DataSourceFactory {

    private final Logger logger = LoggerFactory.getLogger(DruidDataSourceFactory.class);

    /**
     * 参考：
     *      https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8
     *      https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98
     *
     * @param config        数据源及连接池的配置
     * @return
     */
    @Override
    public DataSource create(Object config) {
        Objects.requireNonNull(config);
        if(!(config instanceof Map)) {
            throw new IllegalArgumentException("The type of config should be the Map.");
        }
        Map<String, Object> properties = (Map<String, Object>)config;
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setDriverClassName((String)properties.get(NAME_DRIVER_CLASS_NAME));

        dataSource.setUrl((String) properties.get(NAME_URL));
        dataSource.setUsername((String) properties.get(NAME_USERNAME));
        dataSource.setPassword((String) properties.get(NAME_PASSWORD));
        dataSource.setName((String) properties.get(NAME_NAME));

        //缺省值0，初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
        if (properties.containsKey(NAME_INITIAL_SIZE)) {
            dataSource.setInitialSize((int)properties.get(NAME_INITIAL_SIZE));
        }
        //最小连接池数量
        if (properties.containsKey(NAME_MIN_IDLE)) {
            dataSource.setMinIdle((int)properties.get(NAME_MIN_IDLE));
        }
        //缺省值8，最大连接池数量
        if (properties.containsKey(NAME_MAX_ACTIVE)) {
            dataSource.setMaxActive((int)properties.get(NAME_MAX_ACTIVE));
        }
        //获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
        if (properties.containsKey(NAME_MAX_WAIT)) {
            dataSource.setMaxWait(tryToGetLong(properties.get(NAME_MAX_WAIT)));
        }
        if(properties.containsKey(NAME_USE_UNFAIR_LOCK)) {
            dataSource.setUseUnfairLock((boolean)properties.get(NAME_USE_UNFAIR_LOCK));
        }
        //用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
        if (properties.containsKey(NAME_VALIDATION_QUERY)) {
            dataSource.setValidationQuery((String)properties.get(NAME_VALIDATION_QUERY));
        }
        //缺省值true，申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
        if (properties.containsKey(NAME_TEST_ON_BORROW)) {
            dataSource.setTestOnBorrow((boolean)properties.get(NAME_TEST_ON_BORROW));
        }
        //缺省值false，归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
        if (properties.containsKey(NAME_TEST_ON_RETURN)) {
            dataSource.setTestOnBorrow((boolean)properties.get(NAME_TEST_ON_RETURN));
        }
        //缺省值false，建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
        if(properties.containsKey(NAME_TEST_WHILE_IDLE)) {
            dataSource.setTestWhileIdle((boolean)properties.get(NAME_TEST_WHILE_IDLE));
        }
        //缺省值1分钟，有两个含义：
        //  1) Destroy线程会检测连接的间隔时间，如果连接空闲时间大于等于minEvictableIdleTimeMillis则关闭物理连接。
        //  2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
        if (properties.containsKey(NAME_TIME_BETWEEN_EVICTION_RUNS_MILLIS)) {
            dataSource.setTimeBetweenEvictionRunsMillis(tryToGetLong(properties.get(NAME_TIME_BETWEEN_EVICTION_RUNS_MILLIS)));
        }
        //连接保持空闲而不被驱逐的最小时间
        if (properties.containsKey(NAME_MIN_EVICTABLE_IDLE_TIME_MILLIS)) {
            dataSource.setMinEvictableIdleTimeMillis(tryToGetLong(properties.get(NAME_MIN_EVICTABLE_IDLE_TIME_MILLIS)));
        }
        //物理连接初始化的时候执行的sql
        if (properties.containsKey(NAME_CONNECTION_INIT_SQLS)) {
            dataSource.setConnectionInitSqls((List<String>)properties.get(NAME_CONNECTION_INIT_SQLS));
        }
        //属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：
        //  监控统计用的filter:stat
        //  日志用的filter:log4j
        //  防御sql注入的filter:wall
        try {
            dataSource.setFilters((String)properties.get(NAME_FILTERS));
        } catch (SQLException | NoClassDefFoundError e) {
            logger.error("构建数据源错误: key=" + NAME_FILTERS, e);
        }

        return dataSource;
    }

    @Override
    public void update(DataSource dataSource, Object config) {
        if(dataSource == null) {
            return;
        }
        if(!(dataSource instanceof DruidDataSource)) {
            throw new IllegalArgumentException("The type of dataSource should be the DruidDataSource.");
        }
        if(!(config instanceof Map)) {
            throw new IllegalArgumentException("The type of config should be the Map.");
        }
        DruidDataSource druidDataSource = (DruidDataSource)dataSource;
        Map<String, Object> properties = (Map<String, Object>)config;

//        if(properties.containsKey(NAME_PASSWORD)) {
//            druidDataSource.setPassword((String) properties.get(NAME_PASSWORD));
//        }
        Integer minIdle = (Integer)properties.get(NAME_MIN_IDLE);
        if(minIdle != null) {
            druidDataSource.setMinIdle(minIdle);
        }
        Integer maxActive = (Integer)properties.get(NAME_MAX_ACTIVE);
        if(maxActive != null) {
            druidDataSource.setMaxActive(maxActive);
        }
        if(properties.containsKey(NAME_MAX_WAIT)) {
            Long maxWait = tryToGetLong(properties.get(NAME_MAX_WAIT));
            if (maxWait != null) {
                druidDataSource.setMaxWait(maxWait);
            }
        }
        Boolean useUnfairLock = (Boolean)properties.get(NAME_USE_UNFAIR_LOCK);
        if(useUnfairLock != null) {
            druidDataSource.setUseUnfairLock(useUnfairLock);
        }
        if(properties.containsKey(NAME_TIME_BETWEEN_EVICTION_RUNS_MILLIS)) {
            Long timeBetweenEvictionRunsMillis = tryToGetLong(properties.get(NAME_TIME_BETWEEN_EVICTION_RUNS_MILLIS));
            if (timeBetweenEvictionRunsMillis != null) {
                druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            }
        }
        if(properties.containsKey(NAME_MIN_EVICTABLE_IDLE_TIME_MILLIS)) {
            Long minEvictableIdleTimeMillis = tryToGetLong(properties.get(NAME_MIN_EVICTABLE_IDLE_TIME_MILLIS));
            if (minEvictableIdleTimeMillis != null) {
                druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            }
        }
        String filters = (String)properties.get(NAME_FILTERS);
        if(filters != null) {
            try {
                druidDataSource.setFilters(filters);
            } catch (SQLException | NoClassDefFoundError e) {
                logger.error("构建数据源错误: key=" + NAME_FILTERS, e);
            }
        }
    }

    @Override
    public void close(DataSource dataSource) {
        if(dataSource == null) {
            return;
        }
        if(!(dataSource instanceof DruidDataSource)) {
            throw new IllegalArgumentException("The type of dataSource should be the DruidDataSource.");
        }
        DruidDataSource druidDataSource = (DruidDataSource)dataSource;
        druidDataSource.close();
    }

    private Long tryToGetLong(Object value) {
        if(value == null) {
            return null;
        }
        if(value instanceof Long) {
            return (Long)value;
        } else {
            //在Rest通信中，Long在传输时被转换成了String
            return Long.parseLong((String)value);
        }
    }

}
