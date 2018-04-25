package com.android.banana.commlib.http;


import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by qiaomu on 2017/6/7.
 */

public interface IHttpResponseListener<T> extends Type {
    void onSuccess(RequestContainer request, T t);

    void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception;
}
