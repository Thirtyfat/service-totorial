package com.mglj.shards.service.domain.converter;

import com.mglj.shards.service.api.dto.DatasourceDTO;
import com.mglj.shards.service.domain.Datasource;
import com.mglj.shards.service.domain.po.DatasourcePO;
import com.mglj.shards.service.domain.po.DatasourcePropertyPO;
import com.mglj.shards.service.domain.query.DatasourceQuery;
import com.mglj.shards.service.domain.request.PageDatasourceRequest;
import com.mglj.totorial.framework.mintor.PropertyNames;
import com.mglj.totorial.framework.tool.beans.BeanUtilsEx;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by zsp on 2019/3/8.
 */
public class DatasourceConverter {

    public static DatasourcePO toPO(Datasource datasource) {
        DatasourcePO datasourcePO = datasource == null
                ? null
                : BeanUtilsEx.copyProperties(datasource, () -> new DatasourcePO());
        Map<String, Object> propeties;
        if(datasource != null && datasourcePO != null
                && (propeties = datasource.getProperties()) != null
                && propeties.size() > 0) {
            String key;
            for(Map.Entry<String, Object> entry : propeties.entrySet()) {
                datasourcePO.addProperty(new DatasourcePropertyPO(key = entry.getKey(),
                        PropertyNames.getStringSerializer(key).apply(entry.getValue())));
            }
        }
        return datasourcePO;
    }

    public static Datasource fromPO(DatasourcePO datasourcePO) {
        Datasource datasource = datasourcePO == null
                ? null
                : BeanUtilsEx.copyProperties(datasourcePO, () -> new Datasource());
        List<DatasourcePropertyPO> datasourcePropertyPOList;
        if(datasourcePO != null && datasource != null
                && (datasourcePropertyPOList = datasourcePO.getPropertyList()) != null
                && datasourcePropertyPOList.size() > 0) {
            String key;
            for(DatasourcePropertyPO item : datasourcePropertyPOList) {
                datasource.addProperty(key = item.getKey(),
                        PropertyNames.getStringDeserializer(key).apply(item.getValue()));
            }
        }
        return datasource;
    }

    public static List<Datasource> fromPO(Collection<DatasourcePO> collection) {
        List<Datasource> datasourceList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(collection)) {
            for(DatasourcePO datasourcePO : collection) {
                datasourceList.add(fromPO(datasourcePO));
            }
        }
        return datasourceList;
    }

    public static DatasourceDTO toDTO(Datasource datasource) {
        DatasourceDTO datasourceDTO =  datasource == null
                ? null
                : BeanUtilsEx.copyProperties(datasource, () -> new DatasourceDTO());
        return datasourceDTO;
    }

    public static List<DatasourceDTO> toDTO(Collection<Datasource> collection) {
        return BeanUtilsEx.copyPropertiesForNewList(collection, () -> new DatasourceDTO());
    }

    public static Datasource fromDTO(DatasourceDTO datasourceDTO) {
        return datasourceDTO == null ? null : BeanUtilsEx.copyProperties(datasourceDTO, () -> new Datasource());
    }

    public static List<Datasource> fromDTO(Collection<DatasourceDTO> collection) {
        return BeanUtilsEx.copyPropertiesForNewList(collection, () -> new Datasource());
    }

    public static DatasourceQuery requestToQuery(PageDatasourceRequest request) {
        return request == null ? null : BeanUtilsEx.copyProperties(request, () -> new DatasourceQuery());
    }

}
