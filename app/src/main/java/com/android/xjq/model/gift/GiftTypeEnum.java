package com.android.xjq.model.gift;

/**
 * Created by lingjiu on 2017/3/18.
 */

public enum GiftTypeEnum {

    WORLD_CUP("大力神杯", 50000),

    MVP("MVP", 2000),

    BUS("摆大巴", 220),

    LIKE("点赞", 300),

    FINAL_HIT("绝杀", 100),

    THANK_BOSS("谢谢老板", 300),

    SHIT("大便", 220),

    GOAL("得分", 300),

    STEADY_LIKE_DOG("稳如狗",500);


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private String message;

    private int value;


    GiftTypeEnum(String message, int value) {
        this.message = message;

        this.value = value;
    }
}
