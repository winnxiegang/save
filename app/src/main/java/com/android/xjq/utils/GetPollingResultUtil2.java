package com.android.xjq.utils;

import android.content.Context;
import android.os.Handler;

import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

/**
 * 结果轮询工具类
 * <p>
 * Created by lingjiu
 */
public class GetPollingResultUtil2 implements OnHttpResponseListener {


    private Handler handler;

    private boolean stop = false;

    private PollingCallback callback;
    //总的请求次数
    private int requestCount = 3;
    //当前请求次数
    private int currentRequestCount = 0;

    private HttpRequestHelper httpRequestHelper;

    public GetPollingResultUtil2(Context context, PollingCallback callback) {

        this(context,callback,3);
    }

    public GetPollingResultUtil2(Context context, PollingCallback callback, int requestCount) {

        handler = new Handler();

        httpRequestHelper = new HttpRequestHelper(context, this);

        this.callback = callback;

        this.requestCount = requestCount;
    }

    /**
     * 每隔2秒请求一次
     */
    public void startGetData() {
        stop = false;

        handler.removeCallbacks(requestRunnable);

        handler.post(requestRunnable);
    }

    private Runnable requestRunnable = new Runnable() {
        @Override
        public void run() {
            if (stop) return;

            if (currentRequestCount >= requestCount) {
                return;
            }

            getPayResultData();

            handler.postDelayed(this, 5000);
        }
    };

    private void getPayResultData() {
        callback.startPollingRequest(httpRequestHelper);
        currentRequestCount++;
    }

    /**
     * 停止获取数据
     */
    public void stop() {
        currentRequestCount = 1;
        handler.removeCallbacks(requestRunnable);
        stop = true;
    }

    public void onStop() {
        stop = true;
    }

    public void onResume() {
        stop = false;
        startGetData();
    }

    public void onDestroy() {
        handler.removeCallbacks(requestRunnable);
        callback = null;
        stop = true;
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        callback.pollingResultSuccess();
        stop();
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        //请求失败继续请求,直到达到次数为止
        if (currentRequestCount == requestCount) {
            callback.pollingResultFailed();
            stop();
            return;
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {
        //网络断开,也直接停止轮询
        callback.pollingResultFailed();
        stop();
    }

    @Override
    public void executorFinish() {

    }
}
