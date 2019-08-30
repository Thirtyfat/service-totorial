package com.mglj.totorial.framework.tool.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ServiceLocator implements ApplicationContextAware {

	private final static Logger logger = LoggerFactory.getLogger(ServiceLocator.class);

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ServiceLocator.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException {
		try{
			return (T) applicationContext.getBean(name);
		}catch(NoSuchBeanDefinitionException e){
			logger.error("未定义的" + name, e);
		}catch(BeansException e){
			logger.error("获取对象失败: " + name, e);
		}
		return null;
	}

	public static <T> T getBean(Class<T> clasz) throws BeansException {
		try{
			return (T) applicationContext.getBean(clasz);
		}catch(NoSuchBeanDefinitionException e){
			logger.error("未定义的" + clasz, e);
		}catch(BeansException e){
			logger.error("获取对象失败: " + clasz, e);
		}
		return null;
	}

}
