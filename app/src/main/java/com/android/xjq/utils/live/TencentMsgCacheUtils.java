package com.android.xjq.utils.live;

import android.os.Handler;

import com.android.xjq.bean.live.LiveCommentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2018/3/23.
 */

public class TencentMsgCacheUtils {
    private List<LiveCommentBean> list = new ArrayList<>();
    private Handler mHandler = new Handler();
    private long TIM_INTERVAL = 300;

    //消息接受器
    public void setTimerSendReceiver(TencentMsgCacheUtils.TimerSendReceiver timerSendReceiver) {
        this.timerSendReceiver = timerSendReceiver;
    }

    private TencentMsgCacheUtils.TimerSendReceiver timerSendReceiver;

    //添加消息
    public void addMsg(LiveCommentBean bean) {
        list.add(bean);
    }


    private Runnable sendMsgTask = new Runnable() {
        @Override
        public void run() {
           /* if (list != null && list.size() > 0) {
                LiveCommentBean bean = list.remove(0);
                timerSendReceiver.pullMsg(bean);
            }*/
            //缓存消息大于5条,取三条,否则取一条
            int timMsgCount = list.size() > 5 ? 3 : 1;
            TIM_INTERVAL = list.size() > 5 ? 500L : 300L;
            for (int i = 0; i < timMsgCount; i++) {
                if (list == null || list.size() <= 0)
                    break;
                LiveCommentBean bean = list.remove(0);
                timerSendReceiver.pullMsg(bean);
            }

            mHandler.postDelayed(this, TIM_INTERVAL);
        }
    };

    public void onResume() {
        if (mHandler != null) {
            mHandler.removeCallbacks(sendMsgTask);
            mHandler.postDelayed(sendMsgTask, 200);
        }
    }

    public void onPause() {
        //list.clear();
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
            mHandler = null;
        }
    }

    public interface TimerSendReceiver {
        void pullMsg(LiveCommentBean bean);
    }
}
