package com.android.banana.commlib.liveScore;

import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

/**
 * Created by lingjiu on 2018/1/31.
 */

public interface LiveMatchDataCallback {
    void onSuccess();

    void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed);
}
