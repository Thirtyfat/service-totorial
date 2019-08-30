package com.mglj.totorial.framework.mintor;

import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.*;

/**
 * 数据源分片
 *
 * Created by zsp on 2019/3/7.
 */
public class DataSourceShard {

    /**
     * 名称，用于标识数据源分片
     */
    private final String name;
    /**
     * 主数据源
     */
    private DataSourceWrapper masterDataSource;
    /**
     * 从数据源集合
     */
    private List<DataSourceWrapper> slaveDataSourceList;

    public DataSourceShard(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    /**
     * 添加从数据源
     *
     * @param dataSourceWrapper
     */
    public void addSlaveDataSource(DataSourceWrapper dataSourceWrapper) {
        if(dataSourceWrapper == null) {
            return;
        }
        if(slaveDataSourceList == null) {
            slaveDataSourceList = new ArrayList<>();
        }
        slaveDataSourceList.add(dataSourceWrapper);
    }

    /**
     * 获取所有数据源，包括主、从数据源
     *
     * @return
     */
    public Set<DataSourceWrapper> getAllDataSource() {
        Set<DataSourceWrapper> set = new HashSet<>();
        if(masterDataSource != null) {
            set.add(masterDataSource);
        }
        if(!CollectionUtils.isEmpty(slaveDataSourceList)) {
            for(DataSourceWrapper slaveDataSource : slaveDataSourceList) {
                set.add(slaveDataSource);
            }
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSourceShard that = (DataSourceShard) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String getName() {
        return name;
    }

    public DataSourceWrapper getMasterDataSource() {
        return masterDataSource;
    }

    public void setMasterDataSource(DataSourceWrapper masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public List<DataSourceWrapper> getSlaveDataSourceList() {
        return slaveDataSourceList;
    }

    public void setSlaveDataSourceList(List<DataSourceWrapper> slaveDataSourceList) {
        this.slaveDataSourceList = slaveDataSourceList;
    }

    public static class DataSourceWrapper {

        /**
         * 名称，用于标识数据源
         */
        private String name;

        /**
         * 数据源
         */
        private DataSource dataSource;

        public DataSourceWrapper() {

        }

        public DataSourceWrapper(String name, DataSource dataSource) {
            this.name = name;
            this.dataSource = dataSource;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DataSourceWrapper that = (DataSourceWrapper) o;

            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public DataSource getDataSource() {
            return dataSource;
        }

        public void setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
        }

    }

}
