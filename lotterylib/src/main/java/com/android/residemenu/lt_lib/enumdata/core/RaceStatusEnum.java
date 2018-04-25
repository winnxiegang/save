package com.android.residemenu.lt_lib.enumdata.core;


import com.android.residemenu.lt_lib.enumdata.EnumBase;

/**
 * 
 * @author chenbug
 * 
 * @version $Id: RaceStatusEnum.java, v 0.1 2011-2-16 下午03:05:54 chenbug Exp $
 */
public enum RaceStatusEnum implements EnumBase {

	//
	WAIT("未赛"),

	FINISH("已赛"),

	PLAY("赛中"),

	DELAY("延期"),

	CANCEL("取消");

	private String message;

	private RaceStatusEnum(String message) {
		this.message = message;
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
