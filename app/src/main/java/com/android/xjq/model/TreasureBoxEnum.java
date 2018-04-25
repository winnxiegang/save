package com.android.xjq.model;

import com.android.xjq.R;

/**
 * Created by qiaomu on 2018/3/20.
 */

public enum TreasureBoxEnum {


    BRONZE_TREASURE_CHEST("青铜宝箱", R.drawable.box_5),

    RUSTY_IRON_TREASURE_CHEST("生锈的铁宝箱", R.drawable.box_2),

    BLACK_IRON_TREASURE_CHEST("黑铁宝箱", R.drawable.box_3),

    SHINY_IRON_TREASURE_CHEST(" 发亮的铁宝箱", R.drawable.box_4),

    WOODY_TREASURE_CHEST("木质宝箱", R.drawable.box_1),

    SILVER_TREASURE_CHEST("白银宝箱", R.drawable.box_6),

    GOLDEN_TREASURE_CHEST("黄金宝箱", R.drawable.box_8),

    PLATINUM_TREASURE_CHEST("铂金宝箱", R.drawable.box_9),

    SUPERMACY_TREASURE_CHEST("至尊宝箱", R.drawable.box_10);

    private String mName;
    private int mIcon;

    TreasureBoxEnum(String name, int icon) {

        mName = name;
        mIcon = icon;
    }

    public String getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }

    public static TreasureBoxEnum safeValueOf(String name) {
        if (name == null) {
            return null;
        }
        for (TreasureBoxEnum value : TreasureBoxEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
