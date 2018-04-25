package com.tencent.ilivesdk;

/**
 * ILive通用返回回调
 */
public interface ILiveCallBack<T> {

    /**
     * 操作成功
     * @param data 成功返回值
     */
    void onSuccess(T data);

    /**
     * 操作失败
     * @param module    出错模块
     * @param errCode   错误码
     * @param errMsg    错误描述
     * @see ILiveConstants#Module_ILIVESDK
     * @see ILiveConstants#Module_AVSDK
     * @see ILiveConstants#Module_IMSDK
     * @see ILiveConstants#Module_TLSSDK
     * @see ILiveConstants#NO_ERR
     */
    void onError(String module, int errCode, String errMsg);
}
