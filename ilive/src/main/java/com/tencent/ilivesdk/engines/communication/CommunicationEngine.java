package com.tencent.ilivesdk.engines.communication;

import android.content.Context;

import com.tencent.ilivesdk.ILiveCallBack;

/**
 * 通讯能力抽象接口
 */

public interface CommunicationEngine {

    /**
     * 初始化
     *
     * @param context 上下文
     */
    void init(Context context);

    /**
     * 是否初始化
     *
     */
    boolean isInit();



    /**
     * 获取通讯层当前登录的uin
     *
     * @param id 登录的id
     */
    long getLoginUin(String id);


    /**
     * 判断是否已登录
     *
     * @param id 查询的id
     */
    boolean isLogin(String id);



    /**
     * 音视频信令请求通道
     *
     * @param buff 请求数据
     * @param callBack 回调
     */
    void videoProtoRequest(byte[] buff, ILiveCallBack<byte[]> callBack);
}
