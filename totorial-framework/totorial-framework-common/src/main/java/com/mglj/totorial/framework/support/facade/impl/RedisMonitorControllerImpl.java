package com.mglj.totorial.framework.support.facade.impl;

import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.tool.data.redis.tool.Bucket;
import com.mglj.totorial.framework.tool.data.redis.tool.DirectAccessRequest;
import com.mglj.totorial.framework.tool.data.redis.tool.DirectAccessResponse;
import com.mglj.totorial.framework.tool.data.redis.tool.DirectAccessTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zsp on 2018/9/20.
 */
@RestController
public class RedisMonitorControllerImpl {

    @Autowired(required = false)
    private DirectAccessTemplate directAccessTemplate;

    @PostMapping("monitor/redis/delete")
    public Result<Boolean> delete(@RequestBody DirectAccessRequest request) {
        if(!isRedisAvailable()) {
            return Result.errorWithMsg(Result.ERROR_STATUS, "不支持对redis的访问操作");
        }
        return Result.result(directAccessTemplate.delete(request));
    }

    @PostMapping("monitor/redis/keys")
    public Result<Set<String>> getKeys(@RequestBody DirectAccessRequest request) {
        if(!isRedisAvailable()) {
            return Result.errorWithMsg(Result.ERROR_STATUS, "不支持对redis的访问操作");
        }
        return Result.result(directAccessTemplate.getKeys(request));
    }

    @PostMapping("monitor/redis/set")
    public Result<?> setString(@RequestBody DirectAccessRequest request) {
        if(!isRedisAvailable()) {
            return Result.errorWithMsg(Result.ERROR_STATUS, "不支持对redis的访问操作");
        }
        directAccessTemplate.setString(request);
        return Result.result();
    }

    @PostMapping("monitor/redis/get")
    public Result<DirectAccessResponse<String>> getString(@RequestBody DirectAccessRequest request) {
        if(!isRedisAvailable()) {
            return Result.errorWithMsg(Result.ERROR_STATUS, "不支持对redis的访问操作");
        }
        return Result.result(directAccessTemplate.getString(request));
    }

    @PostMapping("monitor/redis/get-bucket")
    public Result<DirectAccessResponse<List<Bucket<String>>>> getStringBucket(
            @RequestBody DirectAccessRequest request) {
        if(!isRedisAvailable()) {
            return Result.errorWithMsg(Result.ERROR_STATUS, "不支持对redis的访问操作");
        }
        return Result.result(directAccessTemplate.getStringBucket(request));
    }

    @PostMapping("monitor/redis/hget-bucket")
    public Result<DirectAccessResponse<List<Bucket<Map<String, String>>>>> getHashBucket(
            @RequestBody DirectAccessRequest request) {
        if(!isRedisAvailable()) {
            return Result.errorWithMsg(Result.ERROR_STATUS, "不支持对redis的访问操作");
        }
        return Result.result(directAccessTemplate.getHashBucket(request));
    }

    private boolean isRedisAvailable() {
        return directAccessTemplate != null;
    }

}
