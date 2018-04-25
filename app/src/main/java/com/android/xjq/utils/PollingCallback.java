package com.android.xjq.utils;

import com.android.httprequestlib.HttpRequestHelper;

/**
 * Created by lingjiu on 2017/8/24.
 */

public interface PollingCallback {

    void startPollingRequest(HttpRequestHelper httpRequestHelper);

    void pollingResultSuccess();

    void pollingResultFailed();
}
