package com.android.residemenu.lt_lib.enumdata.core.zucai;


import com.android.residemenu.lt_lib.enumdata.EnumBase;

/**
 * 足彩比赛赛果枚举
 * 
 * @author chenbug
 * 
 * @version $Id: ZuCaiRaceResultEnum.java, v 0.1 2012-3-27 下午1:51:35 chenbug Exp
 *          $
 */
public enum ZuCaiRaceResultEnum implements EnumBase {

	WIN("胜", 3),

	DRAW("平", 1),

	LOST("负", 0);

	private String message;

	private int value;

	private ZuCaiRaceResultEnum(String message, int value) {
		this.message = message;
		this.value = value;
	}

	public static ZuCaiRaceResultEnum valueOf(int value) {
		for (ZuCaiRaceResultEnum resultEnum : ZuCaiRaceResultEnum.values()) {
			if (resultEnum.value == value)
				return resultEnum;
		}
		 
		return null;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static ZuCaiRaceResultEnum safeValueOf(int value) {
		for (ZuCaiRaceResultEnum resultEnum : ZuCaiRaceResultEnum.values()) {
			if (resultEnum.value == value)
				return resultEnum;
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

}
