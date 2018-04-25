package com.android.banana.commlib;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by zhouyi on 2017/3/30.
 */

public class LoginInfoHelper {

    //保存的个人信息文件名
    private static final String MY_USER_INFO = "my_user_info";

    private static final String USER_ID = "user_id";
    private static final String ROLE_ID = "role_id";

    private static final String USER_NAME = "user_name";
    private static final String USER_LOGO_URL = "userLogoUrl";
    private static final String NICKNAME = "nickname";
    private static final String ONE_AUTH_ID = "one_auth_id";
    private static final String USER_LEVEL = "user_level";
    private static final String THIRD_CHANNEL_AND_LOGO_URL = "third_channel_and_logo_url";

    private static final String AUTH_CRYPT_KEY = "auth_crypt_key";
    private static final String AUTH_LOGIN_KEY = "auth_login_key";
    private static final String AUTH_SIGN_KEY = "auth_sign_key";

    private static final String USER_CRYPT_KEY = "user_crypt_key";
    private static final String USER_LOGIN_KEY = "user_login_key";
    private static final String USER_SIGN_KEY = "user_sign_key";

    private static final String ACCOUNT_NO = "account_no";
    private static final String APP_ID = "app_id";

    private static final String APP_ID_AT_THIRD = "app_id_at_third";
    private static final String USER_APP_USER_SIGN = "user_app_user_sign";
    private static final String USER_IDENTIFIER = "user_app_identifier";

    private static final String GUEST_APP_USER_SIGN = "guest_app_user_sign";
    private static final String GUEST_IDENTIFIER = "guest_app_identifier";

    private String userId;
    private String userName;//手机号
    private String userLogoUrl;      // 头像
    private String nickName;//用户名
    private String roleId;

    private String oneAuthId;
    private String thirdChannelAndLogoUrl;

    private String authLoginKey;
    private String authSignKey;
    private String authCryptKey;

    private String userLoginKey;
    private String userSignKey;
    private String userCryptKey;
    //腾讯sdk初始化信息
    private String accountNo;
    private String appId;

    //游客登录腾讯sdk的信息
    private String guestAppUserSign;
    private String guestIdentifier;

    //已登录账号，登录sdk的信息
    private String userAppUserSign;
    private String userIdentifier;

    private static LoginInfoHelper mLoginHelper;
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static LoginInfoHelper getInstance() {

        if (sContext == null)
            throw new RuntimeException("must call init first");

        if (mLoginHelper == null) {
            mLoginHelper = new LoginInfoHelper();
            mLoginHelper.getCache();
        }
        return mLoginHelper;
    }

    public static boolean isLogin() {
        return getInstance().getUserId() != null;
    }


    public void writeUserInfoToCache() {
        SharedPreferences settings = sContext.getSharedPreferences(MY_USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_NAME, userName);
        editor.putString(USER_ID, userId);
        editor.putString(USER_LOGO_URL, userLogoUrl);
        editor.putString(NICKNAME, nickName);
        editor.putString(ROLE_ID, roleId);

        editor.putString(USER_LOGIN_KEY, userLoginKey);
        editor.putString(USER_SIGN_KEY, userSignKey);
        editor.putString(USER_CRYPT_KEY, userCryptKey);
        //腾讯云登录信息
        editor.putString(USER_APP_USER_SIGN, userAppUserSign);
        editor.putString(USER_IDENTIFIER, userIdentifier);

        editor.commit();
    }

