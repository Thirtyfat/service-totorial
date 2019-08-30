package com.mglj.totorial.framework.tool.gid;

import java.util.List;

/**
 * Created by zsp on 2018/7/11.
 */
public interface GidGenerator {

    /**
     *
     * @return
     */
    long generate();

    /**
     *
     * @param size
     * @return
     */
    List<Long> generate(int size);

}
