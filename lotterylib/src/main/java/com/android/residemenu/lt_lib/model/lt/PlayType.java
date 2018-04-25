package com.android.residemenu.lt_lib.model.lt;

/**
 * 玩法
 * 
 * @author leslie
 * 
 * @version $Id: PlayType.java, v 0.1 2014年7月12日 下午12:07:21 leslie Exp $
 */
public class PlayType {

	private String name;

	private String message;

	public PlayType(String name, String message) {
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
