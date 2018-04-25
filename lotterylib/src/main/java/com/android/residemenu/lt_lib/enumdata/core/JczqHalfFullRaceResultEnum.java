/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core;

import com.android.residemenu.lt_lib.enumdata.EnumBase;

import org.apache.commons.lang3.StringUtils;



/**
 * 半全场选项枚举
 * 
 * @author chenbug
 * 
 * @version $Id: JczqBqcEnum.java, v 0.1 2011-3-2 下午06:38:11 chenbug Exp $
 */
public enum JczqHalfFullRaceResultEnum implements EnumBase {

	WW(RaceResultEnum.WIN, RaceResultEnum.WIN, "胜胜"),

	WD(RaceResultEnum.WIN, RaceResultEnum.DRAW, "胜平"),

	WL(RaceResultEnum.WIN, RaceResultEnum.LOST, "胜负"),

	DW(RaceResultEnum.DRAW, RaceResultEnum.WIN, "平胜"),

	DD(RaceResultEnum.DRAW, RaceResultEnum.DRAW, "平平"),

	DL(RaceResultEnum.DRAW, RaceResultEnum.LOST, "平负"),

	LW(RaceResultEnum.LOST, RaceResultEnum.WIN, "负胜"),

	LD(RaceResultEnum.LOST, RaceResultEnum.DRAW, "负平"),

	LL(RaceResultEnum.LOST, RaceResultEnum.LOST, "负负"), ;
	
	/**
	 * 半场
	 */
	private RaceResultEnum half;

	/**
	 * 全场
	 */
	private RaceResultEnum full;

	private String message;

	public String standardMessage() {
		return this.half.value().toString() + this.full.value();
	}

	private JczqHalfFullRaceResultEnum(RaceResultEnum half, RaceResultEnum full, String message) {
		this.half = half;
		this.full = full;
		this.message = message;
	}

	public static final JczqHalfFullRaceResultEnum halfFullOf(RaceResultEnum half, RaceResultEnum full) {
		for (JczqHalfFullRaceResultEnum enumObj : JczqHalfFullRaceResultEnum.values()) {
			if (enumObj.half == half && enumObj.full == full) {
				return enumObj;
			}
		}

		return null;
	}
    public static final JczqHalfFullRaceResultEnum halfFullOf(int halfScore, int fullScore) {
        RaceResultEnum half = RaceResultEnum.valueOf(halfScore);
        RaceResultEnum full = RaceResultEnum.valueOf(fullScore);
        for (JczqHalfFullRaceResultEnum enumObj : JczqHalfFullRaceResultEnum.values()) {
            if (enumObj.half == half && enumObj.full == full) {
                return enumObj;
            }
        }

        return null;
    }

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static final JczqHalfFullRaceResultEnum messageOf(String message) {
		for (JczqHalfFullRaceResultEnum enumObj : JczqHalfFullRaceResultEnum.values()) {
			if (StringUtils.equals(enumObj.message(), message)) {
				return enumObj;
			}
		}

		return null;
	}

	public static JczqHalfFullRaceResultEnum safeValueOf(String name) {
		for (JczqHalfFullRaceResultEnum jczqHalfFullRaceResultEnum : JczqHalfFullRaceResultEnum.values()) {
			if (StringUtils.equals(jczqHalfFullRaceResultEnum.name(), name)) {
				return jczqHalfFullRaceResultEnum;
			}
		}

		return null;
	}

	public RaceResultEnum half() {
		return half;
	}

	public RaceResultEnum full() {
		return full;
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
