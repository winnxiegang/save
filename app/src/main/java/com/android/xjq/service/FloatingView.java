package com.android.xjq.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.controller.LiveRoomManager;

import static com.android.xjq.R.id.contentView;

/**
 * Created by lingjiu on 2017/3/2 19:58.
 */
public class FloatingView {

    private LiveRoomManager mLiveView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParam;
    private RelativeLayout mWindowsView;
    private float x, y;
    private ViewGroup mRootView;
    private float mTouchStartX, mTouchStartY;
    private long currentTime;
    private static FloatingView instance;
    private FrameLayout containerView;
    //当前正在播放的hostId
    private String channelId = "";

    private OnFloatViewDestroyListener listener;


    public static FloatingView getInstance(LiveRoomManager context) {
        synchronized (FloatingView.class) {
            if (instance == null) {
                instance = new FloatingView(context);
            }
            if (instance.mLiveView != context) {
                instance.mLiveView = context;
            }
        }

        return instance;
    }

    public static FloatingView getInstance() {
        return instance;
    }

    public void closeWindow() {
        if (mLiveView == null) {
            if (listener != null) listener.onDestroy();
            listener = null;
            return;
        }
        //移除窗口并退出房间
        removeWindow();

        mLiveView.stopPlay();
    }

    public void closeWindow(OnFloatViewDestroyListener listener) {
        this.listener = listener;
        closeWindow();
    }


    private FloatingView() {
    }

    private FloatingView(LiveRoomManager liveView) {

        mLiveView = liveView;

        init();
    }

    /**
     * 初始化系统窗口
     */
    private void init() {
        // 取得系统窗体
        mWindowManager = (WindowManager) XjqApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        // 窗体的布局样式
        mLayoutParam = new WindowManager.LayoutParams();
        // 设置窗体显示类型——TYPE_SYSTEM_ALERT(系统提示)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT > 24) {
                mLayoutParam.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                mLayoutParam.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
        } else {
            mLayoutParam.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        // 设置窗体焦点及触摸：
        mLayoutParam.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置显示的模式
        mLayoutParam.format = PixelFormat.RGBA_8888;
        mLayoutParam.x = XjqApplication.getContext().getResources().getDimensionPixelSize(R.dimen.floating_windows_right_margin);
        mLayoutParam.y = XjqApplication.getContext().getResources().getDimensionPixelSize(R.dimen.floating_windows_bottom_margin);

        // 设置对齐的方法
        mLayoutParam.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        // 设置窗体宽度和高度
        mLayoutParam.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParam.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将制定View解析后添加到窗口管理器里面
        mWindowsView = (RelativeLayout) LayoutInflater.from(XjqApplication.getContext()).inflate(R.layout.layout_float_window, null);
        containerView = ((FrameLayout) mWindowsView.findViewById(R.id.contentView));
    }

    /**
     * 设置rootView
     *
     * @param rootView
     */
    public void setAvRootView(ViewGroup rootView) {
        this.mRootView = rootView;
    }

    public View getRootView() {
        return mRootView;
    }

    /**
     * 移除rootView
     *
     * @return
     */
    public ViewGroup removeRootView() {
        containerView.removeView(mRootView);
        return mRootView;
    }

    /**
     * 移除窗口
     */
    public void removeWindow() {
        if (mWindowsView.getParent() != null) {

            mWindowManager.removeView(mWindowsView);
            //位置还原
            mLayoutParam.x = XjqApplication.getContext().getResources().getDimensionPixelSize(R.dimen.floating_windows_right_margin);

            mLayoutParam.y = XjqApplication.getContext().getResources().getDimensionPixelSize(R.dimen.floating_windows_bottom_margin);

        }
        if (mRootView.getParent() != null) {

            removeRootView();
        }
        mRootView = null;
    }

    public String getChannelId() {
        return channelId;
    }

    /**
     * 显示悬浮窗
     *
     * @param channelId
     */
    public void showWindow(String channelId) throws Exception {

        this.channelId = channelId;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.CENTER;

        containerView.addView(mRootView, params);

        mWindowManager.addView(mWindowsView, mLayoutParam);
//        mRootView = (AVRootView) mWindowsView.findViewById(R.id.rootView);
        mWindowsView.setClickable(true);

        mWindowsView.findViewById(contentView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getRawX();
                y = event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchStartX = event.getRawX();
                        mTouchStartY = event.getRawY();
                        currentTime = SystemClock.currentThreadTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //原始坐标减去移动坐标
                        mLayoutParam.x -= (int) (x - mTouchStartX);
                        mLayoutParam.y -= (int) (y - mTouchStartY);

                        mWindowManager.updateViewLayout(mWindowsView, mLayoutParam);

                        mTouchStartX = x;
                        mTouchStartY = y;
//                        Log.i("main", "x值=" + x + "\ny值=" + y + "\nmTouchX" + mTouchStartX + "\nmTouchY=" + mTouchStartY);
                        break;
                    case MotionEvent.ACTION_UP:

                        if (SystemClock.currentThreadTimeMillis() - currentTime < 15) {
                            Intent intent = new Intent(XjqApplication.getContext(), LiveActivity.class);
                            intent.putExtra("channelId", FloatingView.this.channelId);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            XjqApplication.getContext().startActivity(intent);
                        }
                        break;
                }
                return true;
            }
        });

        mWindowsView.findViewById(R.id.quitIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWindow();
            }
        });
    }

    public void onDestroy() {
        if (listener != null) {
            listener.onDestroy();
        }
        channelId = "";
        listener = null;
        mLiveView = null;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public static interface OnFloatViewDestroyListener {
        public void onDestroy();
    }

}
