package com.mglj.totorial.framework.common.lang.treeing;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点
 *
 * Created by zsp on 2018/8/9.
 */
public class TreeNode<T> {

    /**
     * 值
     */
    private T value;

    /**
     * 父节点
     */
    private TreeNode<T> parent;

    /**
     * 孩子节点
     */
    private List<TreeNode<T>> children;

    public TreeNode() {

    }

    public TreeNode(T value) {
        this.value = value;
    }

    /**
     * 添加孩子节点
     *
     * @param child
     */
    public void addChild(TreeNode<T> child) {
        if(child == null) {
            return;
        }
        if(children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        child.setParent(this);
    }

    /**
     * 删除孩子节点
     *
     * @param child
     */
    public void removeChild(TreeNode<T> child) {
        if(child == null) {
            return;
        }
        if(children == null) {
            children = new ArrayList<>();
        }
        if(children.remove(child)){
            child.setParent(null);
        }
    }

    /**
     * 清空孩子节点
     */
    public void clearChild() {
        if(children != null) {
            children.clear();
            children = null;
        }
    }

    /**
     * 是否有孩子节点
     * @return
     */
    public boolean hasChild() {
        return children != null && children.size() > 0;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "value=" + value +
                '}';
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }

}
