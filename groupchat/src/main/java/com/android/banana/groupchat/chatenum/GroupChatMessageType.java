package com.android.banana.groupchat.chatenum;

/**
 * Created by zaozao on 2017/11/7.
 */

public enum GroupChatMessageType {
    SYSTEM_MESSAGE,//系统通知消息
    SECRET_CHAT,//私聊消息
    GROUP_CHAT,//群聊消息
    LIVE ,    //直播消息
    DEFAULT;
    public static GroupChatMessageType safeValueOf(String name) {

        try {
            return GroupChatMessageType.valueOf(name);
        } catch (Exception e) {
            return DEFAULT;
        }
    }
}
