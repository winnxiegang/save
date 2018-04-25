package com.android.residemenu.lt_lib.enumdata.core.jclq;

import com.android.residemenu.lt_lib.enumdata.EnumBase;

import org.apache.commons.lang3.StringUtils;



/**
 * 竞猜篮球大小枚举
 * 
 * @author chenbug
 * 
 * @version $Id: JclqDxEnum.java, v 0.1 2011-3-3 下午07:23:36 chenbug Exp $
 */
public enum JclqDxEnum implements EnumBase {

	BIG(1, "大"),

	SMALL(2, "小");

	private String message;

	public String standardMessage() {
		return message;
	}

	private int value;

	private JclqDxEnum(int value, String message) {
		this.message = message;
		this.value = value;
	}

	public static JclqDxEnum messageOf(String message) {
		for (JclqDxEnum enumObj : JclqDxEnum.values()) {
			if (StringUtils.equals(enumObj.message, message))
				return enumObj;
		}

		return null;
	}

	public String message() {

		return message;
	}

	public static JclqDxEnum valueOf(int value) {
		for (JclqDxEnum enumObj : JclqDxEnum.values()) {
			if (enumObj.value == value)
				return enumObj;
		}

		return null;
	}

	public Number value() {

		return value;
	}

}
