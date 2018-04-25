package com.android.xjq;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.utils.SharePreferenceUtils;
import com.android.banana.groupchat.msglist.realm.MyDatabaseMigration;
import com.android.banana.groupchat.msglist.realm.MyRealmHelper;
import com.android.bughdupdate.BuglyUpdateUtils;
import com.android.httprequestlib.okhttp.HttpUtil;
import com.android.library.Utils.LogUtils;
import com.android.xjq.controller.schduledetail.injury.BaseBindManager;
import com.android.xjq.service.DownLoadService;
import com.android.xjq.utils.live.SxbLogImpl;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jl.jczj.im.im.ImManager;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.lang.reflect.Method;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.android.banana.commlib.utils.SharePreferenceUtils.TEST_NETWORK_URL;

/**
 * Created by zhouyi on 2017/3/2.
 */

public class XjqApplication extends Application {

    public final static boolean DEBUG = true;

    private static XjqApplication app;

    private static Context context;

    //当前本地保存的版本号，如果每次更新，
    // 需要将本地的图片更新一次，这样下次发布，用户不需要到服务端下载新的礼物背景横幅了
    public static final int GIFT_VERSION = 1;

    //是否显示小窗
    public static boolean isShowFloatingWindow = true;

    public static int step;

    public static RealmConfiguration realmConfig;

    //消息列表保存用的数据库
    private void initRealm() {
        Realm.init(this);

        realmConfig = new RealmConfiguration.Builder()
                .name(MyRealmHelper.DB_NAME) //文件名
                .schemaVersion(0) //版本号
                .migration(new MyDatabaseMigration())
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfig);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        BaseBindManager.init(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = getApplicationContext();
        if (shouldInit()) {
            SxbLogImpl.init(getApplicationContext());
            //初始化APP
            //InitBusinessHelper.initApp(context,0,0);
        }

        Fresco.initialize(context);

        setSamsungPhone();

        BuglyUpdateUtils.buglyUpgradeInit(this, R.drawable.icon_app_logo);

        SharePreferenceUtils.init(this);

        initNetworkUrl();

        initHttpClient();

        initSocialShare();

        initDownLoadService();

        LoginInfoHelper.init(this);

        initCrashReport();

        if (DEBUG) {
            initCanaryLeak();
        }
        initRealm();

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                LiveActivity.startLiveActivity(MainActivity.mSelf, 100785);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        LiveActivity.mSelf.finish();
//                    }
//                }, 10000);
//            }
//        }, 10000);
    }

    private void initCrashReport() {
        CrashReport.initCrashReport(getApplicationContext(), "3521e30aa3", false);
        CrashHandler.getInstance().init(this);
    }


    private void initDownLoadService() {
        try {
            startService(new Intent(this, DownLoadService.class));
        } catch (Exception e) {
        }
    }

    private void initSocialShare() {
        UMShareAPI.get(this);
        Config.DEBUG = BuildConfig.DEBUG;
        //微信 Appid appsecret
        PlatformConfig.setWeixin("wxf2a606073cef8a40", "wxf2a606073cef8a40");

        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("867246490", "867246490", "https://sns.whalecloud.com/sina2/callback");

        //QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1106288558", "eM1A9JO86XxGByn9");
    }

    private void initCanaryLeak() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This pro
            // cess is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }


    private void getHeapSize() {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        int heapSize = manager.getMemoryClass();

        Log.e("当前堆内存大小", heapSize + " ");

    }

    //三星部分手机会出现剪切板内存溢出问题
    private void setSamsungPhone() {
        //三星手机在这边存在内存溢出
        try {
            if ("samsung".equalsIgnoreCase(Build.MANUFACTURER) &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                    Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                Class cls = Class.forName("android.sec.clipboard.ClipboardUIManager");
                Method m = cls.getDeclaredMethod("getInstance", Context.class);
                m.setAccessible(true);
                Object o = m.invoke(null, getApplicationContext());
                Log.e("三星手机测试", o.toString());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initNetworkUrl() {
        ApplicationInfo appInfo = null;
        try {

            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            step = appInfo.metaData.getInt("step", 2);

            setUrl(step);

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
    }

    public static void setUrl(int step) {
        if (DEBUG) {
            step = (int) SharePreferenceUtils.getData(TEST_NETWORK_URL, 0);
            //setJjxUrl(step);
        }
        switch (step) {
            case 0: //线下
                //mapi.xjq.net
                AppParam.API_DOMAIN = "http://mapi1.xjq.net";
                AppParam.FT_API_DOMAIN = "http://ftapi.huored.net";
                AppParam.BT_API_DOMAIN = "http://btapi.huored.net";
                AppParam.JCH_IMAGE_URL = "http://img.jchapi.huored.net";
                AppParam.FT_IMAGE_DOWNLOAD_URL = "http://ft.huored.net/team/logo/%1$s.resource?default=1";
                AppParam.BT_IMAGE_DOWNLOAD_URL = "http://bt.huored.net/team/logo/%1$s.resource?default=1";
                com.android.banana.groupchat.AppParam.IM_MAPI_XJQ = "http://immapi.xjq.net";
                ImManager.offLine();
                break;
            case 2:
                AppParam.API_DOMAIN = "http://mapi.xjball.com";
                AppParam.FT_API_DOMAIN = "http://ftapi.jczj123.com";
                AppParam.BT_API_DOMAIN = "http://btapi.jczj123.com";
                AppParam.JCH_IMAGE_URL = "http://img-api.jczj123.com";
                AppParam.FT_IMAGE_DOWNLOAD_URL = "http://ft.jczj123.com/team/logo/%1$s.resource?default=1";
                AppParam.BT_IMAGE_DOWNLOAD_URL = "http://bt.jczj123.com/team/logo/%1$s.resource?default=1";
                com.android.banana.groupchat.AppParam.IM_MAPI_XJQ = "http://immapi.xjball.com";
                ImManager.onLine();
                break;
        }
    }

    /**
     * 初始化网络连接
     */
    private void initHttpClient() {
        setHttpClient();
    }

    private void setHttpClient() {
        HttpUtil.applicationContext = context;
        if (DEBUG) {
            com.android.httprequestlib.okhttp.HttpUtil.init(new ChuckInterceptor(context));
        } else {
            com.android.httprequestlib.okhttp.HttpUtil.init(new ApiHeaderInterceptor());
        }
        LogUtils.DEBUG = DEBUG;
        HttpUtil.DEBUG = DEBUG;

    }


    /*private void setHttpClient(AsyncHttpClient httpClient) {
        httpClient.setTimeout(10 * 1000); // 设置链接超时，如果不设置，默认为10s
        httpClient.addHeader("appName", "hhs-android");
        httpClient.addHeader("appVersion", LibAppUtil.getPackageInfo(this).versionName);
        httpClient.addHeader("appUserAgent", android.os.Build.VERSION.RELEASE + "-" + android.os.Build.MODEL);
    }*/

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static Context getContext() {
        return context;
    }

    public static XjqApplication getInstance() {
        return app;
    }
}
