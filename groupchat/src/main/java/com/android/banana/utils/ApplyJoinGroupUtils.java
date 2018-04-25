package com.android.banana.utils;

import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

/**
 * Created by zaozao on 2017/10/24.
 * 群聊申请加入的请求，多处使用
 */

public class ApplyJoinGroupUtils implements OnHttpResponseListener{

//    private ApplyJoinListener listener;

    private HttpRequestHelper requestHelper;

    public void setRequestHelper(HttpRequestHelper requestHelper) {
        this.requestHelper = requestHelper;
    }

    //申请加入请求
    private void requestToApplyJoin(){

    }


    @Override
    public void executorSuccess(RequestContainer request, JSONObject jo) {
//        listener.applySuccess();
    }

    @Override
    public void executorFalse(RequestContainer request, JSONObject jo) {
//        listener.applyFalse(jo);
    }

    @Override
    public void executorFailed(RequestContainer request) {
//        listener.applyFailed();
    }

    @Override
    public void executorFinish() {

    }

    private interface ApplyJoinListener{
        void applySuccess();
        void applyFailed();
        void applyFalse(JSONObject jo);
    }
}
