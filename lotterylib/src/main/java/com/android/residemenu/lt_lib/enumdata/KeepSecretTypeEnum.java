package com.android.residemenu.lt_lib.enumdata;

/**
 * 保密模式
 * 
 * @author chenbug
 * 
 * @version $Id: KeepSecretTypeEnum.java, v 0.1 2011-2-25 下午08:26:26 chenbug Exp
 *          $
 */
public enum KeepSecretTypeEnum implements EnumBase {

	PUBLIC("公开"),

	SOLD("销售结束后公开"),

	SECRET("保密"),

	FLOWER("对跟单者公开"),

	HIDE("隐藏（不显示任何消息）"),

	PRIZE_AFTER("开奖后公开"),

	RACE_START_AFTER("开赛后公开");

	private String message;

	private KeepSecretTypeEnum(String message) {
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
