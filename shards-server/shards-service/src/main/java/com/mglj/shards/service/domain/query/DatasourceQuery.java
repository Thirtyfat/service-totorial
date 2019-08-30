package com.mglj.shards.service.domain.query;


import com.mglj.totorial.framework.common.enums.DataSourceRoleEnum;
import com.mglj.totorial.framework.common.lang.PageQuery;

/**
 * 的Query
 * 
 * @author cxf
 * @date 2019-2-27
 */
public class DatasourceQuery extends PageQuery {

    /**
     * 类型 0是写，1是读
     */
    private DataSourceRoleEnum type;
    /**
     * 名称
     */
    private String name;

    /**
     * 设置类型 1是写，2是读
     */
    public void setType(DataSourceRoleEnum value) {
        this.type = value;
    }
    /**
     * 获取类型 1是写，2是读
     */
    public DataSourceRoleEnum getType() {
        return this.type;
    }
    /**
     * 设置名称
     */
    public void setName(String value) {
        this.name = value;
    }
    /**
     * 获取名称
     */
    public String getName() {
        return this.name;
    }

}
