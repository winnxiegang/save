/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core.jczq;

import com.android.residemenu.lt_lib.enumdata.EnumBase;
import com.android.residemenu.lt_lib.enumdata.core.LotteryMixType;
import com.android.residemenu.lt_lib.enumdata.core.PlayType;

import org.apache.commons.lang3.ArrayUtils;



/**
 * 竞彩篮球胜分差玩法类型
 * 
 * @author chenbug
 * 
 * @version $Id: JclqSfPlayTypeEnum.java, v 0.1 2012-2-25 下午03:14:56 chenbug Exp
 *          $
 */
public enum JclqSfcPlayTypeEnum implements PlayType, EnumBase, LotteryMixType {

	M_1_1(1, 1, "单场", null),

	M_2_1(2, 1, "2串1", null),

	M_3_1(3, 1, "3串1", null),

	M_3_3(3, 3, "3串3", new JclqSfcPlayTypeEnum[] { M_2_1 }),

	M_3_4(3, 4, "3串4", new JclqSfcPlayTypeEnum[] { M_2_1, M_3_1 }),

	M_4_1(4, 1, "4串1", null),

	M_4_4(4, 4, "4串4", new JclqSfcPlayTypeEnum[] { M_3_1 }),

	M_4_5(4, 5, "4串5", new JclqSfcPlayTypeEnum[] { M_3_1, M_4_1 }),

	M_4_6(4, 6, "4串6", new JclqSfcPlayTypeEnum[] { M_2_1 }),

	M_4_11(4, 11, "4串11", new JclqSfcPlayTypeEnum[] { M_2_1, M_3_1, M_4_1 }), ;

	private int value1;

	private int value2;

	private String message;;

	/**
	 * 包含的子项
	 */
	private JclqSfcPlayTypeEnum[] subTypes;

	/**
	 * 返回包含的子项
	 * 
	 * @return
	 */
	public JclqSfcPlayTypeEnum[] subTypes() {
		return subTypes;
	}

	/**
	 * 返回所有的X串1
	 * 
	 * @return
	 */
	public JclqSfcPlayTypeEnum[] allM_X_1() {
		// 如果value2为1，则表示自身就是M_X_1格式的
		if (this.value2 == 1)
			return new JclqSfcPlayTypeEnum[] { this };

		// 拷贝返回
		JclqSfcPlayTypeEnum[] returnAry = (JclqSfcPlayTypeEnum[]) ArrayUtils.clone(this.subTypes);
		return returnAry;
	}

	public static final JclqSfcPlayTypeEnum safeValueOf(String name) {
		try {
			return JclqSfcPlayTypeEnum.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param value1
	 * @param value2
	 * @param message
	 */
	private JclqSfcPlayTypeEnum(int value1, int value2, String message, JclqSfcPlayTypeEnum[] subTypes) {
		this.value1 = value1;
		this.value2 = value2;
		this.message = message;
		this.subTypes = subTypes;
	}

	public static JclqSfcPlayTypeEnum valueOf(int value1, int value2) {
		for (JclqSfcPlayTypeEnum enumObj : JclqSfcPlayTypeEnum.values()) {
			if (enumObj.value1 == value1 && enumObj.value2 == value2)
				return enumObj;
		}
		return null;
	}

	/**
	 * @return
	 */
	public int value1() {
		return value1;
	}

	/**
	 * @return
	 */
	public int value2() {
		return value2;
	}

	public String message() {
		// TODO Auto-generated method stub
		return message;
	}

	public Number value() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bench.app.drawcenter.core.model.PlayType#toThirdValue()
	 */
	public String toThirdValue() {
		// TODO Auto-generated method stub
		return this.name();
	}
}
