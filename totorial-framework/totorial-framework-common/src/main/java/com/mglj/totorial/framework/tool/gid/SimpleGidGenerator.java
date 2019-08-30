package com.mglj.totorial.framework.tool.gid;

import com.mglj.totorial.framework.tool.context.ApplicationContext;
import com.mglj.totorial.framework.tool.coordinate.Coordinator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zsp on 2018/7/11.
 */
@Service
public class SimpleGidGenerator implements GidGenerator {

    private final ReentrantLock lock = new ReentrantLock();

    private volatile long applicationSequence = -1;
    private volatile long currentMillis = 0;
    private volatile long incr;

    @Autowired
    private Coordinator coordinator;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public long generate() {
        if(applicationSequence == -1) {
            applicationSequence = coordinator.getSequence();
        }
        try{
            //上锁防止单个节点每毫秒的自增数被耗尽而超用
            lock.lock();
            long millis = System.currentTimeMillis();
            if (currentMillis < millis) {
                //更新当前毫秒，并且自增数归零
                currentMillis = millis;
                incr = 0;
            }
            if (incr >= 0x0FFF) {
                incr = 0;
                //如果当前自增数已达最大值，则归零；判断最新毫秒数，是否已流逝到下一毫秒，否则线程自旋等待时间的流逝
                long m = currentMillis;
                long latestMillis = System.currentTimeMillis();
                while (latestMillis <= m) {
                    Thread.yield();
                    latestMillis = System.currentTimeMillis();
                }
                millis = latestMillis;
                currentMillis = millis;
            }
            //41位时间戳 + 10位机器序号 + 12位自增序列
            return ((millis - getOffset()) << 22) | (applicationSequence << 12) | incr++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Long> generate(int size) {
        if(size < 1) {
            throw new IllegalArgumentException("The size cannot be small than 1.");
        }
        List<Long> list = new ArrayList<>();
        for(int i = 0, len = size; i < len; i++) {
            list.add(generate());
        }
        return list;
    }

    private long getOffset() {
        if(applicationContext != null) {
            return applicationContext.getGidTimestampOffset();
        }
        return 0;
    }
}
