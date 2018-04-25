package com.android.xjq.controller;

import android.view.View;
import android.view.ViewGroup;

import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.controller.live.LiveStateChangeController;
import com.android.xjq.model.VideoPlayerTypeEnum;
import com.android.xjq.service.FloatingView;
import com.tencent.ilivesdk.core.ILiveRoomManager;

/**
 * Created by lingjiu on 2018/1/29.
 */

public class LiveRoomManager implements ILiveBaseManager {
    private final static String TAG = LiveRoomManager.class.getSimpleName();
    private static LiveRoomManager instance;
    protected VideoPlayerTypeEnum currentTypeEnum;
    private ILiveRoomCallBack mILiveRoomCallBack;
    private ILiveBaseManager mLiveBaseManager;
    private FloatingView floatingView;
    private boolean isFirstEnterRoom;
    private ViewGroup mRootView;
    private boolean mShowFloatingView;
    private LiveStateChangeController liveStateChangeController;

    /**
     * 获取房间实例
     *
     * @return
     */
    public static LiveRoomManager getInstance() {
        if (instance == null) {
            synchronized (ILiveRoomManager.class) {
                if (instance == null) {
                    instance = new LiveRoomManager();
                }
            }
        }
        return instance;
    }

    public void init(VideoPlayerTypeEnum videoPlayerTypeEnum, ILiveRoomCallBack iLiveRoomCallBack) {
        if (mLiveBaseManager != null && currentTypeEnum != videoPlayerTypeEnum) {
            addLiveRoomCallback(iLiveRoomCallBack);
            return;
        }
        switch (videoPlayerTypeEnum) {
            case AVROOTVIEW:
                mLiveBaseManager = new AVLiveRoomManager();
                break;
            case TXCLOUDVIDEOVIEW:
                break;
        }
        currentTypeEnum = videoPlayerTypeEnum;
        addLiveRoomCallback(iLiveRoomCallBack);
    }

    public ViewGroup initView() {
        ViewGroup rootView = null;
        switch (currentTypeEnum) {
            case AVROOTVIEW:
                rootView = (ViewGroup) View.inflate(XjqApplication.getContext(), R.layout.view_portrait_live_state, null);
                break;
            case TXCLOUDVIDEOVIEW:

                break;
        }
        return rootView;
    }

    /**
     * 检查悬浮窗并给container添加videoview
     *
     * @param container
     */
    public boolean checkFloatWindow(FloatingView floatingView, ViewGroup container, int height, int width) {
        this.floatingView = floatingView;
        if (floatingView.getRootView() != null) {
            isFirstEnterRoom = false;
            mRootView = floatingView.removeRootView();
            floatingView.removeWindow();
        } else {
            isFirstEnterRoom = true;
            //初始化控件
            mRootView = initView();
        }
        setView(isFirstEnterRoom, mRootView);

        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        mRootView.setLayoutParams(lps);
        //从悬浮窗打开的话,需要将悬浮窗的rootView重新调整大小
        if (!isFirstEnterRoom) {
            changeViewSize(false, container, height, width);
        }
        //设置直播状态改变控制
        liveStateChangeController = new LiveStateChangeController(mRootView);
//        if (container.getChildCount() == 0)
        container.addView(mRootView, 0);
        return isFirstEnterRoom;
    }

    /**
     * 显示悬浮窗
     *
     * @param container
     * @param channelId
     */
    public void showFloatWindow(ViewGroup container, String channelId) {
        mShowFloatingView = true;
        container.removeView(mRootView);
        //先改变rootView的大小
        int width = XjqApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.floating_windows_width);

        int height = XjqApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.floating_windows_height);

        changeViewSize(false, container, height, width);

        floatingView.setAvRootView(mRootView);
        try {
            //在部分魅族/小米手机上,检测不到悬浮窗权限,则会抛出异常,这里try catch掉,不显示悬浮框
            floatingView.showWindow(channelId);
        } catch (Exception e) {
            //回收资源,并关闭悬浮窗
            floatingView.closeWindow();
        }
    }


    @Override
    public void startPlay(String channelId) {
        if (isFirstEnterRoom) {
            mLiveBaseManager.startPlay(channelId);
        } else {
            if (!floatingView.getChannelId().equals(channelId)) {
                switchRoom();
            } else {
                //进入房间成功
                if (mILiveRoomCallBack != null) {
                    mILiveRoomCallBack.onPlaySuccessListener();
                    mILiveRoomCallBack.receiveFirstFrameListener();
                    // liveStateChangeController.receiveFirstFrame();
                }
            }
        }
        //重置
        isFirstEnterRoom = false;
    }

    public ViewGroup getRootView() {
        return mRootView;
    }

    @Override
    public void switchRoom() {
        mLiveBaseManager.switchRoom();
    }

    @Override
    public void stopPlay() {
        mLiveBaseManager.stopPlay();
    }

    @Override
    public void onResume() {
        mLiveBaseManager.onResume();
    }

    @Override
    public void onPause() {
        mLiveBaseManager.onPause();
    }

    @Override
    public void onDestroy() {
        mILiveRoomCallBack = null;
        mRootView = null;
        mLiveBaseManager.onDestroy();
        floatingView.onDestroy();
        liveStateChangeController.onDestroy();
    }

    @Override
    public void cancelAllView() {
        mLiveBaseManager.cancelAllView();
    }

    @Override
    public void showView() {
        mLiveBaseManager.showView();
    }

    @Override
    public void changeViewSize(boolean isLandscape, ViewGroup container, int height, int width) {
        mLiveBaseManager.changeViewSize(isLandscape, container, height, width);
    }

    @Override
    public void setView(boolean isFirstEnterRoom, ViewGroup mRootView) {
        mLiveBaseManager.setView(isFirstEnterRoom, mRootView);
    }

    @Override
    public void addLiveRoomCallback(ILiveRoomCallBack iLiveRoomCallBack) {
        mILiveRoomCallBack = iLiveRoomCallBack;
        mLiveBaseManager.addLiveRoomCallback(iLiveRoomCallBack);
    }

    public void liveStateChange(boolean pushStreamStatus) {
        liveStateChangeController.liveStateChange(pushStreamStatus);
    }
}
