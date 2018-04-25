package com.android.residemenu.lt_lib.enumdata;

/**
 * Created by zhouyi on 2015/4/30.
 */
public enum NMK3ShapeEnum {
    SUM("和值"), // 4,5,6,7,8,9,10,11,12,13,14,15,16,17

    THT3("三同号通选"), // 111|222|333|444|555|666

    // 单式
    THD3("三同号单选"), // 111,222,333,444,555,666

    // 单式
    THD2("二同号单选"), // 11*,22*,33*,44*,55*,66*

    THF2("二同号复选"), // 11,22,33,44,55,66

    // 单式
    BTH3("三不同号"), // 123,124,125,126,134,135,136,145,146,156,234,235,236,245,246,256,345,34

    BTH2("二不同号"), // 12,13,14,15,16,23,24,25,26,34,35,36,45,46,56

    LH3("三连号通选"); // 123,234,345,456


    String message;

    NMK3ShapeEnum(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
