package com.android.residemenu.lt_lib.enumdata.core;


import com.android.residemenu.lt_lib.enumdata.EnumBase;

/**
 * 账务方向
 * 
 * @author chenbug
 * 
 * @version $Id: TransDirectionEnum.java, v 0.1 2010-7-24 下午12:08:33 chenbug Exp
 *          $
 */
public enum TransDirectionEnum implements EnumBase {

	O("支出"),

	I("收入");

	private String message;

	public String message() {
		return message;
	}

	public Number value() {
		return 0;
	}

	private TransDirectionEnum(String message) {
		this.message = message;

	}

}
