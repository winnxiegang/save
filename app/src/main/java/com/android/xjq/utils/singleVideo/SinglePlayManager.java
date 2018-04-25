package com.android.xjq.utils.singleVideo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.android.xjq.utils.singleVideo.VideoViewHolder.PLAY_EVT_QUEUE;
import static com.android.xjq.utils.singleVideo.VideoViewHolder.PLAY_EVT_PAUSED;
import static com.android.xjq.utils.singleVideo.VideoViewHolder.PLAY_EVT_PREPARE;
import static com.android.xjq.utils.singleVideo.VideoViewHolder.PLAY_EVT_PROGRESS;
import static com.android.xjq.utils.singleVideo.VideoViewHolder.PLAY_EVT_RESUMED;
import static com.android.xjq.utils.singleVideo.VideoViewHolder.PLAY_EVT_STOPPED;

/**
 * Created by qiaomu on 2018/2/26.
 * <p>
 * <p>
 * 列表播放视频 管理者
 * <p>
 * 需要在播放视频的页面实现{@link SinglePlayCallback}接口,因为我需要知道 当前是否有视频播放，播放处于处于状态
 * <p>
 * <p>
 * int detachOffset = LibAppUtil.dip2px(getContext(), 50);
 * itemActiveCalculator = new SinglePlayManager();
 * itemActiveCalculator.attach(mRecycler.getRecyclerView(), getLayoutManager(), this, detachOffset);
 */

public class SinglePlayManager {
    private CopyOnWriteArrayList<VideoViewHolder> mHolderList = new CopyOnWriteArrayList<>();

    /**
     * @param recyclerView 列表
     * @param playCallback SinglePlayCallback
     */
    public void attach(@NonNull final RecyclerView recyclerView, @NonNull final SinglePlayCallback playCallback) {
        if (recyclerView == null || playCallback == null)
            return;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                for (VideoViewHolder videoViewHolder : mHolderList) {
                    int playStatus = videoViewHolder.getCurPlayStatus();
                    switch (playStatus) {
                        case PLAY_EVT_PREPARE:
                        case PLAY_EVT_QUEUE:
                        case PLAY_EVT_PAUSED:
                        case PLAY_EVT_RESUMED:
                        case PLAY_EVT_PROGRESS:
                            stopVideoIfDetachWindow(recyclerView, videoViewHolder);
                            break;
                        case PLAY_EVT_STOPPED:
                            break;
                    }
                }
            }
        });
    }

    //计算并停止滑出窗口的 视频播放item
    private void stopVideoIfDetachWindow(RecyclerView recyclerView, VideoViewHolder videoViewHolder) {

        if (videoViewHolder.isDetachWindow(recyclerView)) {
            videoViewHolder.changeVideoHolderEvent(PLAY_EVT_STOPPED);
            mHolderList.remove(videoViewHolder);
        }
    }

    //接收视频播放item的状态变化
    public void onPlayStatusChanged(VideoViewHolder videoViewHolder, int playStatus) {
        switch (playStatus) {
            case PLAY_EVT_QUEUE:
            case PLAY_EVT_RESUMED:
            case PLAY_EVT_PREPARE:
            case PLAY_EVT_PROGRESS:
                stopPreviousAndAddNew(videoViewHolder);
                break;
            case PLAY_EVT_PAUSED:
            case PLAY_EVT_STOPPED:
                break;
        }
    }

    //停止之前所有播放的并释放资源，并且将新，即将播放的添加到管理列表
    private void stopPreviousAndAddNew(VideoViewHolder newVVH) {

        boolean contains = false;
        for (VideoViewHolder vvh : mHolderList) {
            if (vvh != newVVH) {
                vvh.changeVideoHolderEvent(PLAY_EVT_STOPPED);
                mHolderList.remove(vvh);
            } else {
                contains = true;
            }
        }

        if (!contains)
            mHolderList.add(newVVH);
    }


    public void stopAllVideoHolder() {
        for (VideoViewHolder vvh : mHolderList) {
            vvh.changeVideoHolderEvent(PLAY_EVT_STOPPED);
            mHolderList.remove(vvh);
        }
    }
}
