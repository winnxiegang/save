package com.android.xjq.model.comment;

/**
 * Created by zhouyi on 2017/1/10 16:37.
 */

public enum CommentObjectTypeEnum {

    COMMENT_TO_ME,

    COMMENT_REPLY_TO_ME,

    DEFAULT;

    public static CommentObjectTypeEnum safeValueOf(String name) {

        try {
            return CommentObjectTypeEnum.valueOf(name);
        } catch (Exception e) {
            return DEFAULT;
        }
    }

}
