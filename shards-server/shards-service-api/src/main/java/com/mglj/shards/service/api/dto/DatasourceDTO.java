package com.mglj.shards.service.api.dto;


import com.mglj.totorial.framework.common.enums.DataSourceRoleEnum;
import com.mglj.totorial.framework.common.validation.AlertMessage;
import com.mglj.totorial.framework.common.validation.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class DatasourceDTO implements Serializable {

    /**
     * 数据源信息ID
     */
    @NotNull(message = "datasourceIdRequired", groups = { Update.class })
    @AlertMessage(code = "datasourceIdRequired", msg = "ID不能为空")
    private Long datasourceId;
    /**
     * 类型 0是写，1是读
     */
    @NotNull(message = "typeRequired")
    @AlertMessage(code = "typeRequired", msg = "类型不能为空")
    private DataSourceRoleEnum type;
    /**
     * 名称
     */
    @NotNull(message = "nameRequired")
    @AlertMessage(code = "nameRequired", msg = "名称不能为空")
    private String name;
    /**
     *
     */
    private Date updateTime;
    /**
     * 备注
     */
    private String remark;

    /**
     * 数据源属性
     */
    private Map<String, Object> properties;

    /**
     * 设置
     */
    public void setDatasourceId(Long value) {
        this.datasourceId = value;
    }
    /**
     * 获取
     */
    public Long getDatasourceId() {
        return this.datasourceId;
    }
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
    /**
     * 设置
     */
    public void setUpdateTime(Date value) {
        this.updateTime = value;
    }
    /**
     * 获取
     */
    public Date getUpdateTime() {
        return this.updateTime;
    }
    /**
     * 设置备注
     */
    public void setRemark(String value) {
        this.remark = value;
    }
    /**
     * 获取备注
     */
    public String getRemark() {
        return this.remark;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

}