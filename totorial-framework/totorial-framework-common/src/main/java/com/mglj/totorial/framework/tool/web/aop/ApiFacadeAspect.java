//package com.mglj.totorial.framework.tool.web.aop;
//
///**
// * @author yangyang
// * @date 2019/1/16
// */
//
//import com.alipay.lookout.api.Id;
//import com.alipay.lookout.api.Registry;
//import com.google.common.base.Stopwatch;
//import com.yhdx.baseframework.common.exceptions.BaseBizException;
//import com.yhdx.baseframework.common.exceptions.BaseSysException;
//import com.yhdx.baseframework.common.lang.Result;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//import java.util.concurrent.TimeUnit;
//
//@Aspect
//@Order(12)
//@Component
//public class ApiFacadeAspect {
//
//    private final static Logger logger = LoggerFactory.getLogger(ApiFacadeAspect.class);
//
//    @Value("${spring.application.name}")
//    private String applicationName;
//
//    @Autowired
//    private Registry registry;
//
//    @Pointcut("execution(* com.yhdx..*.facade..*.*(..))")
//    public void facade() {
//        // Facade拦截
//    }
//
//    @Around("facade()")
//    public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
//        Result result = null;
//        if (!isResponse(pjp)) {
//            return pjp.proceed();
//        }
//        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
//        String methodName = StringUtils.join(methodSignature.getMethod().getDeclaringClass().getName(), ".", methodSignature.getMethod().getName());
//        try {
//            Stopwatch stopwatch = Stopwatch.createStarted();
//            result = (Result) pjp.proceed();
//            // 接口成功监控
//            String createId = this.getCreateId("ControllerAspectSuccess");
//            Id registryId = registry.createId(createId).withTag("method", methodName);
//            registry.timer(registryId).record(stopwatch.elapsed(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
//        } catch (BaseBizException e) {
//            // 接口失败监控 (基础业务异常)
//            String createId = this.getCreateId("ControllerAspectBusinessException");
//            Id registryId = registry.createId(createId).withTag("method", methodName);
//            registry.counter(registryId).inc();
//            throw new BaseBizException(e);
//        } catch (BaseSysException e) {
//            // 接口失败监控 (基础系统异常)
//            String createId = this.getCreateId("ControllerAspectBusinessException");
//            Id registryId = registry.createId(createId).withTag("method", methodName);
//            registry.counter(registryId).inc();
//            throw new BaseSysException(e);
//        } catch (Throwable t) {
//            // 接口失败监控 (其他异常)
//            String createId = this.getCreateId("ControllerAspectOtherException");
//            Id registryId = registry.createId(createId).withTag("method", methodName);
//            registry.counter(registryId).inc();
//            logger.error("统一服务内部异常处理:" ,  t);
//            return Result.errorWithMsg(Result.INTERNAL_SERVER_ERROR, Result.INTERNAL_SERVER_ERROR_MSG);
//        }
//        return result;
//
//    }
//
//    private boolean isResponse(ProceedingJoinPoint pjp) {
//        Signature signature = pjp.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Method method = methodSignature.getMethod();
//        if (Result.class.isAssignableFrom(method.getReturnType())) {
//            return true;
//        }
//        return false;
//    }
//
//    /***
//     * 生成CreateId
//     * @param msg :
//     * @return : java.lang.String
//     */
//    public String getCreateId(String msg){
//        String createId = "";
//        String[] applicationNameStr = applicationName.split("-");
//        for (String s : applicationNameStr){
//            createId += s.substring(0,1).toUpperCase()+s.substring(1,s.length());
//        }
//        return createId+msg;
//    }
//
//}
