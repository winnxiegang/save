package com.android.xjq.bean.order;

import com.android.banana.commlib.bean.NormalObject;
import com.android.residemenu.lt_lib.model.lt.LotterySubType;
import com.android.residemenu.lt_lib.model.lt.LotteryType;

/**
 * Created by zhouyi on 2017/1/12 17:03.
 */

public class PurchaseSnapshotBean {

    private int totalPaidFee;

    private boolean copyable;

    private String purchaseNo;

    private LotteryType lotteryType;

    private LotterySubType lotterySubType;

    private String userId;

    private String userName;

    private double prizeFee;

    private int copyCountNum;

    private int multiple;

    private int likeCount;

    private int replyCount;

    private boolean liked;

    private boolean prized;

    private int totalFee;

    private String appPurchaseNo;

    private boolean copy;

    private String title;

    private NormalObject platformCreationType;

    private int entryAmount;

    private NormalObject purchaseType;

    private String gmtCreate;

    private int countCopy;

    private double copyCommissionFee;

    private NormalObject prizeStatus;


    private boolean commentOff;

    public void setCopyCommissionFee(double copyCommissionFee) {
        this.copyCommissionFee = copyCommissionFee;
    }

    public double getCopyCommissionFee() {
        return this.copyCommissionFee;
    }

    public int getCountCopy() {
        return countCopy;
    }

    public void setCountCopy(int countCopy) {
        this.countCopy = countCopy;
    }

    public NormalObject getPrizeStatus() {
        return prizeStatus;
    }

    public void setPrizeStatus(NormalObject prizeStatus) {
        this.prizeStatus = prizeStatus;
    }

    public int getProfitCopyCount() {
        return profitCopyCount;
    }

    public void setProfitCopyCount(int profitCopyCount) {
        this.profitCopyCount = profitCopyCount;
    }

    private int profitCopyCount;

    public void setTotalPaidFee(int totalPaidFee) {
        this.totalPaidFee = totalPaidFee;
    }

    public boolean isCopyable() {
        return copyable;
    }

    public void setCopyable(boolean copyable) {
        this.copyable = copyable;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public LotteryType getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(LotteryType lotteryType) {
        this.lotteryType = lotteryType;
    }

    public LotterySubType getLotterySubType() {
        return lotterySubType;
    }

    public void setLotterySubType(LotterySubType lotterySubType) {
        this.lotterySubType = lotterySubType;
    }

    public int getTotalPaidFee() {
        return totalPaidFee;
    }

    public double getPrizeFee() {
        return prizeFee;
    }

    public void setPrizeFee(double prizeFee) {
        this.prizeFee = prizeFee;
    }

    public void setPrizeFee(int prizeFee) {
        this.prizeFee = prizeFee;
    }

    public int getCopyCountNum() {
        return copyCountNum;
    }

    public void setCopyCountNum(int copyCountNum) {
        this.copyCountNum = copyCountNum;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isPrized() {
        return prized;
    }

    public void setPrized(boolean prized) {
        this.prized = prized;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getAppPurchaseNo() {
        return appPurchaseNo;
    }

    public void setAppPurchaseNo(String appPurchaseNo) {
        this.appPurchaseNo = appPurchaseNo;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NormalObject getPlatformCreationType() {
        return platformCreationType;
    }

    public void setPlatformCreationType(NormalObject platformCreationType) {
        this.platformCreationType = platformCreationType;
    }

    public int getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(int entryAmount) {
        this.entryAmount = entryAmount;
    }

    public NormalObject getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(NormalObject purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isCommentOff() {
        return commentOff;
    }

    public void setCommentOff(boolean commentOff) {
        this.commentOff = commentOff;
    }
}
