package com.android.banana.groupchat.chat;

import android.os.Handler;
import android.os.Message;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.groupchat.AppParam;
import com.android.banana.groupchat.bean.SimpleChatConfigBean;
import com.android.banana.http.JczjURLEnum;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.jl.jczj.im.MessageType;
import com.jl.jczj.im.bean.ChatMsgBodyList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by qiaomu on 2017/11/4.
 */

public class SimpleChatHttpHelper implements IHttpResponseListener {
    private static final int MSG_LOOP = 100;//可以进行下一次轮询请求
    private long intervalMill = 3000L;

    private IChatCallback mCallback;
    private String mIdentifyId;
    private String mAuthId;
    private String mTargetUserId;
    private String mLoginUserId;
    private String mSoleId;
    private WrapperHttpHelper mHttpHelper;


    private Handler loopHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_LOOP) {
                queryUnreadMsg(false, 0);
                startLoop();
            }
        }
    };
    private long mMessageSequence;


    public void startLoop() {
        loopHandler.sendEmptyMessageDelayed(MSG_LOOP, intervalMill);
    }

    public SimpleChatHttpHelper(IChatCallback callback, String targetUserId, String loginUserId, String soleId) {
        mCallback = callback;
        mTargetUserId = targetUserId;
        mLoginUserId = loginUserId;
        mSoleId = soleId;
        mHttpHelper = new WrapperHttpHelper(this);
    }

    /**
     * service	String	否	服务名，传USER_SOLE_INIT值
     * destinationUserId	String	否	目标用户id
     * authedUserId	String	是	登陆后的会员ID，未登陆的情况下，不传递
     */
    /*初始化私聊房间*/
    public void initUserSole(String groupId,String sourceType) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.USER_SOLE_INIT, true);
        formBody.put("destinationUserId", mTargetUserId);
        formBody.put("timestamp", System.currentTimeMillis());
        formBody.put("soleId",mSoleId);
        formBody.put("sourceId", groupId);  //群聊传groupId  其他不传
        formBody.put("sourceType",sourceType);//群聊GROUP  其他NORMAL

        formBody.setGenericClaz(SimpleChatConfigBean.class);
        mHttpHelper.startRequest(formBody);
    }

    //2.拉取未读私聊消息/小消息
    public void queryUnreadMsg(boolean refresh, long messageSequence) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.SOLE_MESSAGE_QUERY_BY_SOLE_USER_ENTRY, true);
        formBody.put("fetchAfter", !refresh);
        formBody.put("authId", mAuthId);
        formBody.put("userId", mLoginUserId);
        formBody.put("identifyId", mIdentifyId);
        formBody.put("messageSequence", refresh ? messageSequence : 0);
        formBody.put("timestamp", System.currentTimeMillis());
        formBody.setGenericClaz(ChatMsgBodyList.class);
        formBody.setRequestUrl(AppParam.IM_MAPI_XJQ+ AppParam.FT_API_S_URL);
        mHttpHelper.startRequest(formBody);
    }



    /*发送文本消息*/
    public void sendMsg(String bodyType,String newMsg, int adapterPos,String filePath) {
        RequestFormBody formBody = null;
        if(MessageType.TXT.equals(bodyType)){
            formBody = new RequestFormBody(JczjURLEnum.SOLE_USER_MESSAGE_SEND, true);
            JSONObject object = new JSONObject();
            JSONArray array = new JSONArray();
            try {
                object.put("bodyType", "TEXT");
                object.put("content", newMsg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(object);
            formBody.put("bodiesJsonData", array.toString());

        } else if(MessageType.IMAGE.equals(bodyType)){
            formBody = new RequestFormBody(JczjURLEnum.SOLE_USER_MESSAGE_SEND, true, true);
            File file = new File(filePath);
            formBody.putFormDataPart("file", file.getName(), file);
        }

        formBody.put("identifyId", mIdentifyId);
        formBody.put("timestamp", String.valueOf(System.currentTimeMillis()));
        formBody.put("position", adapterPos);
        formBody.put("typeCode", bodyType);
        formBody.put("userId", mLoginUserId);
        formBody.put("authId", mAuthId);
        formBody.put("soleId", mSoleId);
        mHttpHelper.startRequest(formBody);

    }

    @Override
    public void onSuccess(RequestContainer request, Object result) {
        LogUtils.e("kk","请求成功");
        if (mCallback == null)
            return;
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        switch (requestEnum) {
            case USER_SOLE_INIT:
                try {
                    SimpleChatConfigBean chatConfig = (SimpleChatConfigBean) result;
                    mAuthId = chatConfig.id;
                    mIdentifyId = chatConfig.identifyId;
                    mCallback.onInitSuccess(chatConfig, null);
                    mSoleId = chatConfig.soleId;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case SOLE_MESSAGE_QUERY_BY_SOLE_USER_ENTRY:
                ChatMsgBodyList bodyList = (ChatMsgBodyList) result;
                LogUtils.e("kk","请求成功"+bodyList.toString());
                mCallback.onNewMessages(bodyList);
                if (bodyList != null) {
                    mMessageSequence = bodyList.lastReadMessageSequence;
                }
                break;
            case SOLE_USER_MESSAGE_SEND:
                LogUtils.e("kk","消息发送成功");
                int position = request.getInt("position");
                try {
                    String msgId = ((JSONObject) result).getString("id");
                    long messageSequence = ((JSONObject) result).getLong("messageSequence");
                    mCallback.onMsgSendResult(msgId, position, null,messageSequence);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonError, boolean netFailed) throws Exception {
        LogUtils.e("kk",jsonError.toString());
        if (mCallback == null)
            return;
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        switch (requestEnum) {
            case USER_SOLE_INIT:
                mCallback.onInitSuccess(null, jsonError);
                break;
            case SOLE_MESSAGE_QUERY_BY_SOLE_USER_ENTRY:

                mCallback.onNewMessages(null);
                break;
            case SOLE_USER_MESSAGE_SEND:
                int position = request.getInt("position");
                mCallback.onMsgSendResult("-1", position, jsonError,-1);
                break;
        }
    }

    public void onDestroy() {
        loopHandler.removeCallbacksAndMessages(null);
        mCallback = null;
        mHttpHelper.onDestroy();
    }
}
