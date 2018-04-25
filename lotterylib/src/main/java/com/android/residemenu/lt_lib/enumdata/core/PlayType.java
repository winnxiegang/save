package com.android.residemenu.lt_lib.enumdata.core;

/**
 * 玩法类型
 * 
 * @author chenbug
 * 
 * @version $Id: PlayType.java, v 0.1 2012-4-14 上午11:37:02 chenbug Exp $
 */
public interface PlayType {

	/**
	 * 传递给第三方的值
	 * 
	 * @return
	 */
	public String toThirdValue();

	/**
	 * 玩法的name
	 * 
	 * @return
	 */
	public String name();
}
