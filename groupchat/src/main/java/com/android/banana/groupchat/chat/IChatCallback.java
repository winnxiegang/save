package com.android.banana.groupchat.chat;

import com.android.banana.groupchat.bean.SimpleChatConfigBean;
import com.jl.jczj.im.bean.ChatMsgBodyList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qiaomu on 2017/11/4.
 */

public interface IChatCallback {
    /**
     * 私聊房间初始化配置
     * @param simpleChatConfig
     * @param jsonError
     * @throws JSONException
     */
    void onInitSuccess(SimpleChatConfigBean simpleChatConfig, JSONObject jsonError) throws JSONException;

    /**
     * 轮询或者下拉刷新后回调
     * @param chatMsgBodyList
     */
    void onNewMessages(ChatMsgBodyList chatMsgBodyList);

    /**
     * 消息发送
     * @param newMsgId
     * @param adapterPos
     * @param errorJson
     * @throws JSONException
     */
    void onMsgSendResult(String newMsgId, int adapterPos, JSONObject errorJson,long messageSequence) throws JSONException;
}
