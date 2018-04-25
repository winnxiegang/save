package com.android.banana.groupchat.chatenum;

/**
 * Created by zhouyi on 2016/2/29 10:45.
 */
public enum NotificationMessageTypeEnum {

    REPLY,

    MENTION,

    SUBSCRIBE,

    NOTICE,

    COUPON,

    SUBJECT_COMMENT,

    LT_FOLLOWER_CREATE,

    LOTTERY_PROJECT,

    SYSTEM,

    DEFAULT;


    public static NotificationMessageTypeEnum saveValueOf(String name) {
        try {
            return NotificationMessageTypeEnum.valueOf(name);
        } catch (Exception e) {
            return DEFAULT;
        }
    }
}
