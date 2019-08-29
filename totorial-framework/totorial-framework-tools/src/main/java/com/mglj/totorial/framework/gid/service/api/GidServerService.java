package com.mglj.totorial.framework.gid.service.api;

/**
 * id生成服务Service
 * Created by yj on 2019/8/29.
 **/
public interface GidServerService {

    /**
     * 根据IP获取服务节点的序列号
     *
     * @param ip
     * @return
     */
    Integer getSequenceOrUpdateEmptyGidServer(String ip);
}
