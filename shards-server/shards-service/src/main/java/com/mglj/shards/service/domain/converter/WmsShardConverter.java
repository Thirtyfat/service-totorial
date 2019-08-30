package com.mglj.shards.service.domain.converter;

import com.mglj.shards.service.api.dto.WmsShardDTO;
import com.mglj.shards.service.api.dto.WmsShardMapDTO;
import com.mglj.shards.service.domain.WmsShard;
import com.mglj.shards.service.domain.po.WmsShardPO;
import com.mglj.shards.service.domain.query.WmsShardQuery;
import com.mglj.shards.service.domain.request.PageWmsShardRequest;
import com.mglj.totorial.framework.tool.beans.BeanUtilsEx;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by zsp on 2019/3/11.
 */
public class WmsShardConverter {

    public static WmsShardPO toPO(WmsShard wmsShard) {
        return BeanUtilsEx.copyProperties(wmsShard, () -> new WmsShardPO());
    }

    public static WmsShard fromPO(WmsShardPO wmsShardPO) {
        return BeanUtilsEx.copyProperties(wmsShardPO, () -> new WmsShard());
    }

    public static List<WmsShard> fromPO(Collection<WmsShardPO> collection) {
        return BeanUtilsEx.copyPropertiesForNewList(collection, () -> new WmsShard());
    }

    public static WmsShardDTO toDTO(WmsShard wmsShard) {
        WmsShardDTO wmsShardDTO = BeanUtilsEx.copyProperties(wmsShard, () -> new WmsShardDTO());
        if(wmsShardDTO != null && wmsShard != null) {
            wmsShardDTO.setDatasourceGroup(DatasourceGroupConverter.toDTO(wmsShard.getDatasourceGroup()));
        }
        return wmsShardDTO;
    }

    public static List<WmsShardDTO> toDTO(Collection<WmsShard> wmsShardCollection) {
        List<WmsShardDTO> wmsShardDTOList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(wmsShardCollection)) {
            for(WmsShard wmsShard : wmsShardCollection) {
                wmsShardDTOList.add(toDTO(wmsShard));
            }
        }
        return wmsShardDTOList;
    }

    public static List<WmsShardMapDTO> toMapDTO(Collection<WmsShard> wmsShardCollection) {
        List<WmsShardMapDTO> wmsShardMapDTOList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(wmsShardCollection)) {
            Map<Long, WmsShardMapDTO> map = new HashMap<>();
            Long datasourceGroupId;
            for(WmsShard wmsShard : wmsShardCollection) {
                WmsShardMapDTO wmsShardMapDto = map.get(datasourceGroupId = wmsShard.getDatasourceGroupId());
                if(wmsShardMapDto == null) {
                    wmsShardMapDto = new WmsShardMapDTO();
                    map.put(datasourceGroupId, wmsShardMapDto);
                    wmsShardMapDTOList.add(wmsShardMapDto);
                    wmsShardMapDto.setDatasourceGroup(DatasourceGroupConverter.toDTO(wmsShard.getDatasourceGroup()));
                }
                wmsShardMapDto.addWarehouseId(wmsShard.getWarehouseId());
            }
        }
        return wmsShardMapDTOList;
    }

    public static WmsShardQuery requestToQuery(PageWmsShardRequest request) {
        return request == null ? null : BeanUtilsEx.copyProperties(request, () -> new WmsShardQuery());
    }

}
