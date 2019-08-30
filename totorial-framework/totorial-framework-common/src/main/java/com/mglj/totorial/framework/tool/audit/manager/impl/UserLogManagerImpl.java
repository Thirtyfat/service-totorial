package com.mglj.totorial.framework.tool.audit.manager.impl;

import com.mglj.totorial.framework.tool.audit.domain.UserLog;
import com.mglj.totorial.framework.tool.audit.domain.po.UserLogPO;
import com.mglj.totorial.framework.tool.audit.domain.query.UserLogQuery;
import com.mglj.totorial.framework.tool.audit.manager.api.UserLogManager;
import com.mglj.totorial.framework.tool.beans.BeanUtilsEx;
import com.mglj.totorial.framework.tool.concurrent.threading.ExecutorServiceFactory;
import com.mglj.totorial.framework.tool.concurrent.threading.FixedExecutorServiceFactory;
import com.mglj.totorial.framework.tool.dao.api.OperationDao;
import com.mglj.totorial.framework.tool.dao.api.UserLogDao;
import com.mglj.totorial.framework.tool.metadata.domain.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by zsp on 2018/9/3.
 */
public class UserLogManagerImpl implements UserLogManager, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(UserLogManagerImpl.class);

    private BlockingQueue<UserLog> queue;
    private List<UserLog> tempList;
    private ExecutorServiceFactory executorServiceFactory;
    private ExecutorService threadPool;
    private final static int POLL_TIME_OUT = 5; //秒
    private final static int OFFER_TIME_OUT = 100; //毫秒
    private final static int MAX_LOOP_SIZE = 20;
    private final static int MAX_LOOP_MILLIS = 10000; //10秒

    private UserLogDao userLogDao;
    @Autowired
    public void setUserLogDao(UserLogDao userLogDao) {
        this.userLogDao = userLogDao;
    }
    @Autowired
    private OperationDao operationDao;
    @Override
    public void afterPropertiesSet() throws Exception {
        queue = new LinkedBlockingQueue<>(1024);
        tempList = new ArrayList<>();
        executorServiceFactory = new FixedExecutorServiceFactory("user-log",
                true, 1, 16);
        threadPool = executorServiceFactory.get();
        threadPool.execute(new Worker());
    }

    @Override
    public void destroy() throws Exception {
        executorServiceFactory.destroy();
    }

    class Worker implements Runnable {
        @Override
        public void run() {
            try {
                long time = System.currentTimeMillis();
                for(;;) {
                    UserLog userLog = queue.poll(POLL_TIME_OUT, TimeUnit.SECONDS);
                    if(userLog != null) {
                        tempList.add(userLog);
                    }
                    if(tempList.size() >= MAX_LOOP_SIZE
                            || (System.currentTimeMillis() - time) > MAX_LOOP_MILLIS) {
                        save();
                        time = System.currentTimeMillis();
                    }
                }
            } catch (InterruptedException e) {
                save();
            }
        }
    }

    private void save() {
        if(tempList.size() > 0) {
            int loop = 0;
            for(;;) {
                try {
                    if(loop > 3) {
                        break;
                    }
                    userLogDao.saveAllUserLog(tempList);
                    break;
                } catch(Throwable t) {
                    try {
                        logger.error("记录审计日志时异常", t);
                        Thread.sleep(1000);
                        loop++;
                    } catch(Throwable t2) {
                        t2.printStackTrace();
                    }
                }
            }
            tempList.clear();
        }
    }

    @Override
    public void saveUserLog(UserLog userLog) {
        try {
            queue.offer(userLog, OFFER_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<UserLog> listUserLog(UserLogQuery query) {
        List<UserLogPO> userLogPOList = userLogDao.listUserLog(query);
        if(CollectionUtils.isEmpty(userLogPOList)) {
            return new ArrayList<>();
        }
        List<UserLog> userLogList = BeanUtilsEx.copyPropertiesForNewList(userLogPOList, () -> new UserLog());
        List<String> collect = userLogList.stream().map(e -> {
            return e.getOperationHashCode().toString().concat(",").concat(e.getOperationOffset().toString());
        }).distinct().collect(Collectors.toList());
        List<Operation> operationList=new ArrayList<>();
        for (String str:collect) {
            Operation operation = new Operation();
            operation.setHashCode(Integer.valueOf(str.substring(0,str.indexOf(","))));
            operation.setOffset(Integer.valueOf(str.substring(str.indexOf(",") + 1)));
            operationList.add(operation);
        }
        List<Operation> operationListByHashCodeAndOffset = operationDao.getOperationListByHashCodeAndOffset(operationList);
        for (Operation operation: operationListByHashCodeAndOffset) {
            for (UserLog userlog: userLogList) {
                 if(Objects.equals(userlog.getOperationHashCode(),operation.getHashCode())&&Objects.equals(userlog.getOperationOffset(),operation.getOffset())){
                     userlog.setTitle(operation.getTitle());
                     userlog.setGroup(operation.getGroup());
                }
            }
        }
        return userLogList;
    }

    @Override
    public int countUserLog(UserLogQuery query) {
        return userLogDao.countUserLog(query);
    }

}
