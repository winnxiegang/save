/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata;


import com.android.residemenu.lt_lib.utils.lang.ArrayUtils;

/**
 * 串方式
 * 
 * @author chenbug
 * 
 * @version $Id: LotteryMixTypeEnum.java, v 0.1 2011-2-24 下午01:02:02 chenbug Exp
 *          $
 */
public enum LotteryMixTypeEnum implements EnumBase {

    M_1_1(1, 1, "1串1", null),

	M_2_1(2, 1, "2串1", null),

	M_2_3(2, 3, "2串3", new LotteryMixTypeEnum[] { M_1_1, M_2_1 }),

	M_3_1(3, 1, "3串1", null),

	M_3_3(3, 3, "3串3", new LotteryMixTypeEnum[] { M_2_1 }),

	M_3_4(3, 4, "3串4", new LotteryMixTypeEnum[] { M_2_1, M_3_1 }),

	M_3_7(3, 7, "3串7", new LotteryMixTypeEnum[] { M_1_1, M_2_1, M_3_1 }),

	M_4_1(4, 1, "4串1", null),

	M_4_4(4, 4, "4串4", new LotteryMixTypeEnum[] { M_3_1 }),

	M_4_5(4, 5, "4串5", new LotteryMixTypeEnum[] { M_3_1, M_4_1 }),

	M_4_6(4, 6, "4串6", new LotteryMixTypeEnum[] { M_2_1 }),

	M_4_11(4, 11, "4串11", new LotteryMixTypeEnum[] { M_2_1, M_3_1, M_4_1 }),

	M_4_15(4, 15, "4串15", new LotteryMixTypeEnum[] { M_1_1, M_2_1, M_3_1, M_4_1 }),

	M_5_1(5, 1, "5串1", null),

	M_5_5(5, 5, "5串5", new LotteryMixTypeEnum[] { M_4_1 }),

	M_5_6(5, 6, "5串6", new LotteryMixTypeEnum[] { M_4_1, M_5_1 }),

	M_5_10(5, 10, "5串10", new LotteryMixTypeEnum[] { M_2_1 }),

	M_5_16(5, 16, "5串16", new LotteryMixTypeEnum[] { M_3_1, M_4_1, M_5_1 }),

	M_5_20(5, 20, "5串20", new LotteryMixTypeEnum[] { M_2_1, M_3_1 }),

	M_5_26(5, 26, "5串26", new LotteryMixTypeEnum[] { M_2_1, M_3_1, M_4_1, M_5_1 }),

	M_5_31(5, 31, "5串31", new LotteryMixTypeEnum[] { M_1_1, M_2_1, M_3_1, M_4_1, M_5_1 }),

	M_6_1(6, 1, "6串1", null),

	M_6_6(6, 6, "6串6", new LotteryMixTypeEnum[] { M_5_1 }),

	M_6_7(6, 7, "6串7", new LotteryMixTypeEnum[] { M_5_1, M_6_1 }),

	M_6_15(6, 15, "6串15", new LotteryMixTypeEnum[] { M_2_1 }),

	M_6_20(6, 20, "6串20", new LotteryMixTypeEnum[] { M_3_1 }),

	M_6_22(6, 22, "6串22", new LotteryMixTypeEnum[] { M_4_1, M_5_1, M_6_1 }),

	M_6_35(6, 35, "6串35", new LotteryMixTypeEnum[] { M_2_1, M_3_1 }),

	M_6_42(6, 42, "6串42", new LotteryMixTypeEnum[] { M_3_1, M_4_1, M_5_1, M_6_1 }),

	M_6_50(6, 50, "6串50", new LotteryMixTypeEnum[] { M_2_1, M_3_1, M_4_1 }),

	M_6_57(6, 57, "6串57", new LotteryMixTypeEnum[] { M_2_1, M_3_1, M_4_1, M_5_1, M_6_1 }),

	M_6_63(6, 63, "6串63", new LotteryMixTypeEnum[] { M_1_1, M_2_1, M_3_1, M_4_1, M_5_1, M_6_1 }),

	M_7_1(7, 1, "7串1", null),

	M_7_7(7, 7, "7串7", new LotteryMixTypeEnum[] { M_6_1 }),

	M_7_8(7, 8, "7串8", new LotteryMixTypeEnum[] { M_6_1, M_7_1 }),

	M_7_21(7, 21, "7串21", new LotteryMixTypeEnum[] { M_5_1 }),

	M_7_35(7, 35, "7串35", new LotteryMixTypeEnum[] { M_4_1 }),

	M_7_120(7, 120, "7串120", new LotteryMixTypeEnum[] { M_2_1, M_3_1, M_4_1, M_5_1, M_6_1, M_7_1 }),

	M_8_1(8, 1, "8串1", null),

	M_8_8(8, 8, "8串8", new LotteryMixTypeEnum[] { M_7_1 }),

	M_8_9(8, 9, "8串9", new LotteryMixTypeEnum[] { M_7_1, M_8_1 }),

	M_8_28(8, 28, "8串28", new LotteryMixTypeEnum[] { M_6_1 }),

	M_8_56(8, 56, "8串56", new LotteryMixTypeEnum[] { M_5_1 }),

	M_8_70(8, 70, "8串70", new LotteryMixTypeEnum[] { M_4_1 }),

	M_8_247(8, 247, "8串247", new LotteryMixTypeEnum[] { M_2_1, M_3_1, M_4_1, M_5_1, M_6_1, M_7_1, M_8_1 }),

	M_9_1(9, 1, "9串1", null),

	M_10_1(10, 1, "10串1", null),

	M_11_1(11, 1, "11串1", null),

	M_12_1(12, 1, "12串1", null),

	M_13_1(13, 1, "13串1", null),

	M_14_1(14, 1, "14串1", null),

	M_15_1(15, 1, "15串1", null),

	;

	private int value1;

	private int value2;

	private String message;;

	/**
	 * 包含的子项
	 */
	private LotteryMixTypeEnum[] subTypes;

	/**
	 * 返回包含的子项
	 * 
	 * @return
	 */
	public LotteryMixTypeEnum[] subTypes() {
		return subTypes;
	}

	/**
	 * 返回所有的X串1
	 * 
	 * @return
	 */
	public LotteryMixTypeEnum[] allM_X_1() {
		// 如果value2为1，则表示自身就是M_X_1格式的
		if (this.value2 == 1)
			return new LotteryMixTypeEnum[] { this };

		// 拷贝返回
		LotteryMixTypeEnum[] returnAry = (LotteryMixTypeEnum[]) ArrayUtils.clone(this.subTypes);
		return returnAry;
	}

	/**
	 * 是否有子串
	 * 
	 * @return
	 */
	public boolean isSubTypes() {
		return allM_X_1().length == 1 && allM_X_1()[0] == this;
	}

	/**
	 * @param value1
	 * @param value2
	 * @param message
	 */
	private LotteryMixTypeEnum(int value1, int value2, String message, LotteryMixTypeEnum[] subTypes) {
		this.value1 = value1;
		this.value2 = value2;
		this.message = message;
		this.subTypes = subTypes;
	}

	public static LotteryMixTypeEnum valueOf(int value1, int value2) {
		for (LotteryMixTypeEnum enumObj : LotteryMixTypeEnum.values()) {
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

}
