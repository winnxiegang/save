package com.android.xjq.model;

/**
 * Created by zhouyi on 2016/1/27 11:24.
 */
public enum StoreElementTypeEnum {

    IMAGE("图片"),

    EMOTION("表情"),

    MATCH("赛事"),

    PURCHASE_ORDER("方案"),

    AT("@");

    String message;

    StoreElementTypeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
