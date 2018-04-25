package com.jl.jczj.im.callback;

import com.jl.jczj.im.bean.ChatMsgBodyList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qiaomu on 2017/6/14.
 */

public interface IMCallback {
    /**
     * 群聊消息发送 的回调
     *  @param newMsgId   如果发送成功 那么它就是该条消息的id，  如果发送失败返回-1
     * @param messageSequence
     * @param adapterPos 这条消息对应的适配器中的位置
     * @param errorJson    消息发送失败的错误
     */
    void onMessageSendResult(String newMsgId, long messageSequence, int adapterPos, JSONObject errorJson) throws JSONException;

    /**
     * 下拉刷新 得到的历史消息集合，如果没有历史消息那就是null
     *
     * @param msgList
     */
    void onHistoryMessages(ChatMsgBodyList msgList);


}
