package com.mglj.totorial.framework.tool.context;//package com.yhdx.baseframework.tool.context;
//
//import com.yhdx.baseframework.tool.coordinate.Coordinator;
//import com.yhdx.baseframework.tool.moduling.BasicModule;
//import com.yhdx.baseframework.tool.moduling.ModuleManager;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * Created by zsp on 2018/8/22.
// */
//@Component
//public class ApplicationContextLoader extends BasicModule
//        implements InitializingBean, ContextLoader {
//
//    @Resource(name = "contextLoaderModuleManager")
//    private ModuleManager moduleManager;
//    @Autowired
//    private Coordinator coordinator;
//    @Autowired(required = false)
//    private ApplicationContext applicationContext;
//
//    public ApplicationContextLoader() {
//        super("应用上下文加载");
//    }
//
//    @Override
//    protected void doInitialize() {
//        load();
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        moduleManager.addModule(this);
//    }
//
//    @Override
//    public void load() {
//
//    }
//
//}
