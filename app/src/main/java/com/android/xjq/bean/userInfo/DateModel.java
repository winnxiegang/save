package com.android.xjq.bean.userInfo;

/**
 * 
 * 
 * @author leslie
 * 
 * @version $Id: DateModel.java, v 0.1 2014年7月16日 上午10:40:03 leslie Exp $
 */
public class DateModel {

	private String tag;

	private String dateStr;

	public DateModel() {

	}

	public DateModel(String tag, String dateStr) {
		this.tag = tag;
		this.dateStr = dateStr;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

}
