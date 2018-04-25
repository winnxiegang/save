package com.android.residemenu.lt_lib.model.number;

/**
 * 数字彩投注数据
 * 
 * @author leslie
 * 
 * @version $Id: NumberEntityData.java, v 0.1 2014年8月22日 下午5:12:59 leslie Exp $
 */
public abstract class NumberEntityData {

	/**
	 * 红球区(前区)
	 */
	private String[] red;

	/**
	 * 红球胆(前区)
	 */
	private String[] redDan;

	/**
	 * 注数
	 */
	private int amount;
	/**
	 * 
	 */
	private String title;

	/**
	 * 单式 复式 胆拖
	 */
	private String playType;

	public NumberEntityData() {

	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public String[] getRed() {
		return red;
	}

	public void setRed(String[] red) {
		this.red = red;
	}

	public String[] getRedDan() {
		return redDan;
	}

	public void setRedDan(String[] redDan) {
		this.redDan = redDan;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
