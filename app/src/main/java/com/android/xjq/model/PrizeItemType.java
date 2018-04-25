package com.android.xjq.model;

/**
 * 爆奖池获奖类型
 * Created by lingjiu on 2017/6/26.
 */

public enum PrizeItemType {

    SPECIAL("幸运大奖"),

    FIRST_PRIZE("一等奖"),

    SECOND_PRIZE("二等奖"),

    THIRD_PRIZE("三等奖"),

    DEFAULT("幸运大奖");

    String message;

    public String getMessage() {
        return message;
    }

    PrizeItemType(String message) {
        this.message = message;
    }

    public static PrizeItemType safeValueOf(String name){
        try {
            return PrizeItemType.valueOf(name);
        } catch (Exception e) {
            return DEFAULT;
        }


    }

}
