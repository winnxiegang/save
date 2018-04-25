package com.android.residemenu.lt_lib.enumdata.core;


import com.android.residemenu.lt_lib.enumdata.EnumBase;

public interface LotteryMixType extends EnumBase {

	/**
	 * 返回value1
	 */
	public int value1();

	/**
	 * 返回value2
	 */
	public int value2();

	/**
	 * 返回所有的M_1
	 * 
	 * @return
	 */
	public LotteryMixType[] allM_X_1();

	/**
	 * 返回字符串名称
	 */
	public String name();
}
