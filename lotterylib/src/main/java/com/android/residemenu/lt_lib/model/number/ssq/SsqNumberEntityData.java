package com.android.residemenu.lt_lib.model.number.ssq;


import com.android.residemenu.lt_lib.model.number.NumberEntityData;

/**
 * 双色球数据
 * 
 * @author leslie
 * 
 * @version $Id: SsqNumberEntityData.java, v 0.1 2014年9月2日 下午5:34:30 leslie Exp
 *          $
 */
public class SsqNumberEntityData extends NumberEntityData {

	/**
	 * 蓝球区
	 */
	private String[] blue;

	public SsqNumberEntityData() {

	}

	public String[] getBlue() {
		return blue;
	}

	public void setBlue(String[] blue) {
		this.blue = blue;
	}

}
