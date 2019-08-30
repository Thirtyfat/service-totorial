package com.mglj.totorial.framework.tool.concurrent.threading;

import java.util.concurrent.ExecutorService;

/**
 * Created by zsp on 2018/9/4.
 */
public interface ExecutorServiceFactory {

    ExecutorService get();

    void destroy();

}
