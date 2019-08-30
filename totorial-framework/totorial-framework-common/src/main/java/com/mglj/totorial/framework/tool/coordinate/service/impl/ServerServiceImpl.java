package com.mglj.totorial.framework.tool.coordinate.service.impl;

import com.mglj.totorial.framework.tool.coordinate.domain.Server;
import com.mglj.totorial.framework.tool.coordinate.domain.ServerSequenceConfig;
import com.mglj.totorial.framework.tool.coordinate.service.api.ServerService;
import com.mglj.totorial.framework.tool.dao.api.ServerDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zsp on 2019/1/22.
 */
@Service
public class ServerServiceImpl implements ServerService, InitializingBean {

    private int fetchSize;
    private final SecureRandom random = new SecureRandom();

    @Autowired
    private ServerDao serverDao;

    @Autowired(required = false)
    private ServerSequenceConfig serverSequenceConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        float factor;
        if(serverSequenceConfig == null) {
            factor = ServerSequenceConfig.DEFAULT_EMPTY_SLOT_FETCH_FACTOR;
        } else {
            factor = serverSequenceConfig.getEmptySlotFetchFactor();
        }
        fetchSize = Math.round(Runtime.getRuntime().availableProcessors() * factor);
    }

    @Override
    public void initServer() {
        List<Server> serverList = new ArrayList<>();
        Server server;
        int maxSize =(serverSequenceConfig == null)
                ? 1 << ServerSequenceConfig.DEFAULT_SERVER_SEQUENCE_BITS
                : 1 << serverSequenceConfig.getServerSequenceBits();
        for (int i = 0; i < maxSize; i++) {
            server = new Server();
            server.setName("");
            server.setIp("");
            server.setPort(0);
            server.setSequence(i);
            serverList.add(server);
        }
        serverDao.saveAllServer(serverList);
    }

    @Override
    public Integer getSequenceOrUpdateEmptyServer(String name, String ip, Integer port) {
        Integer sequence = serverDao.getSequenceByIpAndPort(ip, port);
        if (sequence == null) {
            List<Server> emptyServerList;
            int index, affectedRows = 0;
            Set<Integer> indexSet = new HashSet<>();
            Outter: for(int i = 0; i < 3; i++) {
                emptyServerList = serverDao.listEmptyServer(fetchSize);
                for (int j = 0; j < 3; j++) {
                    do {
                        index = random.nextInt(emptyServerList.size() + 1) - 1;
                        if (index < 0) {
                            index = 0;
                        }
                    } while (indexSet.contains(index));
                    indexSet.add(index);
                    sequence = emptyServerList.get(index).getSequence();
                    affectedRows = serverDao.updateEmptyServer(sequence, name, ip, port);
                    if (affectedRows > 0) {
                        break Outter;
                    }
                }
                indexSet.clear();
            }
            if(affectedRows < 1) {
                throw new IllegalStateException("抢占服务节点序号失败");
            }
        }
        return sequence;
    }

    @Override
    public List<Server> listEmptyServer(int size) {
        return serverDao.listEmptyServer(size);
    }

}
