package com.android.banana.groupchat.chatenum;

/**
 * Created by zaozao on 2017/6/6.
 * 群聊成员等级区分
 */

public enum ChatRoomMemberLevelEnum {
    NORMAL("普通成员"),

    GROUP_OWNER("群聊所有者"),

    GROUP_ADMIN("群聊管理员");

    String message;

    ChatRoomMemberLevelEnum(String message) {
        this.message = message;
    }

    public static ChatRoomMemberLevelEnum saveValueOf(String name) {

        for (ChatRoomMemberLevelEnum value : ChatRoomMemberLevelEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return NORMAL;
    }
}
