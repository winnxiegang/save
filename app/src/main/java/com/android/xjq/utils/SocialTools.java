package com.android.xjq.utils;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

/**
 * Created by qiaomu on 2018/2/5.
 * <p>
 * 关注
 * 取消关注
 * <p>
 * 点赞
 * <p>
 * 取消点赞
 */

public class SocialTools {
    private static WrapperHttpHelper sHttpHelper;
    private static SocialTools sSocialTools;

    public interface onSocialCallback {
        void onResponse(JSONObject response, boolean success);
    }

    protected static SocialTools getInstance() {
        if (sSocialTools == null) {
            synchronized (SocialTools.class) {
                if (sSocialTools == null) {
                    sSocialTools = new SocialTools();
                    sHttpHelper = new WrapperHttpHelper(new SocialCallback());
                }
            }
        }
        return sSocialTools;
    }

    public static void like(String objectId, String objectType, onSocialCallback callback) {
        RequestFormBody container = new RequestFormBody(XjqUrlEnum.LIKE, true);
        container.put("objectId", objectId);
        container.put("likeObjectTypeEnum", objectType);
        container.setTag(callback);
        getInstance().sHttpHelper.startRequest(container);
    }

    public static void unLike(String objectId, String objectType, onSocialCallback callback) {
        RequestFormBody container = new RequestFormBody(XjqUrlEnum.CANCEL_LIKE, true);
        container.put("objectId", objectId);
        container.put("likeObjectTypeEnum", objectType);
        container.setTag(callback);
        getInstance().sHttpHelper.startRequest(container);
    }

    //关注
    public static void payAttention(String userId, onSocialCallback callback) {
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.USER_FOLLOW_CREATE, true);
        formBody.setTag(callback);
        formBody.put("userId", userId);
        getInstance().sHttpHelper.startRequest(formBody);
    }

    //取消关注
    public static void cancelAttention(String userId, String userType, onSocialCallback callback) {
        //userType[FOLLOWCANCEL("取消关注"),REMOVEFANS("移除粉丝")]
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.USER_FOLLOW_CANCEL, true);
        formBody.setTag(callback);
        formBody.put("userId", userId);
        formBody.put("userType", userType);
        getInstance().sHttpHelper.startRequest(formBody);
    }

    static class SocialCallback implements IHttpResponseListener {

        @Override
        public void onSuccess(RequestContainer request, Object o) {
            if (request.getTag() == null)
                return;
            ((onSocialCallback) request.getTag()).onResponse(((JSONObject) o), true);
        }

        @Override
        public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
            if (request.getTag() == null)
                return;
            ((onSocialCallback) request.getTag()).onResponse(jsonObject, false);
        }
    }
}
