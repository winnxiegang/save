package com.android.residemenu.lt_lib.enumdata.core.jclq;


import com.android.residemenu.lt_lib.enumdata.EnumBase;

/**
 * 竞猜篮球枚举,注意,这里的message能随意修改
 * 
 * @author chenbug
 * 
 * @version $Id: JclqSfcEnum.java, v 0.1 2011-3-3 下午07:06:21 chenbug Exp $
 */
public enum JclqSfcEnum implements EnumBase {

	G1B5(1, 5, "客胜(1-5)"),

	G6B10(6, 10, "客胜(6-10)"),

	G11B15(11, 15, "客胜(11-15)"),

	G16B20(16, 20, "客胜(16-20)"),

	G21B25(21, 25, "客胜(21-25)"),

	G26(26, Integer.MAX_VALUE, "客胜(26+)"),

	H1B5(1, 5, "主胜(1-5)"),

	H6B10(6, 10, "主胜(6-10)"),

	H11B15(11, 15, "主胜(11-15)"),

	H16B20(16, 20, "主胜(16-20)"),

	H21B25(21, 25, "主胜(21-25)"),

	H26(26, Integer.MAX_VALUE, "主胜(26+)");

	private int min;

	private int max;

	private String message;

	private JclqSfcEnum(int min, int max, String message) {
		this.min = min;
		this.max = max;
		this.message = message;
	}

	public String standardMessage() {
		return message;
	}

	public int min() {
		return min;
	}

	public int max() {
		return max;
	}

	public static JclqSfcEnum[] homeEnum() {
		return new JclqSfcEnum[] { H1B5, H6B10, H11B15, H16B20, H21B25, H26 };
	}

	public static JclqSfcEnum[] guestEnum() {
		return new JclqSfcEnum[] { G1B5, G6B10, G11B15, G16B20, G21B25, G26 };
	}

	public String message() {

		return message;
	}

	public Number value() {

		return null;
	}

}
