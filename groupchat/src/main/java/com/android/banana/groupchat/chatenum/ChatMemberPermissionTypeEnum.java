package com.android.banana.groupchat.chatenum;

/**
 * Created by zaozao on 2017/6/6.
 */

public enum ChatMemberPermissionTypeEnum {
    ALL_POST,

    ONLY_GROUP_ADMIN,

    ONLY_BY_USER_AND_GROUP_ADMIN,

    DEFAULT;


    public static ChatMemberPermissionTypeEnum safeValueOf(String name) {

        try {
            return ChatMemberPermissionTypeEnum.valueOf(name);
        } catch (Exception e) {
            return DEFAULT;
        }
    }
}
