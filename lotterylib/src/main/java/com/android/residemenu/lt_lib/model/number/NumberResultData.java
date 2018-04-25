package com.android.residemenu.lt_lib.model.number;

import java.util.List;
import java.util.Map;

/**
 * 数字彩彩果
 * 
 * @author leslie
 * 
 * @version $Id: NumberResultData.java, v 0.1 2014年8月25日 上午11:03:00 leslie Exp $
 */
public class NumberResultData {

	private Map<String, String> redMap;

	private Map<String, String> blueMap;

	public NumberResultData(Map<String, String> redMap, Map<String, String> blueMap) {
		super();
		this.redMap = redMap;
		this.blueMap = blueMap;

	}

	public Map<String, String> getRedMap() {
		return redMap;
	}

	public void setRedMap(Map<String, String> redMap) {
		this.redMap = redMap;
	}

	public Map<String, String> getBlueMap() {
		return blueMap;
	}

	public void setBlueMap(Map<String, String> blueMap) {
		this.blueMap = blueMap;
	}

}
