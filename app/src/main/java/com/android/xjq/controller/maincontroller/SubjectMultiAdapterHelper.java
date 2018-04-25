package com.android.xjq.controller.maincontroller;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

/**
 * Created by qiaomu on 2017/11/29.
 *
 * @see SubjectMultiAdapter 中网络请求的工具类
 * 点赞，置顶工具类
 */

public class SubjectMultiAdapterHelper implements IHttpResponseListener {
    private WrapperHttpHelper mWrapperHttpHelper = new WrapperHttpHelper(this);
    private SubjectMultiAdapterCallback mAdapterCallback;

    public SubjectMultiAdapterHelper(SubjectMultiAdapterCallback adapterCallback) {
        mAdapterCallback = adapterCallback;
    }


    /**
     * 评论置顶请求
     * @param id
     * @param setTop
     */
    public void createSetTop(int layoutPosition,String id, boolean setTop) {

        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.COMMENT_SET_TOP_OPERATE, true);

        map.put("setTop", setTop ? String.valueOf(1) : String.valueOf(0));

        map.put("commentId", id);

        map.put("position", layoutPosition);

        mWrapperHttpHelper.startRequest(map);
    }


    public void like(int layoutPosition, String objectId, String objectType) {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.LIKE, true);
        container.put("objectId", objectId);
        container.put("position", layoutPosition);
        container.put("likeObjectTypeEnum", objectType);
        mWrapperHttpHelper.startRequest(container);
    }

    public void unLike(int layoutPosition, String objectId, String objectType) {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.CANCEL_LIKE, true);
        container.put("objectId", objectId);
        container.put("position", layoutPosition);
        container.put("likeObjectTypeEnum", objectType);
        mWrapperHttpHelper.startRequest(container);
    }

    @Override
    public void onSuccess(RequestContainer request, Object object) {
        if (mAdapterCallback == null)
            return;
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (requestEnum) {
            case LIKE:
                mAdapterCallback.onLikeOrSetTopResult((JSONObject) object, request.getInt("position"), true, false);
                break;
            case CANCEL_LIKE:
                mAdapterCallback.onUnLikeResult((JSONObject) object, request.getInt("position"), true);
                break;
            case COMMENT_SET_TOP_OPERATE://置顶取消置顶成功
                mAdapterCallback.onLikeOrSetTopResult((JSONObject) object, request.getInt("position"), true, true);
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        if (mAdapterCallback == null)
            return;
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (requestEnum) {
            case LIKE:
                mAdapterCallback.onLikeOrSetTopResult(jsonObject, request.getInt("position"), false, false);
                break;
            case CANCEL_LIKE:
                mAdapterCallback.onUnLikeResult(jsonObject, request.getInt("position"), false);
                break;
            case COMMENT_SET_TOP_OPERATE:
                mAdapterCallback.onLikeOrSetTopResult(jsonObject, request.getInt("position"), false, true);
                break;
        }
    }
}
