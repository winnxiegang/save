package com.android.banana.commlib.liveScore.livescoreEnum;

import android.support.annotation.DrawableRes;

import com.android.banana.commlib.R;


/**
 * Created by qiaomu on 2017/8/21.
 * <p>
 * 动画直播 所有动作类型
 */

public enum AnimTypeEnum {
    GOAL("进球"),

    CORNER("角球"),

    SHOT_ON_TARGET("射正"),

    SHOT_OFF_TARGET("射偏"),

    FREE_KICK("任意球"),

    DANGER_FREE_KICK("危险的任意球"),

    GOAL_KICK("球门球"),

    THROW_IN("界外球"),

    THROW_IN_DANGER("危险的界外球"),

    BALL_SAFE("控球"),

    IN_POSSESSION("控球"),

    ATTACK("进攻"),

    PENALTY("点球"),

    DANGEROS_ATTACK("危险的进攻"),

    YELLOW_CARD("黄牌", R.drawable.action_event_yellow),

    RED_CARD("红牌", R.drawable.action_event_red),

    SUBSTITUTION("换人", R.drawable.action_event_switch),

    OFFSIDE("越位", R.drawable.action_event_offside),

    FIRST_HALF_START("中线开球", R.drawable.action_event_middle_kick),

    FIRST_HALF_END("半场休息", R.drawable.icon_pause_break),

    MATCH_ENDED("比赛结束", R.drawable.icon_finish_large),

    INJURY_TIME("伤停补时", R.drawable.action_event_time_add_injured),

    FIRST_HALF_ET_START("加时赛 上", R.drawable.action_event_time_over_finish),

    FIRST_HALF_ET_END("加时赛 半场休息", R.drawable.action_event_time_over),

    SECOND_HALF_ET_START("加时赛 下", R.drawable.action_event_time_over_finish),

    SECOND_HALF_ET_END("加时赛 完场", R.drawable.action_event_time_over_finish),

    TIME_CHANGED("开球时间更改", R.drawable.action_event_change_start_time),

    KICK_OFF_DELAYED("开球延迟", R.drawable.action_event_position_goal),

    PLAY_SUSPENDED("比赛延期", R.drawable.icon_position_large),

    ABANDONED("比赛取消", R.drawable.icon_cancel_large),

    STANDBY_FOR_PENALTY_SHOOTOUT("点球大战", R.drawable.action_event_pentally),

    SUSPENDED_BAD_WEATHER("天气原因比赛暂停", R.drawable.action_event_pause_weather),

    SUSPENDED_CROWD_DISTURBANCE("球场暴动比赛暂停", R.drawable.action_event_pause_violence),

    SUSPENDED_SERIOUS_INJURY("球员受伤比赛暂停", R.drawable.action_event_pause_injured),

    SUSPENDED_FLOODLIGHT_FAILURE("照明故障比赛暂停", R.drawable.action_event_pause_nolight_),

    SUSPENDED("比赛暂停", R.drawable.icon_event_pause),

    SIGNAL_INTERRUPT("信号中断", R.drawable.action_event_pause_nosign),

    BLOCKED_SHOT(""),

    SAFE_FREE_KICK("任意球"),

    FK5(""),

    BALL("控球"),

    THROW_IN_ATTACK("界外球"),

    THROW_IN_SAFE("界外球"),

    CR_R(""),

    CR_L(""),

    ATTACK_FREE_KICK("任意球"),

    STANDBY_FOR_KICK_OFF(""),

    BETSTOP(""),

    BETSTART(""),

    INJURY(""),

    UNKNOW("未知");


    private String mAction;
    private int mStatusResourceId;

    AnimTypeEnum(String action) {
        mAction = action;
    }

    AnimTypeEnum(String action, @DrawableRes int statusId) {
        mAction = action;
        mStatusResourceId = statusId;
    }

    public String getAction() {
        return mAction;
    }

    public int getStatusResourceId() {
        return mStatusResourceId;
    }


    public static AnimTypeEnum safeValueOf(String action) {

        try {
            return AnimTypeEnum.valueOf(action);

        } catch (Exception e) {
        }

        return UNKNOW;
    }

    /**
     * 同时发生的动作中是否有图片动作
     *
     * @param animTypeEnum
     * @return
     */
    public static boolean hasPictureAction(AnimTypeEnum... animTypeEnum) {
        for (AnimTypeEnum typeEnum : animTypeEnum) {
            if (typeEnum == null)
                continue;
            switch (typeEnum) {
                case YELLOW_CARD://("黄牌"),
                case RED_CARD://("红牌"),
                case SUBSTITUTION://("换人"),
                case OFFSIDE://("越位"),
                case FIRST_HALF_START://("中线开球"),
                case FIRST_HALF_END://("半场休息"),
                case MATCH_ENDED://("比赛结束"),
                case INJURY_TIME://("伤停补时"),
                case FIRST_HALF_ET_END://("加时赛 半场休息"),
                case FIRST_HALF_ET_START://("加时赛 上"),
                case SECOND_HALF_ET_START://("加时赛 下"),
                case SECOND_HALF_ET_END://("加时赛 完场"),
                case TIME_CHANGED://("开球时间更改"),
                case KICK_OFF_DELAYED://("开球延迟"),
                case PLAY_SUSPENDED://("比赛延期"),
                case ABANDONED://("比赛取消"),
                case SUSPENDED_BAD_WEATHER://("天气原因比赛暂停"),
                case SUSPENDED_CROWD_DISTURBANCE://("球场暴动比赛暂停"),
                case SUSPENDED_SERIOUS_INJURY://("球员受伤比赛暂停"),
                case SUSPENDED_FLOODLIGHT_FAILURE://("照明故障比赛暂停"),
                case SUSPENDED://("比赛暂停"),
                case SIGNAL_INTERRUPT://("信号中断"),
                case STANDBY_FOR_PENALTY_SHOOTOUT://(点球大战)
                    return true;
            }
        }
        return false;
    }

    /**
     * 这些动作都是要单独显示,即便同一时刻有两个
     * @param animType
     * @return
     */
    public static boolean shouldSingle(AnimTypeEnum animType) {
        boolean hasPictureAction = hasPictureAction(animType);
        if (hasPictureAction)
            return true;
        switch (animType) {
            case CORNER:
            case SHOT_ON_TARGET://射正
            case SHOT_OFF_TARGET://("射偏"),
            case ATTACK_FREE_KICK:
            case FREE_KICK://("任意球"),
            case SAFE_FREE_KICK:////("任意球"),
            case DANGER_FREE_KICK://("危险的任意球"),
            case GOAL_KICK:
            case THROW_IN_ATTACK:
            case THROW_IN_SAFE:
            case THROW_IN://("界外球"),
            case THROW_IN_DANGER://("危险的界外球"),
            case PENALTY://("点球"),
            case GOAL:
                return true;
        }
        return false;
    }
}
