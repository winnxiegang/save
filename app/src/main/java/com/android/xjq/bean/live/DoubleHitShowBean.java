package com.android.xjq.bean.live;

import com.android.xjq.view.LandscapeGiftShow;

/**
 * Created by zhouyi on 2017/4/13.
 */

public class DoubleHitShowBean {

    //礼物推送数据
    private LiveCommentBean liveCommentBean;

    //群组Id
    private String id;

    //礼物名
    private String giftName;

    //送出的礼物数量
    private int totalGiftCount;

    //总的连击数
    private int allDoubleHitCount;

    //当前已显示的连击数
    private int currentShowCount = 1;

    //增加到池里时间(主要作用是负责30s清除一次过期的数据)
    private long addTime;

    //当前是否在屏幕上显示
    private boolean showing = false;

    //背景图片
    private String imageUrl;

    //当前需要显示的最大连击数
    private int currentShowAllDoubleHit;
    //连击消失后,下一次开始显示的起点
    private int nextShowCount;

    public int getNextShowCount() {
        return nextShowCount;
    }

    public void setNextShowCount(int nextShowCount) {
        this.nextShowCount = nextShowCount;
    }

    private boolean imageFromAssert = true;

    private String giftNormalColor;

    private String giftCountColor;

    public int getCurrentShowAllDoubleHit() {
        return currentShowAllDoubleHit;
    }

    public void setCurrentShowAllDoubleHit(int currentShowAllDoubleHit) {
        this.currentShowAllDoubleHit = currentShowAllDoubleHit;
    }

    public String getGiftNormalColor() {
        return giftNormalColor;
    }

    public void setGiftNormalColor(String giftNormalColor) {
        this.giftNormalColor = giftNormalColor;
    }

    public String getGiftCountColor() {
        return giftCountColor;
    }

    public void setGiftCountColor(String giftCountColor) {
        this.giftCountColor = giftCountColor;
    }

    private LandscapeGiftShow.DoubleHitShowStatusEnum showStatusEnum = LandscapeGiftShow.DoubleHitShowStatusEnum.WAIT;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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


    public LiveCommentBean getLiveCommentBean() {
        return liveCommentBean;
    }

    public void setLiveCommentBean(LiveCommentBean liveCommentBean) {
        this.liveCommentBean = liveCommentBean;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTotalGiftCount() {
        return totalGiftCount;
    }

    public void setTotalGiftCount(int totalGiftCount) {
        this.totalGiftCount = totalGiftCount;
    }

    public LandscapeGiftShow.DoubleHitShowStatusEnum getShowStatusEnum() {
        return showStatusEnum;
    }

    public void setShowStatusEnum(LandscapeGiftShow.DoubleHitShowStatusEnum showStatusEnum) {
        this.showStatusEnum = showStatusEnum;
    }

    public boolean isImageFromAssert() {
        return imageFromAssert;
    }

    public void setImageFromAssert(boolean imageFromAssert) {
        this.imageFromAssert = imageFromAssert;
    }
}
