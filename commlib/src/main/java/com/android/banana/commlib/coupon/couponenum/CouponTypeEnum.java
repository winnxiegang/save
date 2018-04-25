package com.android.banana.commlib.coupon.couponenum;

/**
 * 发送红包的类型
 * Created by zhouyi on 2016/6/12 22:18.
 */
public enum CouponTypeEnum {

    //话题类型
    SUBJECT,

    //方案类型
    LOTTERY_PROJECT,

    //拼手气红包
    GROUP,
    //比分直播足球赛事
    FT_SEASON_RACE,
    //比分直播篮球赛事
    BT_SEASON_RACE,

    USER_GUESS_PRIZE_NOTICE,//有奖竞猜通知

    PRIZE_GUESS,

    CHAT_ROOM,
    //定向红包，或者普通红包，红包详情页红包类型
    NORMAL,

    DEFAULT;


    public static CouponTypeEnum safeValueOf(String name){

        try{
            return CouponTypeEnum.valueOf(name);
        }catch (Exception e){}

        return DEFAULT;

    }
}
