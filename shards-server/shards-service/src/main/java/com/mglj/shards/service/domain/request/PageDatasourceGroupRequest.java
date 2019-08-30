package com.mglj.shards.service.domain.request;


import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;
import com.mglj.totorial.framework.common.lang.PageRequest;

/**
 * Created by zsp on 2019/3/14.
 */
public class PageDatasourceGroupRequest extends PageRequest {

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

    @Override
    public String toString() {
        return "PageDatasourceGroupRequest{" +
                "offset=" + offset +
                ", rows=" + rows +
                ", status=" + status +
                ", name='" + name + '\'' +
                '}';
    }
}
