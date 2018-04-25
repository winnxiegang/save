package com.jl.jczj.im.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.jl.jczj.im.bean.IMParams;
import com.jl.jczj.im.body.ImFormBody;

import org.json.JSONObject;

/**
 * Created by mrs on 2017/5/8.
 */

public class ExitChatGroupService extends IntentService implements OnHttpResponseListener {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private HttpRequestHelper requestHelper;

    public ExitChatGroupService() {
        super("ExitChatGroupService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        requestHelper = new HttpRequestHelper(null, this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null)
            return;
        if (TextUtils.isEmpty(IMParams.authId)){
            stopSelf();
            return;
        }
        ImFormBody params = new ImFormBody(IMUrlEnum.GROUP_USER_ENTRY_LEAVE, true);
        params.put("authId", IMParams.authId);
        params.put("userId", IMParams.userId);
        params.put("identifyId", IMParams.identifyId);
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        requestHelper.addRequest(params);
    }

    @Override
    public void executorSuccess(RequestContainer request, JSONObject jo) {

    }

    @Override
    public void executorFalse(RequestContainer request, JSONObject jo) {

    }

    @Override
    public void executorFailed(RequestContainer request) {

    }

    @Override
    public void executorFinish() {

    }
}
