package com.android.xjq.presenters;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BasePresenter;
import com.android.banana.commlib.http.LoginSignTypeEnum;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.XjqApplication;
import com.android.xjq.bean.login.UserLoginBean;
import com.android.xjq.bean.login.UserSimplesBean;
import com.android.xjq.presenters.viewinface.UserLoginView;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.encrypt.DesedeCryptor;
import com.google.gson.Gson;
import com.tencent.ilivesdk.ILiveCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.android.xjq.utils.XjqUrlEnum.QCLOUD_TLS_USER_SIGN_GET;
import static com.android.xjq.utils.XjqUrlEnum.USER_LOGIN;

/**
 * Created by lingjiu on 2017/10/23.
 */

public class LoginHelper extends BasePresenter<UserLoginView> implements OnHttpResponseListener {

    private HttpRequestHelper httpRequestHelper;
    private String phone;
    private String password;
    private UserLoginBean userLoginBean;
    private String defaultUserId;
    public static final String KEY = "cD6JFvclXnWbW57W056bKoVDGp2KlA2z";
    //是否需要重新登录sdk
    private boolean mNeedSdkReLogin = true;

    private boolean userTagEnabled;//是否需要跳转兴趣页面

    public LoginHelper(UserLoginView view) {
        super(view);
        httpRequestHelper = new HttpRequestHelper(this, getName());
    }

    public LoginHelper(UserLoginView view, boolean needSdkReLogin) {
        super(view);
        this.mNeedSdkReLogin = needSdkReLogin;
        httpRequestHelper = new HttpRequestHelper(this, getName());
    }

