package com.mglj.shards.service.facade.impl;

import com.mglj.shards.service.api.dto.WmsShardDTO;
import com.mglj.shards.service.domain.converter.WmsShardConverter;
import com.mglj.shards.service.domain.query.WmsShardQuery;
import com.mglj.shards.service.domain.request.PageWmsShardRequest;
import com.mglj.shards.service.domain.request.SaveWmsShardRequest;
import com.mglj.shards.service.service.api.WmsShardService;
import com.mglj.totorial.framework.common.lang.Page;
import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.common.validation.AlertMessage;
import com.mglj.totorial.framework.common.validation.Validatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by zsp on 2019/3/11.
 */
@RestController
@RequestMapping("/shard/wms/manage")
public class WmsShardManageControllerImpl {

    @Autowired
    private WmsShardService wmsShardService;

    @Validatable
    @PostMapping("save")
    public Result<?> saveWmsShard(@RequestBody SaveWmsShardRequest request) {
        List<Long> warehouseIdList = request.getWarehouseIdList();
        List<String> warehouseNameList = request.getWarehouseNameList();
        Result<?> result = null;
        for (int i = 0; i < warehouseIdList.size(); i++) {
            Long warehouseId = warehouseIdList.get(i);
            String warehouseName = warehouseNameList.get(i);
            result = wmsShardService.saveShard(warehouseId, warehouseName, request.getDatasourceGroupId());
            if (!result.wasOk()) {
                break;
            }
        }
        return result;
    }

    @Validatable
    @PostMapping("update")
    public Result<?> updateWmsShard(@RequestBody SaveWmsShardRequest request){
        List<Long> warehouseIdList = request.getWarehouseIdList();

        Result<?> result = null;
        for(Long warehouseId : warehouseIdList) {
            result = wmsShardService.updateShard(warehouseId, request.getDatasourceGroupId());
            if(!result.wasOk()) {
                break;
            }
        }
        return result;
    }

    @Validatable
    @PostMapping("delete")
    public Result<?> deleteWmsShard(@RequestBody
                                        @NotNull(message = "warehouseIdRequired")
                                        @AlertMessage(code = "warehouseIdRequired", msg = "ID不能为空")
                                                Long warehouseId){
        wmsShardService.deleteShard(warehouseId);
        return Result.result();
    }

    @PostMapping("page")
    public Result<Page<WmsShardDTO>> pageWmsShard(@RequestBody PageWmsShardRequest request){
        WmsShardQuery wmsShardQuery = WmsShardConverter.requestToQuery(request);
        List<WmsShardDTO> wmsShardDTOList = WmsShardConverter.toDTO(wmsShardService.listShard(wmsShardQuery));
        int count = wmsShardService.countShard(request);
        return Result.result(new Page(wmsShardDTOList, count));
    }

    @PostMapping("warehouse")
    public Result<List<Long>> listAllWarehouseId(){
        return Result.result(wmsShardService.listAllWarehouseId());
    }

}
