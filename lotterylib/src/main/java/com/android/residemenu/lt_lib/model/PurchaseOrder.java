package com.android.residemenu.lt_lib.model;

import com.android.banana.commlib.bean.NormalObject;
import com.android.residemenu.lt_lib.model.lt.CreateType;
import com.android.residemenu.lt_lib.model.lt.KeepSecretType;
import com.android.residemenu.lt_lib.model.lt.LotterySubType;
import com.android.residemenu.lt_lib.model.lt.LotteryType;
import com.android.residemenu.lt_lib.model.lt.PlaySubType;
import com.android.residemenu.lt_lib.model.lt.PlayType;

import java.math.BigDecimal;

/**
 * 购彩记录数据
 *
 * @author leslie
 * @version $Id: PurchaseOrder.java, v 0.1 2014年4月21日 下午5:25:36 leslie Exp $
 */
public class PurchaseOrder {


    /**
     * 佣金比例
     */
    private double copyCommissionRate;
    /**
     * 订购号
     */
    private String purchaseNo;

    private String appPurchaseNo;

    /**
     * 订购发起人别名
     */
    private String createrAlias;

    /**
     * 订单所有人userId
     */
    private String ownerUserId;

    /**
     * 方案总金额
     */
    private int totalFee;
    /**
     * 是否可复制
     */
    private boolean copyable;

    /**
     * 合买方案发起人支付金额
     */
    private int createrPaidFee;
    /**
     * 我支付的金额
     */
    private int paidFee = -1;
    /**
     * 已购买进度
     */
    private int totalPaidFee;

    /**
     * 订单奖金
     */
    private BigDecimal prizeFee;
    /**
     * 我的奖金
     */
    private BigDecimal myPrizeFee;
    /**
     * 方案总中奖金额
     */
    private BigDecimal totalPrizeFee;

    /**
     * 是否是合买
     */
    private boolean combineable;

    /**
     * 订单标题
     */
    private String title;

    /**
     * 倍数
     */
    protected int multiple;

    /**
     * 订单是否需要该用户支付 false 不用； true 需要
     */
    private boolean pay;

    private BigDecimal singleMaxTestPrize;

    private BigDecimal singleMinTestPrize;

    private String userName;

    /**
     * 过关方式
     */
    private String mixTypes;

    /**
     * 单倍复制金额
     */
    private int singleTotalFee;

    private LotteryType lotteryType;

    private LotterySubType lotterySubType;

    private String issueNo;

    private String gmtCreate;

    private int resId;

    private PurchaseType purchaseType;



    private PlayType playType;



    private PlaySubType playSubType;

    /**
     * 出票状态
     */
    private String statusMsg;

    /**
     * 总注数
     */
    private int totalAmount;

    /**
     * 中奖注数
     */
    private int prizeAmount;
    /**
     * 是否开奖
     */
    private boolean prized;
    /**
     * 开奖情况
     */
    private String prizeStatusMessage;

    private String gmtStopSell;

    private KeepSecretType keepSecretType;

    /**
     * 复制来源订单
     */
    private String sourceId;

    /**
     * 方案来源
     */
    private String brunchSource;

    private CreateType createType;

    private boolean show;

    /**
     * 复制数
     */
    private String copyNum;

    private String subjectTitle;

    private String subjectContent;

    private String collectId;

    private boolean selected;

    //是否是可复制方案
    private boolean copy;

    private NormalObject platformCreationType;

    private int likeCount;

    private int replyCount;

    private String shareUrl;

    private boolean liked;

    private int entryAmount;

    /**
     * 发红包是否显示复制限制按钮
     */
    private boolean showCopyProject;

    /**
     *方案宣言实体类对象
     */
    private PurchaseTitleEntity purchaseTitle;

    private NormalObject copyPurchaseType;

    public PurchaseOrder() {

    }

    public boolean isShowCopyProject() {
        return showCopyProject;
    }

    public void setShowCopyProject(boolean showCopyProject) {
        this.showCopyProject = showCopyProject;
    }

    public PurchaseTitleEntity getPurchaseTitle() {
        return purchaseTitle;
    }

    public void setPurchaseTitle(PurchaseTitleEntity purchaseTitle) {
        this.purchaseTitle = purchaseTitle;
    }

    public double getCopyCommissionRate() {
        return copyCommissionRate;
    }

    public void setCopyCommissionRate(double copyCommissionRate) {

        this.copyCommissionRate = copyCommissionRate;

    }

    public void setBrunchSource(String brunchSource) {
        this.brunchSource = brunchSource;
    }

