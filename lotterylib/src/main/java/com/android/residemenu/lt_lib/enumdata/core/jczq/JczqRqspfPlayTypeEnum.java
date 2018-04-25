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
public enum JczqRqspfPlayTypeEnum implements PlayType, EnumBase, LotteryMixType {

	M_1_1(1, 1, "单场", null),

	M_2_1(2, 1, "2串1", null),

	M_3_1(3, 1, "3串1", null),

	M_3_3(3, 3, "3串3", new JczqRqspfPlayTypeEnum[] { M_2_1 }),

	M_3_4(3, 4, "3串4", new JczqRqspfPlayTypeEnum[] { M_2_1, M_3_1 }),

	M_4_1(4, 1, "4串1", null),

	M_4_4(4, 4, "4串4", new JczqRqspfPlayTypeEnum[] { M_3_1 }),

	M_4_5(4, 5, "4串5", new JczqRqspfPlayTypeEnum[] { M_3_1, M_4_1 }),

	M_4_6(4, 6, "4串6", new JczqRqspfPlayTypeEnum[] { M_2_1 }),

	M_4_11(4, 11, "4串11", new JczqRqspfPlayTypeEnum[] { M_2_1, M_3_1, M_4_1 }),

	M_5_1(5, 1, "5串1", null),

	M_5_5(5, 5, "5串5", new JczqRqspfPlayTypeEnum[] { M_4_1 }),

	M_5_6(5, 6, "5串6", new JczqRqspfPlayTypeEnum[] { M_4_1, M_5_1 }),

	M_5_10(5, 10, "5串10", new JczqRqspfPlayTypeEnum[] { M_2_1 }),

	M_5_16(5, 16, "5串16", new JczqRqspfPlayTypeEnum[] { M_3_1, M_4_1, M_5_1 }),

	M_5_20(5, 20, "5串20", new JczqRqspfPlayTypeEnum[] { M_2_1, M_3_1 }),

	M_5_26(5, 26, "5串26", new JczqRqspfPlayTypeEnum[] { M_2_1, M_3_1, M_4_1, M_5_1 }),

	M_6_1(6, 1, "6串1", null),

	M_6_6(6, 6, "6串6", new JczqRqspfPlayTypeEnum[] { M_5_1 }),

	M_6_7(6, 7, "6串7", new JczqRqspfPlayTypeEnum[] { M_5_1, M_6_1 }),

	M_6_15(6, 15, "6串15", new JczqRqspfPlayTypeEnum[] { M_2_1 }),

	M_6_20(6, 20, "6串20", new JczqRqspfPlayTypeEnum[] { M_3_1 }),

	M_6_22(6, 22, "6串22", new JczqRqspfPlayTypeEnum[] { M_4_1, M_5_1, M_6_1 }),

	M_6_35(6, 35, "6串35", new JczqRqspfPlayTypeEnum[] { M_2_1, M_3_1 }),

	M_6_42(6, 42, "6串42", new JczqRqspfPlayTypeEnum[] { M_3_1, M_4_1, M_5_1, M_6_1 }),

	M_6_50(6, 50, "6串50", new JczqRqspfPlayTypeEnum[] { M_2_1, M_3_1, M_4_1 }),

	M_6_57(6, 57, "6串57", new JczqRqspfPlayTypeEnum[] { M_2_1, M_3_1, M_4_1, M_5_1, M_6_1 }),

	M_7_1(7, 1, "7串1", null),

	M_7_7(7, 7, "7串7", new JczqRqspfPlayTypeEnum[] { M_6_1 }),

	M_7_8(7, 8, "7串8", new JczqRqspfPlayTypeEnum[] { M_6_1, M_7_1 }),

	M_7_21(7, 21, "7串21", new JczqRqspfPlayTypeEnum[] { M_5_1 }),

	M_7_35(7, 35, "7串35", new JczqRqspfPlayTypeEnum[] { M_4_1 }),

	M_7_120(7, 120, "7串120", new JczqRqspfPlayTypeEnum[] { M_2_1, M_3_1, M_4_1, M_5_1, M_6_1, M_7_1 }),

	M_8_1(8, 1, "8串1", null),

	M_8_8(8, 8, "8串8", new JczqRqspfPlayTypeEnum[] { M_7_1 }),

	M_8_9(8, 9, "8串9", new JczqRqspfPlayTypeEnum[] { M_7_1, M_8_1 }),

	M_8_28(8, 28, "8串28", new JczqRqspfPlayTypeEnum[] { M_6_1 }),

	M_8_56(8, 56, "8串56", new JczqRqspfPlayTypeEnum[] { M_5_1 }),

	M_8_70(8, 70, "8串70", new JczqRqspfPlayTypeEnum[] { M_4_1 }),

	M_8_247(8, 247, "8串247", new JczqRqspfPlayTypeEnum[] { M_2_1, M_3_1, M_4_1, M_5_1, M_6_1, M_7_1, M_8_1 }),

	;

	private int value1;

	private int value2;

	private String message;;

	/**
	 * 包含的子项
	 */
	private JczqRqspfPlayTypeEnum[] subTypes;

	/**
	 * 返回包含的子项
	 * 
	 * @return
	 */
	public JczqRqspfPlayTypeEnum[] subTypes() {
		return subTypes;
	}

	/**
	 * 返回所有的X串1
	 * 
	 * @return
	 */
	public JczqRqspfPlayTypeEnum[] allM_X_1() {
		// 如果value2为1，则表示自身就是M_X_1格式的
		if (this.value2 == 1)
			return new JczqRqspfPlayTypeEnum[] { this };

		// 拷贝返回
		JczqRqspfPlayTypeEnum[] returnAry = (JczqRqspfPlayTypeEnum[]) ArrayUtils.clone(this.subTypes);
		return returnAry;
	}

	public static final JczqRqspfPlayTypeEnum safeValueOf(String name) {
		try {
			return JczqRqspfPlayTypeEnum.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param value1
	 * @param value2
	 * @param message
	 */
	private JczqRqspfPlayTypeEnum(int value1, int value2, String message, JczqRqspfPlayTypeEnum[] subTypes) {
		this.value1 = value1;
		this.value2 = value2;
		this.message = message;
		this.subTypes = subTypes;
	}

	public static JczqRqspfPlayTypeEnum valueOf(int value1, int value2) {
		for (JczqRqspfPlayTypeEnum enumObj : JczqRqspfPlayTypeEnum.values()) {
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
