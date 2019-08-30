package com.mglj.shards.service.manager.impl;

import com.mglj.shards.service.api.dto.DatasourceGroupHeadDTO;
import com.mglj.shards.service.api.dto.DatasourceHeadDTO;
import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;
import com.mglj.shards.service.dao.api.*;
import com.mglj.shards.service.domain.CriteriaConfig;
import com.mglj.shards.service.domain.Datasource;
import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.shards.service.domain.converter.CriteriaConfigConverter;
import com.mglj.shards.service.domain.converter.DatasourceConverter;
import com.mglj.shards.service.domain.converter.DatasourceGroupConverter;
import com.mglj.shards.service.domain.po.*;
import com.mglj.shards.service.domain.query.DatasourceGroupQuery;
import com.mglj.shards.service.domain.query.DatasourceQuery;
import com.mglj.shards.service.manager.api.DatasourceManager;
import com.mglj.totorial.framework.mintor.PropertyNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zsp on 2019/3/8.
 */
@Service
public class DatasourceManagerImpl implements DatasourceManager {

    private final Logger logger = LoggerFactory.getLogger(DatasourceManagerImpl.class);

    @Autowired
    private CriteriaConfigDao criteriaConfigDao;
    @Autowired
    private DatasourceDao datasourceDao;
    @Autowired
    private DatasourcePropertyDao datasourcePropertyDao;
    @Autowired
    private DatasourceGroupDao datasourceGroupDao;
    @Autowired
    private DatasourceGroupRelationDao datasourceGroupRelationDao;

    @Override
    public Map<String, Object> listGlobalProperty() {
        Map<String, Object> map = new HashMap<>();
        List<CriteriaConfigPO> criteriaConfigPOList = criteriaConfigDao.listCriteriaConfig();
        if(!CollectionUtils.isEmpty(criteriaConfigPOList)) {
            String key;
            Object value;
            for(CriteriaConfigPO criteriaConfigPO : criteriaConfigPOList) {
                key = criteriaConfigPO.getKey();
                try {
                    value = PropertyNames.getStringDeserializer(key).apply(criteriaConfigPO.getValue());
                } catch (Exception e) {
                    logger.error("数据源标准配置错误：key=" + key + ", value=" + criteriaConfigPO.getValue(), e);
                    continue;
                }
                map.put(key, value);
            }
        }

        return map;
    }

    @Override
    public List<CriteriaConfig> listCriteriaConfig() {
        return CriteriaConfigConverter.fromPO(criteriaConfigDao.listCriteriaConfig());
    }

    @Override
    public int saveDatasource(Datasource datasource) {
        DatasourcePO datasourcePO = DatasourceConverter.toPO(datasource);
        datasourceDao.saveDatasource(datasourcePO);
        saveDatasourceProperty(datasourcePO);

        return 1;
    }

    @Override
    public int updateDatasource(Datasource datasource) {
        DatasourcePO datasourcePO = DatasourceConverter.toPO(datasource);
        datasourceDao.updateDatasource(datasourcePO);
        datasourcePropertyDao.deleteDatasourceProperty(datasource.getDatasourceId());
        saveDatasourceProperty(datasourcePO);

        return 1;
    }

    private void saveDatasourceProperty(DatasourcePO datasourcePO) {
        List<DatasourcePropertyPO> propertyList = datasourcePO.getPropertyList();
        if(!CollectionUtils.isEmpty(propertyList)) {
            propertyList.stream().forEach(e -> e.setDatasourceId(datasourcePO.getDatasourceId()));
            datasourcePropertyDao.saveAllDatasourceProperty(propertyList);
        }
    }

    @Override
    public int deleteDatasource(Long datasourceId) {
        datasourceDao.deleteDatasource(datasourceId);
        datasourcePropertyDao.deleteDatasourceProperty(datasourceId);

        return 1;
    }

    @Override
    public boolean isDatasourceNameExisted(String name, Long datasourceId) {
        return datasourceDao.isNameExisted(name, datasourceId);
    }

    @Override
    public boolean areAllDatasourceExist(Collection<Long> datasourceIdCollection) {
        if(CollectionUtils.isEmpty(datasourceIdCollection)) {
            throw new IllegalArgumentException();
        }
        return Objects.equals(datasourceDao.countDatasourceExist(datasourceIdCollection),
                datasourceIdCollection.size());
    }

    @Override
    public Datasource getDatasource(Long datasourceId) {
        DatasourcePO datasourcePO = datasourceDao.getDatasource(datasourceId);
        if(datasourcePO != null) {
            datasourcePO.setPropertyList(datasourcePropertyDao.listDatasourceProperty(datasourceId));
        }
        return DatasourceConverter.fromPO(datasourcePO);
    }

