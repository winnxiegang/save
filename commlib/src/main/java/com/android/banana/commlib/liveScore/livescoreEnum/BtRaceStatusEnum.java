package com.android.banana.commlib.liveScore.livescoreEnum;

public enum BtRaceStatusEnum {

    POSTPONE("推迟"),
    //
    DELAY("延期"),
    //
    PAUSE("中场"),
    //
    OVERTIME("加时"),
    //
    PLAY_S("下半场"),
    //
    PLAY_F("上半场"),

    PLAY_1("第1节"),

    PLAY_2("第2节"),

    PLAY_3("第3节"),

    PLAY_4("第4节"),

    PLAY_OT_1("1'OT"),

    PLAY_OT_2("2'OT"),

    PLAY_OT_3("3'OT"),

    PLAY_OT_4("4'OT"),
    //
    WAIT("等待开始"),
    //
    FINISH("完场"),

    //
    CUT("腰斩"),

    //
    BREAK_OFF("中断"),

    //
    DEC("待定"),

    //
    CANCEL("取消"),

    //
    UNKNOWN("未知");

    String message;

    BtRaceStatusEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static BtRaceStatusEnum safeValueOf(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
        }
        return UNKNOWN;
    }


}
