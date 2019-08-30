package com.mglj.totorial.framework.tool.validation;

import com.mglj.totorial.framework.common.exceptions.ValidationException;
import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.common.validation.AlertMessage;
import com.mglj.totorial.framework.common.validation.Validatable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Created by zsp on 2018/8/7.
 */
@Aspect
public class ValidationAspect {

    private static final Logger logger = LoggerFactory.getLogger(ValidationAspect.class);
    private final Validator validator;

    public ValidationAspect() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Pointcut("@annotation(com.mglj.totorial.framework.common.validation.Validatable)")
    public void validate() {

    }

    @Around("validate()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        Object bean = pjp.getTarget();
        Class<?> clazz = bean.getClass();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        if(logger.isDebugEnabled()) {
            logger.debug("validate class: " + clazz + ", method: " + method);
        }

        boolean present = method.isAnnotationPresent(Validatable.class);
        if(!present) {
            return pjp.proceed();
        }

        Object[] arguments = pjp.getArgs();
        if(logger.isDebugEnabled()) {
            logger.debug("validate arguments: " + arguments);
        }
        if(arguments != null && arguments.length > 0){
            Validatable validatable = method.getAnnotation(Validatable.class);
            Class<?>[] groups = validatable.groups();
            Object result = null;
            for(Object argument : arguments){
                if(argument.getClass().isArray()) {
                    Object[] array = (Object[])argument;
                    for(Object obj : array) {
                        result = buildResult(validate0(groups, validator, obj), method, obj);
                        if(result != null) {
                            return result;
                        }
                    }
                } else if(Collection.class.isAssignableFrom(argument.getClass())) {
                    Collection collection = (Collection)argument;
                    for(Object obj : collection) {
                        result = buildResult(validate0(groups, validator, obj), method, obj);
                        if(result != null) {
                            return result;
                        }
                    }
                } else {
                    result = buildResult(validate0(groups, validator, argument), method, argument);
                }
                if(result != null) {
                    return result;
                }
            }
        }

        return pjp.proceed();
    }

    private Object buildResult(Set<ConstraintViolation<Object>> constraintViolations, Method method, Object obj)
            throws ValidationException {
        if(constraintViolations != null && !constraintViolations.isEmpty()){
            for(ConstraintViolation<Object> constraintViolation : constraintViolations){
                Class<?> returnType = method.getReturnType();
                String code = constraintViolation.getMessage();
                AlertMessage alertMessage = getAlertMessage(obj, code);
                return buildResult(returnType, alertMessage, code);
            }
        }

        return null;
    }

    private Set<ConstraintViolation<Object>> validate0(Class<?>[] groups, Validator validator,
                                                       Object obj) {
        Set<ConstraintViolation<Object>> constraintViolations;
        if(groups != null && groups.length > 0){
            constraintViolations = validator.validate(obj, groups);
        } else {
            constraintViolations = validator.validate(obj);
        }
        return constraintViolations;
    }

    private Object buildResult(Class<?> returnType, AlertMessage alertMessage, String code)
            throws ValidationException {
        String codeMark = null;
        String msg = null;
        if(alertMessage != null) {
            codeMark = alertMessage.enableCodeMask() ? Result.BAD_REQUEST : code;
            msg = (msg = alertMessage.msg()) == null ? "错误的请求" : msg;
        }
        if(returnType == null) {
            throw new ValidationException(codeMark, msg);
        }
        if(Result.class.isAssignableFrom(returnType)) {
            return Result.errorWithMsg(codeMark, msg);
        }
        try {
            return returnType.getConstructor(String.class, String.class).newInstance(codeMark, msg);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error(returnType + "缺少包含两个String变量的构建方法", e);
        }
        return null;
    }

    private AlertMessage getAlertMessage(Object argument, String code) {
        Field[] fields = argument.getClass().getDeclaredFields();
        for(Field field : fields) {
            AlertMessage[] alertMessageArray = field.getAnnotationsByType(AlertMessage.class);
            if (alertMessageArray == null || alertMessageArray.length == 0) {
                continue;
            }
            for(AlertMessage alertMessage : alertMessageArray) {
                if (alertMessage != null && Objects.equals(code, alertMessage.code())) {
                    return alertMessage;
                }
            }
        }
        return null;
    }

}
