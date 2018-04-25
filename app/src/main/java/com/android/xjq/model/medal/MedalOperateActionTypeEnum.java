package com.android.xjq.model.medal;

/**
 * Created by lingjiu on 2017/9/29.
 */

public enum MedalOperateActionTypeEnum {
    ADORN("佩戴"),
    CANCEL("取消"),
    DELETE("删除");

    String action;

    public String getAction() {
        return action;
    }

    MedalOperateActionTypeEnum(String action) {
        this.action =action;
    }
}
