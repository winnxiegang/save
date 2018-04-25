package com.android.xjq.model;

import com.android.xjq.R;

/**
 * Created by lingjiu on 2017/9/13.
 */

public enum BubbleColorEnum {
    YELLOW_HORSE_BUBBLE("#fcee09", R.drawable.icon_yellow_horse_bubble_bg, R.drawable.icon_yellow_horse_bubble, "#000000"),
    LEVEL_ELEVEN_BUBBLE("#7abefc", R.drawable.icon_level_eleven_bubble_bg, R.drawable.icon_level_eleven_bubble, "#000000"),
    LEVEL_SIXTEEN_BUBBLE("#f17b19", R.drawable.icon_level_sixteen_bubble_bg, R.drawable.icon_level_sixteen_bubble, "#9d0f0f"),
    DRAGON_CHAIR_BUBBLE("#990900", R.drawable.icon_dragon_chair_bubble_bg, R.drawable.icon_dragon_chair_bubble, "#ffda8a"),
    NORMAL_BUBBLE("#333333", R.drawable.icon_normal_bubble, R.drawable.icon_normal_bubble, "#000000");

    /*#fcee09: "LV16级可获得",
        #f17b19: "黄马用户可获得",
        #7abefc: "LV11级可获得",
        #990900: "龙椅用户可获得",
        #333333: "无气泡"*/
    String fontColor;
    int resId;
    int templateResId;
    String textColor;

    public int getResId() {
        return resId;
    }

    public String getFontColor() {
        return fontColor;
    }

    public int getTemplateResId() {
        return templateResId;
    }

    public String getTextColor() {
        return textColor;
    }

    BubbleColorEnum(String fontColor, int resId, int templateResId, String textColor) {
        this.fontColor = fontColor;
        this.resId = resId;
        this.templateResId = templateResId;
        this.textColor = textColor;
    }

    public static BubbleColorEnum safeValueOf(String fontColor) {
        //默认是无气泡
        BubbleColorEnum bubbleColorEnum = NORMAL_BUBBLE;
        BubbleColorEnum[] enums = values();
        for (BubbleColorEnum anEnum : enums) {
            if (anEnum.getFontColor().equalsIgnoreCase(fontColor)) {
                bubbleColorEnum = anEnum;
            }
        }
        return bubbleColorEnum;
    }


}
