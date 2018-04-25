package com.android.xjq.model.gift;

/**
 * Created by lingjiu on 2017/3/8.
 */

public enum GiftCountEnum {

    GIFT_COUNT1(1,""),

    GIFT_COUNT2(10,""),

    GIFT_COUNT3(50,""),

    GIFT_COUNT_ALL(0,"全部");

    //GIFT_COUNT5(1888,"触发连击+本频道横幅");

    //GIFT_COUNT6(0,"自定义");

    private int value;

    public int getValue() {
        return value;
    }
    private String message;

    public String getMessage(){
        return message;
    }

    GiftCountEnum(int value,String message){
        this.value = value;
        this.message = message;
    }
}
