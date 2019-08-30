package com.mglj.totorial.framework.common.lang;

import java.io.Serializable;

/**
 * Created by zsp on 2018/8/14.
 */
public class PageRequest extends PageQuery implements Serializable {

    public final static int DEFAULT_PAGE_SIZE = 20;

    private static final long serialVersionUID = -1L;

    /**
     * 分页起始页索引
     */
    private int pageIndex;

    /**
     * 分页每页记录数
     */
    private int pageSize = DEFAULT_PAGE_SIZE;

    public PageRequest() {
        this(0, DEFAULT_PAGE_SIZE);
    }

    public PageRequest(int pageSize) {
        this(0, pageSize);
    }

    public PageRequest(int pageIndex, int pageSize) {
        checkPageIndex(pageIndex);
        checkPageSize(pageSize);
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        build();
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }

    public PageRequest buildPageIndex(int pageIndex) {
        checkPageIndex(pageIndex);
        setPageIndex(pageIndex);
        return this;
    }

    public PageRequest buildPageSize(int pageSize) {
        checkPageSize(pageSize);
        setPageSize(pageSize);
        return this;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        checkPageIndex(pageIndex);
        this.pageIndex = pageIndex;
        build();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        checkPageSize(pageSize);
        this.pageSize = pageSize;
        build();
    }

    private void build() {
        offset = pageIndex * pageSize;
        rows = pageSize;
    }

    private void checkPageIndex(int pageIndex) {
        if(pageIndex < 0) {
            throw new IndexOutOfBoundsException("The pageIndex should be than -1.");
        }
    }

    private void checkPageSize(int pageSize) {
        if(pageSize < 1) {
            throw new IndexOutOfBoundsException("The pageSize should be than 0.");
        }
    }

}
