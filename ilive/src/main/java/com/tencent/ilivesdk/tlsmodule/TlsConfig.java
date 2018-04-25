package com.tencent.ilivesdk.tlsmodule;


/**
 * TLS 配置项
 */
public class TlsConfig {

    // TLS SDK
    public static String COUNTRY_CODE = "86";
    public static int LANGUAGE_CODE = 2052;
    public static int TIMEOUT = 8000;
    public static String APP_VERSION = "1.0";     // 指的是TLSDemo的版本号，而不是TLSSDK的版本号

    // QQ SDK
    public static String QQ_APP_ID = "";
    public static String QQ_APP_KEY = "";

    // WeChat SDK
    public static String WX_APP_ID;
    public static String WX_APP_SECRET;



    public static void setQqAppIdAndAppKey(String qqAppId, String qqAppKey) {
        QQ_APP_ID = qqAppId;
        QQ_APP_KEY = qqAppKey;
    }

    public static void setWxAppIdAndAppSecret(String wxAppId, String wxAppSecret) {
        WX_APP_ID = wxAppId;
        WX_APP_SECRET = wxAppSecret;
    }


    public static void setTimeout(int timeout) {
        TIMEOUT = timeout;
    }
}
