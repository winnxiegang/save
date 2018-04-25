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
 * @version $Id: JczqGoalsEnum.java, v 0.1 2011-3-2 下午03:05:00 chenbug Exp $
 */
public enum JczqGoalsEnum implements EnumBase {

	G0(0, "0球"),

	G1(1, "1球"),

	G2(2, "2球"),

	G3(3, "3球"),

	G4(4, "4球"),

	G5(5, "5球"),

	G6(6, "6球"),

	G7(7, "7球+");

	private String message;

	private int goals;

	public String standardMessage() {
		return Integer.toString(goals);
	}

	private JczqGoalsEnum(int goals, String message) {
		this.message = message;
		this.goals = goals;
	}

	public static JczqGoalsEnum safeValueOf(String name) {
		for (JczqGoalsEnum jczqGoalsEnum : JczqGoalsEnum.values()) {
			if (StringUtils.equals(jczqGoalsEnum.name(), name)) {
				return jczqGoalsEnum;
			}
		}

		return null;
	}

	/**
	 * @param goals
	 * @return
	 */
	public static final JczqGoalsEnum goalsOf(int goals) {
		for (JczqGoalsEnum goalsEnum : JczqGoalsEnum.values()) {
			if (goalsEnum.goals == goals) {
				return goalsEnum;
			}
		}

		return null;
	}

	/**
	 * @param message
	 * @return
	 */
	public static final JczqGoalsEnum messageOf(String message) {
		for (JczqGoalsEnum goalsEnum : JczqGoalsEnum.values()) {
			if (StringUtils.equals(message, goalsEnum.message)
					|| StringUtils.equals(message, StringUtils.replace(goalsEnum.message, "球", ""))) {
				return goalsEnum;
			}
		}

		return null;
	}

	public String message() {

		return message;
	}

	public Number value() {

		return goals;
	}

}
