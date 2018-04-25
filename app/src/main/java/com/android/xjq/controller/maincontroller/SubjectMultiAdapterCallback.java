package com.android.xjq.controller.maincontroller;

import org.json.JSONObject;

/**
 * Created by qiaomu on 2017/11/29.
 * 首页中 关于列表中网络请求操作的回调借口
 */

public interface SubjectMultiAdapterCallback {
    /**
     * 点赞，置顶，取消置顶
     *
     * @param position 所在列表中的位置
     * @param success
     */
    void onLikeOrSetTopResult(JSONObject jsonObject, int position, boolean success, boolean setTopRequest);

    /**
     * 取消点赞
     *
     * @param position 所在列表中的位置
     * @param success
     */
    void onUnLikeResult(JSONObject jsonObject, int position, boolean success);
}
