/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core;


import com.android.residemenu.lt_lib.enumdata.EnumBase;

/**
 * 竞彩足球冠亚军玩法类型
 * 
 * @author chenbug
 * 
 * @version $Id: JczqGyjPlayTypeEnum.java, v 0.1 2012-2-25 下午03:14:56 chenbug
 *          Exp $
 */
public enum JczqGyjPlayTypeEnum implements PlayType, EnumBase {

	SIMPLEX(1, "单式"),

	DUPLEX(3, "复式"), ;

	private int value;

	private String message;

	private JczqGyjPlayTypeEnum(int value, String message) {
		this.value = value;
		this.message = message;
	}

	public static final JczqGyjPlayTypeEnum safeValueOf(int value) {
		for (JczqGyjPlayTypeEnum enumObj : JczqGyjPlayTypeEnum.values()) {
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
