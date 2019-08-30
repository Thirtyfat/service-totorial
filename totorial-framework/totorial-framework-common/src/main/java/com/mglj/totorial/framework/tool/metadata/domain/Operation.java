package com.mglj.totorial.framework.tool.metadata.domain;

import java.util.Objects;

/**
 * 功能描述
 *
 * Created by zsp on 2018/8/30.
 */
public class Operation {

    /**
     * ID
     */
    private Long id;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * hash码：hash(className + methodName)
     */
    private Integer hashCode;
    /**
     * hash碰撞后，记录偏移量
     */
    private Integer offset;
    /**
     * 功能名称
     */
    private String title;
    /**
     * 功能分组
     */
    private String group;

    public static int getHashCode(String className0, String methodName0) {
        if(className0 == null || methodName0 == null) {
            return 0;
        }
        return (className0 + methodName0).hashCode();
    }

    @Override
    public int hashCode() {
        return getHashCode(className, methodName);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(!(obj instanceof  Operation)){
            return false;
        }
        if(className == null || methodName == null) {
            return false;
        }
        Operation target = (Operation) obj;
        return Objects.equals(className, target.getClassName())
                && Objects.equals(methodName, target.getMethodName());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getHashCode() {
        return hashCode;
    }

    public void setHashCode(Integer hashCode) {
        this.hashCode = hashCode;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
