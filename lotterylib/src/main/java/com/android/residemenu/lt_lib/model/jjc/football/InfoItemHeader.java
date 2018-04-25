package com.android.residemenu.lt_lib.model.jjc.football;

import android.text.SpannableStringBuilder;

/**
 * 二级标题信息
 * 
 * @author leslie
 * 
 * @version $Id: InfoItemHeader.java, v 0.1 2015-1-15 上午11:35:59 leslie Exp $
 */
public class InfoItemHeader {

	private int resId;

	private SpannableStringBuilder builderTip;

	public InfoItemHeader(int resId, SpannableStringBuilder builderTip) {
		super();
		this.resId = resId;
		this.builderTip = builderTip;
	}

	public int getResId() {
		return resId;
	}

	public SpannableStringBuilder getBuilderTip() {
		return builderTip;
	}

}
