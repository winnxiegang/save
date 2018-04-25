package com.tencent.ilivesdk.tools.speedtest;


import java.util.List;

/**
 * 测速回调
 */

public interface ILiveSpeedTestCallback {



    /** 出错时回调
     * @param code 错误码
     * @param desc 错误描述
     */
    void onError(int code, String desc);

    /**
     *  开始测速
     *
     * @param serverInfoList 测速接口机列表
     */
    void onStart(List<ILiveServerInfo> serverInfoList);


    /**
     *  进度回调，每当开始新的服务器测速时触发
     *
     *  @param serverInfo 当前测试服务器
     *  @param totalPkg 总发测试包数
     *  @param pkgGap 发包间隔
     */
    void onProgress(ILiveServerInfo serverInfo, int totalPkg, int pkgGap);


    /**
     *  测速结束
     *
     * @param results 结果列表
     */
    void onFinish(List<ILiveSpeedTestResult> results);

}
