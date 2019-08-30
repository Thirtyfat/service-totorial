package com.mglj.totorial.framework.tool.data.loader;

/**
 * 数据加载
 *
 * Created by zsp on 2018/8/21.
 */
public interface DataLoader {

    public final static String DOMAIN_DATA_LOADER = "data-loader";

    /**
     * 加载数据
     */
    void load();

    /**
     * 卸载数据
     */
    void unload();

    /**
     * 重新加载
     */
    void reload();

}