    //匿名登录，腾讯服务器登录信息
    public void writeGuestInfoToCache() {
        SharedPreferences settings = sContext.getSharedPreferences(MY_USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(GUEST_APP_USER_SIGN, guestAppUserSign);
        editor.putString(GUEST_IDENTIFIER, guestIdentifier);
        editor.commit();
    }

    //app初始化登录信息
    public void writeAppInfoToCache() {
        SharedPreferences settings = sContext.getSharedPreferences(MY_USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(ACCOUNT_NO, accountNo);
        editor.putString(APP_ID, appId);
        editor.commit();
    }

    public void clearUserName() {
        SharedPreferences settings = sContext.getSharedPreferences(MY_USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_NAME, null);
        editor.commit();
    }

    //清空用户登录数据
    public void clearUserInfoCache() {

        SharedPreferences settings = sContext.getSharedPreferences(MY_USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(USER_ID, null);
        //editor.putString(USER_NAME, null);

        editor.putString(USER_LOGO_URL, null);
        editor.putString(NICKNAME, null);

        editor.putString(USER_LOGIN_KEY, null);
        editor.putString(USER_SIGN_KEY, null);
        editor.putString(USER_CRYPT_KEY, null);


        editor.putString(APP_ID_AT_THIRD, null);
        editor.putString(USER_APP_USER_SIGN, null);
        editor.putString(USER_IDENTIFIER, null);

        editor.commit();
    }


    //获取本地缓存
    public void getCache() {
        SharedPreferences sharedata = sContext.getSharedPreferences(MY_USER_INFO, 0);
        userId = sharedata.getString(USER_ID, null);
        roleId = sharedata.getString(ROLE_ID, null);
        userName = sharedata.getString(USER_NAME, null);
        userLogoUrl = sharedata.getString(USER_LOGO_URL, null);
        nickName = sharedata.getString(NICKNAME, null);
        oneAuthId = sharedata.getString(ONE_AUTH_ID, null);
        thirdChannelAndLogoUrl = sharedata.getString(THIRD_CHANNEL_AND_LOGO_URL, null);

        authCryptKey = sharedata.getString(AUTH_CRYPT_KEY, null);
        authLoginKey = sharedata.getString(AUTH_LOGIN_KEY, null);
        authSignKey = sharedata.getString(AUTH_SIGN_KEY, null);

        userCryptKey = sharedata.getString(USER_CRYPT_KEY, null);
        userLoginKey = sharedata.getString(USER_LOGIN_KEY, null);
        userSignKey = sharedata.getString(USER_SIGN_KEY, null);

        accountNo = sharedata.getString(ACCOUNT_NO, null);
        appId = sharedata.getString(APP_ID, null);

        userAppUserSign = sharedata.getString(USER_APP_USER_SIGN, null);
        userIdentifier = sharedata.getString(USER_IDENTIFIER, null);

        guestAppUserSign = sharedata.getString(GUEST_APP_USER_SIGN, null);
        guestIdentifier = sharedata.getString(GUEST_IDENTIFIER, null);
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogoUrl() {
        return userLogoUrl;
    }

    public void setUserLogoUrl(String userLogoUrl) {
        this.userLogoUrl = userLogoUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOneAuthId() {
        return oneAuthId;
    }

    public void setOneAuthId(String oneAuthId) {
        this.oneAuthId = oneAuthId;
    }

    public String getAuthLoginKey() {
        return authLoginKey;
    }

    public void setAuthLoginKey(String authLoginKey) {
        this.authLoginKey = authLoginKey;
    }

    public String getAuthSignKey() {
        return authSignKey;
    }

    public void setAuthSignKey(String authSignKey) {
        this.authSignKey = authSignKey;
    }

    public String getAuthCryptKey() {
        return authCryptKey;
    }

    public void setAuthCryptKey(String authCryptKey) {
        this.authCryptKey = authCryptKey;
    }

    public String getUserLoginKey() {
        return userLoginKey;
    }

    public void setUserLoginKey(String userLoginKey) {
        this.userLoginKey = userLoginKey;
    }

    public String getUserSignKey() {
        return userSignKey;
    }

    public String getUserCryptKey() {
        return userCryptKey;
    }

    public void setUserCryptKey(String userCryptKey) {
        this.userCryptKey = userCryptKey;
    }

    public void setUserSignKey(String userSignKey) {
        this.userSignKey = userSignKey;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getGuestAppUserSign() {
        return guestAppUserSign;
    }

    public void setGuestAppUserSign(String guestAppUserSign) {
        this.guestAppUserSign = guestAppUserSign;
    }

    public String getGuestIdentifier() {
        return guestIdentifier;
    }

    public void setGuestIdentifier(String guestIdentifier) {
        this.guestIdentifier = guestIdentifier;
    }

    public String getUserAppUserSign() {
        return userAppUserSign;
    }

    public void setUserAppUserSign(String userAppUserSign) {
        this.userAppUserSign = userAppUserSign;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getThirdChannelAndLogoUrl() {
        return thirdChannelAndLogoUrl;
    }

    public void setThirdChannelAndLogoUrl(String thirdChannelAndLogoUrl) {
        this.thirdChannelAndLogoUrl = thirdChannelAndLogoUrl;
    }

}
