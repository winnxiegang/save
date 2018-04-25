package com.android.residemenu.lt_lib.model.jjc;

/**
 * 竞技彩彩果数据
 * 
 * @author leslie
 * 
 * @version $Id: PrizedResult.java, v 0.1 2014年7月11日 下午5:50:50 leslie Exp $
 */
public class PrizedResult {

	private String name;

	private String message;

	private String subTypeName;

	private String subType;

	private String sp;

	public PrizedResult(String name, String message) {
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

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getSubTypeName() {
		return subTypeName;
	}

	public void setSubTypeName(String subTypeName) {
		this.subTypeName = subTypeName;
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

	
}
