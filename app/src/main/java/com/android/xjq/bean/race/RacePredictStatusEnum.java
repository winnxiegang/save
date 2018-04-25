package com.android.xjq.bean.race;


/**
 * Created by zhouyi on 2016/5/16 17:25.
 */
public enum RacePredictStatusEnum {
    READY_CALCULATE("待计算"),

    CORRECT("命中状态"),

    NOT_CORRECT("未命中状态"),

    DRAW("走盘"),

    DEFAULT("");

    private String message;

    public String getMessage() {
        return message;
    }

    RacePredictStatusEnum(String message) {
        this.message = message;
    }

    public static RacePredictStatusEnum safeValueOf(String name) {

        try {
            return RacePredictStatusEnum.valueOf(name);

        } catch (Exception e) {
        }

        return DEFAULT;
    }


}
