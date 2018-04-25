package com.android.residemenu.lt_lib.enumdata;

/**
 * Created by 周毅 on 2015/5/13 14:26.
 */
public enum Jx11x5BuyTypeEnum {

    NORMAL("普通"),

    DANTUO("胆拖");

    String message;

    Jx11x5BuyTypeEnum(String messgae) {
        this.message = messgae;
    }

    public String message(){
        return message;
    }
}
