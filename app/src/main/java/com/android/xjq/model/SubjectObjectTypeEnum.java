package com.android.xjq.model;

/**
 * Created by zhouyi on 2017/1/10 16:24.
 */

public enum SubjectObjectTypeEnum {

    FT_SEASON_RACE,

    BT_SEASON_RACE,

    LOTTERY_PROJECT,

    ARTICLE,

    CMS,//群聊热点资讯

    CMS_NEWS,//群聊热点资讯

    PERSON,//群聊人物

    NORMAL,

    DEFAULT;

    public static SubjectObjectTypeEnum safeValueOf(String name) {

        try {
            return SubjectObjectTypeEnum.valueOf(name);
        } catch (Exception e) {
            return DEFAULT;
        }
    }

}
