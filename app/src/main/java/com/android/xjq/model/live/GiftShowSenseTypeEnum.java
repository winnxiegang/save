package com.android.xjq.model.live;

/**
 * Created by zhouyi on 2017/4/7.
 */

public enum GiftShowSenseTypeEnum {

    ALL_PLATFORM("全平台"),

    CURRENT_PLATFORM("直播间"),

    COMMON("公共聊天"),//如果连击数大于1需要显示相应的连击数。

    LIVE_ROOM_SPECIAL("直播间特殊效果"),

    DEFAULT("");

    private String message;

    public static GiftShowSenseTypeEnum safeValueOf(String name) {
        try {
            return GiftShowSenseTypeEnum.valueOf(name);
        } catch (Exception e) {
        }
        return DEFAULT;
    }

    GiftShowSenseTypeEnum(String message) {
        this.message = message;
    }
}
