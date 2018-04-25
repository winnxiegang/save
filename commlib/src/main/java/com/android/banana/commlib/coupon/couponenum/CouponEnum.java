package com.android.banana.commlib.coupon.couponenum;

/**
 * Created by lingjiu on 2017/7/24.
 */

public enum CouponEnum {
    NORMAL_GROUP_COUPON("AVERAGE"),
    LUCKY_GROUP_COUPON("RANDOM");

    String message;

    public String getMessage() {
        return message;
    }

    CouponEnum(String message){
        this.message = message;
    }
}
