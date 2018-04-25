package com.android.xjq.model.message;

/**
 * Created by kokuma on 2017/11/28.
 */

public enum SystemMessageTypeEnum {

    COUPON("红包"),

    SYSTEM("系统消息"),

    DEFAULT("");

    private String message;

    SystemMessageTypeEnum(String message) {
        this.message = message;
    }

    public static SystemMessageTypeEnum saveValueOf(String name) {

        try {
            return SystemMessageTypeEnum.valueOf(name);
        } catch (Exception e) {

        }
        return DEFAULT;
    }
}
