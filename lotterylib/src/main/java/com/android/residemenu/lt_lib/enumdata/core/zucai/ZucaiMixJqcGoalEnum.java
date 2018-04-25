package com.android.residemenu.lt_lib.enumdata.core.zucai;


import com.android.residemenu.lt_lib.enumdata.EnumBase;
import com.android.residemenu.lt_lib.utils.lang.StringUtils;

public enum ZucaiMixJqcGoalEnum  implements EnumBase {

    G0("0", 0),

	G1("1", 1),

	G2("2", 2),

	G3M("3+", 3),
	
	STAR("*",4);
    
	private String message;

    private int value;

	private ZucaiMixJqcGoalEnum(String message, int value) {
		this.message = message;
		this.value = value;
	}

	public static ZucaiMixJqcGoalEnum valueOf(int value) {
		for (ZucaiMixJqcGoalEnum enumObj : ZucaiMixJqcGoalEnum.values()) {
			if (enumObj.value == value)
				return enumObj;
		}
		 
		return null;
	}

	public static ZucaiMixJqcGoalEnum valueOfMessage(String message) {
		for (ZucaiMixJqcGoalEnum enumObj : ZucaiMixJqcGoalEnum.values()) {
			if (StringUtils.equals(enumObj.message, message))
				return enumObj;
		}
	 
		return null;
	}

	public String message() {
		return message;
	}

	public Number value() {
		return value;
	}
}
