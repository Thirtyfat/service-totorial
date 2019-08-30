package com.mglj.shards.service.service.impl;

import com.mglj.shards.service.aop.Sync;
import com.mglj.shards.service.api.dto.WmsShardChangeDTO;
import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;
import com.mglj.shards.service.domain.Datasource;
import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.shards.service.domain.SyncMessage;
import com.mglj.shards.service.domain.WmsShard;
import com.mglj.shards.service.domain.builder.SyncMessageBuilder;
import com.mglj.shards.service.domain.query.WmsShardQuery;
import com.mglj.shards.service.manager.api.DatasourceManager;
import com.mglj.shards.service.manager.api.WmsShardManager;
import com.mglj.shards.service.service.api.DatasourceEventHandler;
import com.mglj.shards.service.service.api.DatasourcePredicator;
import com.mglj.shards.service.service.api.SyncService;
import com.mglj.shards.service.service.api.WmsShardService;
import com.mglj.shards.service.service.impl.builder.WmsShardChangeDTOBuilder;
import com.mglj.totorial.framework.common.enums.DataSourceRoleEnum;
import com.mglj.totorial.framework.common.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mglj.shards.service.api.DatasourceConstants.ACTION_DATASOURCE_GROUP_MEMBER_MODIFICATION;
import static com.mglj.shards.service.api.DatasourceConstants.ACTION_DATASOURCE_PROPERTY_MODIFICATION;
import static com.mglj.shards.service.api.WmsShardConstants.ACTION_WMS_SHARD_ADDITION;
import static com.mglj.shards.service.api.WmsShardConstants.ACTION_WMS_SHARD_DELETION;
import static com.mglj.shards.service.api.WmsShardConstants.ACTION_WMS_SHARD_MODIFICATION;


/**
 * Created by zsp on 2019/3/11.
 */
