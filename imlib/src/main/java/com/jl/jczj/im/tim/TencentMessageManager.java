package com.jl.jczj.im.tim;

import android.text.TextUtils;

import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jl.jczj.im.IBaseMessageManager;
import com.jl.jczj.im.MessageType;
import com.jl.jczj.im.bean.ChatMsgBody;
import com.jl.jczj.im.bean.ChatMsgBodyList;
import com.android.banana.commlib.bean.ErrorBean;
import com.jl.jczj.im.bean.IMParams;
import com.jl.jczj.im.body.ImFormBody;
import com.jl.jczj.im.callback.IMCallback;
import com.jl.jczj.im.callback.IMComCallback;
import com.jl.jczj.im.callback.IMTimCallback;
import com.jl.jczj.im.im.ImManager;
import com.jl.jczj.im.service.IMUrlEnum;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupSystemElem;
import com.tencent.TIMGroupSystemElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyi on 2017/5/27.
 */

public class TencentMessageManager implements IBaseMessageManager, TIMMessageListener, OnHttpResponseListener {
    private static final String TAG = "TencentMessageManager";

    private HttpRequestHelper httpRequestHelper = new HttpRequestHelper(this, false);
    private boolean firstFetchChat = true;
    private static final int MESSAGE_COUNT_PAGE = 50;
    private List<IMCallback> mImCallbacks = new ArrayList<>();
    private List<IMTimCallback> mIMTimCallbacks = new ArrayList<>();


    private Gson gson;

    public TencentMessageManager() {
        gson = new Gson();
    }

    @Override
    public void receiveMessage(String message, ErrorBean errorBean) {
        if (mImCallbacks == null || mImCallbacks.size() == 0)
            return;

        final ChatMsgBodyList bodyList = gson.fromJson(message, ChatMsgBodyList.class);
        if (bodyList == null || bodyList.messages == null) {
            for (IMCallback mcallback : mImCallbacks) {
                mcallback.onHistoryMessages(null);
            }
            return;
        }


        //过滤掉禁言 身份变更的推送-------------------------基于这个版本。
        List<ChatMsgBody> removeList = new ArrayList<>();
        for (ChatMsgBody msgBody : bodyList.messages) {
            String typeCode = msgBody.typeCode;
            if (TextUtils.equals(TimMessageType.GROUP_USER_ROLE_CHANGE_TEXT, typeCode)
                    || TextUtils.equals(TimMessageType.GROUP_MESSAGE_FORBIDDEN_SENDER_REMOVE_TEXT, typeCode)
                    || TextUtils.equals(TimMessageType.GROUP_MESSAGE_FORBIDDEN_SENDER_ADD_TEXT, typeCode)
                    || TextUtils.equals(TimMessageType.USER_QUIT_GROUP_TEXT, typeCode)
                    || TextUtils.equals(MessageType.GROUP_MESSAGE_DELETE_TEXT, typeCode)
                    || TextUtils.equals(MessageType.INVITED_TO_GROUP_TYPE, typeCode)) {
                removeList.add(msgBody);
            }
        }
        bodyList.messages.removeAll(removeList);
        for (IMCallback mcallback : mImCallbacks) {
            mcallback.onHistoryMessages(bodyList);
        }
    }

