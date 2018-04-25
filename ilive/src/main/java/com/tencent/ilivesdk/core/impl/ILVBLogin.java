package com.tencent.ilivesdk.core.impl;

import android.text.TextUtils;

import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.TIMUserStatusListener;
import com.tencent.av.sdk.AVCallback;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVError;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveFunc;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.ilivesdk.core.ILiveLoginManager;


/**
 * 登录模块的实现类
 */
public class ILVBLogin extends ILiveLoginManager implements TIMUserStatusListener {

    private final int AV_CONTEXT_STARTING = 0;

    private final int AV_CONTEXT_START_ERROR = 1;

    private final int AV_CONTEXT_START_COMPLETE = 2;

    private final int AV_CONTEXT_STOP = 3;

    private int mAvContextStartStatus = AV_CONTEXT_STOP;


    private ILiveCallBack loginListener;
    private TILVBStatusListener statusListener;
    private AVContext mAVContext = null;
    private AVContext.StartParam mParam = null;
    private String mMyUserId = "";
    private static final String TAG = ILVBLogin.class.getSimpleName();
    //private QualityReportHelper helper = new QualityReportHelper();

    @Override
    public void onForceOffline() {
        ILiveLog.d(TAG, "onForceOffline->entered!");
        mMyUserId = "";
        if (null != statusListener) {
            statusListener.onForceOffline(ILiveConstants.ERR_KICK_OUT, "kick out");
        }
        // 回收资源
        //iLiveLogout(null);

    }

    @Override
    public void onUserSigExpired() {
        ILiveLog.d(TAG, "onUserSigExpired->entered!");
        if (null != statusListener) {
            statusListener.onForceOffline(ILiveConstants.ERR_EXPIRE, "sig expire");
        }
        // 回收资源
        iLiveLogout(null);
    }

    @Override
    public void setUserStatusListener(TILVBStatusListener listener) {
        statusListener = listener;
    }

