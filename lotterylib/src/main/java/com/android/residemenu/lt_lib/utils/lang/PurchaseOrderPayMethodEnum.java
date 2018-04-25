/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.utils.lang;


import com.android.residemenu.lt_lib.enumdata.EnumBase;

/**
 * 订单支付方式
 * 
 * @author chenbug
 * 
 * @version $Id: PurchaseOrderPayMethodEnum.java, v 0.1 2014-6-9 下午12:00:11
 *          chenbug Exp $
 */
public enum PurchaseOrderPayMethodEnum implements EnumBase {

	// 充值转支付
	DEPOSITE_PAY("充值转支付"),
	// 支付
	PAY("支付");

	private String message;

	private PurchaseOrderPayMethodEnum(String message) {

		this.message = message;
	}

	public String message() {

		return this.message;
	}

	public Number value() {

		return null;
	}

}
