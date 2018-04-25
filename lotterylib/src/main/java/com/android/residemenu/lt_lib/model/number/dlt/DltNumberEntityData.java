package com.android.residemenu.lt_lib.model.number.dlt;


import com.android.residemenu.lt_lib.model.number.NumberEntityData;

/**
 * 大乐透数据
 * 
 * @author leslie
 * 
 * @version $Id: DltNumberEntityData.java, v 0.1 2014年9月2日 下午5:35:38 leslie Exp
 *          $
 */
public class DltNumberEntityData extends NumberEntityData {

	/**
	 * 蓝球区(后区)
	 */
	private String[] blue;

	/**
	 * 后区胆
	 */
	private String[] blueDan;

	public DltNumberEntityData() {

	}

	public String[] getBlue() {
		return blue;
	}

	public void setBlue(String[] blue) {
		this.blue = blue;
	}

	public String[] getBlueDan() {
		return blueDan;
	}

	public void setBlueDan(String[] blueDan) {
		this.blueDan = blueDan;
	}

}
