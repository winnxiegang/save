package com.android.xjq.controller;

import com.android.xjq.bean.live.LiveCommentBean;

/**
 * Created by lingjiu on 2018/1/29.
 */

public interface ILiveRoomCallBack {

    void onPreparePlay();

    void onPlaySuccessListener();

    void receiveFirstFrameListener();

    void onPlayFailedListener();

    void onExitComplete();

    void receiveMessage(LiveCommentBean liveCommentBean);

    void onHasVideo();

    void onNoVideo();
}
