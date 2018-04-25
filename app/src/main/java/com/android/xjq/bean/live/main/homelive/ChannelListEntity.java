package com.android.xjq.bean.live.main.homelive;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by lingjiu on 2017/3/30.
 */
public class ChannelListEntity {
    /**
     * anchorId : 100007
     * anchorName : 主播4
     * anchorPushStream : false
     * channelTitle : 很凶的倩姐
     * gmtStartPushStream : 2017-03-17 18:45:40
     * id : 200003
     * inChannelUsers : 4
     */

    private int anchorId;
    private String anchorName;
    private boolean anchorPushStream;
    private String channelTitle;
    private String channelName;
    private String gmtStartPushStream;
    private int id;
    private int inChannelUsers;
    private String logoUrl;
    @Expose
    private List<String> userHorseList;
    @Expose
    private boolean isHasCoupon;

    public boolean isHasCoupon() {
        return isHasCoupon;
    }

    public void setHasCoupon(boolean hasCoupon) {
        isHasCoupon = hasCoupon;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List<String> getHorseList() {
        return userHorseList;
    }

    public void setHorseList(List<String> horseList) {
        this.userHorseList = horseList;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getAnchorName() {
        return anchorName;
    }

    public void setAnchorName(String anchorName) {
        this.anchorName = anchorName;
    }

    public boolean isAnchorPushStream() {
        return anchorPushStream;
    }

    public void setAnchorPushStream(boolean anchorPushStream) {
        this.anchorPushStream = anchorPushStream;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getGmtStartPushStream() {
        return gmtStartPushStream;
    }

    public void setGmtStartPushStream(String gmtStartPushStream) {
        this.gmtStartPushStream = gmtStartPushStream;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInChannelUsers() {
        return inChannelUsers;
    }

    public void setInChannelUsers(int inChannelUsers) {
        this.inChannelUsers = inChannelUsers;
    }
}
