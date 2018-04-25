package com.android.residemenu.lt_lib.model.user;

/**
 * 账务方向
 * 
 * @author leslie
 * 
 * @version $Id: TransDirection.java, v 0.1 2014年7月14日 下午6:05:57 leslie Exp $
 */
public class TransDirection {

	private String name;

	private String message;

	public TransDirection(String name, String message) {
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
