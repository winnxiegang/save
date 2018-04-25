package com.android.xjq.bean.live;

/**
 * Created by zhouyi on 2017/4/11.
 */

public class GiftBean {

    private int id;

    private String giftName;

    private int allDoubleHitCount;

    private int currentShowCount;

    private long addTime;

    private boolean showing = false;


    @Override
    public boolean equals(Object obj) {
        GiftBean bean = (GiftBean) obj;
        if (bean.getId() == id && bean.allDoubleHitCount > allDoubleHitCount) {
            return true;
        } else {
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getAllDoubleHitCount() {
        return allDoubleHitCount;
    }

    public void setAllDoubleHitCount(int allDoubleHitCount) {
        this.allDoubleHitCount = allDoubleHitCount;
    }

    public int getCurrentShowCount() {
        return currentShowCount;
    }

    public void setCurrentShowCount(int currentShowCount) {
        this.currentShowCount = currentShowCount;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }
}
