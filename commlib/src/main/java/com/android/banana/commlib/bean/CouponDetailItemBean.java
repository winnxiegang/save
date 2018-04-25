package com.android.banana.commlib.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by lingjiu on 2017/7/26.
 */
public class CouponDetailItemBean {
    /**
     * amount : 1160.0
     * gmtCreate : 2017-07-26 16:15:24
     * gmtModified : 2017-07-26 16:15:39
     * gmtReceive : 2017-07-26 16:15:39
     * groupCouponId : 8802170726000002893
     * id : a6c754e6-a2d8-4863-bff4-263cf45eeb49
     * loginName : lingjiu
     * receiveStatus : ALLOCATED
     * receiveUserId : 8201703310470031
     * userLogoUrl : http://livemapi.huohongshe.net/userLogo.resource?userId=8201703310470031&timestamp=1490956255000
     */

    private double amount;
    private String gmtCreate;
    private String gmtModified;
    private String gmtReceive;
    private String groupCouponId;
    private String id;
    private String loginName;
    private String receiveStatus;
    private String receiveUserId;
    private String userLogoUrl;
    @Expose
    private boolean isLuckyKing;

    public boolean isLuckyKing() {
        return isLuckyKing;
    }

    public void setLuckyKing(boolean luckyKing) {
        isLuckyKing = luckyKing;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getGmtReceive() {
        return gmtReceive;
    }

    public void setGmtReceive(String gmtReceive) {
        this.gmtReceive = gmtReceive;
    }

    public String getGroupCouponId() {
        return groupCouponId;
    }

    public void setGroupCouponId(String groupCouponId) {
        this.groupCouponId = groupCouponId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getUserLogoUrl() {
        return userLogoUrl;
    }

    public void setUserLogoUrl(String userLogoUrl) {
        this.userLogoUrl = userLogoUrl;
    }
}
