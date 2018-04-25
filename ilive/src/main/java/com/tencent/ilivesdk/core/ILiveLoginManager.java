package com.tencent.ilivesdk.core;

import com.tencent.av.sdk.AVContext;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.impl.ILVBLogin;

/**
 * SDK 登录类
 */
public abstract class ILiveLoginManager {

    /* 内部私有静态实例，目的是实现延迟加载 */
    private static class ILVBLoginHolder {
        private static ILVBLogin instance = new ILVBLogin();
    }

    /**
     * 获取登录管理类实例
     *
     * @return
     */
    public static ILVBLogin getInstance() {
        return ILVBLoginHolder.instance;
    }

    /**
     * 设置用户状态回调(每次登录前都需要重新设置)
     * @param listener
     */
    public abstract void setUserStatusListener(TILVBStatusListener listener);





    /**
     * iLiveSDK 登录(独立模式下直接使用该接口，托管模式需先用tlsLogin登录)
     *
     * @param id 用户id
     * @param sig 用户密钥
     */
    public abstract void iLiveLogin(final String id, String sig, ILiveCallBack tilvbLoginListener);


    /**
     * iLiveSDK 登出
     */
    public abstract void iLiveLogout(ILiveCallBack tilvbLoginListener);



    /**
     * 获取用户id
     * @return id
     */
    public abstract String getMyUserId();


    /**
     * 获取AVSDK的句柄
     * @return  AVContext 句柄
     */
    public abstract AVContext getAVConext();



    /**
     * 用户状态回调
     */
    public interface TILVBStatusListener {
        void onForceOffline(int error, String message);
    }

}
