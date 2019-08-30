package com.mglj.shards.service.api.dto;


import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;
import com.mglj.totorial.framework.common.validation.AlertMessage;
import com.mglj.totorial.framework.common.validation.Update;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by zsp on 2019/3/11.
 */
public class DatasourceGroupDTO {

    /**
     * 数据源groupid
     */
    @NotNull(message = "datasourceGroupIdRequired", groups = { Update.class })
    @AlertMessage(code = "datasourceGroupIdRequired", msg = "ID不能为空")
    private Long datasourceGroupId;
    /**
     * 名称
     */
    @NotNull(message = "nameRequired")
    @AlertMessage(code = "nameRequired", msg = "名称不能为空")
    private String name;
    /**
     * 0是关闭，1是开启
     */
    private DatasourceGroupStatusEnum status;
    /**
     *
     */
    private Date updateTime;
    /**
     * 备注
     */
    private String remark;

    /**
     *
     */
    private List<DatasourceDTO> datasourceList;

    /**
     * 设置数据源groupid
     */
    public void setDatasourceGroupId(Long value) {
        this.datasourceGroupId = value;
    }
    /**
     * 获取数据源groupid
     */
    public Long getDatasourceGroupId() {
        return this.datasourceGroupId;
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
     * 设置0是关闭，1是开启
     */
    public void setStatus(DatasourceGroupStatusEnum value) {
        this.status = value;
    }
    /**
     * 获取0是关闭，1是开启
     */
    public DatasourceGroupStatusEnum getStatus() {
        return this.status;
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

    public List<DatasourceDTO> getDatasourceList() {
        return datasourceList;
    }

    public void setDatasourceList(List<DatasourceDTO> datasourceList) {
        this.datasourceList = datasourceList;
    }
}
