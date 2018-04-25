package com.android.xjq.presenters;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.library.Utils.LogUtils;
import com.android.xjq.XjqApplication;
import com.android.xjq.service.FloatingView;
import com.tencent.TIMManager;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.impl.ILVBLogin;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;

import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * 初始化
 * 包括imsdk等
 */
public class InitBusinessHelper {

    public static final String TAG = InitBusinessHelper.class.getSimpleName();
    private static boolean hasInitApp = false;
    private static int reconnectCount;

    public static void setHasInitApp(boolean hasInitApp) {
        InitBusinessHelper.hasInitApp = hasInitApp;
    }

    public static boolean isLoginSuccess() {
        return !TextUtils.isEmpty(ILiveLoginManager.getInstance().getMyUserId());
    }

    //是否正在登录中
    public static boolean isLoginning() {
        return !isLoginSuccess() && reconnectCount >= 1;
    }

    public static boolean isHasInitApp() {
        return hasInitApp;
    }

    private InitBusinessHelper() {
    }

    /**
     * 初始化App
     */
    public static void initApp(final Context context, String appId, String accountType) {

        //初始化avsdk imsdk
        TIMManager.getInstance().disableBeaconReport();

        // 初始化ILiveSDK
        ILiveSDK.getInstance().initSdk(context, Integer.parseInt(appId), Integer.parseInt(accountType));

        // 初始化直播模块
        ILVLiveConfig liveConfig = new ILVLiveConfig();

        liveConfig.messageListener(MessageEvent.getInstance());

        ILVLiveManager.getInstance().init(liveConfig);

      /*  //初始化CrashReport系统
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(context);*/
    }

    public static void iLiveLogin(final String identifier, final String appUserSign, final ILiveCallBack callBack) {

        if (!StringUtils.isBlank(ILVBLogin.getInstance().getMyUserId())) return;

        if (!hasInitApp) {
            initApp(XjqApplication.getContext(),
                    LoginInfoHelper.getInstance().getAppId(),
                    LoginInfoHelper.getInstance().getAccountNo());
            hasInitApp = true;
        }
        LogUtils.e(TAG, "--------------正在登录-----------");
        reconnectCount = 1;

        //登录
        ILiveLoginManager.getInstance().iLiveLogin(identifier, appUserSign, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (callBack != null)
                    callBack.onSuccess(data);
                reconnectCount = 0;
                LogUtils.e(TAG, "--------------登录成功-----------");
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                if (callBack != null)
                    callBack.onError(module, errCode, errMsg);
                if (reconnectCount >= 3) return;
                reconnectCount++;
                LogUtils.e(TAG, "--------------重连次数-----------" + reconnectCount);
                ILiveLoginManager.getInstance().iLiveLogin(identifier, appUserSign, this);
            }
        });

        ILiveLoginManager.getInstance().setUserStatusListener(new ILiveLoginManager.TILVBStatusListener() {
            @Override
            public void onForceOffline(int error, String message) {
                switch (error) {
                    case ILiveConstants.ERR_EXPIRE://账号过期
                        // AccountQuitDialogActivity.startAccountQuitDialogActivity(true, "你的账号已在其他地方登录");
                        break;
                    case ILiveConstants.ERR_KICK_OUT://被踢出
                        /*if (FloatingView.getInstance() != null && FloatingView.getInstance().getRootView() != null) {
                            FloatingView.getInstance().closeWindow();
                        }
                        //EventBus.getDefault().post(new EventBusMessage(EventBusMessage.USER_ALREADY_LOGIN_OTHER));
                        AccountQuitDialogActivity.startAccountQuitDialogActivity(true, "你的账号已在其他地方登录");*/
                        break;
                }
            }
        });
    }

    private static boolean shouldInit() {
        ActivityManager am = ((ActivityManager) XjqApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = XjqApplication.getContext().getPackageName();
        int myPid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static void reLoginLive(final String identifier, final String appUserSign, final ILiveCallBack callBack) {

        //这边如果发现getAvContext()不为空，说明sdk已经登录。就不去调用登录接口了
       /* if (ILiveLoginManager.getInstance().getAVConext() != null) {
            if (callBack != null) {
                callBack.onSuccess(0);
            }

        } else*/

        if (ILiveLoginManager.getInstance().getMyUserId().equals(identifier)) {
            callBack.onSuccess(0);
            return;
        }

        final ILiveCallBack logoutCallBack = new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //登录
                iLiveLogin(identifier, appUserSign, callBack);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //如果错误也重新登录
                iLiveLogin(identifier, appUserSign, callBack);
            }
        };

        if (FloatingView.getInstance() != null && FloatingView.getInstance().getRootView() != null) {
            FloatingView.getInstance().closeWindow(new FloatingView.OnFloatViewDestroyListener() {
                @Override
                public void onDestroy() {
                    iLiveLogoutOut(logoutCallBack);
                }
            });
        } else {
            iLiveLogoutOut(logoutCallBack);
        }

    }

    public static void iLiveLogoutOut(final ILiveCallBack callBack) {
        if (FloatingView.getInstance() != null && FloatingView.getInstance().getRootView() != null) {
            FloatingView.getInstance().closeWindow(new FloatingView.OnFloatViewDestroyListener() {
                @Override
                public void onDestroy() {
                    ILiveLoginManager.getInstance().iLiveLogout(callBack);
                }
            });
        } else {
            ILiveLoginManager.getInstance().iLiveLogout(callBack);
        }

    }
}
