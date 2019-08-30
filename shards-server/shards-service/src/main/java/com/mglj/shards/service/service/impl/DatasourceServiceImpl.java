package com.mglj.shards.service.service.impl;

import com.mglj.shards.service.aop.Sync;
import com.mglj.shards.service.api.dto.DatasourceGroupHeadDTO;
import com.mglj.shards.service.api.dto.DatasourceHeadDTO;
import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;
import com.mglj.shards.service.domain.CriteriaConfig;
import com.mglj.shards.service.domain.Datasource;
import com.mglj.shards.service.domain.DatasourceGroup;
import com.mglj.shards.service.domain.query.DatasourceGroupQuery;
import com.mglj.shards.service.domain.query.DatasourceQuery;
import com.mglj.shards.service.manager.api.DatasourceManager;
import com.mglj.shards.service.service.api.DatasourceEventHandler;
import com.mglj.shards.service.service.api.DatasourcePredicator;
import com.mglj.shards.service.service.api.DatasourceService;
import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.mintor.PropertyNames;
import com.mglj.totorial.framework.tool.gid.GidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mglj.totorial.framework.mintor.PropertyNames.*;


/**
 * Created by zsp on 2019/3/8.
 */
@Service
public class DatasourceServiceImpl implements DatasourceService {

    private final Logger logger = LoggerFactory.getLogger(DatasourceServiceImpl.class);

    @Autowired
    private DatasourceManager datasourceManager;
    @Autowired
    private GidGenerator gidGenerator;

    @Autowired(required = false)
    private List<DatasourcePredicator> datasourcePredicatorList;
    @Autowired(required = false)
    private List<DatasourceEventHandler> datasourceEventHandlerList;

    @Override
    public List<CriteriaConfig> listCriteriaConfig() {
        return datasourceManager.listCriteriaConfig();
    }

    @Transactional
    @Override
    public Result<?> saveDatasource(Datasource datasource) {
        Result<?> result = validateDatasource(datasource);
        if(!result.wasOk()) {
           return result;
        }
        if(datasourceManager.isDatasourceNameExisted(datasource.getName(), null)) {
            return Result.errorWithMsg(Result.CONFLICT, "数据源名" + datasource.getName() + "已存在");
        }
        datasource.setDatasourceId(gidGenerator.generate());
        datasource.setUpdateTime(new Date());
        try {
            datasourceManager.saveDatasource(datasource);
        } catch (DuplicateKeyException e) {
            return Result.errorWithMsg(Result.CONFLICT, "数据源名" + datasource.getName() + "已存在");
        }

        return Result.result();
    }

    @Sync
    @Transactional
    @Override
    public Result<?> updateDatasource(Datasource datasource) {
        Result<?> result = validateDatasource(datasource);
        if(!result.wasOk()) {
            return result;
        }
        Datasource beforeUpdateDatasource = datasourceManager.getDatasource(datasource.getDatasourceId());
        if(beforeUpdateDatasource == null) {
            return Result.errorWithMsg(Result.NOT_FOUND, "数据源" + datasource.getDatasourceId() + "已被删除");
        }

        datasource.setUpdateTime(new Date());
        datasourceManager.updateDatasource(datasource);

        fireDatasourceModificationEvent(beforeUpdateDatasource, datasource);

        return Result.result();
    }

    private Result<?> validateDatasource(Datasource datasource) {
        Map<String, Object> properties = datasource.getProperties();
        if(CollectionUtils.isEmpty(properties)) {
            return Result.errorWithMsg(Result.BAD_REQUEST, "配置属性不能为空");
        }
        if(!PropertyNames.validate(properties.keySet())) {
            return Result.errorWithMsg(Result.BAD_REQUEST, "合法的配置属性包括: "
                    + StringUtils.arrayToDelimitedString(PropertyNames.getKeyNames(), ","));
        }
        if(!properties.containsKey(NAME_URL)
                || !properties.containsKey(NAME_USERNAME)
                || !properties.containsKey(NAME_PASSWORD)) {
            return Result.errorWithMsg(Result.BAD_REQUEST, "配置属性必须包括："
                    + NAME_URL + ", " + NAME_USERNAME + ", " + NAME_PASSWORD);
        }
        return testDatasource0(datasource);
    }

