package com.android.banana.commlib.bean;

/**
 * Created by lingjiu on 2017/7/24.
 */

public class SendCouponConfig {

    /**
     * couponAverageMinAmount : 1000
     * normalGroupMinAmount : 1000
     * isSupportShowTotalCountAndAmount : 1
     * isSupportGroupCouponPay : 1
     * couponMinTotalAmount : 1000
     * normalGroupMaxAmount : 50000000
     * couponTotalCount : 1000
     * couponMaxTotalAmount : 50000000
     */
    //红包过期返回账户时间
    private double expiredHours;
    //红包标题限制
    private int titleLength;
    //定时红包时间限制
    private String sendTimeLimit;
    //红包最大个数(拼手气,普通通用)
    private String couponTotalCount;
    //拼手气红包
    private String couponMaxTotalAmount;
    private String couponAverageMinAmount;
    private String couponMinTotalAmount;
    //普通红包
    private String normalGroupMinAmount;
    private String normalGroupMaxAmount;
    private String isSupportShowTotalCountAndAmount;
    private String isSupportGroupCouponPay;

    public double getExpiredHours() {
        return expiredHours;
    }

    public void setExpiredHours(double expiredHours) {
        this.expiredHours = expiredHours;
    }

    public int getTitleLength() {
        return titleLength;
    }

    public void setTitleLength(int titleLength) {
        this.titleLength = titleLength;
    }

    public String getSendTimeLimit() {
        return sendTimeLimit;
    }

    public void setSendTimeLimit(String sendTimeLimit) {
        this.sendTimeLimit = sendTimeLimit;
    }

    public String getCouponAverageMinAmount() {
        return couponAverageMinAmount;
    }

    public void setCouponAverageMinAmount(String couponAverageMinAmount) {
        this.couponAverageMinAmount = couponAverageMinAmount;
    }

    public String getNormalGroupMinAmount() {
        return normalGroupMinAmount;
    }

    public void setNormalGroupMinAmount(String normalGroupMinAmount) {
        this.normalGroupMinAmount = normalGroupMinAmount;
    }

    public String getIsSupportShowTotalCountAndAmount() {
        return isSupportShowTotalCountAndAmount;
    }

    public void setIsSupportShowTotalCountAndAmount(String isSupportShowTotalCountAndAmount) {
        this.isSupportShowTotalCountAndAmount = isSupportShowTotalCountAndAmount;
    }

    public String getIsSupportGroupCouponPay() {
        return isSupportGroupCouponPay;
    }

    public void setIsSupportGroupCouponPay(String isSupportGroupCouponPay) {
        this.isSupportGroupCouponPay = isSupportGroupCouponPay;
    }

    public String getCouponMinTotalAmount() {
        return couponMinTotalAmount;
    }

    public void setCouponMinTotalAmount(String couponMinTotalAmount) {
        this.couponMinTotalAmount = couponMinTotalAmount;
    }

    public String getNormalGroupMaxAmount() {
        return normalGroupMaxAmount;
    }

    public void setNormalGroupMaxAmount(String normalGroupMaxAmount) {
        this.normalGroupMaxAmount = normalGroupMaxAmount;
    }

    public String getCouponTotalCount() {
        return couponTotalCount;
    }

    public void setCouponTotalCount(String couponTotalCount) {
        this.couponTotalCount = couponTotalCount;
    }

    public String getCouponMaxTotalAmount() {
        return couponMaxTotalAmount;
    }

    public void setCouponMaxTotalAmount(String couponMaxTotalAmount) {
        this.couponMaxTotalAmount = couponMaxTotalAmount;
    }
}
