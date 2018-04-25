package com.jl.jczj.im.loop;

import android.text.TextUtils;

import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.google.gson.Gson;
import com.jl.jczj.im.IBaseMessageManager;
import com.jl.jczj.im.bean.ChatMsgBodyList;
import com.android.banana.commlib.bean.ErrorBean;
import com.jl.jczj.im.bean.IMParams;
import com.jl.jczj.im.bean.ImChatConfig;
import com.jl.jczj.im.body.ImFormBody;
import com.jl.jczj.im.callback.IMCallback;
import com.jl.jczj.im.callback.IMComCallback;
import com.jl.jczj.im.callback.IMTimCallback;
import com.jl.jczj.im.im.ImManager;
import com.jl.jczj.im.service.IMUrlEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by zhouyi on 2017/5/27.
 */
public class LoopMessageManager implements IBaseMessageManager, OnHttpResponseListener {
    private HttpRequestHelper httpRequestHelper = new HttpRequestHelper(this, false);
    private MessageLoopOperator messageLoopOperator;
    private IMCallback imCallback;
    private Gson gson;
    private boolean firstFetchChat = true;
    private static final int MESSAGE_COUNT_PAGE = 50;

    public LoopMessageManager() {
        gson = new Gson();
        init();
    }

    private void init() {
        messageLoopOperator = new MessageLoopOperator();
        messageLoopOperator.init(new LoopMessageListener() {
            @Override
            public void execute() {
                loadMessage();
            }
        });
    }

    public void loadMessage() {

    }

    @Override
    public void receiveMessage(String message, ErrorBean errorBean) {
        if (imCallback == null)
            return;
        ChatMsgBodyList bodyList = gson.fromJson(message, ChatMsgBodyList.class);
        imCallback.onHistoryMessages(bodyList);
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
        // formBody.put("inviteId", IMParams.inviteId);
        formBody.put("timestamp", System.currentTimeMillis());

        //  String groupCode = TextUtils.isEmpty(IMParams.groupCode) ? IMParams.objectType + "_" + IMParams.objectId : IMParams.groupCode;
        formBody.put("groupCode", IMParams.groupCode);
        httpRequestHelper.startRequest(formBody, false);

    }

    @Override
    public void getMessageList(boolean loadMore, long messageSequence) {
        if (TextUtils.isEmpty(IMParams.identifyId)) {

            return;
        }

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
    public void leaveGroup(String groupId) {
        ImFormBody formBody = new ImFormBody(IMUrlEnum.GROUP_USER_ENTRY_LEAVE, true);
        formBody.put("authId", IMParams.authId);
        formBody.put("userId", IMParams.userId);
        formBody.put("identifyId", IMParams.identifyId);
        formBody.put("timestamp", System.currentTimeMillis());
        httpRequestHelper.startRequest(formBody, false);
    }

    @Override
    public void exitGroup(IMComCallback callback) {//2
        ImFormBody map = new ImFormBody(IMUrlEnum.GROUP_USER_QUIT, true);
        map.put("userId", IMParams.userId);
        map.put("groupCode", IMParams.groupCode);
        map.put("groupId", IMParams.groupId);
        map.put("quitReason", "exit_group");
        map.put("timestamp", System.currentTimeMillis());
        map.put("authId", IMParams.authId);
        map.setImComCallback(callback);
        httpRequestHelper.startRequest(map, false);
    }

    @Override
    public void removeImCallback(IMCallback imCallback) {
        this.imCallback = null;
        //应该接着取消或移除请求
    }

    @Override
    public void removeTimCallback(IMTimCallback timCallBack) {

    }

    @Override
    public void addImCallback(IMCallback imCallback) {
        this.imCallback = imCallback;
        //因为loopmessage被ImagroupManager持有,也是单一实例存在的，
        //在每次重新进入房间的时候,loopmessage并不会被重新初始化,所以firstFetchChat字段手动重置
        firstFetchChat = true;
    }

    @Override
    public void addTimCallback(IMTimCallback imCallback) {

    }

    @Override
    public void executorSuccess(RequestContainer request, JSONObject jo) {
        IMUrlEnum urlEnum = (IMUrlEnum) request.getRequestEnum();
        switch (urlEnum) {
            case GROUP_MESSAGE_QUERY_BY_USER://消息轮询
                receiveMessage(jo == null ? null : jo.toString(), null);
                break;
            case GROUP_USER_ENTRY_BASED_MESSAGE_SEND://发送消息
                if (imCallback != null) {
                    int position = request.getInt("position");
                    long messageSequence = 0l;
                    String id = null;
                    try {
                        messageSequence = jo.getLong("messageSequence");
                        id = jo.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        imCallback.onMessageSendResult(id, messageSequence, position, null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case GROUP_USER_ENTRY_ENTER: {//进入聊天室成功
                ImChatConfig configure = gson.fromJson(jo.toString(), ImChatConfig.class);
                IMParams.identifyId = configure.identifyId;

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
                if (imCallback != null) {
                    int position = request.getInt("position");
                    String id = null;
                    try {
                        id = jo.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        imCallback.onMessageSendResult(id, 0, position, jo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case GROUP_USER_ENTRY_ENTER: {//进入聊天室成功

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


    private Type getInterfaceActualType(Object calz) {
        Type[] types = calz.getClass().getGenericInterfaces();
        if (types != null && types.length > 0 && types[0] instanceof ParameterizedType) {
            return ((ParameterizedType) types[0]).getActualTypeArguments()[0];
        }
        return null;
    }

    @Override
    public void executorFailed(RequestContainer request) {

    }

    @Override
    public void executorFinish() {

    }
}
