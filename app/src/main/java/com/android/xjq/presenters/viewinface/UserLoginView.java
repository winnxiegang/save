package com.android.xjq.presenters.viewinface;

import com.android.banana.commlib.base.IMvpView;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

/**
 * 登录回调
 * <p>
 * Created by lingjiu on 2017/10/23.
 */

public interface UserLoginView extends IMvpView {

    void authLoginSuccess(boolean userTagEnabled);

    void anonymousLoginSuccess();

    void authLoginFailed(RequestContainer requestContainer, JSONObject jsonObject);
}
