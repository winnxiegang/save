package com.android.residemenu.lt_lib.enumdata;




public enum LotterySelecterEnum {

	JCZQ_RQSPF(6, "竞足", "让球胜平负"),

    JCZQ_SPF(0, "竞足", "胜平负"),

    JCZQ_BF(1, "竞足", "比分"),

    JCZQ_JQS(8, "竞足", "进球数"),

    JCZQ_BQC(1, "竞足", "半全场"),

    JCZQ_HHGG(6, "竞足", "混合过关"),

    JCZQ_GDDG(6, "竞足", "固定单关"),

    JCLQ_DX(2, "竞篮", "大小分"),

    JCLQ_SF(2, "竞篮", "胜负"),

    JCLQ_RF(2, "竞篮", "让分"),

    JCLQ_HHGG(2, "竞篮", "混合过关"),

    JCLQ_SFC(1, "竞篮", "胜分差"),

    ZUCAI_SFC(3, "足彩", "胜负彩"),

	ZUCAI_JQC(8, "足彩", "进球彩"),

	ZUCAI_R9(3, "足彩", "任选九"),

	ZUCAI_BQC(6, "足彩", "六场半全"),
	
	BJDC_RQSPF(3,"北京单场","胜平负"),
	
	BJDC_BF(1, "北京单场","比分"),

	BJDC_JQS(8,"北京单场", "进球数"),

	BJDC_BQC(1, "北京单场", "半全场"),

	BJDC_SXDS(4,"北京单场", "上下单双");
	
	private int size;

	private String title;

	private String tip;

	private LotterySelecterEnum(int size, String title, String tip) {
		this.size = size;
		this.title = title;
		this.tip = tip;
	}

	public int getSize() {
		return size;
	}

	public String getTip() {
		return tip;
	}

	public String getTitle() {
		return title;
	}

}
