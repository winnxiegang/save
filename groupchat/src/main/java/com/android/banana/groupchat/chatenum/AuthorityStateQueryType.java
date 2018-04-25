package com.android.banana.groupchat.chatenum;

import android.support.annotation.StringDef;

/**
 * Created by qiaomu on 2017/6/29.
 */

public class AuthorityStateQueryType {

    @StringDef({GroupType.RACE_LIVE_CHAT, GroupType.RELATION_GROUP_CHAT})
    public @interface GroupType {
        String RELATION_GROUP_CHAT = "RELATION_GROUP_CHAT";//("圈子聊天室"),

        String RACE_LIVE_CHAT = "RACE_LIVE_CHAT";//("比分直播聊天室")
    }

    @StringDef({ActionType.RELATION_GROUP_POST_TEXT, ActionType.RELATION_GROUP_POST_COUPON, ActionType.RELATION_GROUP_POST_PICTURE})
    public @interface ActionType {
        String RELATION_GROUP_POST_TEXT = "RELATION_GROUP_POST_TEXT";//("社团发文字"),

        String RELATION_GROUP_POST_COUPON = "RELATION_GROUP_POST_COUPON";//("社团发红包"),

        String RELATION_GROUP_POST_PICTURE = "RELATION_GROUP_POST_PICTURE";//("社团发图片")

    }
}
