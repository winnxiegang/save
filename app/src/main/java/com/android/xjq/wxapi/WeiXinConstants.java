package com.android.xjq.wxapi;

/**
 * Created by lingjiu on 2017/6/9.
 */

public class WeiXinConstants {
    /**
     * 申请下来的appId
     */
    public static final String APP_ID_ONLINE = "wxf2a606073cef8a40";

    /**
     * 线下
     */
    public static final String APP_ID_OFFLINE = "wx44ad5ddb598c6038";


    public static String appId;

    public static String getAppId() {
        return appId;
    }

    public static void setAppId(String appId) {
        WeiXinConstants.appId = appId;
    }
}