    /**
     * 匿名登录
     */
    public void anonymousLogin() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.QCLOUD_TLS_USER_SIGN_ANONYMOUS_GET, false);
        map.put("identifier", UUID.randomUUID().toString());
        httpRequestHelper.startRequest(map);
    }

    /**
     * 实名登录
     */
    public void autonmLogin() {
        XjqRequestContainer map = new XjqRequestContainer(QCLOUD_TLS_USER_SIGN_GET, true);
        map.put("identifier", UUID.randomUUID().toString());
        //map.put("authedUserId",LoginInfoHelper.getInstance().getUserId());
        httpRequestHelper.startRequest(map);
    }

    /**
     * 用户名密码登录
     */
    public void startLogin(String phone, String password) {
        mvpView.showProgressDialog(null);
        this.phone = phone;
        this.password = password;
        List<RequestContainer> list = new ArrayList<>();
        list.add(postOneAuthLogin());
        httpRequestHelper.startRequest(list);
    }

    /**
     * 角色切换
     *
     * @param loginUserId
     */
    public void selectorRoleLogin(String loginUserId) {
        mvpView.showProgressDialog(null);
        defaultUserId = loginUserId;
        List<RequestContainer> list = new ArrayList<>();
        list.add(postUserLogin());
        httpRequestHelper.startRequest(list);
    }

    private XjqRequestContainer postOneAuthLogin() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.ONE_AUTH_LOGIN, false);
        map.put("cell", phone);
        map.put("password", password);
        map.setSubDependencyListener(new RequestContainer.SubDependencyListener() {
            @Override
            public void execute() {
                httpRequestHelper.addRequest(postUserLogin());
            }
        });
        return map;
    }

    private XjqRequestContainer postUserLogin() {
        XjqRequestContainer map = new XjqRequestContainer(USER_LOGIN, true, LoginSignTypeEnum.ONE_AUTH_SIGN);
        map.put("loginUserId", defaultUserId);
        map.setSubDependencyListener(new RequestContainer.SubDependencyListener() {
            @Override
            public void execute() {
                httpRequestHelper.addRequest(postQLSLogin());
            }
        });
        return map;
    }

    private XjqRequestContainer postQLSLogin() {
        XjqRequestContainer map = new XjqRequestContainer(QCLOUD_TLS_USER_SIGN_GET, true);
        map.put("identifier", UUID.randomUUID().toString());
        return map;
    }

    private void responseSuccessUserLogin(JSONObject jsonObject) {
        try {
            JSONObject userPlatformLogin = jsonObject.getJSONObject("userPlatformLogin");
            String userId = userPlatformLogin.getString("userId");
            String signKey = userPlatformLogin.getString("signKey");
            String loginKey = userPlatformLogin.getString("loginKey");
            String cryptKey = userPlatformLogin.getString("cryptKey");
            JSONObject loginUserInfo = jsonObject.getJSONObject("loginUserInfo");
            String loginName = loginUserInfo.getString("loginName");
            String avatarUrl = loginUserInfo.getString("avatarUrl");
            String userLevelPicUrl = loginUserInfo.getString("userLevelPicUrl");
            userTagEnabled = loginUserInfo.getBoolean("userTagEnabled");
            String thirdChannelAndLogoUrl = "";
            if (loginUserInfo.has("thirdChannelAndLogoUrlMap")) {
                JSONObject thirdChannelAndLogoUrlMap = loginUserInfo.getJSONObject("thirdChannelAndLogoUrlMap");
                if (thirdChannelAndLogoUrlMap.has("OAP")) {
                    thirdChannelAndLogoUrl = thirdChannelAndLogoUrlMap.getString("OAP");
                }
            }
            LoginInfoHelper helper = LoginInfoHelper.getInstance();
            helper.setUserLoginKey(loginKey);
            helper.setUserCryptKey(cryptKey);
            helper.setUserSignKey(signKey);
            helper.setNickName(loginName);
            helper.setThirdChannelAndLogoUrl(thirdChannelAndLogoUrl);
            helper.setUserId(userId);
            LoginInfoHelper.getInstance().writeUserInfoToCache();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessOneAuthLogin(JSONObject jsonObject) {
        userLoginBean = new Gson().fromJson(jsonObject.toString(), UserLoginBean.class);
        defaultUserId = userLoginBean.getUserSelectInfo().getDefaultUserId();
        LoginInfoHelper helper = LoginInfoHelper.getInstance();
        helper.setOneAuthId(userLoginBean.getOneAuthPlatformLogin().getOneAuthId());
        helper.setAuthLoginKey(userLoginBean.getOneAuthPlatformLogin().getLoginKey());
        helper.setAuthSignKey(userLoginBean.getOneAuthPlatformLogin().getSignKey());
        helper.setAuthCryptKey(userLoginBean.getOneAuthPlatformLogin().getCryptKey());
        helper.setUserName(phone);
        for (UserSimplesBean bean : userLoginBean.getUserSelectInfo().getUserSimples()) {
            if (bean.getUserId().equals(userLoginBean.getUserSelectInfo().getDefaultUserId())) {
                LoginInfoHelper.getInstance().setNickName(bean.getLoginName());
                LoginInfoHelper.getInstance().setUserId(bean.getUserId());
            }
        }
    }

    private void responseQLSLogin(JSONObject jsonObject) {
        try {

            JSONObject liveInfo = jsonObject.getJSONObject("liveInfo");

            int accountType = liveInfo.getInt("accountType");
            String appSdkId = liveInfo.getString("appSdkId");
            String identifier = liveInfo.getString("identifier");
            String userSig = liveInfo.getString("userSig");

            String decryptUserSign = new DesedeCryptor(LoginInfoHelper.getInstance().getUserCryptKey()).decrypt(userSig);

            LoginInfoHelper.getInstance().setAccountNo(String.valueOf(accountType));
            LoginInfoHelper.getInstance().setAppId(appSdkId);

            LoginInfoHelper.getInstance().setUserAppUserSign(decryptUserSign);
            LoginInfoHelper.getInstance().setUserIdentifier(identifier);

            LoginInfoHelper.getInstance().writeAppInfoToCache();
            LoginInfoHelper.getInstance().writeUserInfoToCache();

            loginILiveManager(identifier, decryptUserSign, false);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void responseSuccessAnonymousLogin(JSONObject jsonObject) {

        JSONObject liveInfo = null;

        try {
            liveInfo = jsonObject.getJSONObject("liveInfo");
            int accountType = liveInfo.getInt("accountType");
            String appSdkId = liveInfo.getString("appSdkId");

            String identifier = liveInfo.getString("identifier");
            String userSig = liveInfo.getString("userSig");

            String decryptUserSign = new DesedeCryptor(KEY).decrypt(userSig);

            LoginInfoHelper.getInstance().setAccountNo(String.valueOf(accountType));
            LoginInfoHelper.getInstance().setAppId(appSdkId);

            LoginInfoHelper.getInstance().setGuestAppUserSign(decryptUserSign);
            LoginInfoHelper.getInstance().setGuestIdentifier(identifier);

            LoginInfoHelper.getInstance().writeAppInfoToCache();
            LoginInfoHelper.getInstance().writeGuestInfoToCache();

            loginILiveManager(identifier, decryptUserSign, true);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void loginILiveManager(final String identifier,
                                  final String appUserSign,
                                  final boolean isAnonymous) {

        if (!mNeedSdkReLogin && mvpView != null) {
            if (isAnonymous) {
                mvpView.anonymousLoginSuccess();
            } else {
                mvpView.authLoginSuccess(userTagEnabled);
            }
            return;
        }

        InitBusinessHelper.reLoginLive(identifier, appUserSign, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (mvpView == null) {
                    return;
                }
                if (isAnonymous) {
                    mvpView.anonymousLoginSuccess();
                } else {
                    mvpView.authLoginSuccess(userTagEnabled);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.showLong(XjqApplication.getContext(), "登录失败，请重新登录");
            }
        });
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        if (mvpView == null) {
            return;
        }
        switch ((XjqUrlEnum) requestContainer.getRequestEnum()) {
            case ONE_AUTH_LOGIN:
                responseSuccessOneAuthLogin(jsonObject);
                break;
            case USER_LOGIN:
                responseSuccessUserLogin(jsonObject);
                break;
            case QCLOUD_TLS_USER_SIGN_GET:
                responseQLSLogin(jsonObject);
                break;
            case QCLOUD_TLS_USER_SIGN_ANONYMOUS_GET:
                responseSuccessAnonymousLogin(jsonObject);
                break;
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        mvpView.authLoginFailed(requestContainer, jsonObject);
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {
        mvpView.hideProgressDialog();
    }
}
