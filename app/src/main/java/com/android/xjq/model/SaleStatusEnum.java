package com.android.xjq.model;

/**
 * Created by lingjiu on 2018/3/1.
 */

public enum SaleStatusEnum {
    WAIT("未开始"),

    PROGRESSING("进行中"),

    PAUSE("暂停"),

    FINISH("结束"),

    CANCEL("取消"),

    DEFAULT("默认");

    String message;

    SaleStatusEnum(String message) {
        this.message = message;
    }

    public static SaleStatusEnum safeValueOf(String name) {
        try {
            return SaleStatusEnum.valueOf(name);
        } catch (Exception e) {
            return DEFAULT;
        }
    }
}
