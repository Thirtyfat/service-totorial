package com.mglj.shards.service.manager.impl;

import com.mglj.shards.service.dao.api.WmsShardDao;
import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.shards.service.domain.WmsShard;
import com.mglj.shards.service.domain.converter.WmsShardConverter;
import com.mglj.shards.service.domain.query.WmsShardQuery;
import com.mglj.shards.service.manager.api.DatasourceManager;
import com.mglj.shards.service.manager.api.WmsShardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zsp on 2019/3/11.
 */
@Service
public class WmsShardManagerImpl implements WmsShardManager {

    @Autowired
    private WmsShardDao wmsShardDao;
    @Autowired
    private DatasourceManager datasourceManager;

    @Override
    public int saveShard(Long warehouseId, Long datasourceGroupId) {
        return wmsShardDao.saveShard(warehouseId, datasourceGroupId);
    }

    @Override
    public int updateShard(Long warehouseId, Long datasourceGroupId) {
        return wmsShardDao.updateShard(warehouseId, datasourceGroupId);
    }

    @Override
    public int deleteShard(Long warehouseId) {
        return wmsShardDao.deleteShard(warehouseId);
    }

    @Override
    public DatasourceGroup getShard(Long warehouseId) {
        Long datasourceGroupId = wmsShardDao.getShard(warehouseId);
        if(datasourceGroupId != null) {
            return datasourceManager.getDatasourceGroup(datasourceGroupId);
        }
        return null;
    }

    @Override
    public List<WmsShard> listShard(WmsShardQuery query) {
        List<WmsShard> wmsShardList = WmsShardConverter.fromPO(wmsShardDao.listShard(query));
        build(wmsShardList, false);
        return wmsShardList;
    }

    @Override
    public int countShard(WmsShardQuery query) {
        return wmsShardDao.countShard(query);
    }

    @Override
    public List<WmsShard> listAllShard() {
        List<WmsShard> wmsShardList = WmsShardConverter.fromPO(wmsShardDao.listAllShard());
        build(wmsShardList, true);
        return wmsShardList;
    }

    @Override
    public List<Long> listWarehouseId(Long datasourceGroupId) {
        return wmsShardDao.listWarehouseId(datasourceGroupId);
    }

    @Override
    public List<Long> listAllWarehouseId() {
        return wmsShardDao.listAllWarehouseId();
    }

    private void build(List<WmsShard> wmsShardList, boolean withDatasource) {
        if(!CollectionUtils.isEmpty(wmsShardList)) {
            Set<Long> datasourceGroupIdSet = wmsShardList.stream()
                    .map(WmsShard::getDatasourceGroupId).collect(Collectors.toSet());
            List<DatasourceGroup> datasourceGroupList = datasourceManager.listDatasourceGroupByIds(datasourceGroupIdSet,
                    withDatasource);
            if(!CollectionUtils.isEmpty(datasourceGroupList)) {
                Map<Long, DatasourceGroup> datasourceGroupMap = datasourceGroupList.stream()
                        .collect(Collectors.toMap(DatasourceGroup::getDatasourceGroupId, e -> e));
                for(WmsShard wmsShard : wmsShardList) {
                    wmsShard.setDatasourceGroup(datasourceGroupMap.get(wmsShard.getDatasourceGroupId()));
                }
            }
        }
    }

}
