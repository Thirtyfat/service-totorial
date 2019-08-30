package com.mglj.totorial.framework.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by zsp on 2018/10/8.
 */
public class MapUtils {

    /**
     * 把Map中的键值拷贝到对象对应的字段上。
     *
     * @param map
     * @param obj
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static void flatCopyMap2Bean(Map<String, Object> map, Object obj)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (map == null || obj == null) {
            return;
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (map.containsKey(key)) {
                Object value = map.get(key);
                Method setter = property.getWriteMethod();
                setter.invoke(obj, value);
            }
        }
    }

    /**
     * 把对象转换成map。
     *
     * @param obj
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> flatConvertBean2Map(Object obj)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return flatConvertBean2Map(obj, (String[])null);
    }

    /**
     * 把对象转换成map。
     *
     * @param obj
     * @param ignoreFileds 忽略的字段。
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> flatConvertBean2Map(Object obj, String... ignoreFileds)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> ignoreFiledList = new ArrayList<String>();
        ignoreFiledList.add("class");
        if (ignoreFileds != null && ignoreFileds.length > 0) {
            for(String field : ignoreFileds) {
                ignoreFiledList.add(field);
            }
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!ignoreFiledList.contains(key)) {
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);
                if(value != null) {
                    map.put(key, value);
                }
            }
        }

        return map;
    }

    /**
     * 把Map中的键值拷贝到对象对应的字段上。
     *
     * @param map
     * @param obj
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static void flatCopyStringMap2Bean(Map<String, String> map, Object obj)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (map == null || obj == null) {
            return;
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (map.containsKey(key)) {
                Object value = map.get(key);
                Method setter = property.getWriteMethod();
                Class<?> clasz = property.getPropertyType();
                if(String.class.equals(clasz)){
                    setter.invoke(obj, value);
                } else if(Date.class.equals(clasz)) {
                    setter.invoke(obj, new Date(Long.parseLong((String)value)));
                } else if(Boolean.class.equals(clasz) || boolean.class.equals(clasz)) {
                    setter.invoke(obj, Boolean.parseBoolean((String)value));
                } else if(Integer.class.equals(clasz) || int.class.equals(clasz)) {
                    setter.invoke(obj, Integer.parseInt((String)value));
                } else if(Long.class.equals(clasz) || long.class.equals(clasz)) {
                    setter.invoke(obj, Long.parseLong((String)value));
                } else if(Float.class.equals(clasz) || float.class.equals(clasz)) {
                    setter.invoke(obj, Float.parseFloat((String)value));
                } else if(Double.class.equals(clasz) || double.class.equals(clasz)) {
                    setter.invoke(obj, Double.parseDouble((String)value));
                } else {
                    //do nothing
                }
            }
        }
    }

    /**
     * 把对象转换成map。
     *
     * @param obj
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, String> flatConvertBean2StringMap(Object obj)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return flatConvertBean2StringMap(obj, (String[])null);
    }

    /**
     * 把对象转换成map。
     *
     * @param obj
     * @param ignoreFileds 忽略的字段。
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, String> flatConvertBean2StringMap(Object obj, String... ignoreFileds)
            throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        List<String> ignoreFiledList = new ArrayList<String>();
        ignoreFiledList.add("class");
        if (ignoreFileds != null && ignoreFileds.length > 0) {
            for(String field : ignoreFileds) {
                ignoreFiledList.add(field);
            }
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!ignoreFiledList.contains(key)) {
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);
                if(value != null) {
                    Class<?> clasz = property.getPropertyType();
                    if(String.class.equals(clasz)){
                        map.put(key, (String)value);
                    } else if(Date.class.equals(clasz)) {
                        map.put(key, String.valueOf(((Date)value).getTime()));
                    } else {
                        map.put(key, String.valueOf(value));
                    }
                }
            }
        }

        return map;
    }

}
