package com.android.xjq.model.comment;

/**
 * Created by zhouyi on 2016/1/8 10:31.
 */
public enum CommentOrderByEnum {

    SET_TOP_AND_GMT_DESC("gmt_create asc ,floor asc"),

    SET_TOP_AND_GMT_AES("gmt_create asc ,floor asc"),

    GMT_CREATE_DESC("gmt_create asc ,floor asc");

    String s1;

    CommentOrderByEnum(String s) {
        s1 = s;
    }

    public String getS1() {
        return s1;
    }
}
