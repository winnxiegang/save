package com.tencent.ilivesdk.tlsmodule;

import android.content.Context;

import com.tencent.ilivesdk.ILiveSDK;

import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSStrAccRegListener;

/**
 * tls账号业务
 */
public class TlsBusiness {

    public static TLSLoginHelper loginHelper;
    public static TLSAccountHelper accountHelper;


    /**
     * 登出
     * @param id
     */
    public static void logout(String id) {
        loginHelper.clearUserInfo(id);
    }

    /**
     * TLS初始化
     * @param context 应用Context
     */
    public static void init(Context context) {
        loginHelper = TLSLoginHelper.getInstance().init(context.getApplicationContext(),
                ILiveSDK.getInstance().getAppId(), ILiveSDK.getInstance().getAccountType(), TlsConfig.APP_VERSION);
        loginHelper.setTimeOut(TlsConfig.TIMEOUT);
        loginHelper.setLocalId(TlsConfig.LANGUAGE_CODE);

        accountHelper = TLSAccountHelper.getInstance().init(context.getApplicationContext(),
                ILiveSDK.getInstance().getAppId(), ILiveSDK.getInstance().getAccountType(), TlsConfig.APP_VERSION);
        accountHelper.setCountry(Integer.parseInt(TlsConfig.COUNTRY_CODE)); // 存储注册时所在国家，只须在初始化时调用一次
        accountHelper.setTimeOut(TlsConfig.TIMEOUT);
        accountHelper.setLocalId(TlsConfig.LANGUAGE_CODE);

    }

    /**
     * 登录TLS账号系统  (托管方式)
     *
     * @param id
     * @param password
     */
    public static int tlsLogin(String id, String password, TLSPwdLoginListener listener) {
        return loginHelper.TLSPwdLogin(id, password.getBytes(), listener);
    }


    /**
     * 在TLS模块注册一个账号 (托管方式)
     *
     * @param id
     * @param psw
     */
    public static int tlsRegister(final String id, final String psw, final TLSStrAccRegListener listener) {
        return accountHelper.TLSStrAccReg(id, psw, listener);
    }


    /**
     * 获取最后一次ID
     * @return id
     */
    public static String getLastUserIdentifier() {
        if (loginHelper == null) return null;
        if (loginHelper.getLastUserInfo() == null) return null;
        return loginHelper.getLastUserInfo().identifier;
    }

    /**
     * 获取指定Sig
     * @param identify  身份
     * @return 返回Sig
     */
    public static String getUserSig(String identify) {
        return loginHelper.getUserSig(identify);
    }

    /**
     * 是否需要登录
     * @param identifier 身份
     * @return true 需要 false 不需要
     */
    public static boolean needLogin(String identifier) {
        if (identifier == null)
            return true;
        return loginHelper.needLogin(identifier);
    }
}
