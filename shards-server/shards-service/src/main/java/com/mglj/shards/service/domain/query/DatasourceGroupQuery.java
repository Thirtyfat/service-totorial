package com.mglj.shards.service.domain.query;


import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;
import com.mglj.totorial.framework.common.lang.PageQuery;

/**
 * Created by zsp on 2019/3/14.
 */
public class DatasourceGroupQuery extends PageQuery {

    private DatasourceGroupStatusEnum status;
    private String name;

    public DatasourceGroupStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DatasourceGroupStatusEnum status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
