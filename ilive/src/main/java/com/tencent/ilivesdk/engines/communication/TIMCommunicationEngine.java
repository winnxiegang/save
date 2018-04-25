package com.tencent.ilivesdk.engines.communication;

import android.content.Context;

import com.tencent.TIMValueCallBack;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.imsdk.IMMsfCoreProxy;
import com.tencent.imsdk.IMMsfUserInfo;
import com.tencent.imsdk.av.MultiVideoTinyId;

/**
 *
 */

public class TIMCommunicationEngine implements CommunicationEngine {
    /**
     * 初始化
     *
     * @param context 上下文
     */
    @Override
    public void init(Context context) {

    }

    /**
     * 是否初始化
     */
    @Override
    public boolean isInit() {
        return false;
    }

    /**
     * 获取通讯层当前登录的uin
     *
     * @param id 登录的id
     */
    @Override
    public long getLoginUin(String id) {
        IMMsfUserInfo msfUserInfo = IMMsfCoreProxy.get().getMsfUserInfo(id);
        if (msfUserInfo == null) {
            return 0;
        }else {
            return msfUserInfo.getTinyid();
        }
    }

    /**
     * 判断是否已登录
     *
     * @param id 查询的id
     */
    @Override
    public boolean isLogin(String id) {
        IMMsfUserInfo msfUserInfo = IMMsfCoreProxy.get().getMsfUserInfo(id);
        return msfUserInfo != null && msfUserInfo.getTinyid() != 0;
    }

    /**
     * 音视频信令请求通道
     *
     * @param buff     请求数据
     * @param callBack 回调
     */
    @Override
    public void videoProtoRequest(byte[] buff, final ILiveCallBack<byte[]> callBack) {
        MultiVideoTinyId.get().requestMultiVideoInfo(buff, new TIMValueCallBack<byte[]>() {

            @Override
            public void onError(int i, String s) {
                callBack.onError(ILiveConstants.Module_IMSDK, i, s);
            }

            @Override
            public void onSuccess(byte[] bytes) {
                callBack.onSuccess(bytes);
            }
        });
    }
}
