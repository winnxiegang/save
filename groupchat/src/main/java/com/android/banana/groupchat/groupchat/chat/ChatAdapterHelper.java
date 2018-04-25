package com.android.banana.groupchat.groupchat.chat;

import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.banana.commlib.coupon.couponenum.GroupCouponAllocateStatusEnum;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.groupchat.chatenum.ChatRoomMemberLevelEnum;
import com.android.banana.groupchat.ilistener.ChatAdapterHelperCallback;
import com.android.banana.http.JczjURLEnum;
import com.android.httprequestlib.RequestContainer;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qiaomu on 2017/6/9.
 */

public class ChatAdapterHelper implements IHttpResponseListener {
    private WrapperHttpHelper mHttpHelper;
    private ChatAdapterHelperCallback mAdapterCallback;
    public boolean mShowLabel;  /*是否显示标签*/
    public boolean mIsManager; /*是否是管理员*/
    public boolean mIsOwner; /*是否是管理员*/
    public int mChatType;/*入口类型--比分直播/群聊*/
    public String mUserRoleCode;

    public void setHelperCallback(ChatAdapterHelperCallback helperCallback) {
        mAdapterCallback = helperCallback;
    }

    @IntDef({ChatType.LIVE_SCORE, ChatType.GROUP_CHAT})
    public @interface ChatType {
        int LIVE_SCORE = 0;//比分直播入口
        int GROUP_CHAT = 1;//群聊入口
    }

    public ChatAdapterHelper(@ChatType int chatType) {
        this("NORMAL", chatType);
    }


    //-------------------------------NORMAL,MANAGER          在上面
    public ChatAdapterHelper(String userRoleCode, @ChatType int chatType) {
        this.mUserRoleCode = userRoleCode;
        this.mIsManager = !TextUtils.equals(userRoleCode, ChatRoomMemberLevelEnum.NORMAL.name());
        this.mIsOwner= TextUtils.equals(userRoleCode, ChatRoomMemberLevelEnum.GROUP_OWNER.name());
        this.mChatType = chatType;
        this.mHttpHelper = new WrapperHttpHelper(this);
        this.mShowLabel = chatType == ChatType.LIVE_SCORE;
    }

    public void fetchCoupon(String groupCouponId, int adapterPosition) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.QUERY_AVAILABLE_COUPON, true);
        formBody.put("groupCouponId", groupCouponId);
        formBody.put("adapterPosition", adapterPosition);
        mHttpHelper.startRequest(formBody);
    }

    public void checkForbidden(int posInAdapter, boolean sendByMe, String userId,String sendUserName, String id, String groupId) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.QUERY_GROUP_USER_FORBIDDEN, true);
        formBody.put("posInAdapter", posInAdapter);
        formBody.put("sendByMe", sendByMe);
        formBody.put("groupId", groupId);
        formBody.put("userId", userId);
        formBody.put("sendUserName",sendUserName);
        formBody.put("id", id);
        mHttpHelper.startRequest(formBody, false);
    }

    public void cancelForbidden(String msgId, String userId, String groupId) {
        RequestFormBody container = new RequestFormBody(JczjURLEnum.USER_FORBBIDEN_ACTION_CANCEL, true);
        container.put("id", msgId);
        container.put("groupId", groupId);
        container.put("userId", userId);
        mHttpHelper.startRequest(container, false);
    }

    public void deleteMessage(String groupId, int posInAdapter, String... messageIds) {
        String relationGroupType = mChatType == ChatType.LIVE_SCORE ? "RACE_LIVE_CHAT" : "RELATION_GROUP_CHAT";
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.GROUP_MESSAGE_DELETE, true);
        formBody.put("position", posInAdapter);
        formBody.put("groupId", groupId);
        formBody.put("reason", "SPAM");
        formBody.putStringArray("messageId", messageIds);
        formBody.put("relationGroupType", relationGroupType);
        mHttpHelper.startRequest(formBody, false);
    }

    public void removeChatRoomMember(String groupId, String userId) {
        RequestFormBody map = new RequestFormBody(JczjURLEnum.CHAT_MEMBER_REMOVE_RELATION_GROUP, true);
        map.put("groupId", groupId);
        map.put("userId", userId);
        map.put("relationGroupType", "FAMLIY_CHAT");
        mHttpHelper.startRequest(map, true);
    }

    @Override
    public void onSuccess(RequestContainer request, Object result) {
        if (mAdapterCallback == null) return;
        JczjURLEnum anEnum = (JczjURLEnum) request.getRequestEnum();
        switch (anEnum) {
            case QUERY_AVAILABLE_COUPON:
                try {
                    JSONObject object = (JSONObject) result;
                    int adapterPos = request.getInt("adapterPosition");
                    GroupCouponInfoBean couponBean = null;
                    if (object.has("couponInfo")) {
                        JSONObject couponInfo = object.getJSONObject("couponInfo");
                        couponBean = new Gson().fromJson(couponInfo.toString(), GroupCouponInfoBean.class);
                        String allocateStatus = couponBean.getStatus().getMessage();
                        boolean allocate = GroupCouponAllocateStatusEnum.safeValueOf(allocateStatus) == GroupCouponAllocateStatusEnum.ALLOCATED;
                        couponBean.setIsOwnAllocated(allocate);
                    }
                    mAdapterCallback.onQueryAvailableCouponResult(adapterPos, couponBean, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case QUERY_GROUP_USER_FORBIDDEN:
                try {
                    boolean forbidden = ((JSONObject) result).getBoolean("forbidden");
                    mAdapterCallback.onCheckForbiddenResult(request, forbidden, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case USER_FORBBIDEN_ACTION_CANCEL:
                try {
                    mAdapterCallback.onCancelForbiddenResult(true, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case GROUP_MESSAGE_DELETE:
                int position = request.getInt("position");
                try {
                    mAdapterCallback.onDeleteMessageResult(position, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case CHAT_MEMBER_REMOVE_RELATION_GROUP:
                try {
                    mAdapterCallback.onRemoveGroupChatMemberResult(-1, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject object, boolean netFailed) {
        if (mAdapterCallback == null) return;
        JczjURLEnum anEnum = (JczjURLEnum) request.getRequestEnum();
        switch (anEnum) {
            case QUERY_AVAILABLE_COUPON:
                try {
                    GroupCouponInfoBean couponInfoBean = null;
                    if (object.has("couponInfo")) {
                        JSONObject couponInfo = object.getJSONObject("couponInfo");
                        couponInfoBean = new Gson().fromJson(couponInfo.toString(), GroupCouponInfoBean.class);
                        String allocateStatus = couponInfoBean.getStatus().getMessage();
                        boolean allocate = GroupCouponAllocateStatusEnum.safeValueOf(allocateStatus) == GroupCouponAllocateStatusEnum.ALLOCATED;
                        couponInfoBean.setIsOwnAllocated(allocate);
                    }
                    mAdapterCallback.onQueryAvailableCouponResult(request.getInt("adapterPosition"), couponInfoBean, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case QUERY_GROUP_USER_FORBIDDEN:
                try {
                    mAdapterCallback.onCheckForbiddenResult(request, false, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case USER_FORBBIDEN_ACTION_CANCEL:
                try {
                    mAdapterCallback.onCancelForbiddenResult(false, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case GROUP_MESSAGE_DELETE:
                int position = Integer.parseInt(request.getFiledsMap().get("position"));
                try {
                    mAdapterCallback.onDeleteMessageResult(position, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case CHAT_MEMBER_REMOVE_RELATION_GROUP:
                try {
                    mAdapterCallback.onRemoveGroupChatMemberResult(-1, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
