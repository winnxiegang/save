package com.tencent.ilivesdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.tencent.TIMManager;
import com.tencent.av.sdk.AVAudioCtrl;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVVideoCtrl;
import com.tencent.ilivesdk.core.ILiveLoginManager;


/**
 * SDK
 */
public class ILiveSDK {
    private static String TAG = "ILiveSDK";
    private Context mApplicationContext;

    private int mAppId = 0;
    private int mAccountType = 0;
    private String mVersion = "1.4.1";

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public boolean runOnMainThread(Runnable r, long delay) {
        return mMainHandler.postDelayed(r, delay);
    }

    public int getAppId() {
        return mAppId;
    }

    public int getAccountType() {
        return mAccountType;
    }


    private ILiveSDK() {
    }

    /* 内部私有静态实例，目的是实现延迟加载 */
    private static class TIMSdkHolder {
        private static ILiveSDK instance = new ILiveSDK();
    }

    /**
     * 获取iLiveSDK单例
     *
     * @return
     */
    public static ILiveSDK getInstance() {
        return TIMSdkHolder.instance;
    }


    /**
     * 初始化 SDK
     *
     * @param context     APPContext
     * @param appId       app ID
     * @param accountType 类型
     */
    public void initSdk(Context context, int appId, int accountType) {

        this.mApplicationContext = context.getApplicationContext();
        this.mAppId = appId;
        this.mAccountType = accountType;

        TIMManager.getInstance().init(mApplicationContext);

        // 添加异常日志上报
        //TIMIntManager.getInstance().setAvSDKVersionToBugly("e217b9cd0c", ILiveConstants.Module_ILIVESDK+getVersion());

        // 初始化日志
        //ILVBLog.init(mApplicationContext);
        //ILiveLog.d(TAG, ILiveConstants.LOG_KEY_PR + "|initSdk->init appid:" + appId + ", accountType:" + accountType);
        //ILiveLog.d(TAG, ILiveConstants.LOG_KEY_PR + "|initSdk->version: " + getVersion());

        /*//数据采集
        QualityReportHelper helper = new QualityReportHelper();
        helper.init(ILiveConstants.EVENT_ILIVE_INIT,getAppId(),"");
        helper.report();*/

    }

    /**
     * 获取版本号
     *
     * @return 颁布号
     */
    public String getVersion() {
        return mVersion;
    }

    /**
     * 封装接口 获取音视频控制类
     *
     * @return AVContext对象
     */
    // AVSDK 相关(保留接口)
    public AVContext getAVContext() {
        return ILiveLoginManager.getInstance().getAVConext();
    }

    /**
     * 封装接口 获取音频控制类
     *
     * @return AVAudioCtrl对象
     */
    public AVAudioCtrl getAvAudioCtrl() {
        if (null != getAVContext()) {
            return getAVContext().getAudioCtrl();
        }
        return null;
    }

    /**
     * 封装接口 获取视频控制类
     *
     * @return AVVideoCtrl对象
     */
    public AVVideoCtrl getAvVideoCtrl() {
        if (null != getAVContext()) {
            return getAVContext().getVideoCtrl();
        }
        return null;
    }


    /**
     * 获取IMSDK 句柄
     *
     * @return
     */
    public TIMManager getTIMManger() {
        return TIMManager.getInstance();
    }


    /**
     * 获取应用上下文Context
     *
     * @return ApplicationContext
     */
    public Context getAppContext() {
        return mApplicationContext;
    }

    public void setAppContext(Context context) {
        mApplicationContext = context.getApplicationContext();
    }

    // 新定义接口
}
