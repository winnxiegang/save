package com.android.xjq.model.message;

/**
 * Created by kokuma on 2017/11/28.
 */

public enum SystemMessageSubTypeEnum {

    DIRECTED_COUPON("定向红包"),

    LT_FOLLOWER_CANCEL("撤单通知"),

    LT_FOLLOWER_FAIL_SOLD("流标通知"),

    LT_PURCHASE_NEAR_STOP_SELL("临近发单通知"),

    FOLLOW_USER_CAN_FETCH("粉丝可抢通知"),

    COPY_PROJECT_CAN_FETCH("跟单可抢通知"),

    USER_GUESS_PRIZE_NOTICE("有奖竞猜派奖通知"),

    SUBJECT_DELETE_NOTICE_TO_USER("话题删除通知会员"),

    COMMENT_DELETE_NOTICE_TO_USER("评论删除通知会员"),
    ARTICLE_SELECTED_UNDO_NOTICE_USER("稿费不予发放通知会员"),


    USER_FORBIDDEN_ACTION_NOTICE("禁言通知"),

    CHAT_ROOM_INVITE_NOTICE("聊天室邀请通知"),
    ARTICLE_SELECTED_NOTICE_USER("文章入选通知会员"),
    ARTICLE_SET_ETITLE_NOTICE_USER("文章加精通知会员"),
    ARTICLE_SET_TOP_NOTICE_USER("文章置顶通知会员"),
    USER_INFO_AUDIT_APPLY_NOTICE_TO_USER("用户信息审核通知会员"),

    CHANNEL_AREA_LIVE_NOTICE("直播节目即将开播"),

    DEFAULT("");

    private String message;

    SystemMessageSubTypeEnum(String message) {
        this.message = message;
    }

    public static SystemMessageSubTypeEnum saveValueOf(String name) {

        try {
            return SystemMessageSubTypeEnum.valueOf(name);
        }catch (Exception e){

        }

        return DEFAULT;
    }
}
