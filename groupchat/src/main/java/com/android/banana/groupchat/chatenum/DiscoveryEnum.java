package com.android.banana.groupchat.chatenum;

/**
 * Created by zhouyi on 2015/12/14 15:34.
 */
public enum DiscoveryEnum {

    IMAGE("图片"),

    CHAT_ROOM("聊天室"),

    JCLQ_MATCH("竞彩篮球"),

    JCZQ_MATCH("竞彩足球"),

    INSERT_ARTICLE_JCZQ_RACE("竞彩大学文章"),

    INSERT_JCZQ_RACE("竞彩大学插入足球赛事"),

    PURCHASE("方案"),

    NONE("无"),

    DEFAULT("");

    private String message;

    DiscoveryEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static DiscoveryEnum saveValueOf(String name) {
        DiscoveryEnum[] values = DiscoveryEnum.values();
        for (DiscoveryEnum value : values) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return DEFAULT;
    }
}
