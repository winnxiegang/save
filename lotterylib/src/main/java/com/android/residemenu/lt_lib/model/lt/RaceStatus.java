package com.android.residemenu.lt_lib.model.lt;

/**
 * 比赛状态
 * 
 * @author leslie
 * 
 * @version $Id: RaceStatus.java, v 0.1 2014年7月9日 下午5:04:53 leslie Exp $
 */
public class RaceStatus {

	private String name;

	private String message;

	public RaceStatus(String name, String message) {
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
