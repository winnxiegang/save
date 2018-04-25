package com.android.residemenu.lt_lib.enumdata.core;

public enum ShowTypeEnum {
	
	ALL_SHOW("全部显示"),

	ALL_NOT_SHOW("全部不显示"),

	PART_SHOW("部分显示"), ;
	private String message;

	private ShowTypeEnum(String message) {
		this.message = message;
	}

	public String message() {
		// TODO Auto-generated method stub
		return message;
	}

}