    @Override
    public Datasource getDatasourceHead(Long datasourceId) {
        return DatasourceConverter.fromPO(datasourceDao.getDatasource(datasourceId));
    }

    @Override
    public Map<String, Object> getDatasourceProperties(Long datasourceId) {
        Map<String, Object> properties = new HashMap<>();
        List<DatasourcePropertyPO> propertyList = datasourcePropertyDao.listDatasourceProperty(datasourceId);
        if(!CollectionUtils.isEmpty(propertyList)) {
            String key;
            Object value;
            for(DatasourcePropertyPO item : propertyList) {
                key = item.getKey();
                try {
                    value = PropertyNames.getStringDeserializer(key).apply(item.getValue());
                } catch (Exception e) {
                    logger.error("数据源" + datasourceId + "配置错误：key=" + key + ", value=" + item.getValue(), e);
                    continue;
                }
                properties.put(key, value);
            }
        }

        return properties;
    }

    @Override
    public List<Datasource> listDatasourceByIds(Collection<Long> datasourceIdCollection) {
        return listDatasourceByIds0(datasourceIdCollection, true);
    }

    private List<Datasource> listDatasourceByIds0(Collection<Long> datasourceIdCollection, boolean withProperties) {
        List<Datasource> datasourceList = DatasourceConverter.fromPO(
                datasourceDao.listDatasourceByIds(datasourceIdCollection));
        if(withProperties && !CollectionUtils.isEmpty(datasourceList)) {
            List<DatasourcePropertyPO> datasourcePropertyPOList
                    = datasourcePropertyDao.listDatasourcePropertyByIds(datasourceIdCollection);
            if(!CollectionUtils.isEmpty(datasourcePropertyPOList)) {
                Map<Long, List<DatasourcePropertyPO>> datasourcePropertyMap = datasourcePropertyPOList.stream()
                        .collect(Collectors.groupingBy(DatasourcePropertyPO::getDatasourceId));
                for (Datasource datasource : datasourceList) {
                    List<DatasourcePropertyPO> subDatasourcePropertyPOList
                            = datasourcePropertyMap.get(datasource.getDatasourceId());
                    String key;
                    Object value;
                    for (DatasourcePropertyPO item : subDatasourcePropertyPOList) {
                        key = item.getKey();
                        try {
                            value = PropertyNames.getStringDeserializer(key).apply(item.getValue());
                        } catch (Exception e) {
                            logger.error("数据源" + datasource.getDatasourceId() + "配置错误：key="
                                    + key + ", value=" + item.getValue(), e);
                            continue;
                        }
                        datasource.addProperty(key, value);
                    }
                }
            }
        }
        return datasourceList;
    }

    @Override
    public List<Datasource> listDatasource(DatasourceQuery query) {
        return DatasourceConverter.fromPO(datasourceDao.listDatasource(query));
    }

    @Override
    public int countDatasource(DatasourceQuery query) {
        return datasourceDao.countDatasource(query);
    }

    @Override
    public List<DatasourceHeadDTO> headDatasource(DatasourceQuery query) {
        return datasourceDao.headDatasource(query);
    }

    @Override
    public boolean isDatasourceRelation(Long datasourceId) {
        return !CollectionUtils.isEmpty(datasourceGroupRelationDao.listDatasourceGroupId(datasourceId));
    }

    @Override
    public List<Long> listDatasourceGroupId(Long datasourceId) {
        return datasourceGroupRelationDao.listDatasourceGroupId(datasourceId);
    }

    @Override
    public int saveDatasourceGroup(DatasourceGroup datasourceGroup) {
        DatasourceGroupPO datasourceGroupPO = DatasourceGroupConverter.toPO(datasourceGroup);
        datasourceGroupDao.saveDatasourceGroup(datasourceGroupPO);
        saveDatasourceGroupRelation(datasourceGroup);

        return 1;
    }

    @Override
    public int updateDatasourceGroup(DatasourceGroup datasourceGroup) {
        DatasourceGroupPO datasourceGroupPO = DatasourceGroupConverter.toPO(datasourceGroup);
        datasourceGroupDao.updateDatasourceGroup(datasourceGroupPO);
        datasourceGroupRelationDao.deleteDatasourceGroupRelationByGroup(datasourceGroup.getDatasourceGroupId());
        saveDatasourceGroupRelation(datasourceGroup);

        return 1;
    }

    private void saveDatasourceGroupRelation(DatasourceGroup datasourceGroup) {
        List<Long> datasourceIdList = datasourceGroup.getDatasourceIdList();
        if(!CollectionUtils.isEmpty(datasourceIdList)) {
            datasourceGroupRelationDao.saveDatasourceGroupRelationByGroup(
                    datasourceGroup.getDatasourceGroupId(),
                    datasourceIdList);
        }
    }

