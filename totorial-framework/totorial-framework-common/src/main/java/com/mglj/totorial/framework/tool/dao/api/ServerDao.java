package com.mglj.totorial.framework.tool.dao.api;


import com.mglj.totorial.framework.tool.coordinate.domain.Server;

import java.util.Collection;
import java.util.List;

/**
 * Created by zsp on 2018/7/11.
 */
public interface ServerDao {

    /**
     *
     * @param serverCollection
     * @return
     */
    int saveAllServer(Collection<Server> serverCollection);

    /**
     *
     * @param sequence
     * @param name
     * @param ip
     * @param port
     * @return
     */
    int updateEmptyServer(Integer sequence, String name, String ip, Integer port);

    /**
     * 根据ip, 端口获取服务实例的顺序号
     *
     * @param ip
     * @param port
     * @return
     */
    Integer getSequenceByIpAndPort(String ip, Integer port);

    /**
     *
     * @param size
     * @return
     */
    List<Server> listEmptyServer(int size);

}