@Service
public class WmsShardServiceImpl implements WmsShardService,
        DatasourcePredicator, DatasourceEventHandler {

    @Autowired
    private WmsShardManager wmsShardManager;
    @Autowired
    private DatasourceManager datasourceManager;
    @Autowired
    private SyncService syncService;
    @Autowired
    private Map<Integer, WmsShardChangeDTOBuilder> wmsShardChangeDTOBuilderMap;

    @Sync
    @Transactional
    @Override
    public Result<?> saveShard(Long warehouseId, String warehouseName, Long datasourceGroupId) {
        Result<?> result = validateDatasourceGroup(datasourceGroupId);
        if (!result.wasOk()) {
            return result;
        }
        try {
            wmsShardManager.saveShard(warehouseId, datasourceGroupId);

            syncService.publishSyncMessage(new SyncMessageBuilder()
                    .setAction(ACTION_WMS_SHARD_ADDITION)
                    .setRemark("保存WMS分片：仓库=" + warehouseName + ", 仓库ID=" + warehouseId + " 数据源组=" + datasourceGroupId)
                    .addItem(warehouseId)
                    .get());
        } catch (DuplicateKeyException e) {
            return Result.errorWithMsg(Result.CONFLICT, "仓库" + warehouseName + "分片已存在");
        }

        return Result.result();
    }

    @Sync
    @Transactional
    @Override
    public Result<?> updateShard(Long warehouseId, Long datasourceGroupId) {
        Result<?> result = validateDatasourceGroup(datasourceGroupId);
        if(!result.wasOk()) {
            return result;
        }
        wmsShardManager.updateShard(warehouseId, datasourceGroupId);

        syncService.publishSyncMessage(new SyncMessageBuilder()
                .setAction(ACTION_WMS_SHARD_MODIFICATION)
                .setRemark("更新WMS分片：仓库=" + warehouseId + ", 数据源组=" + datasourceGroupId)
                .addItem(warehouseId)
                .get());

        return Result.result();
    }

    private Result<?> validateDatasourceGroup(Long datasourceGroupId) {
        DatasourceGroup datasourceGroup = datasourceManager.getDatasourceGroup(datasourceGroupId);
        if(datasourceGroup == null) {
            return Result.errorWithMsg(Result.BAD_REQUEST,
                    "数据源分组" + datasourceGroupId + "不存在");
        }
        if(DatasourceGroupStatusEnum.DISABLE.equals(datasourceGroup.getStatus())) {
            return Result.errorWithMsg(Result.BAD_REQUEST,
                    "数据源分组" + datasourceGroup.getName() + "已停用，不能作为数据分片");
        }
        List<Datasource> datasourceList = datasourceGroup.getDatasourceList();
        if(CollectionUtils.isEmpty(datasourceList)) {
            return Result.errorWithMsg(Result.BAD_REQUEST,
                    "数据源分组" + datasourceGroup.getName() + "未定义数据源，不能作为数据分片");
        }
        if(!datasourceList.stream().anyMatch(
                e -> Objects.equals(DataSourceRoleEnum.WRITABLE, e.getType()))) {
            return Result.errorWithMsg(Result.BAD_REQUEST,
                    "数据源分组" + datasourceGroup.getName() + "未定义写库，不能作为数据分片");
        }
        return Result.result();
    }

    @Sync
    @Transactional
    @Override
    public void deleteShard(Long warehouseId) {
        wmsShardManager.deleteShard(warehouseId);

        syncService.publishSyncMessage(new SyncMessageBuilder()
                .setAction(ACTION_WMS_SHARD_DELETION)
                .setRemark("删除WMS分片：仓库=" + warehouseId)
                .addItem(warehouseId)
                .get());
    }

    @Override
    public DatasourceGroup getShard(Long warehouseId) {
        DatasourceGroup datasourceGroup = wmsShardManager.getShard(warehouseId);
        if(datasourceGroup != null) {
            Map<String, Object> globalProperties = datasourceManager.listGlobalProperty();
            setGlobalProperyIfAbsent(globalProperties, datasourceGroup);
        }
        return datasourceGroup;
    }

    @Override
    public List<WmsShard> listShard(WmsShardQuery query) {
        return wmsShardManager.listShard(query);
    }

    @Override
    public int countShard(WmsShardQuery query) {
        return wmsShardManager.countShard(query);
    }

    @Override
    public List<WmsShard> listAllShard() {
        List<WmsShard> wmsShardList =  wmsShardManager.listAllShard();
        if(!CollectionUtils.isEmpty(wmsShardList)) {
            Map<String, Object> globalProperties = datasourceManager.listGlobalProperty();
            for (WmsShard wmsShard : wmsShardList) {
                setGlobalProperyIfAbsent(globalProperties, wmsShard.getDatasourceGroup());
            }
        }
        return wmsShardList;
    }

    @Override
    public WmsShardChangeDTO getLatestWmsShard(Long syncMessageId) {
        SyncMessage syncMessage = syncService.getSyncMessage(syncMessageId);
        return wmsShardChangeDTOBuilderMap.get(syncMessage.getAction()).build(syncMessage);
    }

    @Override
    public List<Long> listAllWarehouseId() {
        return wmsShardManager.listAllWarehouseId();
    }

    private void setGlobalProperyIfAbsent(Map<String, Object> globalProperties, DatasourceGroup datasourceGroup) {
        List<Datasource> datasourceList = datasourceGroup.getDatasourceList();
        Map<String, Object> properties;
        for(Datasource datasource : datasourceList) {
            properties = datasource.getProperties();
            for(Map.Entry<String, Object> globalPropery : globalProperties.entrySet()) {
                if(!properties.containsKey(globalPropery.getKey())) {
                    properties.put(globalPropery.getKey(), globalPropery.getValue());
                }
            }
        }
    }

    @Override
    public Result<?> isDatasourceDeletionAllowed(Datasource datasource) {
        // WMS分片：仓库与数据源组关联，不会直接与数据源关联，
        // 因此数据源的删除联级判断由数据源组来决定，此处不做任何判断
        return Result.result();
    }

    @Override
    public Result<?> isDatasourceGroupStatusChangedAllowed(DatasourceGroup datasourceGroup,
                                                           DatasourceGroupStatusEnum status) {
        if(Objects.equals(DatasourceGroupStatusEnum.DISABLE, status)
                && isDatasourceGroupAssigned(datasourceGroup.getDatasourceGroupId())) {
            return Result.errorWithMsg(Result.ERROR_STATUS,
                    "数据源组" + datasourceGroup.getName() + "已被一个或多个仓库分配，不允许停用");
        }
        return Result.result();
    }

    @Override
    public Result<?> isDatasourceGroupDeletionAllowed(DatasourceGroup datasourceGroup) {
        if(isDatasourceGroupAssigned(datasourceGroup.getDatasourceGroupId())) {
            return Result.errorWithMsg(Result.ERROR_STATUS,
                    "数据源组" + datasourceGroup.getName() + "已被一个或多个仓库分配，不允许删除");
        }
        return Result.result();
    }

    @Override
    public void handleDatasourcePropertyModification(Datasource datasource,
                                                     Map<String, Object> addOrModifyProperties,
                                                     Map<String, Object> removeProperties) {
        Long datasourceId = datasource.getDatasourceId();
        if(!isDatasourceAssigned(datasourceId)) {
            return;
        }
        SyncMessage syncMessage = new SyncMessageBuilder()
                .setAction(ACTION_DATASOURCE_PROPERTY_MODIFICATION)
                .addItem(datasourceId)
                .get();
        StringBuilder builder = new StringBuilder(512);
        builder.append("更新数据源" + datasource.getName() + "的属性");
        if(!CollectionUtils.isEmpty(addOrModifyProperties)) {
            appendRemark(builder, addOrModifyProperties, "添加");
        }
        if(!CollectionUtils.isEmpty(removeProperties)) {
            appendRemark(builder, removeProperties, "删除");
        }
        syncMessage.setRemark(builder.toString());
        syncService.publishSyncMessage(syncMessage);
    }

    @Override
    public void handleDatasourceGroupDatasourceModification(DatasourceGroup datasourceGroup,
                                                            List<Datasource> addDatasourceList,
                                                            List<Datasource> removeDatasourceList) {
        Long datasourceGroupId = datasourceGroup.getDatasourceGroupId();
        if(!isDatasourceGroupAssigned(datasourceGroupId)) {
            return;
        }
        SyncMessage syncMessage = new SyncMessageBuilder()
                .setAction(ACTION_DATASOURCE_GROUP_MEMBER_MODIFICATION)
                .addItem(datasourceGroupId)
                .get();
        StringBuilder builder = new StringBuilder(512);
        builder.append("更新组" + datasourceGroup.getName() + "的数据源集合");
        if(!CollectionUtils.isEmpty(addDatasourceList)) {
            appendRemark(builder, addDatasourceList, "添加");
        }
        if(!CollectionUtils.isEmpty(removeDatasourceList)) {
            appendRemark(builder, removeDatasourceList, "删除");
        }
        syncMessage.setRemark(builder.toString());
        syncService.publishSyncMessage(syncMessage);
    }

    private boolean isDatasourceGroupAssigned(Long datasourceGroupId) {
        return !CollectionUtils.isEmpty(wmsShardManager.listWarehouseId(datasourceGroupId));
    }

    private boolean isDatasourceAssigned(Long datasourceId) {
        List<Long> datasourceGroupIdList = datasourceManager.listDatasourceGroupId(datasourceId);
        return !CollectionUtils.isEmpty(datasourceGroupIdList)
                && datasourceGroupIdList.stream().anyMatch(
                        e -> !CollectionUtils.isEmpty(wmsShardManager.listWarehouseId(e)));
    }

    private void appendRemark(StringBuilder builder, Map<String, Object> properties, String action) {
        if(!CollectionUtils.isEmpty(properties)) {
            builder.append(", ").append(action).append("的属性[");
            Iterator<String> it = properties.keySet().iterator();
            for(int i = 0, len = properties.size(), mask = len - 1; i < len; i++) {
                builder.append(it.next());
                if(i < mask) {
                    builder.append(", ");
                }
            }
            builder.append("]");
        }
    }

    private void appendRemark(StringBuilder builder, List<Datasource> datasourceList, String action) {
        if(!CollectionUtils.isEmpty(datasourceList)) {
            builder.append(", ").append(action).append("的数据源[");
            for(int i = 0, len = datasourceList.size(), mask = len - 1; i < len; i++) {
                builder.append(datasourceList.get(i).getName());
                if(i < mask) {
                    builder.append(", ");
                }
            }
            builder.append("]");
        }
    }

}
