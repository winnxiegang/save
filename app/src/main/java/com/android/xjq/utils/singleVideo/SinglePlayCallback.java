package com.android.xjq.utils.singleVideo;

/**
 * Created by qiaomu on 2018/2/26.
 */

public interface SinglePlayCallback {

    /**
     * @param videoViewHolder 当前视频播放item的持有类
     * @param playStatus      视频 的状态
     */
    void onPlayStatusChanged(VideoViewHolder videoViewHolder, int playStatus);
}
