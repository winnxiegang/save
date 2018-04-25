package com.android.xjq.utils;

import android.os.Handler;

import com.android.xjq.bean.live.LiveCommentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2017/7/14.
 */

public class GiftMsgUtils {
    private List<LiveCommentBean> list = new ArrayList<>();
    private Handler mHandler = new Handler();
    private static final int giftDelayTime = 1500;

    //消息接受器
    public void setTimerSendReceiver(TimerSendReceiver timerSendReceiver) {
        this.timerSendReceiver = timerSendReceiver;
    }

    private TimerSendReceiver timerSendReceiver;

    //添加消息
    public void addMsg(LiveCommentBean bean) {
        list.add(bean);
    }


    private Runnable sendMsgTask = new Runnable() {
        @Override
        public void run() {
            if (list != null && list.size() > 0) {
                LiveCommentBean bean = list.remove(0);
                timerSendReceiver.pullMsg(bean);
            }
            mHandler.postDelayed(this, giftDelayTime);
        }
    };

    public void onResume() {
        if (mHandler != null) {
            mHandler.removeCallbacks(sendMsgTask);
            mHandler.postDelayed(sendMsgTask, 200);
        }
    }

    public void onPause() {
        list.clear();
        if (mHandler != null) {
            mHandler.removeCallbacks(sendMsgTask);
        }
    }

    public void onDestroy() {
        list.clear();
        list = null;
        timerSendReceiver = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public interface TimerSendReceiver {
        void pullMsg(LiveCommentBean bean);
    }
}
