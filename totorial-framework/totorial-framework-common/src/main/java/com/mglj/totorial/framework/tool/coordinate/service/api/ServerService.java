package com.mglj.totorial.framework.tool.coordinate.service.api;


import com.mglj.totorial.framework.tool.coordinate.domain.Server;

import java.util.List;

/**
 * Created by zsp on 2019/1/22.
 */
public interface ServerService {

    /**
     * 初始化服务节点
     */
    void initServer();

    /**
     * 根据IP和端口获取服务节点的序列号
     *
     * @param name
     * @param ip
     * @param port
     * @return
     */
    Integer getSequenceOrUpdateEmptyServer(String name, String ip, Integer port);

    /**
     * 获取空的(未分配的)服务节点
     *
     * @param size
     * @return
     */
    List<Server> listEmptyServer(int size);

}
