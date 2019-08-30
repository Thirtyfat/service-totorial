package com.mglj.shards.service.domain.converter;

import com.mglj.shards.service.api.dto.DatasourceDTO;
import com.mglj.shards.service.api.dto.DatasourceGroupDTO;
import com.mglj.shards.service.domain.Datasource;
import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.shards.service.domain.po.DatasourceGroupPO;
import com.mglj.shards.service.domain.query.DatasourceGroupQuery;
import com.mglj.shards.service.domain.request.PageDatasourceGroupRequest;
import com.mglj.shards.service.domain.request.SaveDatasourceGroupRequest;
import com.mglj.totorial.framework.tool.beans.BeanUtilsEx;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zsp on 2019/3/11.
 */
public class DatasourceGroupConverter {

    public static DatasourceGroupPO toPO(DatasourceGroup datasourceGroup) {
        return BeanUtilsEx.copyProperties(datasourceGroup, () -> new DatasourceGroupPO());
    }

    public static DatasourceGroup fromPO(DatasourceGroupPO datasourceGroupPO) {
        return BeanUtilsEx.copyProperties(datasourceGroupPO, () -> new DatasourceGroup());
    }

    public static List<DatasourceGroup> fromPO(Collection<DatasourceGroupPO> datasourceGroupPOCollection) {
        return BeanUtilsEx.copyPropertiesForNewList(datasourceGroupPOCollection, () -> new DatasourceGroup());
    }

    public static DatasourceGroupDTO toDTO(DatasourceGroup datasourceGroup) {
        DatasourceGroupDTO datasourceGroupDTO = BeanUtilsEx.copyProperties(datasourceGroup,
                () -> new DatasourceGroupDTO());
        List<Datasource> datasourceList = datasourceGroup.getDatasourceList();
        if(!CollectionUtils.isEmpty(datasourceList)) {
            datasourceGroupDTO.setDatasourceList(DatasourceConverter.toDTO(datasourceList));
        }

        return datasourceGroupDTO;
    }

    public static List<DatasourceGroupDTO> toDTO(Collection<DatasourceGroup> datasourceGroupCollection) {
        List<DatasourceGroupDTO> datasourceGroupDTOList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(datasourceGroupCollection)) {
            for(DatasourceGroup datasourceGroup : datasourceGroupCollection) {
                datasourceGroupDTOList.add(toDTO(datasourceGroup));
            }
        }
        return datasourceGroupDTOList;
    }

    public static DatasourceGroup fromDTO(DatasourceGroupDTO datasourceGroupDTO) {
        DatasourceGroup datasourceGroup = BeanUtilsEx.copyProperties(datasourceGroupDTO,
                () -> new DatasourceGroup());
        List<DatasourceDTO> datasourceDTOList = datasourceGroupDTO.getDatasourceList();
        if(!CollectionUtils.isEmpty(datasourceDTOList)) {
            datasourceGroup.setDatasourceList(DatasourceConverter.fromDTO(datasourceDTOList));
        }
        return datasourceGroup;
    }

    public static DatasourceGroup fromRequest(SaveDatasourceGroupRequest request) {
        DatasourceGroup datasourceGroup = BeanUtilsEx.copyProperties(request, () -> new DatasourceGroup());
        List<Long> datasourceIdList = request.getDatasourceIdList();
        if(!CollectionUtils.isEmpty(datasourceIdList)) {
            datasourceGroup.setDatasourceIdList(datasourceIdList);
        }
        return datasourceGroup;
    }

    public static DatasourceGroupQuery requestToQuery(PageDatasourceGroupRequest request) {
        return request == null ? null : BeanUtilsEx.copyProperties(request, () -> new DatasourceGroupQuery());
    }

}
