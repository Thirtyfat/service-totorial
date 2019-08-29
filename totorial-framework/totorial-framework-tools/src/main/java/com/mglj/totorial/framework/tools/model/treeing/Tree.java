package com.mglj.totorial.framework.tools.model.treeing;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 树的操作
 *
 * Created by yj on 2018/8/13.
 */
public class Tree {

    /**
     * 构建一棵树
     *
     * @param list
     * @param keyFetcher
     * @param parentFetcher
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> List<TreeNode<T>> buildTree(List<T> list,
                                                     Function<T, K> keyFetcher,
                                                     Function<T, K> parentFetcher,
                                                     Predicate<K> isRoot){
        if(list == null || list.size() == 0) {
            throw new  NullPointerException("The list is null.");
        }
        Objects.requireNonNull(keyFetcher, "The keyFetcher is null.");
        Objects.requireNonNull(parentFetcher, "The parentFetcher is null.");
        Objects.requireNonNull(isRoot, "The isRoot is null.");

        List<TreeNode<T>> tree = new ArrayList<>();
        List<T> tempList = new ArrayList<>();
        tempList.addAll(list);
        TreeNode<T> currentParentNode = null;
        for(int i = 0; i < 10; i++) {
            Iterator<T> it = tempList.iterator();
            while (it.hasNext()) {
                T value = it.next();
                K parentKey = parentFetcher.apply(value);
                if (isRoot.test(parentKey)) {
                    tree.add(new TreeNode<>(value));
                    it.remove();
                } else {
                    K currentParentKey = currentParentNode != null ?
                            keyFetcher.apply(currentParentNode.getValue()) : null;
                    if(Objects.equals(currentParentKey, parentKey)) {
                        currentParentNode.addChild(new TreeNode<>(value));
                        it.remove();
                    } else {
                        TreeNode<T> parentNode = findParent(tree, parentKey, keyFetcher);
                        if (parentNode != null) {
                            parentNode.addChild(new TreeNode<>(value));
                            currentParentNode = parentNode;
                            it.remove();
                        }
                    }
                }
            }
        }

        return tree;
    }

    /**
     * 删除树节点
     *
     * @param node
     * @param recursive
     * @param <T>
     */
    public static <T> void remove(TreeNode<T> node, boolean recursive) {
        if(node == null) {
            return;
        }
        if(recursive) {
            List<TreeNode<T>> treeNodes = Arrays.asList(node);
            postorderDepthFirstVisit(treeNodes,
                    (childNode)->{
                        childNode.clearChild();
                        childNode.setParent(null);
                    });
        } else {
            node.clearChild();
            node.setParent(null);
        }
    }

    /**
     * 删除树节点
     *
     * @param tree
     * @param recursive
     * @param <T>
     */
    public static <T> void remove(List<TreeNode<T>> tree, boolean recursive) {
        if(tree == null || tree.size() == 0) {
            return;
        }
        if(recursive) {
            postorderDepthFirstVisit(tree,
                    (childNode)->{
                        childNode.clearChild();
                        childNode.setParent(null);
                    });
        } else {
            for(TreeNode<T> node : tree) {
                node.clearChild();
                node.setParent(null);
            }
        }
        tree.clear();
    }

    /**
     * 查找指定的父节点
     *
     * @param tree
     * @param parentKey
     * @param keyFetcher
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> TreeNode<T> findParent(List<TreeNode<T>> tree, K parentKey,
                   Function<T, K> keyFetcher) {
        if(tree == null || tree.size() == 0) {
            throw new  NullPointerException("The tree is null.");
        }
        Objects.requireNonNull(keyFetcher, "The keyFetcher is null.");

        Deque<TreeNode<T>> stack = new LinkedList<>();
        inverseAddAll(stack, tree);
        while (!stack.isEmpty()) {
            TreeNode<T> node = stack.pollLast();
            K key = keyFetcher.apply(node.getValue());
            if(Objects.equals(key, parentKey)) {
               return node;
            } else if(node.hasChild()) {
                inverseAddAll(stack, node.getChildren());
            }
        }
        return null;
    }

    /**
     * 先序深度优先遍历
     *
     * @param tree
     * @param action
     * @param <T>
     */
    public static <T> void preorderDepthFirstVisit(List<TreeNode<T>> tree, Consumer<TreeNode<T>> action) {
        if(tree == null || tree.size() == 0) {
            return;
        }
        Objects.requireNonNull(action, "The action is null.");

        Deque<TreeNode<T>> stack = new LinkedList<>();
        inverseAddAll(stack, tree);
        while (!stack.isEmpty()) {
            TreeNode<T> node = stack.pollLast();
            action.accept(node);
            if(node.hasChild()) {
                inverseAddAll(stack, node.getChildren());
            }
        }
    }

    /**
     * 后序深度优先遍历
     *
     * @param tree
     * @param action
     * @param <T>
     */
    public static <T> void postorderDepthFirstVisit(List<TreeNode<T>> tree, Consumer<TreeNode<T>> action) {
        if(tree == null || tree.size() == 0) {
            return;
        }
        Objects.requireNonNull(action, "The action is null.");

        Deque<TreeNode<T>> stack = new LinkedList<>();
        Deque<Boolean> visitedStack = new LinkedList<>();
        inverseAddAll(stack, visitedStack, tree);
        while (!stack.isEmpty()) {
            boolean visited = visitedStack.pollLast();
            TreeNode<T> node;
            if(!visited) {
                node = stack.peekLast();
                visitedStack.add(true);
            } else {
                node = stack.pollLast();
                action.accept(node);
            }
            if(node.hasChild()) {
                inverseAddAll(stack, visitedStack, node.getChildren());
            }
        }
    }

    /**
     * 广度优先遍历
     *
     * @param tree
     * @param action
     * @param <T>
     */
    public static <T> void breadthFirstVisit(List<TreeNode<T>> tree, Consumer<TreeNode<T>> action) {
        if(tree == null || tree.size() == 0) {
            return;
        }
        Objects.requireNonNull(action, "The action is null.");

        Deque<TreeNode<T>> queue = new LinkedList<>();
        queue.addAll(tree);
        while (!queue.isEmpty()) {
            TreeNode<T> node = queue.pollFirst();
            action.accept(node);
            if(node.hasChild()) {
                queue.addAll(node.getChildren());
            }
        }
    }

    private static <T> void inverseAddAll(Deque<TreeNode<T>> deque,
                                          List<TreeNode<T>> list) {
        for(int i = list.size() - 1; i >= 0; i--) {
            deque.add(list.get(i));
        }
    }

    private static <T> void inverseAddAll(Deque<TreeNode<T>> deque,
                                          Deque<Boolean> visitedDeque,
                                          List<TreeNode<T>> list) {
        for(int i = list.size() - 1; i >= 0; i--) {
            deque.add(list.get(i));
            visitedDeque.add(false);
        }
    }

}
