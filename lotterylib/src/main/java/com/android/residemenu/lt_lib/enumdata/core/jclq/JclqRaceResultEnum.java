package com.android.residemenu.lt_lib.enumdata.core.jclq;

import com.android.residemenu.lt_lib.enumdata.EnumBase;

import org.apache.commons.lang3.StringUtils;



/**
 * 竞猜篮球胜负枚举
 * 
 * @author chenbug
 * 
 * @version $Id: JclqDxEnum.java, v 0.1 2011-3-3 下午07:23:36 chenbug Exp $
 */
public enum JclqRaceResultEnum implements EnumBase {

	WIN(1, "主胜"),

	LOST(2, "主负");

	private String message;

	private int value;

	public String standardMessage() {
		return message;
	}

	private JclqRaceResultEnum(int value, String message) {
		this.message = message;
		this.value = value;
	}

	public String message() {
		// TODO Auto-generated method stub
		return message;
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static JclqRaceResultEnum messageOf(String message) {
		for (JclqRaceResultEnum enumObj : JclqRaceResultEnum.values()) {
			if (StringUtils.equals(message, enumObj.message)) {
				return enumObj;
			}
		}

		return null;
	}

	public static JclqRaceResultEnum valueOf(int value) {
		for (JclqRaceResultEnum enumObj : JclqRaceResultEnum.values()) {
			if (enumObj.value == value)
				return enumObj;
		}

		return null;
	}

	/**
	 * 对传入的参数message，判断是否存在枚举中的message，如果存在，则返回对应的枚举，否则返回null
	 * 
	 * @param message
	 * @return
	 */
	public static JclqRaceResultEnum indexMessageOf(String message) {
		if (StringUtils.isEmpty(message)) {

			return null;
		}
		for (JclqRaceResultEnum enumObj : JclqRaceResultEnum.values()) {
			if (StringUtils.indexOf(message, enumObj.message) >= 0) {
				return enumObj;
			}
		}

		return null;
	}

	public Number value() {

		return value;
	}

}
