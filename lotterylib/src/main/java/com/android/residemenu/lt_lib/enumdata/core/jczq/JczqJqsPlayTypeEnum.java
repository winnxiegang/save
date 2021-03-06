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
 * 竞彩足球玩法类型
 * 
 * @author chenbug
 * 
 * @version $Id: JczqPlayTypeEnum.java, v 0.1 2012-2-25 下午03:14:56 chenbug Exp $
 */
public enum JczqJqsPlayTypeEnum implements PlayType, EnumBase, LotteryMixType {

	M_1_1(1, 1, "单场", null),

	M_2_1(2, 1, "2串1", null),

	M_3_1(3, 1, "3串1", null),

	M_3_3(3, 3, "3串3", new JczqJqsPlayTypeEnum[] { M_2_1 }),

	M_3_4(3, 4, "3串4", new JczqJqsPlayTypeEnum[] { M_2_1, M_3_1 }),

	M_4_1(4, 1, "4串1", null),

	M_4_4(4, 4, "4串4", new JczqJqsPlayTypeEnum[] { M_3_1 }),

	M_4_5(4, 5, "4串5", new JczqJqsPlayTypeEnum[] { M_3_1, M_4_1 }),

	M_4_6(4, 6, "4串6", new JczqJqsPlayTypeEnum[] { M_2_1 }),

	M_4_11(4, 11, "4串11", new JczqJqsPlayTypeEnum[] { M_2_1, M_3_1, M_4_1 }),

	M_5_1(5, 1, "5串1", null),

	M_5_5(5, 5, "5串5", new JczqJqsPlayTypeEnum[] { M_4_1 }),

	M_5_6(5, 6, "5串6", new JczqJqsPlayTypeEnum[] { M_4_1, M_5_1 }),

	M_5_10(5, 10, "5串10", new JczqJqsPlayTypeEnum[] { M_2_1 }),

	M_5_16(5, 16, "5串16", new JczqJqsPlayTypeEnum[] { M_3_1, M_4_1, M_5_1 }),

	M_5_20(5, 20, "5串20", new JczqJqsPlayTypeEnum[] { M_2_1, M_3_1 }),

	M_5_26(5, 26, "5串26", new JczqJqsPlayTypeEnum[] { M_2_1, M_3_1, M_4_1, M_5_1 }),

	M_6_1(6, 1, "6串1", null),

	M_6_6(6, 6, "6串6", new JczqJqsPlayTypeEnum[] { M_5_1 }),

	M_6_7(6, 7, "6串7", new JczqJqsPlayTypeEnum[] { M_5_1, M_6_1 }),

	M_6_15(6, 15, "6串15", new JczqJqsPlayTypeEnum[] { M_2_1 }),

	M_6_20(6, 20, "6串20", new JczqJqsPlayTypeEnum[] { M_3_1 }),

	M_6_22(6, 22, "6串22", new JczqJqsPlayTypeEnum[] { M_4_1, M_5_1, M_6_1 }),

	M_6_35(6, 35, "6串35", new JczqJqsPlayTypeEnum[] { M_2_1, M_3_1 }),

	M_6_42(6, 42, "6串42", new JczqJqsPlayTypeEnum[] { M_3_1, M_4_1, M_5_1, M_6_1 }),

	M_6_50(6, 50, "6串50", new JczqJqsPlayTypeEnum[] { M_2_1, M_3_1, M_4_1 }),

	M_6_57(6, 57, "6串57", new JczqJqsPlayTypeEnum[] { M_2_1, M_3_1, M_4_1, M_5_1, M_6_1 }),

	;

	private int value1;

	private int value2;

	private String message;;

	/**
	 * 包含的子项
	 */
	private JczqJqsPlayTypeEnum[] subTypes;

	/**
	 * 返回包含的子项
	 * 
	 * @return
	 */
	public JczqJqsPlayTypeEnum[] subTypes() {
		return subTypes;
	}

	/**
	 * 返回所有的X串1
	 * 
	 * @return
	 */
	public JczqJqsPlayTypeEnum[] allM_X_1() {
		// 如果value2为1，则表示自身就是M_X_1格式的
		if (this.value2 == 1)
			return new JczqJqsPlayTypeEnum[] { this };

		// 拷贝返回
		JczqJqsPlayTypeEnum[] returnAry = (JczqJqsPlayTypeEnum[]) ArrayUtils.clone(this.subTypes);
		return returnAry;
	}

	public static final JczqJqsPlayTypeEnum safeValueOf(String name) {
		try {
			return JczqJqsPlayTypeEnum.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param value1
	 * @param value2
	 * @param message
	 */
	private JczqJqsPlayTypeEnum(int value1, int value2, String message, JczqJqsPlayTypeEnum[] subTypes) {
		this.value1 = value1;
		this.value2 = value2;
		this.message = message;
		this.subTypes = subTypes;
	}

	public static JczqJqsPlayTypeEnum valueOf(int value1, int value2) {
		for (JczqJqsPlayTypeEnum enumObj : JczqJqsPlayTypeEnum.values()) {
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
