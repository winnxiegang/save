package com.android.banana.groupchat.ilistener;

import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qiaomu on 2018/3/1.
 */

public class ChatAdapterHelperCallbackImpl implements ChatAdapterHelperCallback {
    @Override
    public void onQueryAvailableCouponResult(int adapterPos, GroupCouponInfoBean groupCouponInfo, JSONObject jsonError) throws JSONException {

    }

    @Override
    public void onCheckForbiddenResult(RequestContainer request, boolean forbidden, JSONObject jsonError) throws JSONException {

    }

    @Override
    public void onCancelForbiddenResult(boolean success, JSONObject jsonError) throws JSONException {

    }

    @Override
    public void onDeleteMessageResult(int adapterPos, JSONObject jsonError) throws JSONException {

    }

    @Override
    public void onRemoveGroupChatMemberResult(int adapterPos, JSONObject jsonError) throws JSONException {

    }


}
