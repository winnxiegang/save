package com.android.banana.commlib.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.library.Utils.LogUtils;

/**
 * Created by lingjiu on 2017/3/16.
 */

public class CountdownTextView extends TextView {

    private CountDownTimer countDownTimer;
    /**
     * 倒计时时长
     */
    private long countdownTime = 3 * 1000;

    /**
     * 倒计时结束监听
     */
    private OnCountdownListener onCountdownListener;

    private String formatPattern;

    public interface OnCountdownListener {
        void countdownDuring(long countdownTime);

        void countdownEnd();
    }

    public CountdownTextView(Context context) {
        this(context, null);
    }

    public CountdownTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setOnCountdownListener(OnCountdownListener onCountdownListener) {
        this.onCountdownListener = onCountdownListener;
    }


    public void start(long countdownTime, String formatPattern) {
        this.formatPattern = formatPattern;
        start(countdownTime);
    }

    public void start(long countdownTime) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        this.countdownTime = countdownTime;
        countDownTimer = new CountDownTimer(this.countdownTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (formatPattern != null)
                    setText(String.format(formatPattern, TimeUtils.timeFormat(millisUntilFinished)));
                if (onCountdownListener != null) {
                    onCountdownListener.countdownDuring(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                if (onCountdownListener != null) {
                    onCountdownListener.countdownEnd();
                }
            }
        };

        countDownTimer.start();
    }

    public void cancel() {
        if (countDownTimer == null) {
            return;
        }
        countDownTimer.cancel();
        countDownTimer = null;
    }


    /**
     * 默认倒计时多少秒
     *
     * @param time
     * @return
     */
    public CharSequence stringFormat(long time) {
        StringBuilder ssb = new StringBuilder();
        int miao = (int) (time / 1000);
        ssb.append(miao + "s后重新发送");
        return ssb;
    }

    //计算剩余天时分秒
    public static String formatSecond(double s){//秒单位
        String restTime ="";
        if(s != 0){
            String format;
            Object[] array;
            Integer days = (int)(s/(60*60*24));
            Integer hours =(int) (s/(60*60)-days*24);
            Integer minutes = (int) (s/60-hours*60-days*24*60);
            Integer seconds = (int) (s-minutes*60-hours*60*60-days*24*60*60);
//            format="%1$,d：%2$,d：%3$,d：%4$,d";
//            array=new Object[]{days,hours,minutes,seconds};
//            restTime= String.format(format, array);

            if(days>0){
                restTime = days+"天"+hours+"时"+minutes+"分";
            }else if(hours>0){
                restTime = hours+"时"+minutes+"分";
            }else if(minutes>0){
                restTime = minutes+"分";
            }else{
            }
        }else{
        }
        LogUtils.e("kk","restTime----"+restTime);
        return  restTime;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (countDownTimer == null) {
            return;
        }
        countDownTimer.cancel();
        countDownTimer = null;
    }
}