    @Override
    public void iLiveLogin(final String id, String sig, ILiveCallBack tilvbLoginListener) {
        loginListener = tilvbLoginListener;
        TIMUser user = new TIMUser();
        user.setAccountType("" + ILiveSDK.getInstance().getAccountType());
        user.setAppIdAt3rd("" + ILiveSDK.getInstance().getAppId());
        user.setIdentifier(id);
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-iLiveLogin strart |id:" + id);

        // 监控用户状态事件
        TIMManager.getInstance().setUserStatusListener(this);

        //发起登录请求
        TIMManager.getInstance().login(
                ILiveSDK.getInstance().getAppId(),
                user,
                sig, //用户帐号签名，由私钥加密获得，具体请参考文档
                new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        ILiveLog.e(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-iLiveLogin|login failed:" + i + "|" + s);
                        ILiveFunc.notifyError(loginListener, ILiveConstants.Module_IMSDK, i, s);
                        //helper.init(ILiveConstants.EVENT_ILIVE_LOGIN, i, s);
                        //helper.report();
                        setAvContextStop();
                        mMyUserId = "";
                        loginListener = null;   // 用完置空
                    }

                    @Override
                    public void onSuccess() {
                        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-iLiveLogin|login success");
                        ILiveFunc.notifySuccess(loginListener, 0);
                        //startContext(id);
                        setAvContextStop();
                        mMyUserId = id;
                    }
                });
    }

    private void logoutIM(final ILiveCallBack callBack) {
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int code, String message) {
                ILiveLog.e(TAG, ILiveConstants.LOG_KEY_PR + "|logoutIM failed:" + code + "|" + message);
                ILiveFunc.notifyError(callBack, ILiveConstants.Module_IMSDK, code, message);
            }

            @Override
            public void onSuccess() {
                ILiveFunc.notifySuccess(callBack, 0);
            }
        });
    }

    private void logoutSDK(final ILiveCallBack logoutListener) {
        if (TextUtils.isEmpty(TIMManager.getInstance().getLoginUser())) {
            ILiveFunc.notifyError(logoutListener, ILiveConstants.Module_ILIVESDK, ILiveConstants.ERR_NOT_LOGIN, "im logout already");
            //stopContext(null);      // IM注销失败也要停止AVSDK
        } else {
            logoutIM(new ILiveCallBack() {
                @Override
                public void onSuccess(Object data) {
                    mMyUserId = "";
                    ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|iLiveLogout onSuccess");
                    ILiveFunc.notifySuccess(logoutListener, 0);
                    //stopContext(logoutListener);    // IM注销失败也要停止AVSDK
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    mMyUserId = "";
                    ILiveLog.e(TAG, ILiveConstants.LOG_KEY_PR + "|iLiveLogout onError");
                    ILiveFunc.notifyError(logoutListener, ILiveConstants.Module_IMSDK, errCode, errMsg);
                    //stopContext(null);  // IM注销失败也要停止AVSDK
                }
            });
        }
    }

    public void iLiveLogout(final ILiveCallBack logoutListener) {
        statusListener = null;
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-iLiveLogout enter");
        //ILiveRoomManager.getInstance().quitRoom(null);  // 可能不会回调，如被踢
        logoutSDK(logoutListener);
    }


    /**
     * 获取用户id
     *
     * @return id
     */
    @Override
    public String getMyUserId() {
        return mMyUserId;
    }

    @Override
    public AVContext getAVConext() {
        if (mAvContextStartStatus == AV_CONTEXT_START_COMPLETE) {
            return mAVContext;
        } else {
            return null;
        }

    }


    /**
     * 启动AVSDK系统的回调接口
     */
    private AVCallback mStartContextCompleteCallback = new AVCallback() {
        @Override
        public void onComplete(int result, String message) {
            if (result == AVError.AV_OK || result == AVError.AV_ERR_HAS_IN_THE_STATE) {
                ILiveLog.d(TAG, "ILVB-iLiveLogin|start AVSDK context complete!");
                // 初始化视频
                ILiveFunc.notifySuccess(loginListener, 0);
                //helper.init(ILiveConstants.EVENT_ILIVE_LOGIN, 0, "");
                //helper.report();
            } else {
                ILiveLog.d(TAG, "ILVB-iLiveLogin|start AVSDK context failed:" + result + "|" + message);
                String s = "AV StartContext fail ";
                logoutIM(null);
                ILiveFunc.notifyError(loginListener, ILiveConstants.Module_AVSDK, result, s);
                //helper.init(ILiveConstants.EVENT_ILIVE_LOGIN, result, s);
                //helper.report();
                //stopContext(null);
            }
            loginListener = null;   // 用完置空
        }
    };


   /* */

    /**
     * 实际初始化AVSDK
     *//*
    private void startContext(String identifier) {
        ILiveLog.d(TAG, "ILVB-iLiveLogin|start AVSDK context:" + hasAVContext());
        if (hasAVContext() == false) {
            mParam = new AVContext.StartParam();
            mParam.sdkAppId = ILiveSDK.getInstance().getAppId();
            mParam.accountType = "" + ILiveSDK.getInstance().getAccountType();
            mParam.appIdAt3rd = Integer.toString(ILiveSDK.getInstance().getAppId());
            mParam.identifier = identifier;
            mAVContext = AVContext.createInstance(ILiveSDK.getInstance().getAppContext(), false);
            if (null == mAVContext) {
                ILiveLog.w(TAG, "ILVB-iLiveLogin|create Instance null");
                logoutIM(null);
                ILiveFunc.notifyError(loginListener, ILiveConstants.Module_AVSDK, -1, "start av context return null");
                loginListener = null;   // 用完置空
                return;
            }
            mAVContext.start(mParam, mStartContextCompleteCallback);
        } else {
            ILiveFunc.notifySuccess(loginListener, 0);
            loginListener = null;   // 用完置空
        }
    }

    void stopContext(ILiveCallBack logoutListener) {
        ILiveLog.d(TAG, "stopContext->enter has context:" + hasAVContext());
        if (hasAVContext()) {
            mAVContext.stop();
            avDestory(true);
            ILiveFunc.notifySuccess(logoutListener, 0);
        } else {
            ILiveFunc.notifyError(logoutListener, ILiveConstants.Module_AVSDK, ILiveConstants.ERR_SDK_FAILED, "AVcontext == null");
        }
    }*/
    public void startContext(String identifier, final ILiveCallBack callBack) {

        if (mAvContextStartStatus != AV_CONTEXT_STOP &&
                mAvContextStartStatus != AV_CONTEXT_START_ERROR) {
            return;
        }

        mParam = new AVContext.StartParam();
        mParam.sdkAppId = ILiveSDK.getInstance().getAppId();
        mParam.accountType = "" + ILiveSDK.getInstance().getAccountType();
        mParam.appIdAt3rd = Integer.toString(ILiveSDK.getInstance().getAppId());
        mParam.identifier = identifier;

        if (hasAVContext()) {
            stopContext();
        } else {
            mAVContext = AVContext.createInstance(ILiveSDK.getInstance().getAppContext(), false);
        }

        if (mAVContext != null) {
            mAvContextStartStatus = AV_CONTEXT_STARTING;
            mAVContext.start(mParam, new AVCallback() {
                @Override
                public void onComplete(int result, String message) {
                    if (result == AVError.AV_OK || result == AVError.AV_ERR_HAS_IN_THE_STATE) {
                        mAvContextStartStatus = AV_CONTEXT_START_COMPLETE;
                        ILiveLog.d(TAG, "ILVB-iLiveLogin|start AVSDK context complete!");
                        ILiveFunc.notifySuccess(callBack, 0);

                    } else {
                        ILiveLog.d(TAG, "ILVB-iLiveLogin|start AVSDK context failed:" + result + "|" + message);
                        String s = "AV StartContext fail ";
                        mAvContextStartStatus = AV_CONTEXT_START_ERROR;
                        ILiveFunc.notifyError(callBack, ILiveConstants.Module_AVSDK, result, s);
                    }
                }
            });
        } else {
            mAvContextStartStatus = AV_CONTEXT_STOP;
            ILiveFunc.notifyError(callBack, ILiveConstants.Module_AVSDK, -1, "start av context return null");
        }

    }

    /**
     * AvContext是否已经启动
     * @return
     */
    public boolean isAvContextStarted() {
        return mAvContextStartStatus == AV_CONTEXT_START_COMPLETE;
    }

    void stopContext() {
        if (hasAVContext()) {
            mAVContext.stop();
        }
    }

    private void setAvContextStop() {
        mAvContextStartStatus = AV_CONTEXT_STOP;
    }


    /**
     * 销毁AVSDK
     *
     * @param result
     */
    private void avDestory(boolean result) {
        ILiveLog.w(TAG, "ILVB-iLiveLogout|avDestory");
        mAVContext.destroy();
        mAVContext = null;
    }

    /**
     * 是否存在AVContext实例类
     *
     * @return
     */
    boolean hasAVContext() {
        return mAVContext != null;
    }


}
