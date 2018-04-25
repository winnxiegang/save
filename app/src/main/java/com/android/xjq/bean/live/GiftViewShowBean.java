package com.android.xjq.bean.live;

import java.util.ArrayList;

/**
 * Created by zhouyi on 2017/4/13.
 */

public class GiftViewShowBean {

    private LiveCommentBean liveCommentBean;

    private boolean showing;

    private ArrayList<String> imageUrl;

    private int currentCalculateCount;

    private String textColor;

    private String leftMessage;

    private String giftName;

    private int totalGiftCount = 0;

    //房间号颜色
    private String roomNumberColor;

    //房间号
    private String roomNumber;

    //接收人姓名
    private String receiveName;

    private String sendName;

    //默认设置图片来自assets文件夹下
    private boolean imageFromAsserts=true;

    private String giftNumberColor;

    public String getGiftNumberColor() {
        return giftNumberColor;
    }

    public void setGiftNumberColor(String giftNumberColor) {
        this.giftNumberColor = giftNumberColor;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public LiveCommentBean getLiveCommentBean() {
        return liveCommentBean;
    }

    public void setLiveCommentBean(LiveCommentBean liveCommentBean) {
        this.liveCommentBean = liveCommentBean;
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCurrentCalculateCount() {
        return currentCalculateCount;
    }

    public void setCurrentCalculateCount(int currentCalculateCount) {
        this.currentCalculateCount = currentCalculateCount;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getRoomNumberColor() {
        return roomNumberColor;
    }

    public void setRoomNumberColor(String roomNumberColor) {
        this.roomNumberColor = roomNumberColor;
    }

    public String getLeftMessage() {
        return leftMessage;
    }

    public void setLeftMessage(String leftMessage) {
        this.leftMessage = leftMessage;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getTotalGiftCount() {
        return totalGiftCount;
    }

    public void setTotalGiftCount(int totalGiftCount) {
        this.totalGiftCount = totalGiftCount;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public boolean isImageFromAsserts() {
        return imageFromAsserts;
    }

    public void setImageFromAsserts(boolean imageFromAsserts) {
        this.imageFromAsserts = imageFromAsserts;
    }
}
