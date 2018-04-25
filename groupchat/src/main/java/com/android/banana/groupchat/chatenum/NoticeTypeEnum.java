package com.android.banana.groupchat.chatenum;


/**
 * Created by zaozao on 2017/11/4.
 */

public enum NoticeTypeEnum {
    COMMON,//"常用消息通知"),

    GROUP_CHAT_JOIN_ADUIT_NOTICE,//("群聊用户加入审核通知"),已同意  已拒绝

    GROUP_CHAT_APPLY_JOIN_NOTICE,//("群聊用户申请加入通知"),同意按钮，拒绝按钮，等待

    GROUP_CHAT_USER_REMOVE_NOTICE,//("群聊用户被移除群通知"),

    GROUP_CREATE_SUCCESS,//("用户创建群成功"),

    GROUP_CHAT_INVITE_JOIN_NOTICE,//(增加的邀请类型)

    DEFAULT;

    public static NoticeTypeEnum safeValueOf(String name) {

        try {
            return NoticeTypeEnum.valueOf(name);
        } catch (Exception e) {
            return DEFAULT;
        }
    }

}
