package com.android.xjq.controller;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.library.Utils.LogUtils;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by lingjiu on 2017/3/14.
 */

public class GestureDetectorController extends GestureDetector.SimpleOnGestureListener {

    private LiveActivity mContext;

    private View functionView;

    private boolean isCloseVideo;

    private View operateLayout;

    /**
     * 为避免调节过快,音频灵敏度调节值
     */
    private static float STEP_VOLUME;

    private boolean firstScroll;

    private int GESTURE_FLAG;

    private static final int GESTURE_MODIFY_VOLUME = 1;

    private static final int GESTURE_MODIFY_BRIGHT = 2;

    private int playerHeight;

    private int playerWidth;

    /**
     * 当前音量
     */
    private int currentVolume;

    /**
     * 当前亮度
     */
    private float mBrightness = -1f;

    private ImageView operateIv;

    private TextView operateTv;

    private boolean isLandscape;

    public GestureDetectorController(LiveActivity context, View functionView) {
        mContext = context;

        this.functionView = functionView;

        gestureDetector = new GestureDetector(this);

    }

    /**
     * 横屏时增加对音频,亮度的操作
     *
     * @param context
     * @param functionView
     * @param operateLayout
     */
    public GestureDetectorController(LiveActivity context, View functionView, View operateLayout) {
        mContext = context;

        this.functionView = functionView;

        this.operateLayout = operateLayout;

        operateIv = ((ImageView) operateLayout.findViewById(R.id.operateIv));

        operateTv = ((TextView) operateLayout.findViewById(R.id.operateTv));

        gestureDetector = new GestureDetector(this);

        Display defaultDisplay = mContext.getWindowManager().getDefaultDisplay();

        playerWidth = defaultDisplay.getWidth();

        playerHeight = defaultDisplay.getHeight();

        STEP_VOLUME= playerHeight/15;

    }


    public void setTouchEvent(MotionEvent event) {
        setTouchEvent(event, false);
    }

    public void setTouchEvent(MotionEvent event, boolean isCloseVideo) {

        gestureDetector.onTouchEvent(event);

        this.isCloseVideo = isCloseVideo;
    }

    private GestureDetector gestureDetector;

    private Handler mHandler = new Handler();

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        if (isCloseVideo) {
            return true;
        }
        //双击的时候切换横竖屏
        if (MotionEvent.ACTION_UP == e.getAction()) {
            mContext.setRequestedOrientation(
                    mContext.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ?
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mHandler.removeCallbacks(hideViewRunnable);
        if (functionView.getVisibility() == View.VISIBLE) {
            functionView.setVisibility(View.GONE);
            mContext.full(true);
        } else {
            functionView.setVisibility(View.VISIBLE);
            mContext.full(false);
            mHandler.postDelayed(hideViewRunnable, 5000);
        }

        return true;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        firstScroll = true;// 设定是触摸屏幕后第一次scroll的标志
        return false;
    }

    int downY;

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (operateLayout == null) {
            return false;
        }
        LogUtils.e("GestureDetectorController", "onScroll  firstScroll="+firstScroll);
        float mOldX = e1.getX(), mOldY = e1.getY();
        int y = (int) e2.getRawY();

        if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
            // 横向的距离变化大则调整进度，纵向的变化大则调整音量
            downY = (int) e1.getRawY();
            if (Math.abs(distanceX) <= Math.abs(distanceY)) {
                mHandler.removeCallbacks(hideOperateViewRunnable);
                operateLayout.setVisibility(View.VISIBLE);
                if (mOldX > playerWidth*3 / 5) {// 音量
                    GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
                    if (currentVolume == 0) {
                        operateIv.setImageResource(R.drawable.icon_simple_audio_close);
                        operateTv.setText("静音");
                    } else {
                        operateIv.setImageResource(R.drawable.icon_simple_audio_open);
                        operateTv.setText(currentVolume * 100 / 15 + "%");
                    }
                } else if (mOldX < playerWidth*3 / 5) {// 亮度
                    GESTURE_FLAG = GESTURE_MODIFY_BRIGHT;
                    operateIv.setImageResource(R.drawable.icon_simple_light);
                }
            }
        }

        // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
        if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {

            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                if (downY - y >= STEP_VOLUME) {// 为避免调节过快，distanceY应大于一个设定值
                    setAudio(1);
                    downY = y;
                } else if (downY - y <= -STEP_VOLUME) {// 音量调小
                    setAudio(-1);
                    downY = y;
                }

               /* if (distanceY >= LibAppUtil.dip2px(mContext, STEP_VOLUME)) {// 为避免调节过快，distanceY应大于一个设定值
                    setAudio(1);
                } else if (distanceY <= -LibAppUtil.dip2px(mContext, STEP_VOLUME)) {// 音量调小
                    setAudio(-1);
                }*/
            }
        }

        // 如果每次触摸屏幕后第一次scroll是调节亮度，那之后的scroll事件都处理亮度调节，直到离开屏幕执行下一次操作
        else if (GESTURE_FLAG == GESTURE_MODIFY_BRIGHT) {
            onBrightnessSlide((mOldY - y) / playerHeight);
        }

        firstScroll = false;// 第一次scroll执行完成，修改标志
        return false;
    }

    /**
     * 手指抬起的动作监听
     */
    public void setMotionEventUp() {
        if (operateLayout == null) {
            return;
        }
        //滑动完毕之后获取,抬起的动作
        mHandler.postDelayed(hideOperateViewRunnable, 500);
        mBrightness = mContext.getWindow().getAttributes().screenBrightness;
    }

    public void hideFunctionView(){
        if (functionView.getVisibility() == View.VISIBLE) {
            mHandler.removeCallbacks(hideViewRunnable);
            mHandler.postDelayed(hideViewRunnable,2000);
        }
    }


    //滑动完毕之后获取抬起的动作(不太灵敏)
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        /*if (operateLayout == null) {
            return false;
        }

        if (MotionEvent.ACTION_UP == e2.getAction()) {
            mHandler.postDelayed(hideOperateViewRunnable, 500);
            mBrightness = mContext.getWindow().getAttributes().screenBrightness;
        }*/
        return false;
    }

    private Runnable hideOperateViewRunnable = new Runnable() {
        @Override
        public void run() {
            operateLayout.setVisibility(View.GONE);
        }
    };


    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        Log.d("==onBrightnessSlide==", "mBrightness = " + mBrightness);

        if (mBrightness < 0) {
            mBrightness = mContext.getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

        }
        WindowManager.LayoutParams lpa = mContext.getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        mContext.getWindow().setAttributes(lpa);

        operateTv.setText((int) (lpa.screenBrightness * 100) + "%");
    }


    //加减音量
    public void setAudio(int volume) {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);
        //当前音量
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //最大音量
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        Log.d("==d==", "max = " + max);
        Log.d("==d==", "current = " + currentVolume);
        currentVolume = currentVolume + volume;
        if (currentVolume >= 0 && currentVolume <= max) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND);
            int percentage = (currentVolume * 100) / max;
            operateTv.setText(percentage + "%");
            //静音
            if (currentVolume == 0) {
                operateIv.setImageResource(R.drawable.icon_simple_audio_close);
                operateTv.setText("静音");
            } else {
                operateIv.setImageResource(R.drawable.icon_simple_audio_open);
            }

        } else {
            currentVolume = currentVolume > 0 ? max : 0;
            return;
        }


        //audioManager.adjustVolume(i+volume,AudioManager.FLAG_PLAY_SOUND);

    }


    private Runnable hideViewRunnable = new Runnable() {
        @Override
        public void run() {
            mContext.full(true);
            functionView.setVisibility(View.GONE);
        }
    };

    public void onDestroy() {
        mHandler.removeCallbacks(hideViewRunnable);
        if (operateLayout != null) {
            operateLayout = null;
            mHandler.removeCallbacks(hideOperateViewRunnable);
        }
        functionView = null;
        mContext = null;
    }

}
