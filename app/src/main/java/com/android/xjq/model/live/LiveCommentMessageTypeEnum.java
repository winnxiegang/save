package com.android.xjq.model.live;

/**
 * Created by zhouyi on 2017/4/5.
 */

public enum LiveCommentMessageTypeEnum {

    GIFTCORE_GIFT_GIVE_SUM_TEXT("礼物赠送统计效果通知文本消息"),

    SEAT_LONG_SET_NOTICE("主播设置龙椅"),

    TEXT("普通文本消息"),

    NORMAL("普通文本消息"),

    CHANNEL_PUSH_STREAM_STATUS_CHANGE("频道推流状态变化通知"),

    COUPON_CREATE_SUCCESS_NOTICE_TEXT("红包通知文本消息"),

    PRIZECORE_PRIZE_NOTICE_TEXT("爆奖池中奖通知文本消息"),

    GIFTCORE_GIFT_GIVE_TEXT("礼物赠送通知文本消息"),

    SPECIAL_EFFECT_ENTER_NOTICE("特殊用户入场"),

    CHANNEL_TRANSFER_NOTICE("直播间传送消息"),

    USER_MEDAL_AWARD_NOTICE("首次获得粉丝勋章消息"),

    USER_MEDAL_OVER_MAX_AWARD_NUM_NOTICE("粉丝勋章数达到上限通知"),

    GIFTCORE_GIFT_AREA_LINK_NOTICE("礼物更新通知"),

    DECREE_USER_PRIZED_TEXT("圣旨红包派奖通知"),

    LUCKYDRAW_DECREE_CREATE_TEXT("圣旨发送成功通知"),

    DEFAULT("");


    private String message;

    LiveCommentMessageTypeEnum(String message) {
        this.message = message;
    }

    public static LiveCommentMessageTypeEnum safeValueOf(String name) {
        try {
            return LiveCommentMessageTypeEnum.valueOf(name);
        } catch (Exception e) {

        }
        return DEFAULT;
    }
}
