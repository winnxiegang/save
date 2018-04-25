package com.android.xjq.bean.live.main.gift;

import android.text.TextUtils;

import static com.android.xjq.model.gift.GiftCountEnum.GIFT_COUNT_ALL;

/**
 * Created by lingjiu on 2017/3/8.
 */

public class GiftCountInfo {
    private long num;

    private String desc;

    private boolean isSelected;

    public GiftCountInfo() {
    }

    public GiftCountInfo(int num, String desc) {
        this.num = num;
        this.desc = desc;
    }

    public GiftCountInfo reset() {
        this.num = 1;
        this.desc = null;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GiftCountInfo) {
            GiftCountInfo countInfo = (GiftCountInfo) obj;
            return countInfo.num == num ? TextUtils.equals(countInfo.desc, desc) : false;
        }
        return super.equals(obj);
    }


    public boolean isAll() {
        return TextUtils.equals(GIFT_COUNT_ALL.getMessage(), desc);
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
