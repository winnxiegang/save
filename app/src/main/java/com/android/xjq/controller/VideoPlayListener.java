package com.android.xjq.controller;

/**
 * Created by lingjiu on 2017/3/29.
 */

public interface VideoPlayListener {

    void onPreparePlay();

    void onPlaySuccessListener();

    void receiveFirstFrameListener();

    void onPlayFailedListener();

    void onExitComplete();

    void onHasVideo();

    void onNoVideo();
}