    @Override
    public int updateDatasourceGroupStatus(Long datasourceGroupId, DatasourceGroupStatusEnum status) {
        datasourceGroupDao.updateDatasourceGroupStatus(datasourceGroupId, status);

        return 1;
    }

    @Override
    public int deleteDatasourceGroup(Long datasourceGroupId) {
        datasourceGroupDao.deleteDatasourceGroup(datasourceGroupId);
        datasourceGroupRelationDao.deleteDatasourceGroupRelationByGroup(datasourceGroupId);

        return 1;
    }

    @Override
    public boolean isDatasourceGroupNameExisted(String name, Long datasourceGroupId) {
        return datasourceGroupDao.isNameExisted(name, datasourceGroupId);
    }

    @Override
    public DatasourceGroup getDatasourceGroup(Long datasourceGroupId) {
        DatasourceGroup datasourceGroup = DatasourceGroupConverter.fromPO(
                datasourceGroupDao.getDatasourceGroup(datasourceGroupId));
        List<Long> datasourceIdList = datasourceGroupRelationDao.listDatasourceId(datasourceGroupId);
        if(!CollectionUtils.isEmpty(datasourceIdList)) {
            datasourceGroup.setDatasourceList(listDatasourceByIds0(datasourceIdList, true));
        }
        return datasourceGroup;
    }

    @Override
    public DatasourceGroup getDatasourceGroupHead(Long datasourceGroupId) {
        return DatasourceGroupConverter.fromPO(datasourceGroupDao.getDatasourceGroup(datasourceGroupId));
    }

    @Override
    public List<DatasourceGroup> listDatasourceGroupByIds(Collection<Long> collection, boolean withDatasource) {
        List<DatasourceGroup> datasourceGroupList = DatasourceGroupConverter.fromPO(
                datasourceGroupDao.listDatasourceGroupByIds(collection));
        if(withDatasource) {
            listDatasourceGroupRelation(datasourceGroupList, true);
        }
        return datasourceGroupList;
    }

    @Override
    public List<DatasourceGroup> listDatasourceGroup(DatasourceGroupQuery query) {
        List<DatasourceGroup> datasourceGroupList = DatasourceGroupConverter.fromPO(
                datasourceGroupDao.listDatasourceGroup(query));
        listDatasourceGroupRelation(datasourceGroupList, false);
        return datasourceGroupList;
    }

    @Override
    public int countDatasourceGroup(DatasourceGroupQuery query) {
        return datasourceGroupDao.countDatasourceGroup(query);
    }

    @Override
    public List<DatasourceGroupHeadDTO> headDatasourceGroup(DatasourceGroupQuery query) {
        return datasourceGroupDao.headDatasourceGroup(query);
    }

    private void listDatasourceGroupRelation(List<DatasourceGroup> datasourceGroupList,
                                             boolean withDatasourceProperties) {
        if(!CollectionUtils.isEmpty(datasourceGroupList)) {
            List<DatasourceGroupRelationPO> datasourceGroupRelationPOList
                    = datasourceGroupRelationDao.listDatasourceGroupRelationByGroup(
                            datasourceGroupList.stream().map(DatasourceGroup::getDatasourceGroupId).collect(Collectors.toSet()));
            Map<Long, List<DatasourceGroupRelationPO>> datasourceGroupRelationPOMap = datasourceGroupRelationPOList.stream()
                    .collect(Collectors.groupingBy(DatasourceGroupRelationPO::getDatasourceGroupId));

            Set<Long> datasourceIdSet = datasourceGroupRelationPOList.stream()
                    .map(DatasourceGroupRelationPO::getDatasourceId).collect(Collectors.toSet());
            List<Datasource> datasourceList = listDatasourceByIds0(datasourceIdSet, withDatasourceProperties);
            Map<Long, Datasource> datasourceMap = datasourceList.stream().collect(
                    Collectors.toMap(Datasource::getDatasourceId, e -> e));

            for(DatasourceGroup datasourceGroup : datasourceGroupList) {
                List<DatasourceGroupRelationPO> relationPOList
                        = datasourceGroupRelationPOMap.get(datasourceGroup.getDatasourceGroupId());
                if(!CollectionUtils.isEmpty(relationPOList)) {
                    for (DatasourceGroupRelationPO relationPO : relationPOList) {
                        datasourceGroup.addDatasource(datasourceMap.get(relationPO.getDatasourceId()));
                    }
                }
            }
        }
    }

}
