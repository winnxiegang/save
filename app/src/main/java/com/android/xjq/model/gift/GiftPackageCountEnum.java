package com.android.xjq.model.gift;

/**
 * Created by lingjiu on 2017/3/8.
 */

public enum GiftPackageCountEnum {
    GIFT_PACKAGE1(1,""),

    GIFT_PACKAGE2(2,""),

    GIFT_PACKAGE3(5,""),

    GIFT_PACKAGE4(10,""),

    GIFT_PACKAGE5(20,"");

//    GIFT_PACKAGE6(0,"自定义");

    private int value;

    public int getValue() {
        return value;
    }
    private String message;

    public String getMessage(){
        return message;
    }

    GiftPackageCountEnum(int value,String message){
        this.value = value;
        this.message = message;
    }
}
