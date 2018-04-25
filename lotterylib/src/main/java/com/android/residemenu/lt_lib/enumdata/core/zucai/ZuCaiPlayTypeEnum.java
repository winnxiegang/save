/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core.zucai;


import com.android.residemenu.lt_lib.enumdata.EnumBase;
import com.android.residemenu.lt_lib.enumdata.core.PlayType;

/**
 * 足彩玩法类型
 * 
 * @author chenbug
 * 
 * @version $Id: ZuCaiPlayTypeEnum.java, v 0.1 2012-2-25 下午03:14:56 chenbug Exp
 *          $
 */
public enum ZuCaiPlayTypeEnum implements PlayType, EnumBase {

	SIMPLEX(1, "单式"),

	DUPLEX(3, "复式"), ;

	private int value;

	private String message;

	private ZuCaiPlayTypeEnum(int value, String message) {
		this.value = value;
		this.message = message;
	}

	public static final ZuCaiPlayTypeEnum safeValueOf(int value) {
		for (ZuCaiPlayTypeEnum enumObj : ZuCaiPlayTypeEnum.values()) {
			if (enumObj.value == value)
				return enumObj;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bench.common.enums.EnumBase#message()
	 */
	public String message() {
		// TODO Auto-generated method stub
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bench.common.enums.EnumBase#value()
	 */
	public Number value() {
		// TODO Auto-generated method stub
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bench.app.drawcenter.core.model.PlayType#toThirdValue()
	 */
	public String toThirdValue() {
		// TODO Auto-generated method stub
		return Integer.toString(value);
	}
}
