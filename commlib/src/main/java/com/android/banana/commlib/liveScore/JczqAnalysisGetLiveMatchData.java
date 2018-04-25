package com.android.banana.commlib.liveScore;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.android.banana.commlib.bean.liveScoreBean.JclqMatchLiveBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqMatchLiveBean;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.liveScore.livescoreEnum.FtRaceStatusEnum;
import com.android.banana.commlib.liveScore.livescoreEnum.LiveScoreUrlEnum;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by zhouyi on 2016/5/25 11:44.
 */
public class JczqAnalysisGetLiveMatchData {

    private boolean mIsFootball;

    private String mInnerRaceId;

    WrapperHttpHelper mHttpHelper;

    private long timeStamp = 0;

    private Timer timer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getLiveData();
        }
    };


    private boolean stop = false;

    //当前比赛状态
    private FtRaceStatusEnum mFtStatus = FtRaceStatusEnum.WAIT;

    private IHttpResponseListener mLiveMatchDataCallback;


    public JczqAnalysisGetLiveMatchData(boolean isFootball, String innerRaceId, IHttpResponseListener liveMatchDataCallback) {

        mIsFootball = isFootball;

        mInnerRaceId = innerRaceId;

        mHttpHelper = new WrapperHttpHelper(liveMatchDataCallback);

        mLiveMatchDataCallback = liveMatchDataCallback;

    }

    private void getLiveData() {
        RequestFormBody formBody;
        if (mIsFootball) {
            formBody = new RequestFormBody(LiveScoreUrlEnum.DYNAMIC_DATA_QUERY);
        } else {
            formBody = new RequestFormBody(LiveScoreUrlEnum.DYNAMIC_SCORE_DATA);
        }
        formBody.put("timestamp", String.valueOf(timeStamp));
        formBody.put("raceIds", String.valueOf(mInnerRaceId));

        if (mIsFootball) {
            formBody.setRequestUrl(AppParam.FT_API_DOMAIN + AppParam.FT_API_S_URL);
            formBody.setGenericClaz(JczqMatchLiveBean.class);
        } else {
            formBody.setRequestUrl(AppParam.BT_API_DOMAIN + AppParam.BT_API_S_URL);
            formBody.setGenericClaz(JclqMatchLiveBean.class);
        }

        mHttpHelper.startRequest(formBody, true);
    }

    /**
     * 开始获取数据
     */
    public void startGetData() {

        stop = false;

        if (!canGetData()) {
            return;
        }

        //long betweenTime = TimeUtils.date1SubDate2MsWithNoAbs(mRaceDetail.getJczqData().getStartDate(), mRaceDetail.getNowDate());

       /* if (betweenTime > 0) {
            //startTimer(betweenTime);
        } else {
            getLiveData();
        }*/
        refreshData();

    }

    private boolean canGetData() {
        if (TextUtils.equals(mInnerRaceId, "0")) {
            return false;
        }
        return true;
    }

    private void startTimer(long delay) {
        timer = new Timer();
        timer.schedule(new MyTimerTask(), 0, delay);
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private void refreshData() {
        if (stop) {
            return;
        }
        startTimer(5 * 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 停止获取数据
     */
    public void stop() {

        stop = true;

        stopTimer();

    }

    public FtRaceStatusEnum getFtStatus() {
        return mFtStatus;
    }


    /**
     * 是否可以请求统计。如果是推迟或者延时，依旧显示比赛暂未开始
     */
    public boolean isCanRequestAnalytics() {
        switch (mFtStatus) {
            case POSTPONE:
            case DELAY:
            case DEC:
                return false;
        }
        return true;
    }


    public void onDestroy() {
        stop();
        mHttpHelper.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }

    }
}
