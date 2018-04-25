package com.android.xjq.utils;

import android.os.Handler;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.library.Utils.LogUtils;

/**
 * 结果轮询工具类
 * <p>
 * Created by lingjiu
 */
public class GetPollingResultUtil {


    private Handler handler;
    private boolean stop = false;
    //总的请求次数
    private int requestCount = -1;

    //当前请求第几次
    private int currentRequestCount = 0;
    private PollingCallback pollingCallback;
    private WrapperHttpHelper httpHelper;

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public GetPollingResultUtil(PollingCallback pollingCallback, IHttpResponseListener listener) {
        handler = new Handler();
        this.pollingCallback = pollingCallback;
        httpHelper = new WrapperHttpHelper(listener);
    }


    /**
     * 每隔5秒请求一次
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
            if (currentRequestCount == requestCount) {
                //数据重置
                currentRequestCount = 0;
                return;
            }
            LogUtils.e("GetPollingResultUtil", "currentRequestCount=" + currentRequestCount);
            getPayResultData();
            handler.postDelayed(this, 5000);
        }
    };

    private void getPayResultData() {
        currentRequestCount++;
        if (pollingCallback != null) pollingCallback.onTick(httpHelper, currentRequestCount);
    }

    public interface PollingCallback {
        void onTick(WrapperHttpHelper httpHelper, int currentRequestCount);
    }

    /**
     * 停止获取数据
     */
    public void stop() {
        handler.removeCallbacks(requestRunnable);
        currentRequestCount = 1;
        stop = true;
    }

    public void onResume() {
        stop = false;
        startGetData();
    }

    public void onDestroy() {
        stop = true;
        handler.removeCallbacks(requestRunnable);
        handler = null;
        pollingCallback = null;
    }

}
