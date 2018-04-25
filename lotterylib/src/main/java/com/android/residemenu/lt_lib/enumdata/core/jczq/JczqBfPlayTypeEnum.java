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
public enum JczqBfPlayTypeEnum implements PlayType, EnumBase, LotteryMixType {

	M_1_1(1, 1, "单场", null),

	M_2_1(2, 1, "2串1", null),

	M_3_1(3, 1, "3串1", null),

	M_3_3(3, 3, "3串3", new JczqBfPlayTypeEnum[] { M_2_1 }),

	M_3_4(3, 4, "3串4", new JczqBfPlayTypeEnum[] { M_2_1, M_3_1 }),

	M_4_1(4, 1, "4串1", null),

	M_4_4(4, 4, "4串4", new JczqBfPlayTypeEnum[] { M_3_1 }),

	M_4_5(4, 5, "4串5", new JczqBfPlayTypeEnum[] { M_3_1, M_4_1 }),

	M_4_6(4, 6, "4串6", new JczqBfPlayTypeEnum[] { M_2_1 }),

	M_4_11(4, 11, "4串11", new JczqBfPlayTypeEnum[] { M_2_1, M_3_1, M_4_1 }),

	;

	private int value1;

	private int value2;

	private String message;;

	/**
	 * 包含的子项
	 */
	private JczqBfPlayTypeEnum[] subTypes;

	/**
	 * 返回包含的子项
	 * 
	 * @return
	 */
	public JczqBfPlayTypeEnum[] subTypes() {
		return subTypes;
	}

	/**
	 * 返回所有的X串1
	 * 
	 * @return
	 */
	public JczqBfPlayTypeEnum[] allM_X_1() {
		// 如果value2为1，则表示自身就是M_X_1格式的
		if (this.value2 == 1)
			return new JczqBfPlayTypeEnum[] { this };

		// 拷贝返回
		JczqBfPlayTypeEnum[] returnAry = (JczqBfPlayTypeEnum[]) ArrayUtils.clone(this.subTypes);
		return returnAry;
	}

	public static final JczqBfPlayTypeEnum safeValueOf(String name) {
		try {
			return JczqBfPlayTypeEnum.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param value1
	 * @param value2
	 * @param message
	 */
	private JczqBfPlayTypeEnum(int value1, int value2, String message, JczqBfPlayTypeEnum[] subTypes) {
		this.value1 = value1;
		this.value2 = value2;
		this.message = message;
		this.subTypes = subTypes;
	}

	public static JczqBfPlayTypeEnum valueOf(int value1, int value2) {
		for (JczqBfPlayTypeEnum enumObj : JczqBfPlayTypeEnum.values()) {
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
