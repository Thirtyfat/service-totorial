package com.mglj.totorial.framework.tools.model;

/**
 * 数据查询分页
 *
 * Created by yj on 2018/8/17.
 */
public class PageQuery {

    public final static int DEFAULT_ROWS = 20;

    /**
     * 数据分页的偏移索引
     */
    protected int offset;

    /**
     * 数据分页每页记录行数
     */
    protected int rows;

    public PageQuery() {
        this(0, DEFAULT_ROWS);
    }

    public PageQuery(int rows) {
        this(0, rows);
    }

    public PageQuery(int offset, int rows) {
        if(offset < 0) {
            offset = 0;
        }
        if(rows < 1) {
            rows = 1;
        }
        this.offset = offset;
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PageQuery{" +
                "offset=" + offset +
                ", rows=" + rows +
                '}';
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

}
