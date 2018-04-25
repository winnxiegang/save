package com.android.xjq.presenters;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BasePresenter;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.XjqApplication;
import com.android.xjq.presenters.viewinface.UserLoginView;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.encrypt.DesedeCryptor;
import com.tencent.ilivesdk.ILiveCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static com.android.xjq.utils.XjqUrlEnum.QCLOUD_TLS_USER_SIGN_GET;

/**
 * Created by lingjiu on 2017/11/3.
 */

public class LoginHelper2 extends BasePresenter<UserLoginView> implements OnHttpResponseListener {

    private HttpRequestHelper httpRequestHelper;
    private String phone;
    public static final String KEY = "cD6JFvclXnWbW57W056bKoVDGp2KlA2z";

    private boolean userTagEnabled;//是否需要跳转兴趣页面

    public LoginHelper2(UserLoginView view) {
        super(view);
        httpRequestHelper = new HttpRequestHelper(this, getName());
    }


    /**
     * 腾讯云登录
     */
    public void autonmLogin() {
        //mvpView.showProgressDialog(null);
        httpRequestHelper.startRequest(postQLSLogin());
    }

    /**
     * 用户名密码登录
     */
    public void startLogin(String phone, String password) {
        this.phone = phone;
        mvpView.showProgressDialog(null);
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.USER_LOGIN, false);
        map.put("cell", phone);
        map.put("loginPassword", password);
        httpRequestHelper.startRequest(map);
    }

    private XjqRequestContainer postQLSLogin() {
        XjqRequestContainer map = new XjqRequestContainer(QCLOUD_TLS_USER_SIGN_GET, true);
        map.put("identifier", UUID.randomUUID().toString());
        return map;
    }

    private void responseSuccessLoginInfoLogin(JSONObject jsonObject) throws JSONException {
        String signKey = jsonObject.getString("signKey");
        String loginKey = jsonObject.getString("loginKey");
        String loginName = jsonObject.getString("loginName");
        String userId = jsonObject.getString("userId");
        String userLogoUrl = jsonObject.getString("userLogoUrl");
        String cryptKey = jsonObject.getString("cryptKey");
        userTagEnabled = jsonObject.getBoolean("userTagEnabled");
        boolean selectedUserTag = jsonObject.getBoolean("selectedUserTag");
        userTagEnabled = userTagEnabled && (!selectedUserTag);
        LoginInfoHelper helper = LoginInfoHelper.getInstance();
        helper.setUserSignKey(signKey);
        helper.setUserLoginKey(loginKey);
        helper.setNickName(loginName);
        helper.setUserId(userId);
        helper.setUserLogoUrl(userLogoUrl);
        helper.setUserCryptKey(cryptKey);
        helper.setUserName(phone);
        LoginInfoHelper.getInstance().writeUserInfoToCache();
        //登录腾讯云
        autonmLogin();
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
            //登录腾讯云
            loginILiveManager(identifier, decryptUserSign, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void loginILiveManager(final String identifier,
                                  final String appUserSign,
                                  final boolean isAnonymous) {

        InitBusinessHelper.reLoginLive(identifier, appUserSign, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (mvpView == null) {
                    return;
                }
                mvpView.hideProgressDialog();
                if (isAnonymous) {
                    mvpView.anonymousLoginSuccess();
                } else {
                    mvpView.authLoginSuccess(userTagEnabled);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.showLong(XjqApplication.getContext(), "登录失败，请重新登录");
                if (mvpView != null) {
                    mvpView.hideProgressDialog();
                    // mvpView.authLoginFailed(null, null);
                }

            }
        });
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch ((XjqUrlEnum) requestContainer.getRequestEnum()) {
                case USER_LOGIN:
                    responseSuccessLoginInfoLogin(jsonObject);
                    break;
                case QCLOUD_TLS_USER_SIGN_GET:
                    responseQLSLogin(jsonObject);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            if (QCLOUD_TLS_USER_SIGN_GET == requestContainer.getRequestEnum()) {
                ErrorBean errorBean = new ErrorBean(jsonObject);
                String name = errorBean.getError().getName();
                //首页登陆腾讯云的时候,不处理异地登陆问题
                if ("USER_LOGIN_EXPIRED".equals(name) || "LOGIN_ELSEWHERE".equals(name)) return;
            }
            if (mvpView != null) {
                mvpView.hideProgressDialog();
                mvpView.showErrorMsg(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {
        if (mvpView != null) mvpView.hideProgressDialog();
    }

    @Override
    public void executorFinish() {
        //if (mvpView != null) mvpView.hideProgressDialog();
    }


}
