package com.android.residemenu.lt_lib.model.user;


import com.android.residemenu.lt_lib.enumdata.SdkTypeEnum;

/**
 * 
 * 通道
 * 
 * @author leslie
 * 
 * @version $Id: FundChannel.java, v 0.1 2014年6月27日 下午3:00:49 leslie Exp $
 */
public class FundChannel {

	private String id;

    private double chargeRate;

    private double maxChargeFee;

	private String describe;

	/**
	 * 工商银行
	 */
	private String name;

	private int orderNumber;

	private String image;

	private String sortType;
	/**
	 * ICBC
	 */
	private String bankCode;

	/**
	 * 是否选中这条
	 */
	private boolean selected;

	private int resId;
	
	/**
	 * wap 或sso充值
	 */
	private SdkTypeEnum typeEnum;

	private String logoUrl;

	/**
	 * 0:借记卡
	 * 1：信用卡
	 */
	private String umPayValue;

	public FundChannel() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}
	
	public SdkTypeEnum getTypeEnum() {
		return typeEnum;
	}

	public void setTypeEnum(SdkTypeEnum typeEnum) {
		this.typeEnum = typeEnum;
	}

    public double getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(double chargeRate) {
        this.chargeRate = chargeRate;
    }

    public double getMaxChargeFee() {
        return maxChargeFee;
    }

    public void setMaxChargeFee(double maxChargeFee) {
        this.maxChargeFee = maxChargeFee;
    }

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getUmPayValue() {
		return umPayValue;
	}

	public void setUmPayValue(String umPayValue) {
		this.umPayValue = umPayValue;
	}
}
