package com.android.xjq.view;

import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

/**
 * Created by zhouyi on 2017/4/18.
 */

public interface UserLoginListener {

    public void authLoginSuccess();

    public void authLoginFailed(RequestContainer requestContainer, JSONObject jsonObject);

    public void anonymousLoginSuccess();

    public void anonymousLoginFailed();

}
