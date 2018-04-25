package com.android.residemenu.lt_lib.model.user;

/**
 * 我的提款帐号信息
 * 
 * @author leslie
 * 
 * @version $Id: MyBankCard.java, v 0.1 2014年10月20日 下午6:34:10 leslie Exp $
 */
public class MyBankCard {

	private String id;

	private String cardNo;

	/**
	 * 是否是默认帐户
	 */
	private boolean defaultSelect;

	private BankName bankName;

	private int resId;;

	/**
	 * 是否选中这条
	 */
	private boolean selected;

	private double chargeRate;

	private String logoUrl;

	public MyBankCard() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public boolean isDefaultSelect() {
		return defaultSelect;
	}

	public void setDefaultSelect(boolean defaultSelect) {
		this.defaultSelect = defaultSelect;
	}

	public BankName getBankName() {
		return bankName;
	}

	public void setBankName(BankName bankName) {
		this.bankName = bankName;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public double getChargeRate() {
		return chargeRate;
	}

	public void setChargeRate(double chargeRate) {
		this.chargeRate = chargeRate;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
}
