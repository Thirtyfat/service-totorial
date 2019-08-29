package com.mglj.totorial.framework.tools.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络工具
 * 
 * @date 2016-03-11
 * @author yj
 */
public class NetUtils {
	
    /**
     * 取本机的机器名称
     * 
     * @return
     * @throws UnknownHostException 
     */
    public static String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * 取本机的IP
     * 
     * @return
     * @throws UnknownHostException 
     */
    public static String getHostAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
    
    /**
     * 取本机的地址信息
     * 
     * @return
     * @throws UnknownHostException 
     */
    public static InetAddress getLocalHost() throws UnknownHostException  {
		return InetAddress.getLocalHost();
    }
	
}
