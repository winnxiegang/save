package com.android.residemenu.lt_lib.model;

import android.os.Parcelable;

import java.util.List;

/**
 * 
 * 
 * @author leslie
 * 
 * @version $Id: AbstractPurchase.java, v 0.1 2014年6月11日 上午11:49:56 leslie Exp $
 */
public abstract class AbstractPurchase<T> implements Parcelable {

	/**
	 * 合买、代购
	 */
	protected String payMode;

	/**
	 * 彩种
	 */
	protected String lotteryType;

	protected List<T> dataList;

    /**
     * 奖金优化选择的数据
     */
    protected List<T> checkedDataList;

	protected int multiple;

	/**
	 * 本人支付
	 */
	protected int payFee;
	/**
	 * 合买最少认购金额
	 */
	protected int leastPayFee;
	/**
	 * 保密方式
	 */
	protected String secretType;
	/**
	 * 保底资金
	 */
	protected int assureFee;

	/**
	 * 最少保底资金
	 */
	protected int leastAssureFee;
	/**
	 * 最大保底金
	 */
	protected int maxAssureFee;
	/**
	 * 合买佣金比例
	 */
	protected String commissionRate;
	/**
	 * 复制佣金比例
	 */
	protected String copyCommissionRate;

	/**
	 * 过关方式
	 */
	protected String mixTypes;

	/**
	 * 玩法
	 */
	protected String playType;

    /**
     * 购买类型(普通购买,奖金优化购买)
     */
    protected  String buyType;

    public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public int getPayFee() {
		return payFee;
	}

	/**
	 * 设置本人支付
	 * 
	 * @param payFee
	 */
	public void setPayFee(int payFee) {
		this.payFee = payFee;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public String getSecretType() {
		return secretType;
	}

	public void setSecretType(String secretType) {
		this.secretType = secretType;
	}

	public int getAssureFee() {
		return assureFee;
	}

	/**
	 * 设置保底
	 * 
	 * @param assureFee
	 */
	public void setAssureFee(int assureFee) {
		this.assureFee = assureFee;
	}

	public String getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(String commissionRate) {
		this.commissionRate = commissionRate;
	}

	public String getCopyCommissionRate() {
		return copyCommissionRate;
	}

	public void setCopyCommissionRate(String copyCommissionRate) {
		this.copyCommissionRate = copyCommissionRate;
	}


	public String getMixTypes() {
		return mixTypes;
	}

	public void setMixTypes(String mixTypes) {
		this.mixTypes = mixTypes;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public int getLeastAssureFee() {
		return leastAssureFee;
	}

	public void setLeastAssureFee(int leastAssureFee) {
		this.leastAssureFee = leastAssureFee;
	}

	public int getLeastPayFee() {
		return leastPayFee;
	}

	public void setLeastPayFee(int leastPayFee) {
		this.leastPayFee = leastPayFee;
	}

	public int getMaxAssureFee() {
		return maxAssureFee;
	}

	public void setMaxAssureFee(int maxAssureFee) {
		this.maxAssureFee = maxAssureFee;
	}

    public String getBuyType() {
        return buyType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public List<T> getCheckedDataList() {
        return checkedDataList;
    }

    public void setCheckedDataList(List<T> checkedDataList) {
        this.checkedDataList = checkedDataList;
    }
}
