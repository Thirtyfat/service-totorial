package com.mglj.shards.service.facade.impl;

import com.mglj.shards.service.api.dto.*;
import com.mglj.shards.service.api.enums.DatasourceGroupStatusEnum;
import com.mglj.shards.service.domain.converter.CriteriaConfigConverter;
import com.mglj.shards.service.domain.converter.DatasourceConverter;
import com.mglj.shards.service.domain.converter.DatasourceGroupConverter;
import com.mglj.shards.service.domain.query.DatasourceGroupQuery;
import com.mglj.shards.service.domain.query.DatasourceQuery;
import com.mglj.shards.service.domain.request.PageDatasourceGroupRequest;
import com.mglj.shards.service.domain.request.PageDatasourceRequest;
import com.mglj.shards.service.domain.request.SaveDatasourceGroupRequest;
import com.mglj.shards.service.service.api.DatasourceService;
import com.mglj.totorial.framework.common.lang.Page;
import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.common.validation.AlertMessage;
import com.mglj.totorial.framework.common.validation.Create;
import com.mglj.totorial.framework.common.validation.Update;
import com.mglj.totorial.framework.common.validation.Validatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by zsp on 2019/3/8.
 */
@RestController
@RequestMapping("/shard/datasource/manage")
public class DatasourceManageControllerImpl {

    @Autowired
    private DatasourceService datasourceService;

    @PostMapping("criteria/list")
    public Result<List<CriteriaConfigDTO>> listCriteriaConfig() {
        return Result.result(CriteriaConfigConverter.toDTO(datasourceService.listCriteriaConfig()));
    }

    @Validatable(groups = { Create.class })
    @PostMapping("save")
    public Result<?> saveDatasource(@RequestBody DatasourceDTO datasourceDTO){
        return datasourceService.saveDatasource(DatasourceConverter.fromDTO(datasourceDTO));
    }

    @Validatable(groups = { Update.class })
    @PostMapping("update")
    public Result<?> updateDatasource(@RequestBody DatasourceDTO datasourceDTO){
        return datasourceService.updateDatasource(DatasourceConverter.fromDTO(datasourceDTO));
    }

    @Validatable
    @PostMapping("get-properties")
    public Result<Map<String, Object>> getDatasourceProperties(@RequestBody
                                                                   @NotNull(message = "datasourceIdRequired")
                                                                   @AlertMessage(code = "datasourceIdRequired", msg = "ID不能为空")
                                                                           Long datasourceId){
        return Result.result(datasourceService.getDatasourceProperties(datasourceId));
    }

    @Validatable
    @PostMapping("delete")
    public Result<?> deleteDatasource(@RequestBody
                                          @NotNull(message = "datasourceIdRequired")
                                          @AlertMessage(code = "datasourceIdRequired", msg = "ID不能为空")
                                                  Long datasourceId){
        return datasourceService.deleteDatasource(datasourceId);
    }

    @PostMapping("head")
    public Result<List<DatasourceHeadDTO>> headDatasource(@RequestBody PageDatasourceRequest request) {
        DatasourceQuery query = DatasourceConverter.requestToQuery(request);
        return Result.result(datasourceService.headDatasource(query));
    }

    @PostMapping("page")
    public Result<Page<DatasourceDTO>> pageDatasource(@RequestBody PageDatasourceRequest request){
        DatasourceQuery query = DatasourceConverter.requestToQuery(request);
        List<DatasourceDTO> datasourceDTOList = DatasourceConverter.toDTO(datasourceService.listDatasource(query));
        int count = datasourceService.countDatasource(query);
        return Result.result(new Page(datasourceDTOList, count));
    }

    @Validatable
    @PostMapping("test")
    public Result<?> testDatasource(@RequestBody
                                      @NotNull(message = "datasourceIdRequired")
                                      @AlertMessage(code = "datasourceIdRequired", msg = "ID不能为空")
                                              Long datasourceId){
        return datasourceService.testDatasource(datasourceId);
    }

    @Validatable(groups = { Create.class })
    @PostMapping("/group/save")
    public Result<?> saveDatasourceGroup(@RequestBody SaveDatasourceGroupRequest request){
        if(CollectionUtils.isEmpty(request.getDatasourceIdList())) {
            return Result.errorWithMsg(Result.BAD_REQUEST, "数据源不能为空");
        }
        return datasourceService.saveDatasourceGroup(DatasourceGroupConverter.fromRequest(request));
    }

    @Validatable(groups = { Update.class })
    @PostMapping("/group/update")
    public Result<?> updateDatasourceGroup(@RequestBody SaveDatasourceGroupRequest request){
        if(CollectionUtils.isEmpty(request.getDatasourceIdList())) {
            return Result.errorWithMsg(Result.BAD_REQUEST, "数据源不能为空");
        }
        return datasourceService.updateDatasourceGroup(DatasourceGroupConverter.fromRequest(request));
    }

    @Validatable
    @PostMapping("/group/enable")
    public Result<?> updateDatasourceGroupForEnable(@RequestBody
                                                        @NotNull(message = "datasourceGroupIdRequired")
                                                        @AlertMessage(code = "datasourceGroupIdRequired", msg = "ID不能为空")
                                                                Long datasourceGroupId){
        return datasourceService.updateDatasourceGroupStatus(datasourceGroupId, DatasourceGroupStatusEnum.ENABLE);
    }

    @Validatable
    @PostMapping("/group/disable")
    public Result<?> updateDatasourceGroupForDisable(@RequestBody
                                                    @NotNull(message = "datasourceGroupIdRequired")
                                                    @AlertMessage(code = "datasourceGroupIdRequired", msg = "ID不能为空")
                                                            Long datasourceGroupId){
        return datasourceService.updateDatasourceGroupStatus(datasourceGroupId, DatasourceGroupStatusEnum.DISABLE);
    }

    @Validatable
    @PostMapping("/group/delete")
    public Result<?> deleteDatasourceGroup(@RequestBody
                                                     @NotNull(message = "datasourceGroupIdRequired")
                                                     @AlertMessage(code = "datasourceGroupIdRequired", msg = "ID不能为空")
                                                             Long datasourceGroupId){
        return datasourceService.deleteDatasourceGroup(datasourceGroupId);
    }

    @PostMapping("/group/head/enable")
    public Result<List<DatasourceGroupHeadDTO>> headEnabledDatasourceGroup() {
        return Result.result(datasourceService.headEnabledDatasourceGroup());
    }

    @PostMapping("/group/page")
    public Result<Page<DatasourceGroupDTO>> pageDatasourceGroup(@RequestBody PageDatasourceGroupRequest request){
        DatasourceGroupQuery query = DatasourceGroupConverter.requestToQuery(request);
        List<DatasourceGroupDTO> datasourceGroupDTOList = DatasourceGroupConverter.toDTO(datasourceService.listDatasourceGroup(query));
        int count = datasourceService.countDatasourceGroup(query);
        return Result.result(new Page(datasourceGroupDTOList, count));
    }

}
