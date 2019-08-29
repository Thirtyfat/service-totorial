package com.mglj.totorial.framework.gid.service.api;

/**
 * 全局ID生成服务。
 * Created by yj on 2019/8/29.
 **/
public interface GidService {

    /**
     * 生成一个ID。
     *
     * @return 返回一个全局唯一的ID
     */
    long generate();
}
