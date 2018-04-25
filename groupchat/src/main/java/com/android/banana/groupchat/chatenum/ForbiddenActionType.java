package com.android.banana.groupchat.chatenum;

import android.support.annotation.StringDef;

/**
 * Created by qiaomu on 2017/6/7.
 */

public class ForbiddenActionType {
    /**
     * POST("发言"),
     * <p>
     * PUSH_ANALYSIS("发分析"),
     * <p>
     * RACE_CHAT("比赛聊天"),
     * <p>
     * RELATION_GROUP_POST("聊天室发言"),
     * <p>
     * JOIN_RELATION_GROUP("加入群聊"),
     * <p>
     * USER_JOIN_RELATION_GROUP_COUNT_HAS_REACHED_THE_UPPER_LIMIT(聊天室太过火爆)
     * <p>
     * <p>
     * GROUP_HAS_DISMISSED(聊天室已解散)
     */
    @StringDef({Type.GROUP_USER_ENTRY_NOT_FOUND,
            Type.SIGN_ERROR, Type.POST, Type.PUSH_ANALYSIS,
            Type.RACE_CHAT, Type.RELATION_GROUP_POST,
            Type.JOIN_RELATION_GROUP, Type.JOIN_LIMIT,
            Type.GROUP_HAS_DISMISSED,
            Type.USER_HAD_REMOVE_CHAT_ROOM,
            Type.RELATION_GROUP_CHAT,
            Type.PRIVATE_CHAT_ROOM,
            Type.RACE_CHAT_FORBIDDEN})
    public @interface Type {
        String POST = "POST";
        String PUSH_ANALYSIS = "PUSH_ANALYSIS";
        String RACE_CHAT = "RACE_CHAT";
        String RACE_CHAT_FORBIDDEN = "RACE_CHAT_FORBIDDEN";
        String RELATION_GROUP_CHAT = "RELATION_GROUP_CHAT";
        String RELATION_GROUP_POST = "RELATION_GROUP_POST";
        String JOIN_RELATION_GROUP = "JOIN_RELATION_GROUP";
        String JOIN_LIMIT = "USER_JOIN_RELATION_GROUP_COUNT_HAS_REACHED_THE_UPPER_LIMIT";
        String GROUP_HAS_DISMISSED = "GROUP_HAS_DISMISSED";
        String PRIVATE_CHAT_ROOM = "PRIVATE_RELATION_GROUP_NOT_INVITE_CAN_NOT_JOIN";//私密聊天室未被邀请

        String SIGN_ERROR = "SIGN_ERROR";//签名
        String GROUP_USER_ENTRY_NOT_FOUND = "GROUP_USER_ENTRY_NOT_FOUND";//群聊用户入口未找到
        String USER_HAD_REMOVE_CHAT_ROOM = "USER_HAD_REMOVE_CHAT_ROOM";//被移除聊天室
        String USER_LOGIN_EXPIRED = "USER_LOGIN_EXPIRED";
    }
}
