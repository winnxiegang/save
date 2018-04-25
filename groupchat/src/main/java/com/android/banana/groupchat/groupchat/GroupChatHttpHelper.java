package com.android.banana.groupchat.groupchat;

import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.groupchat.bean.ChatConfigBean;
import com.android.banana.groupchat.bean.GroupChatTopic1;
import com.android.banana.groupchat.chatenum.AuthorityStateQueryType;
import com.android.banana.groupchat.ilistener.IMGroupChatCallback;
import com.android.banana.http.JczjURLEnum;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.google.gson.Gson;
import com.jl.jczj.im.bean.IMParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by qiaomu on 2017/6/7.
 */

public class GroupChatHttpHelper implements IHttpResponseListener {
    private IMGroupChatCallback callback;
    private WrapperHttpHelper httpHelper;
    private String mGroupId;

    public GroupChatHttpHelper(IMGroupChatCallback listener, String groupId) {
        this.callback = listener;
        mGroupId = groupId;
        this.httpHelper = new WrapperHttpHelper(this);
    }

    public void enterRoomOperate() {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.USER_ENTER_GROUP_OPERATE, true);
        formBody.put("groupId", mGroupId);
        formBody.put("timestamp", System.currentTimeMillis());
        formBody.setGenericClaz(ChatConfigBean.class);
        httpHelper.startRequest(formBody);
    }

    public void queryChatRoomTopic(String groupChatId) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.THEME_QUERY, true);
        formBody.put("objectId", groupChatId);
        formBody.put("objectType", "GROUP_CHAT");
        formBody.setGenericClaz(GroupChatTopic1.class);
        httpHelper.startRequest(formBody, true);
    }

    public void checkIMLogin(String groupId) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.USER_AUTH_LOGIN_CHAT_VALIDATE, true);
        formBody.put("platformType", "CELL_CLIENT");
        formBody.put("groupId", groupId);
        formBody.put("timestamp", System.currentTimeMillis());
        formBody.setGenericClaz(ChatConfigBean.class);
        httpHelper.startRequest(formBody, true);
    }

    public void checkChatRoomConfig(int clickViewId, String groupChatId, @AuthorityStateQueryType.ActionType String actionType) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.USER_AUTHORITY_OR_GROUP_QUERY, true);
        formBody.put("groupChatId", groupChatId);
        formBody.put("clickViewId", clickViewId);
        //    formBody.put("newMsg", newMsg);
        formBody.put("timestamp", System.currentTimeMillis());
        formBody.put("relationGroupType", AuthorityStateQueryType.GroupType.RELATION_GROUP_CHAT);
        //AuthorityStateQueryType.ActionType.RELATION_GROUP_POST_COUPON : AuthorityStateQueryType.ActionType.RELATION_GROUP_POST_TEXT
        formBody.put("relationGroupUserActionType", actionType);
        formBody.setGenericClaz(ChatConfigBean.class);
        httpHelper.startRequest(formBody);
    }

    public void sendMessage(String mode, String message, int position) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.GROUP_USER_MESSAGE_SEND, true);
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            object.put("bodyType", mode);
            object.put("content", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(object);
        formBody.put("bodyType", mode);
        formBody.put("groupId", mGroupId);
        formBody.put("identifyId", IMParams.identifyId);
        formBody.put("userId", IMParams.userId);
        formBody.put("authId", IMParams.authId);
        formBody.put("bodiesJsonData", array.toString());
        formBody.put("timestamp", String.valueOf(System.currentTimeMillis()));
        formBody.put("position", position);
        httpHelper.startRequest(formBody, true);

    }

    public void sendImageMessage(String mode, String filePath, int position) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.GROUP_USER_MESSAGE_SEND, true, true);

        File file = new File(filePath);
        formBody.putFormDataPart("file", file.getName(), file);
        formBody.put("bodyType", mode);
        formBody.put("groupId", mGroupId);
        formBody.put("identifyId", IMParams.identifyId);
        formBody.put("userId", IMParams.userId);
        formBody.put("authId", IMParams.authId);
        formBody.put("timestamp", String.valueOf(System.currentTimeMillis()));
        formBody.put("position", position);
        httpHelper.startRequest(formBody, true);
    }


    @Override
    public void onSuccess(com.android.httprequestlib.RequestContainer request, Object result) {
        LogUtils.e("kk",result.toString());
        if (callback == null)
            return;
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        switch (requestEnum) {
            case THEME_QUERY:
                try {
                    callback.onChatTopicQueryResult((GroupChatTopic1) result, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case USER_AUTH_LOGIN_CHAT_VALIDATE:
                callback.onUserAuthorityLoginQueryResult((ChatConfigBean) result);
                break;
            case JCLQ_DYNAMIC_SCORE_DATA:
                break;
            case FT_DYNAMIC_SOCRE:
                break;
            case USER_ENTER_GROUP_OPERATE:
                callback.onEnterChatRoomOperateResult((ChatConfigBean) result, null, null);
                break;
            case GROUP_MESSAGE_DELETE: {
                int position = Integer.parseInt(request.getFiledsMap().get("position"));
            }
            break;
            case USER_AUTHORITY_OR_GROUP_QUERY:
                try {
                    callback.onUserAuthorityQueryResult(request, (ChatConfigBean) result, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case GROUP_USER_MESSAGE_SEND: {
                JSONObject jsonObject = (JSONObject) result;
                int position = request.getInt("position");
                String id = null;
                long messageSequence = 0l;
                try {
                    messageSequence = jsonObject.getLong("messageSequence");
                    id = jsonObject.getString("messageId");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    callback.onMessageSendResult(id, messageSequence, position, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            break;


        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject object, boolean netFailed) throws Exception {
        if (callback == null)
            return;
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        switch (requestEnum) {
            case USER_ENTER_GROUP_OPERATE:
                callback.onEnterChatRoomOperateResult(null, new ErrorBean(object), object);
                return;
            case GROUP_MESSAGE_DELETE:
                int position = Integer.parseInt(request.getFiledsMap().get("position"));
                //  callback.deleteMessageResult(position, error);
                return;
            case USER_AUTHORITY_OR_GROUP_QUERY:
                try {
                    if (netFailed) {
                        callback.onUserAuthorityQueryResult(request, null, object);
                    } else {
                        callback.onUserAuthorityQueryResult(request, new Gson().fromJson(object.toString(), ChatConfigBean.class), object);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case THEME_QUERY:
                try {
                    callback.onChatTopicQueryResult(null, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case GROUP_USER_MESSAGE_SEND: {
                int posInAdapter = request.getInt("position");
                callback.onMessageSendResult("-1", 0, posInAdapter,object);

            }
        }
        if (object != null) callback.onFailed(request,object);
    }

    public boolean isRequestFinished() {
        return httpHelper.isRequestFinished();
    }

    public void onDestroy() {
        httpHelper.onDestroy();
        this.callback = null;
    }
}
