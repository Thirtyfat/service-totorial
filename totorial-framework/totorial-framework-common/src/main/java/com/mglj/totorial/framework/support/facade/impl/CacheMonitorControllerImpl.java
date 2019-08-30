package com.mglj.totorial.framework.support.facade.impl;

import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.common.validation.Validatable;
import com.mglj.totorial.framework.support.domain.dto.CacheDTO;
import com.mglj.totorial.framework.support.domain.request.CacheRequest;
import com.mglj.totorial.framework.tool.caching.Cache;
import com.mglj.totorial.framework.tool.caching.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsp on 2018/11/6.
 */
@RestController
public class CacheMonitorControllerImpl {

    @Autowired(required = false)
    private CacheManager cacheManager;

    @Validatable
    @PostMapping(value = "/monitor/cache/remove")
    public Result<Boolean> remove(@RequestBody CacheRequest request) {
        if(!isCacheAvailable()) {
            return Result.errorWithMsg(Result.ERROR_STATUS, "不支持缓存操作");
        }
        Cache cache = cacheManager.getCache(request.getName());
        if(cache == null) {
            return Result.errorWithMsg(Result.NOT_FOUND, "未找到缓存");
        }
        if(!StringUtils.hasText(request.getKey())) {
            return Result.errorWithMsg("key-required", "key不能为空");
        }
        return Result.result(cache.remove(request.getKey()));
    }

    @Validatable
    @PostMapping(value = "/monitor/cache/get")
    public Result<Object> get(@RequestBody CacheRequest request) {
        if(!isCacheAvailable()) {
            return Result.errorWithMsg(Result.ERROR_STATUS, "不支持缓存操作");
        }
        Cache cache = cacheManager.getCache(request.getName());
        if(cache == null) {
            return Result.errorWithMsg(Result.NOT_FOUND, "未找到缓存");
        }
        if(!StringUtils.hasText(request.getKey())) {
            return Result.errorWithMsg("key-required", "key不能为空");
        }
        Object result = cache.get(request.getKey());
        if(result != null) {
            if(result instanceof String) {
                return Result.result(result);
            } else {
                return Result.result(String.valueOf(result));
            }
        } else {
            return Result.result();
        }
    }

    @PostMapping(value = "/monitor/cache/ttl")
    public Result<Long> ttl(@RequestBody CacheRequest request) {
        if(!isCacheAvailable()) {
            return Result.errorWithMsg(Result.ERROR_STATUS, "不支持缓存操作");
        }
        Cache cache = cacheManager.getCache(request.getName());
        if(cache == null) {
            return Result.errorWithMsg(Result.NOT_FOUND, "未找到缓存");
        }
        if(!StringUtils.hasText(request.getKey())) {
            return Result.errorWithMsg("key-required", "key不能为空");
        }
        return Result.result(cache.getExpire(request.getKey()));
    }

    @GetMapping(value = "/monitor/cache/list")
    public Result<List<CacheDTO>> list() {
        if(!isCacheAvailable()) {
            return Result.errorWithMsg(Result.ERROR_STATUS, "不支持缓存操作");
        }
        CacheManager cacheManager0 = cacheManager;
        List<CacheDTO> cacheItemList = new ArrayList<CacheDTO>();
        List<Cache> cacheList = cacheManager0.getCacheList();
        if(cacheList != null) {
            for(Cache cache : cacheList) {
                CacheDTO cacheItem = new CacheDTO();
                cacheItem.setName(cache.getName());
                cacheItem.setDescription(cache.getDescription());
                cacheItem.setExpiredSeconds(cache.getExpiredSeconds());
                cacheItem.setHits(cacheManager0.getHits(cache));
                cacheItem.setEmptyHits(cacheManager0.getEmptyHits(cache));
                cacheItem.setMisses(cacheManager0.getMisses(cache));
                cacheItemList.add(cacheItem);
            }
        }
        return Result.result(cacheItemList);
    }

    private boolean isCacheAvailable() {
        return cacheManager != null;
    }

}
