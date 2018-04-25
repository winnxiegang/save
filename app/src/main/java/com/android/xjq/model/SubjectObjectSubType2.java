package com.android.xjq.model;

/**
 * Created by qiaomu on 2018/2/7.
 *
 * 香蕉球2期 动态枚举
 */

public enum SubjectObjectSubType2 {
    //NORMAL=说说  写专栏=PERSONAL_ARTICLE  香蕉号文章=ARTICLE
    NORMAL("普通"),

    ARTICLE("文章"),

    PERSONAL_ARTICLE("个人文章"),

    TRANSMIT_SUBJECT("转发话题"),

    MARK_ATTITUDE_COMMENT("评论被标记态度"),

    MARK_ATTITUDE_PERSONAL_ARTICLE("个人文章被标记态度"),

    MARK_ATTITUDE_MSG_SCREEN("弹幕被标记态度"),

    MARK_ATTITUDE_ROCKETS("火箭被标记为态度"),

    EVENT_WIN_PK("赢得PK"),

    EVENT_WIN_CHEER("赢得北单"),

    EVENT_SEND_COUPON("发红包"),

    EVENT_SPONSOR_DRAW("赞助抽奖"),

    EVENT_REWARD_ANCHOR("打赏主播"),

    XJQ_VIDEO("香蕉球视频"),


    DEFAULT("default");


    private String mDescription;

    SubjectObjectSubType2(String description) {
        mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }

    public static SubjectObjectSubType2 safeEnumOf(String name) {
        if (name == null) {
            return DEFAULT;
        }
        for (SubjectObjectSubType2 value : SubjectObjectSubType2.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return DEFAULT;
    }
}
