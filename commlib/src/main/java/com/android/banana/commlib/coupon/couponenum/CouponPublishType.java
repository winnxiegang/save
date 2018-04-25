package com.android.banana.commlib.coupon.couponenum;

/**
 * Created by lingjiu on 2017/7/24.
 */

public enum CouponPublishType {
    IMMEDIATE("实时发送"),
    TIMED("定时发送"),
    ANCHOR_ASSIST("主播代发");

    String message;

    CouponPublishType(String message) {
        this.message = message;
    }
}
