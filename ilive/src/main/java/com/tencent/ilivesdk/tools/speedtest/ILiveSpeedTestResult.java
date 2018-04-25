package com.tencent.ilivesdk.tools.speedtest;


/**
 *  测速结果
 */

public interface ILiveSpeedTestResult {


    /**
     *  获取测速目标服务器信息
     */
    ILiveServerInfo getServerInfo();

    /**
     *  获取上行丢包率
     *
     *  @return 上行丢包率（万分比）
     */
    int getUpLoss();

    /**
     *  获取下行丢包率
     *
     *  @return 下行丢包率（万分比）
     */
    int getDownLoss();

    /**
     *  获取平均时延
     *
     *  @return 平均时延，单位毫秒
     */
    int getAvgRtt();

    /**
     *  获取最大时延
     *
     *  @return 最大时延，单位毫秒
     */
    int getMaxRtt();

    /**
     *  获取最小时延
     *
     *  @return 最小时延，单位毫秒
     */
    int getMinRtt();

    /**
     *  获取上行乱序
     *
     *  @return 上行乱序数，服务器收到包的顺序和发送顺序相比较获得的逆序数
     */
    int getUpDisorder();

    /**
     *  获取下行乱序
     *
     *  @return 下行乱序数，客户端收到回包的顺序和发送顺序相比较获得的逆序数
     */
    int getDownDisorder();



}
