/**
 *
 */
package com.android.residemenu.lt_lib.enumdata;


/**
 * 队伍事件
 *
 * @author chenbug 2009-10-22 上午10:02:37
 */
public enum FtRaceStatusEnum implements EnumBase {

    //
    POSTPONE("推迟", -13),
    //
    DELAY("延期", -14),
    //
    PAUSE("中场休息", 2),
    //
    OVERTIME("加时", 4),
    //
    PLAY_S("下半场", 3),
    //
    PLAY_F("上半场", 1),
    //
    WAIT("等待开始", 0),
    //
    FINISH("完场", -1),
    //
    CUT("腰斩", -12),
    //
    DEC("待定", -11),
    //
    CANCEL("取消", -10),
    //
    UNKNOWN("未知", -9999);

    private String message;

    private int value;

    private FtRaceStatusEnum(String message, int value) {
        this.message = message;
        this.value = value;
    }

    public static boolean isBeforeFinish(FtRaceStatusEnum status) {
        return status == PAUSE || status == PLAY_S || status == PLAY_F || status == WAIT;
    }

    public static FtRaceStatusEnum saveValueOf(String name) {

        if (name == null) {
            return UNKNOWN;
        }
        for (FtRaceStatusEnum value : FtRaceStatusEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return UNKNOWN;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.bench.common.enums.EnumBase#message()
     */
    public String message() {
        // TODO Auto-generated method stub
        return message;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.bench.common.enums.EnumBase#value()
     */
    public Number value() {
        return value;
    }

}