    private void fireDatasourceModificationEvent(Datasource beforeUpdateDatasource,
                                                 Datasource afterUpdateDatasource) {
        if(datasourceEventHandlerList != null) {
            Map<String, Object> beforeUpdateProperties = beforeUpdateDatasource.getProperties();
            Map<String, Object> afterUpdateProperties = afterUpdateDatasource.getProperties();
            Map<String, Object> removeProperties = new HashMap<>();
            if(afterUpdateProperties.size() > 0) {
                String key;
                for (Map.Entry<String, Object> beforeUpdateEntry : beforeUpdateProperties.entrySet()) {
                    key = beforeUpdateEntry.getKey();
                    if (afterUpdateProperties.get(key) == null) {
                        removeProperties.put(key, beforeUpdateEntry.getValue());
                    }
                }
            }
            boolean isPropertyChanged = afterUpdateProperties.size() > 0 || removeProperties.size() > 0;
            for(DatasourceEventHandler datasourceEventHandler : datasourceEventHandlerList) {
                if(isPropertyChanged) {
                    datasourceEventHandler.handleDatasourcePropertyModification(afterUpdateDatasource,
                            afterUpdateProperties, removeProperties);
                }
            }
        }
    }

    @Transactional
    @Override
    public Result<?> deleteDatasource(Long datasourceId) {
        Datasource datasource = datasourceManager.getDatasourceHead(datasourceId);
        if(datasource == null) {
            return Result.errorWithMsg(Result.NOT_FOUND, "数据源" + datasourceId + "已被删除");
        }
        if(datasourceManager.isDatasourceRelation(datasourceId)) {
            return Result.errorWithMsg(Result.ERROR_STATUS,
                    "数据源" + datasource.getName() + "已被组关联，不允许直接删除");
        }
        Result<?> result = validateDatasource(
                predicator -> predicator.isDatasourceDeletionAllowed(datasource));
        if(result.wasOk()) {
            datasourceManager.deleteDatasource(datasourceId);
        }
        return result;
    }

    @Override
    public Map<String, Object> getDatasourceProperties(Long datasourceId) {
        return datasourceManager.getDatasourceProperties(datasourceId);
    }

    @Override
    public List<Datasource> listDatasource(DatasourceQuery query) {
        return datasourceManager.listDatasource(query);
    }

    @Override
    public int countDatasource(DatasourceQuery query) {
        return datasourceManager.countDatasource(query);
    }

    @Override
    public List<DatasourceHeadDTO> headDatasource(DatasourceQuery query) {
        return datasourceManager.headDatasource(query);
    }

    @Override
    public Result<?> testDatasource(Long datasourceId) {
        Datasource datasource = datasourceManager.getDatasource(datasourceId);
        if(datasource == null) {
            return Result.errorWithMsg(Result.BAD_REQUEST, "不存在数据源：" + datasourceId);
        }

        return testDatasource0(datasource);
    }

