package com.jl.jczj.im.callback;

import com.jl.jczj.im.bean.ChatMsgBody;
import com.jl.jczj.im.tim.TimMessageType;

/**
 * Created by qiaomu on 2017/11/6.
 * <p>
 * 用于接收推送过来的消息，包括普通消息 系统消息
 */

public interface IMTimCallback {
    /**
     * 接收到推送过来的消息
     *
     * @param msgBody
     */
    void onTimNewMessage(ChatMsgBody msgBody);

    /**
     * 接收到的系统通知消息，
     *
     * @param groupId     对应的群组groupid
     * @param messageType 对应的事件类型  ,请查看{@link TimMessageType}
     */
    void onTimSystemMessage(String groupId,  String messageType);
}
