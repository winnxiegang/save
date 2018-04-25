package com.android.banana.commlib.coupon.couponenum;

/**
 * Created by lingjiu on 2017/11/6.
 */

public enum  CouponPlatformType {
    //直播间
    CHANNEL_AREA("CHANNEL_AREA"),
    //群聊
    GROUP_CHAT("GROUP_CHAT");

    String message;

    public String getMessage() {
        return message;
    }

    CouponPlatformType(String message){
        this.message = message;
    }
}
