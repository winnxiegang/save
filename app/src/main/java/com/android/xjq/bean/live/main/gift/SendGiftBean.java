package com.android.xjq.bean.live.main.gift;

/**
 * Created by lingjiu on 2017/6/19.
 */

public class SendGiftBean {

    private int giftConfigId;
    private long totalCount;
    private String payType;
    private String totalAmount;

    public SendGiftBean() {
    }

    public SendGiftBean(int id, long totalCount, String payType, String totalAmount) {
        giftConfigId = id;
        this.totalCount = totalCount;
        this.payType = payType;
        this.totalAmount = totalAmount;
    }

    public int getGiftConfigId() {
        return giftConfigId;
    }

    public void setGiftConfigId(int giftConfigId) {
        this.giftConfigId = giftConfigId;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
