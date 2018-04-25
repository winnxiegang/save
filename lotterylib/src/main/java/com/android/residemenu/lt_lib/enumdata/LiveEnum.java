package com.android.residemenu.lt_lib.enumdata;

/**
 * 各种比分直播
 */
public enum LiveEnum {

    JZBF("竞足比分"),
    JLBF("竞篮比分"),
    DCBF("单场比分"),
    ZCBF("足彩比分");
    private String name;

    LiveEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static LiveEnum valueOfName(String name) {
        for (LiveEnum enumObj : LiveEnum.values()) {
            if (enumObj.getName().equals(name))
                return enumObj;
        }
        return null;
    }

}
