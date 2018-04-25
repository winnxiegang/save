package com.android.residemenu.lt_lib.model.lt;

/**
 * 彩票一级
 * 
 * @author leslie
 * 
 * @version $Id: LotteryType.java, v 0.1 2014年7月8日 下午4:57:36 leslie Exp $
 */
public class LotteryType {

	private String name;

	private String message;

	public LotteryType(String name, String message) {
		super();
		this.name = name;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
