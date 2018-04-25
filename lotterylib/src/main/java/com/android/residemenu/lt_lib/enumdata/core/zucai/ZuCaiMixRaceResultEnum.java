package com.android.residemenu.lt_lib.enumdata.core.zucai;


import com.android.residemenu.lt_lib.enumdata.EnumBase;

/**
 * author by zhouyi
 */
public enum ZuCaiMixRaceResultEnum implements EnumBase {

	WIN("3", 3),

	DRAW("1", 1),

	LOST("0", 0),
	
	STAR("*",4);

	private String message;

	private int value;

	private ZuCaiMixRaceResultEnum(String message, int value) {
		this.message = message;
		this.value = value;
	}

	public static ZuCaiMixRaceResultEnum valueOf(int value) {
		for (ZuCaiMixRaceResultEnum resultEnum : ZuCaiMixRaceResultEnum.values()) {
			if (resultEnum.value == value)
				return resultEnum;
		}
		 
		return null;
	}

    public static ZuCaiMixRaceResultEnum valueOfMessage(String value){
        for (ZuCaiMixRaceResultEnum resultEnum : ZuCaiMixRaceResultEnum.values()) {
            if (resultEnum.message.equals(value))
                return resultEnum;
        }

        return null;
    }

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static ZuCaiMixRaceResultEnum safeValueOf(int value) {
		for (ZuCaiMixRaceResultEnum resultEnum : ZuCaiMixRaceResultEnum.values()) {
			if (resultEnum.value == value)
				return resultEnum;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bench.common.enums.EnumBase#message()
	 */
	public String message() {
		 
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bench.common.enums.EnumBase#value()
	 */
	public Number value() {
		 
		return value;
	}

}
