package com.mglj.totorial.framework.common.util;

import java.net.*;
import java.util.Enumeration;

/**
 * Created by zsp on 2018/7/11.
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
     * 取本机的IP
     *
     * @return
     * @throws UnknownHostException
     * @throws SocketException
     */
    public static String getIp() throws UnknownHostException, SocketException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        Enumeration<InetAddress> addrs;
        while (networks.hasMoreElements()) {
            addrs = networks.nextElement().getInetAddresses();
            while (addrs.hasMoreElements()) {
                ip = addrs.nextElement();
                if (ip != null && ip instanceof Inet4Address && ip.isSiteLocalAddress()
                        && !ip.getHostAddress().equals(hostAddress)) {
                    return ip.getHostAddress();
                }
            }
        }
        return hostAddress;
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
