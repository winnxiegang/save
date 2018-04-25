package com.android.xjq.model.gift;

/**
 * Created by lingjiu on 2017/4/1.
 */

public enum PayType {

    GOLD_COIN("金币"),

    GIFT_COIN("礼金"),

    POINT_COIN("香蕉币");


    String message;

    public String getMessage() {
        return message;
    }

    PayType(String message) {

        this.message = message;
    }
}