    private Result<?> testDatasource0(Datasource datasource) {
        Map<String, Object> properties = datasource.getProperties();
        Map<String, Object> globalProperties = datasourceManager.listGlobalProperty();
        String driverClassName = (String)properties.get(NAME_DRIVER_CLASS_NAME);
        if(driverClassName ==  null) {
            driverClassName = (String)globalProperties.get(NAME_DRIVER_CLASS_NAME);
        }
        if(driverClassName == null) {
            return Result.errorWithMsg(Result.ERROR_STATUS,
                    "数据源" + datasource.getName() + "未定义driverClassName");
        }
        String validationQuery = (String)properties.get(NAME_VALIDATION_QUERY);
        if(validationQuery == null) {
            validationQuery = (String)globalProperties.get(NAME_VALIDATION_QUERY);
        }
        if(validationQuery == null) {
            return Result.errorWithMsg(Result.ERROR_STATUS,
                    "数据源" + datasource.getName() + "未定义validationQuery");
        }

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String errorMsg = "";
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection((String)properties.get(NAME_URL),
                    (String)properties.get(NAME_USERNAME), (String)properties.get(NAME_PASSWORD));
            statement = connection.createStatement();
            statement.setQueryTimeout(5);
            resultSet = statement.executeQuery(validationQuery);
            if (resultSet.next()) {
                return Result.result();
            }
            errorMsg = "验证SQL\"" + validationQuery + "\"未返回任何结果";
        } catch (Exception e) {
            logger.error("测试数据源" + datasource.getName() + "异常", e);
            errorMsg = e.getMessage();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("关闭连接异常", e);
                errorMsg = errorMsg + " " + e.getMessage();
            }
        }
        return Result.errorWithMsg(Result.ERROR_STATUS, "连接失败：" + errorMsg);
    }

    @Transactional
    @Override
    public Result<?> saveDatasourceGroup(DatasourceGroup datasourceGroup) {
        Result<?> result = validateDatasourceGroup(datasourceGroup);
        if(!result.wasOk()) {
            return result;
        }
        if(datasourceManager.isDatasourceGroupNameExisted(datasourceGroup.getName(), null)) {
            return Result.errorWithMsg(Result.CONFLICT, "数据源组名" + datasourceGroup.getName() + "已存在");
        }
        datasourceGroup.setDatasourceGroupId(gidGenerator.generate());
        datasourceGroup.setStatus(DatasourceGroupStatusEnum.DISABLE);
        datasourceGroup.setUpdateTime(new Date());
        try {
            datasourceManager.saveDatasourceGroup(datasourceGroup);
        } catch (DuplicateKeyException e) {
            return Result.errorWithMsg(Result.CONFLICT, "数据源组名" + datasourceGroup.getName() + "已存在");
        }

        return Result.result();
    }

    @Sync
    @Transactional
    @Override
    public Result<?> updateDatasourceGroup(DatasourceGroup datasourceGroup) {
        Result<?> result = validateDatasourceGroup(datasourceGroup);
        if(!result.wasOk()) {
            return result;
        }
        DatasourceGroup beforeUpdateDatasourceGroup
                = datasourceManager.getDatasourceGroup(datasourceGroup.getDatasourceGroupId());
        if(beforeUpdateDatasourceGroup == null) {
            return Result.errorWithMsg(Result.NOT_FOUND,
                    "数据源组" + datasourceGroup.getDatasourceGroupId() + "已被删除");
        }

        datasourceGroup.setUpdateTime(new Date());
        datasourceManager.updateDatasourceGroup(datasourceGroup);

        fireDatasourceGroupModificationEvent(beforeUpdateDatasourceGroup, datasourceGroup);

        return Result.result();
    }

    private Result validateDatasourceGroup(DatasourceGroup datasourceGroup) {
        List<Long> datasourceIdList = datasourceGroup.getDatasourceIdList();
        if(!CollectionUtils.isEmpty(datasourceIdList)
                && !datasourceManager.areAllDatasourceExist(datasourceIdList)) {
            return Result.errorWithMsg(Result.BAD_REQUEST, "输入了不存在的数据源");
        }
        return Result.result();
    }

    private void fireDatasourceGroupModificationEvent(DatasourceGroup beforeUpdateDatasourceGroup,
                                                      DatasourceGroup afterUpdateDatasourceGroup) {
        if(datasourceEventHandlerList != null) {
            List<Datasource> beforeUpdateDatasourceList = beforeUpdateDatasourceGroup.getDatasourceList();
            List<Long> afterUpdateDatasourceIdList = afterUpdateDatasourceGroup.getDatasourceIdList();
            Set<Long> afterUpdateDatasourceIdSet = afterUpdateDatasourceIdList.stream().collect(Collectors.toSet());
            List<Datasource> addDatasourceList = new ArrayList<>();
            List<Datasource> removeDatasourceList = new ArrayList<>();
            for(Datasource beforeUpdateDatasource : beforeUpdateDatasourceList) {
                if(afterUpdateDatasourceIdSet.contains(beforeUpdateDatasource.getDatasourceId())) {
                    afterUpdateDatasourceIdSet.remove(beforeUpdateDatasource.getDatasourceId());
                } else {
                    removeDatasourceList.add(beforeUpdateDatasource);
                }
            }
            if(afterUpdateDatasourceIdSet.size() > 0) {
                addDatasourceList.addAll(datasourceManager.listDatasourceByIds(afterUpdateDatasourceIdSet));
            }
            boolean isDatasourceChanged = addDatasourceList.size() > 0 || removeDatasourceList.size() > 0;
            for(DatasourceEventHandler datasourceEventHandler : datasourceEventHandlerList) {
                if(isDatasourceChanged) {
                    datasourceEventHandler.handleDatasourceGroupDatasourceModification(afterUpdateDatasourceGroup,
                            addDatasourceList, removeDatasourceList);
                }
            }
        }
    }

    @Transactional
    @Override
    public Result<?> updateDatasourceGroupStatus(Long datasourceGroupId, DatasourceGroupStatusEnum status) {
        DatasourceGroup datasourceGroup = datasourceManager.getDatasourceGroupHead(datasourceGroupId);
        if(datasourceGroup == null) {
            return Result.errorWithMsg(Result.NOT_FOUND, "数据源组" + datasourceGroupId + "已被删除");
        }
        Result<?> result = validateDatasource(
                predicator -> predicator.isDatasourceGroupStatusChangedAllowed(datasourceGroup, status));
        if(result.wasOk()) {
            datasourceManager.updateDatasourceGroupStatus(datasourceGroupId, status);
        }
        return result;
    }

    @Transactional
    @Override
    public Result<?> deleteDatasourceGroup(Long datasourceGroupId) {
        DatasourceGroup datasourceGroup = datasourceManager.getDatasourceGroupHead(datasourceGroupId);
        if(datasourceGroup == null) {
            return Result.errorWithMsg(Result.NOT_FOUND, "数据源组" + datasourceGroupId + "已被删除");
        }
        Result<?> result = validateDatasource(
                predicator -> predicator.isDatasourceGroupDeletionAllowed(datasourceGroup));
        if(result.wasOk()) {
            datasourceManager.deleteDatasourceGroup(datasourceGroupId);
        }
        return result;
    }

    @Override
    public List<DatasourceGroup> listDatasourceGroup(DatasourceGroupQuery query) {
        return datasourceManager.listDatasourceGroup(query);
    }

    @Override
    public int countDatasourceGroup(DatasourceGroupQuery query) {
        return datasourceManager.countDatasourceGroup(query);
    }

    @Override
    public List<DatasourceGroupHeadDTO> headEnabledDatasourceGroup() {
        DatasourceGroupQuery query = new DatasourceGroupQuery();
        query.setStatus(DatasourceGroupStatusEnum.ENABLE);
        return datasourceManager.headDatasourceGroup(query);
    }

    private Result<?> validateDatasource(Function<DatasourcePredicator, Result<?>> predication) {
        if(datasourcePredicatorList != null) {
            Result<?> result;
            for(DatasourcePredicator datasourcePredicator : datasourcePredicatorList) {
                result = predication.apply(datasourcePredicator);
                if (!result.wasOk()) {
                    return result;
                }
            }
        }
        return Result.result();
    }

}
