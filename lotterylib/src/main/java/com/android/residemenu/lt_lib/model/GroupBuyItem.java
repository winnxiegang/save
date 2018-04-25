package com.android.residemenu.lt_lib.model;

/**
 * 
 * 合买大厅
 * 
 * @author leslie
 * 
 * @version $Id: GroupBuyItem.java, v 0.1 2014年4月21日 下午5:56:09 leslie Exp $
 */
public class GroupBuyItem {

	/**
	 * 订单id
	 */
	private String id;
	/**
	 * 彩票类别
	 */
	private String ltType;

	private String lotterySubType;
	/**
	 * 订单总额
	 */
	private String totalFee;
	/**
	 * 剩余金额
	 */
	private String canBuy;

	/**
	 * 单位复制金额
	 */
	private String singleTotalFee;

	/**
	 * 所属人id
	 */
	private String ownerUserId;

	/**
	 * 所属人别名
	 */
	private String ownerAlias;

	private String createrAlias;
	/**
	 * 保底金额
	 */
	private String assureFee;
	/**
	 * 截止时间
	 */
	private String stopSell;

	/**
	 * 进度
	 */
	private String jindu;

	/**
	 * 参与人数
	 */
	private String userCount;

	/**
	 * 是否可复制,自己的方案false
	 */
	private boolean copyable;
	/**
	 * 当前方案是否可合买
	 */
	private boolean combineable;

	private boolean isRecommend;

	private String tag;



	public GroupBuyItem() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLtType() {
		return ltType;
	}

	public void setLtType(String ltType) {
		this.ltType = ltType;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getCanBuy() {
		return canBuy;
	}

	public void setCanBuy(String canBuy) {
		this.canBuy = canBuy;
	}

	public String getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public String getOwnerAlias() {
		return ownerAlias;
	}

	public void setOwnerAlias(String ownerAlias) {
		this.ownerAlias = ownerAlias;
	}

	public String getAssureFee() {
		return assureFee;
	}

	public void setAssureFee(String assureFee) {
		this.assureFee = assureFee;
	}

	public String getStopSell() {
		return stopSell;
	}

	public void setStopSell(String stopSell) {
		this.stopSell = stopSell;
	}

	public String getJindu() {
		return jindu;
	}

	public void setJindu(String jindu) {
		this.jindu = jindu;
	}

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public boolean isCopyable() {
		return copyable;
	}

	public void setCopyable(boolean copyable) {
		this.copyable = copyable;
	}

	public String getCreaterAlias() {
		return createrAlias;
	}

	public void setCreaterAlias(String createrAlias) {
		this.createrAlias = createrAlias;
	}

	public String getSingleTotalFee() {
		return singleTotalFee;
	}

	public void setSingleTotalFee(String singleTotalFee) {
		this.singleTotalFee = singleTotalFee;
	}

	public String getLotterySubType() {
		return lotterySubType;
	}

	public void setLotterySubType(String lotterySubType) {
		this.lotterySubType = lotterySubType;
	}

	public boolean isCombineable() {
		return combineable;
	}

	public void setCombineable(boolean combineable) {
		this.combineable = combineable;
	}

	public boolean isRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(boolean isRecommend) {
		this.isRecommend = isRecommend;
	}

		public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
