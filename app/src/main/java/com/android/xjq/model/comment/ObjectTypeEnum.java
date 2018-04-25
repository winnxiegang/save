package com.android.xjq.model.comment;

/**
 * Created by zhouyi on 2015/12/18 15:46.
 */
public enum ObjectTypeEnum {

    LOTTERY_PROJECT("方案"),

    SUBJECT("话题"),

    COMMENT_REPLY("评论回复"),

    COMMENT("评论"),

    CMS_NEWS("新闻资讯"),

    COUPON("动态红包"),

    JCZQ_RACE("足球赛事"),

    JCLQ_RACE("篮球赛事"),

    CHAT_ROOM("聊天室"),

    DEFAULT(null);

    private String message;

    public static ObjectTypeEnum safeValueOf(String name) {

        try {
            return ObjectTypeEnum.valueOf(name);
        } catch (Exception e) {
            return DEFAULT;
        }
    }

    ObjectTypeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
