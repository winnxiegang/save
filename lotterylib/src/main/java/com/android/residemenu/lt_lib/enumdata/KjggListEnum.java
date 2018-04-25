package com.android.residemenu.lt_lib.enumdata;

public enum KjggListEnum {

	SSQ("双色球"),
	DLT("大乐透"),
	SFC("胜负彩/任九"),
	JQC("进球彩"),
	BQC("半全场"),
    JCZQ("竞彩足球"),
	JCLQ("竞彩篮球"),
	BJDC("北京单场"),
	K3("内蒙古快3"),
	FC_3D("福彩3D"),
	X11X5("江西11选5");
	
	String name;
	KjggListEnum(String name){
		this.name = name;
	}
	public String getMessage(){
		return name;
	}
	
}
