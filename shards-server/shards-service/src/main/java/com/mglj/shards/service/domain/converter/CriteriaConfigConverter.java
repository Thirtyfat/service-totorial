package com.mglj.shards.service.domain.converter;


import com.mglj.shards.service.api.dto.CriteriaConfigDTO;
import com.mglj.shards.service.domain.CriteriaConfig;
import com.mglj.shards.service.domain.po.CriteriaConfigPO;
import com.mglj.totorial.framework.tool.beans.BeanUtilsEx;

import java.util.Collection;
import java.util.List;

/**
 * Created by zsp on 2019/3/18.
 */
public class CriteriaConfigConverter {

    public static List<CriteriaConfig> fromPO(Collection<CriteriaConfigPO> collection) {
        return BeanUtilsEx.copyPropertiesForNewList(collection, () -> new CriteriaConfig());
    }

    public static List<CriteriaConfigDTO> toDTO(Collection<CriteriaConfig> collection) {
        return BeanUtilsEx.copyPropertiesForNewList(collection, () -> new CriteriaConfigDTO());
    }

}
