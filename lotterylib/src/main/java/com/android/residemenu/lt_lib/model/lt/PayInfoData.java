package com.android.residemenu.lt_lib.model.lt;

public class PayInfoData {

	private String tag;
	/**
	 * 需要支付金额
	 */
	private String total;
	/**
	 * 相差金额
	 */
	private String differValue;

	public PayInfoData() {

	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDifferValue() {
		return differValue;
	}

	public void setDifferValue(String differValue) {
		this.differValue = differValue;
	}
	
	
	
}