    public String getBrunchSource() {
        return brunchSource;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getTotalPaidFee() {
        return totalPaidFee;
    }

    public void setTotalPaidFee(int totalPaidFee) {
        this.totalPaidFee = totalPaidFee;
    }

    public boolean isPay() {
        return pay;
    }

    public void setPay(boolean pay) {
        this.pay = pay;
    }

    public BigDecimal getSingleMaxTestPrize() {
        return singleMaxTestPrize;
    }

    public void setSingleMaxTestPrize(BigDecimal singleMaxTestPrize) {
        this.singleMaxTestPrize = singleMaxTestPrize;
    }

    public BigDecimal getSingleMinTestPrize() {
        return singleMinTestPrize;
    }

    public void setSingleMinTestPrize(BigDecimal singleMinTestPrize) {
        this.singleMinTestPrize = singleMinTestPrize;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMixTypes() {
        return mixTypes;
    }

    public void setMixTypes(String mixTypes) {
        this.mixTypes = mixTypes;
    }

    public int getSingleTotalFee() {
        return singleTotalFee;
    }

    public void setSingleTotalFee(int singleTotalFee) {
        this.singleTotalFee = singleTotalFee;
    }

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public int getPaidFee() {
        return paidFee;
    }

    public void setPaidFee(int paidFee) {
        this.paidFee = paidFee;
    }

    public BigDecimal getPrizeFee() {
        return prizeFee;
    }

    public void setPrizeFee(BigDecimal prizeFee) {
        this.prizeFee = prizeFee;
    }

    public BigDecimal getTotalPrizeFee() {
        return totalPrizeFee;
    }

    public void setTotalPrizeFee(BigDecimal totalPrizeFee) {
        this.totalPrizeFee = totalPrizeFee;
    }

    public String getCreaterAlias() {
        return createrAlias;
    }

    public void setCreaterAlias(String createrAlias) {
        this.createrAlias = createrAlias;
    }

    public int getCreaterPaidFee() {
        return createrPaidFee;
    }

    public void setCreaterPaidFee(int createrPaidFee) {
        this.createrPaidFee = createrPaidFee;
    }

    public boolean isCombineable() {
        return combineable;
    }

    public void setCombineable(boolean combineable) {
        this.combineable = combineable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(int prizeAmount) {
        this.prizeAmount = prizeAmount;
    }

    public boolean isPrized() {
        return prized;
    }

    public void setPrized(boolean prized) {
        this.prized = prized;
    }

    public String getPrizeStatusMessage() {
        return prizeStatusMessage;
    }

    public void setPrizeStatusMessage(String prizeStatusMessage) {
        this.prizeStatusMessage = prizeStatusMessage;
    }

    public boolean isCopyable() {
        return copyable;
    }

    public void setCopyable(boolean copyable) {
        this.copyable = copyable;
    }

    public String getGmtStopSell() {
        return gmtStopSell;
    }

    public void setGmtStopSell(String gmtStopSell) {
        this.gmtStopSell = gmtStopSell;
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

    public KeepSecretType getKeepSecretType() {
        return keepSecretType;
    }

    public void setKeepSecretType(KeepSecretType keepSecretType) {
        this.keepSecretType = keepSecretType;
    }

    public PlayType getPlayType() {
        return playType;
    }

    public void setPlayType(PlayType playType) {
        this.playType = playType;
    }

    public PlaySubType getPlaySubType(){
        return  playSubType;
    }

    public void setPlaySubType(PlaySubType playSubType){
        this.playSubType = playSubType;
    }


    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public CreateType getCreateType() {
        return createType;
    }

    public void setCreateType(CreateType createType) {
        this.createType = createType;
    }

    public BigDecimal getMyPrizeFee() {
        return myPrizeFee;
    }

    public void setMyPrizeFee(BigDecimal myPrizeFee) {
        this.myPrizeFee = myPrizeFee;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getCopyNum() {
        return copyNum;
    }

    public void setCopyNum(String copyNum) {
        this.copyNum = copyNum;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectContent() {
        return subjectContent;
    }

    public void setSubjectContent(String subjectContent) {
        this.subjectContent = subjectContent;
    }

    public String getCollectId() {
        return collectId;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public NormalObject getPlatformCreationType() {
        return platformCreationType;
    }

    public void setPlatformCreationType(NormalObject platformCreationType) {
        this.platformCreationType = platformCreationType;
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

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(int entryAmount) {
        this.entryAmount = entryAmount;
    }

    public void setPurchaseType(PurchaseType purchaseType) {
        this.purchaseType = purchaseType;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }
}
