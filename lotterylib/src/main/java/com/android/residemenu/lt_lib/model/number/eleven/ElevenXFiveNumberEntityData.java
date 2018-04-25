package com.android.residemenu.lt_lib.model.number.eleven;


import com.android.residemenu.lt_lib.model.number.NumberEntityData;

/**
 * 11选5数据
 * 
 * @author leslie
 * 
 * @version $Id: ElevenXFiveNumberEntityData.java, v 0.1 2014年9月2日 下午5:34:59
 *          leslie Exp $
 */
public class ElevenXFiveNumberEntityData extends NumberEntityData {

	/**
	 * 任选或直选第一排数据
	 */
	private String[] firstNumberList;

	/**
	 * 第二排数据
	 */
	private String[] secondNumberList;
	/**
	 * 第三排数据
	 */
	private String[] thirdNumbrtList;

	private String playSubType;

	public ElevenXFiveNumberEntityData() {

	}

	public String[] getFirstNumberList() {
		return firstNumberList;
	}

	public void setFirstNumberList(String[] firstNumberList) {
		this.firstNumberList = firstNumberList;
	}

	public String[] getSecondNumberList() {
		return secondNumberList;
	}

	public void setSecondNumberList(String[] secondNumberList) {
		this.secondNumberList = secondNumberList;
	}

	public String[] getThirdNumbrtList() {
		return thirdNumbrtList;
	}

	public void setThirdNumbrtList(String[] thirdNumbrtList) {
		this.thirdNumbrtList = thirdNumbrtList;
	}

	public String getPlaySubType() {
		return playSubType;
	}

	public void setPlaySubType(String playSubType) {
		this.playSubType = playSubType;
	}

	
}
