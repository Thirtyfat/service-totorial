package com.mglj.totorial.framework.tool.coordinate;

/**
 * Created by zsp on 2018/7/11.
 */
public interface Coordinator {

    int getSequence();

    int getSequence(String ip);

    int getSequence(int port);

    int getSequence(String ip, int port);

}
