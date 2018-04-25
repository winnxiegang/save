package com.tencent.ilivesdk.tools.speedtest;

import java.net.InetAddress;

/**
 *  服务器信息
 */

public interface ILiveServerInfo {

    /**
     *  获取服务器地址
     */
    InetAddress getAddress();

    /**
     *  获取服务器端口
     */
    int getPort();
}
