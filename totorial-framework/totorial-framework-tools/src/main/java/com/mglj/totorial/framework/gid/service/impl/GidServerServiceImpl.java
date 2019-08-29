package com.mglj.totorial.framework.gid.service.impl;

import com.mglj.totorial.framework.gid.dao.api.GidServerDao;
import com.mglj.totorial.framework.gid.domain.GidServer;
import com.mglj.totorial.framework.gid.service.api.GidServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yj on 2019/8/29.
 **/
@Service
public class GidServerServiceImpl implements GidServerService {

    private final int fetchSize = Runtime.getRuntime().availableProcessors() * 2;
    private final SecureRandom random = new SecureRandom();

    private static final Logger logger = LoggerFactory.getLogger(GidServerServiceImpl.class);

    @Autowired
    private GidServerDao gidServerDao;

    /**
     * 根据IP获取服务节点的序列号
     *
     * @param ip
     * @return
     */
    @Override
    public Integer getSequenceOrUpdateEmptyGidServer(String ip) {
        Integer sequence = gidServerDao.getSequenceByIp(ip);
        if (sequence == null) {
            List<GidServer> emptyServerList;
            int index, affectedRows = 0;
            Set<Integer> indexSet = new HashSet<>();
            Outter: for(int i = 0; i < 3; i++) {
                emptyServerList = gidServerDao.listEmpty(fetchSize);
                for (int j = 0; j < 3; j++) {
                    do {
                        index = random.nextInt(emptyServerList.size() + 1) - 1;
                        if (index < 0) {
                            index = 0;
                        }
                    } while (indexSet.contains(index));
                    indexSet.add(index);
                    logger.info("empty element {} subscript {}" , emptyServerList.get(index),index);
                    sequence = emptyServerList.get(index).getSequence();
                    affectedRows = gidServerDao.updateEmpty(sequence, ip);
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
}
