package com.android.residemenu.lt_lib.enumdata;

/**
 * 
 * @author chenbug
 * 
 * @version $Id: PayModeEnum.java, v 0.1 2011-2-25 下午08:11:37 chenbug Exp $
 */
public enum PayModeEnum implements EnumBase {

	SAVE(false, "保存方案"),

	/**
	 * 代购
	 */
	SINGLE(true, "全额认购"),

	/**
	 * 合买
	 */
	COMBINE(true, "发起合买");

	private String message;

	/**
	 * 是否订购，即是否需要支付金额
	 */
	private boolean purchase;

	private PayModeEnum(boolean purchase, String message) {
		this.purchase = purchase;
		this.message = message;
	}

	public boolean purchase() {
		return this.purchase;
	}

	public String message() {

		return message;
	}

	public Number value() {

		return null;
	}

}
