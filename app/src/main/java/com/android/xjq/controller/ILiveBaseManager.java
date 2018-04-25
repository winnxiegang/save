package com.android.xjq.controller;

import android.view.ViewGroup;

/**
 * Created by lingjiu on 2018/1/29.
 */

public interface ILiveBaseManager {
    void startPlay(String channelId);

    void switchRoom();

    void stopPlay();

    void onResume();

    void onPause();

    void onDestroy();

    void cancelAllView();

    void showView();

    void addLiveRoomCallback(ILiveRoomCallBack iLiveRoomCallBack);

    void changeViewSize(boolean isLandscape, ViewGroup container, int height, int width);

    void setView(boolean isFirstEnterRoom, ViewGroup mRootView);
}