    @Override
    public void sendMessage(String mode, String message, int position) {
        ImFormBody formBody = new ImFormBody(IMUrlEnum.GROUP_USER_ENTRY_BASED_MESSAGE_SEND, true);
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            object.put("bodyType", mode);
            object.put("content", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(object);
        formBody.put("identifyId", IMParams.identifyId);
        formBody.put("userId", IMParams.userId);
        formBody.put("authId", IMParams.authId);
        formBody.put("bodiesJsonData", array.toString());
        formBody.put("timestamp", String.valueOf(System.currentTimeMillis()));
        formBody.put("position", position);
        httpRequestHelper.startRequest(formBody, false);
    }


    @Override
    public void joinGroup() {

        ImFormBody formBody = new ImFormBody(IMUrlEnum.GROUP_USER_ENTRY_ENTER, true);
        formBody.put("authKey", IMParams.authKey);
        formBody.put("authId", IMParams.authId);
        formBody.put("userId", IMParams.userId);
        //formBody.put("inviteId", IMParams.inviteId);
        formBody.put("timestamp", System.currentTimeMillis());

        formBody.put("groupId", IMParams.groupId);
        httpRequestHelper.startRequest(formBody, false);
    }

    @Override
    public void leaveGroup(String groupId) {
        //TIMGroupManager.getInstance().quitGroup();
    }

    @Override
    public void getMessageList(boolean loadMore, long messageSequence) {
        ImFormBody formBody = new ImFormBody(IMUrlEnum.GROUP_MESSAGE_QUERY_BY_USER, true);
        formBody.put("identifyId", IMParams.identifyId);
        formBody.put("userId", IMParams.userId);
        formBody.put("authId", IMParams.authId);
        formBody.put("messageSequence", loadMore ? messageSequence : 0);
        formBody.put("fetchAfter", loadMore ? "0" : "1");
        formBody.put("recordCount", MESSAGE_COUNT_PAGE);
        if (firstFetchChat)
            formBody.put("messageSequenceOrderBy", "DESC");
        formBody.put("timestamp", System.currentTimeMillis());
        formBody.setRequestUrl(ImManager.URL);
        firstFetchChat = false;
        httpRequestHelper.startRequest(formBody, false);
    }


    @Override
    public void exitGroup(IMComCallback callback) {
        ImFormBody map = new ImFormBody(IMUrlEnum.GROUP_USER_QUIT, true);
        map.put("userId", IMParams.userId);
        map.put("groupId", IMParams.groupId);
        //  map.put("groupId", IMParams.groupId);
        // map.put("quitReason", "exit_group");
        map.put("timestamp", System.currentTimeMillis());
        map.put("authId", IMParams.authId);
        map.setImComCallback(callback);
        httpRequestHelper.startRequest(map, false);
    }

    @Override
    public void removeImCallback(IMCallback imCallback) {
        mImCallbacks.remove(imCallback);
    }

    @Override
    public void removeTimCallback(IMTimCallback timCallBack) {
        mIMTimCallbacks.remove(timCallBack);
        if (mIMTimCallbacks.size() == 0)
            TIMManager.getInstance().removeMessageListener(this);
    }

    @Override
    public void addImCallback(IMCallback imCallback) {
        if (imCallback != null && !mImCallbacks.contains(imCallback))
            mImCallbacks.add(imCallback);
    }

    @Override
    public void addTimCallback(IMTimCallback timCallback) {
        if (timCallback != null && !mIMTimCallbacks.contains(timCallback))
            mIMTimCallbacks.add(timCallback);

        if (mIMTimCallbacks.size() <= 1)
            TIMManager.getInstance().addMessageListener(this);
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        parseIMMessage(list);
        return false;
    }

    private void parseIMMessage(List<TIMMessage> list) {
        List<TIMMessage> tlist = list;
        if (tlist == null) return;
        for (int i = tlist.size() - 1; i >= 0; i--) {
            TIMMessage currMsg = tlist.get(i);
            LogUtils.i(TAG, "接收到腾讯推送的消息: ");
            if (currMsg == null)
                continue;
            for (int j = 0; j < currMsg.getElementCount(); j++) {
                if (currMsg.getElement(j) == null)
                    continue;
                final TIMElem elem = currMsg.getElement(j);
                TIMElemType type = elem.getType();
                //定制消息
                if (type == TIMElemType.Custom) {
                    handleCustomMsg(elem);
                    continue;
                } else if (type == TIMElemType.GroupSystem) {
                    TIMGroupSystemElem groupSystemElem = (TIMGroupSystemElem) elem;
                    String groupId = groupSystemElem.getGroupId();
                    if (!TextUtils.isEmpty(groupId) && groupId.length() > 3)
                        groupId = groupId.substring(3, groupId.length());

                    TIMGroupSystemElemType subtype = groupSystemElem.getSubtype();
                    //系统消息----被踢出群组
                    if (subtype == TIMGroupSystemElemType.TIM_GROUP_SYSTEM_KICK_OFF_FROM_GROUP_TYPE) {
                        for (final IMTimCallback timCallback : mIMTimCallbacks) {
                            timCallback.onTimSystemMessage(groupId, TimMessageType.USER_QUIT_GROUP_TEXT);
                        }
                    } else if (subtype == TIMGroupSystemElemType.TIM_GROUP_SYSTEM_INVITED_TO_GROUP_TYPE) {
                        //系统消息----邀请加入群组
                    }

                }
            }
        }
    }

    private void handleCustomMsg(TIMElem elem) {
        try {
            if (null == mIMTimCallbacks || mIMTimCallbacks.size() == 0) {
                return;
            }
            String customText = null;
            try {
                customText = new String(((TIMCustomElem) elem).getData(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            LogUtils.e("push_message", customText);
            try {
                JSONObject object = new JSONObject(customText);
                LogUtils.e("kkk","推送过来的消息体内容：：："+object.toString());
                //过滤掉禁言 身份变更的推送-------------------------基于这个版本。
                String typeCode = object.getString("typeCode");
                if (TextUtils.equals(TimMessageType.GROUP_USER_ROLE_CHANGE_TEXT, typeCode)
                        || TextUtils.equals(TimMessageType.GROUP_MESSAGE_FORBIDDEN_SENDER_REMOVE_TEXT, typeCode)
                        || TextUtils.equals(TimMessageType.GROUP_MESSAGE_FORBIDDEN_SENDER_ADD_TEXT, typeCode)) {
                    return;
                }
                String groupTypeCode = object.getString("groupTypeCode");
                if (TextUtils.equals(groupTypeCode, "GROUP_CHAT")) {
                    final ChatMsgBody chatMsgBody = gson.fromJson(customText, ChatMsgBody.class);
                    for (final IMTimCallback timCallback : mIMTimCallbacks) {
                        timCallback.onTimNewMessage(chatMsgBody);
                    }
                }
            } catch (JsonSyntaxException e) {

            }

        } catch (Exception e) {
        }
    }

    @Override
    public void executorSuccess(RequestContainer request, JSONObject jo) {
        IMUrlEnum urlEnum = (IMUrlEnum) request.getRequestEnum();
        switch (urlEnum) {
            case GROUP_MESSAGE_QUERY_BY_USER://消息轮询
                receiveMessage(jo == null ? null : jo.toString(), null);
                break;
            case GROUP_USER_ENTRY_BASED_MESSAGE_SEND://发送消息
                if (mImCallbacks.size() > 0) {
                    int position = request.getInt("position");
                    String id = null;
                    long messageSequence = 0l;
                    try {
                        messageSequence = jo.getLong("messageSequence");
                        id = jo.getString("id");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (IMCallback mcallback : mImCallbacks) {
                        try {
                            mcallback.onMessageSendResult(id, messageSequence, position, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
            case GROUP_USER_ENTRY_LEAVE:
                break;
            case GROUP_USER_QUIT:
                IMComCallback imComCallback = ((ImFormBody) request).imComCallback;
                if (imComCallback != null) {
                    Type type = getInterfaceActualType(imComCallback);
                    if (type != null) imComCallback.onSuccess(gson.fromJson(jo.toString(), type));
                }
                break;
        }
    }

    @Override
    public void executorFalse(RequestContainer request, JSONObject jo) {

        IMUrlEnum urlEnum = (IMUrlEnum) request.getRequestEnum();
        switch (urlEnum) {
            case GROUP_MESSAGE_QUERY_BY_USER://消息轮询
                if (jo != null)
                    receiveMessage(null, gson.fromJson(jo.toString(), ErrorBean.class));
                else
                    receiveMessage(null, null);
                break;
            case GROUP_USER_ENTRY_BASED_MESSAGE_SEND://发送消息
                if (mImCallbacks.size() > 0) {
                    int position = request.getInt("position");
                    String msgid = null;
                    try {
                        msgid = jo.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (IMCallback callback : mImCallbacks) {
                        try {
                            callback.onMessageSendResult(msgid, 0, position, jo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case GROUP_USER_ENTRY_LEAVE:
                break;
            case GROUP_USER_QUIT:
                IMComCallback imComCallback = ((ImFormBody) request).imComCallback;
                if (imComCallback != null) {
                    imComCallback.onError(gson.fromJson(jo.toString(), ErrorBean.class));
                }
                break;
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }

    private Type getInterfaceActualType(Object calz) {
        Type[] types = calz.getClass().getGenericInterfaces();
        if (types != null && types.length > 0 && types[0] instanceof ParameterizedType) {
            return ((ParameterizedType) types[0]).getActualTypeArguments()[0];
        }
        return null;
    }

}
