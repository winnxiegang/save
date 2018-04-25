package com.android.residemenu.lt_lib.model.lt;

/**
 * 方案创建类型
 * 
 * @author leslie
 * 
 */
public class CreateType {

	private String name;

	private String message;

	public CreateType(String name, String message) {
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
