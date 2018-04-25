package com.android.residemenu.lt_lib.model.lt;

/**
 * 保密方式
 * 
 * @author leslie
 * 
 * @version $Id: KeepSecretType.java, v 0.1 2014年7月8日 下午6:22:51 leslie Exp $
 */
public class KeepSecretType {

	private String name;

	private String message;

	public KeepSecretType(String name, String message) {
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
