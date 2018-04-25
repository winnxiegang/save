/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core;

import com.android.residemenu.lt_lib.enumdata.EnumBase;

import org.apache.commons.lang3.StringUtils;



/**
 * 
 * @author chenbug
 * 
 * @version $Id: RaceResultEnum.java, v 0.1 2011-2-16 下午02:47:18 chenbug Exp $
 */
public enum RaceResultEnum implements EnumBase {

	WIN("胜", 3, "主胜"),

	DRAW("平", 1, "主平"),

	LOST("负", 0, "主负");

	private String message;

	private String aliasMessage;

	private int value;

	private RaceResultEnum(String message, int value, String aliasMessage) {
		this.message = message;
		this.value = value;
		this.aliasMessage = aliasMessage;
	}

	public String standardMessage() {
		return Integer.toString(value);
	}

	public String message() {
		return message;
	}

	public static RaceResultEnum safeValueOf(String name) {
		for (RaceResultEnum raceResultEnum : RaceResultEnum.values()) {
			if (StringUtils.equals(raceResultEnum.name(), name)) {
				return raceResultEnum;
			}
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	public static RaceResultEnum valueOf(int value) {
		for (RaceResultEnum enumObj : RaceResultEnum.values()) {
			if (enumObj.value == value) {
				return enumObj;
			}
		}

		return null;
	}

	public static RaceResultEnum messageOf(String message) {
		for (RaceResultEnum enumObj : RaceResultEnum.values()) {
			if (StringUtils.equals(enumObj.message, message)) {
				return enumObj;
			}
		}

		return null;
	}

	public String aliasMessage() {
		return this.aliasMessage;
	}

	public Number value() {

		return value;
	}

}
