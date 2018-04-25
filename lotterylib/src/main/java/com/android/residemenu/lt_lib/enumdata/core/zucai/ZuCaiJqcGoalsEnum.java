/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core.zucai;


import com.android.residemenu.lt_lib.enumdata.EnumBase;
import com.android.residemenu.lt_lib.utils.lang.StringUtils;

/**
 * 足彩进球彩进球数枚举
 * 
 * @author chenbug
 * 
 * @version $Id: ZuCaiJqcGoalsEnum.java, v 0.1 2011-6-13 上午10:29:28 chenbug Exp
 *          $
 */
public enum ZuCaiJqcGoalsEnum implements EnumBase {

	G0("0", 0),

	G1("1", 1),

	G2("2", 2),

	G3M("3+", 3);
    private String message;
	private int value;

	private ZuCaiJqcGoalsEnum(String message, int value) {
		this.message = message;
		this.value = value;
	}

	public static ZuCaiJqcGoalsEnum valueOf(int value) {
		for (ZuCaiJqcGoalsEnum enumObj : ZuCaiJqcGoalsEnum.values()) {
			if (enumObj.value == value)
				return enumObj;
		}
		 
		return null;
	}

	public static ZuCaiJqcGoalsEnum valueOfMessage(String message) {
		for (ZuCaiJqcGoalsEnum enumObj : ZuCaiJqcGoalsEnum.values()) {
			if (StringUtils.equals(enumObj.message, message))
				return enumObj;
		}
	 
		return null;
	}

	public String message() {
		// TODO Auto-generated method stub
		return message;
	}

	public Number value() {
		// TODO Auto-generated method stub
		return value;
	}

}
