/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core;

/**
 * 玩法子类型
 * 
 * @author chenbug
 * 
 * @version $Id: PlaySubType.java, v 0.1 2012-4-14 上午11:37:02 chenbug Exp $
 */
public interface PlaySubType {

	/**
	 * 传递给第三方的值
	 * 
	 * @return
	 */
	public String toThirdValue();

	/**
	 * 子玩法的name
	 * 
	 * @return
	 */
	public String name();
}
