package com.mglj.shards.service.domain.po;

/**
 * Created by zsp on 2019/3/12.
 */
public class DatasourceGroupRelationPO {

    private Long datasourceGroupId;

    private Long datasourceId;

    public Long getDatasourceGroupId() {
        return datasourceGroupId;
    }

    public void setDatasourceGroupId(Long datasourceGroupId) {
        this.datasourceGroupId = datasourceGroupId;
    }

    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }
}
