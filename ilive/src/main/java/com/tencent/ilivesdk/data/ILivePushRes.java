package com.tencent.ilivesdk.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class ILivePushRes {

    final List<ILivePushUrl> urls = new ArrayList<>();
    final long chnlId;
    final long taskId;

    public ILivePushRes(long chnlId, long taskId) {
        this.chnlId = chnlId;
        this.taskId = taskId;
    }

    public void addUrl(ILivePushUrl item) {
        urls.add(item);
    }



    /**
     * 获取创建直播频道返回的播放地址
     * @return 返回播放地址列表
     * @see ILivePushUrl
     */
    public List<ILivePushUrl> getUrls(){
        return this.urls;
    }

    /**
     * 获取创建直播频道返回的频道ID
     * @return 返回频道ID
     */
    public long getChnlId(){
        return this.chnlId;
    }

    /**
     * 获取推流时进行录制的任务ID，开启录制选项的时候有效
     * @return 录制任务ID
     */
    public long getTaskId(){
        return this.taskId;
    }
}
