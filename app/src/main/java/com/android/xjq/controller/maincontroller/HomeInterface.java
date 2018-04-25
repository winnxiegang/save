package com.android.xjq.controller.maincontroller;

import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

/**
 * Created by zaozao on 2017/11/26.
 */

public interface HomeInterface<T> {

    void responseSuccessHttp(T jo, RequestContainer requestContainer);

    void responseFalseHttp(JSONObject jo, RequestContainer requestContainer);
}
