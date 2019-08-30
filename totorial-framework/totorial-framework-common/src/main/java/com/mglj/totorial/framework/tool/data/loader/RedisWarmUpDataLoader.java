package com.mglj.totorial.framework.tool.data.loader;

import com.mglj.totorial.framework.tool.context.ApplicationContext;
import com.mglj.totorial.framework.tool.data.redis.StringOperationTemplate;
import com.mglj.totorial.framework.tool.moduling.BasicModule;
import com.mglj.totorial.framework.tool.moduling.ModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Redis数据预热加载
 *
 * Created by zsp on 2018/8/21.
 */
public class RedisWarmUpDataLoader extends BasicModule implements DataLoader, InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(RedisWarmUpDataLoader.class);

    protected final String dataTag;

    @Resource(name = "dataLoadStateStringTemplate")
    protected StringOperationTemplate<String, Integer> dataLoadStateStringTemplate;
    @Autowired
    protected ApplicationContext context;
    @Resource(name = "redisDataLoaderModuleManager")
    protected ModuleManager moduleManager;

    private final static int loadingFlag = 1;

    protected RedisWarmUpDataLoader(String dataTag) {
        super("redis数据预热加载[" + dataTag + "]");
        Objects.requireNonNull(dataTag, "The dataTag is null.");
        this.dataTag = dataTag;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.moduleManager.addModule(this);
    }

    @Override
    protected void doInitialize() {
        load();
    }

    @Override
    public void load() {
        boolean flag = false;
        try{
            if(dataLoadStateStringTemplate.setIfAbsent(dataTag, loadingFlag)) {
                flag = true;
                dataLoadStateStringTemplate.expire(dataTag, 600);
                loadData();
            }
        } catch (Exception e){
            logger.error("加载redis预热数据[" + dataTag + "]时发生异常", e);
            unload();
        } finally {
            if(flag) {
                try {
                    dataLoadStateStringTemplate.delete(dataTag);
                } catch (Exception e) {
                    logger.error("删除redis预热数据[" + dataTag + "]的加载状态时发生异常", e);
                }
            }
        }
    }

    @Override
    public void unload() {
        try{
            unloadData();
        } catch (Exception e){
            logger.error("卸载redis预热数据[" + dataTag + "]时发生异常", e);
        }
    }

    @Override
    public void reload() {
        unload();
        load();
    }

    protected void loadData(){
        //do nothing
    }

    protected void unloadData(){
        //do nothing
    }

}
