/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core.jczq;


import com.android.residemenu.lt_lib.enumdata.core.JczqGoalsEnum;
import com.android.residemenu.lt_lib.enumdata.core.JczqSpfEnum;
import com.android.residemenu.lt_lib.enumdata.core.RaceResultEnum;
import com.android.residemenu.lt_lib.utils.lang.StringUtils;

/**
 * 
 * @author chenbug
 * 
 * @version $Id: JczqHelper, v 0.1 2011-2-24 下午04:27:19 chenbug Exp $
 */
public class JczqHelper {

	/**
	 * 获得让球胜平负的彩果 如果未开奖 返回null
	 * 
	 * @param jczqData
	 * @return
	 */
	public static RaceResultEnum getRqspfRaceResult(int fullHomeScorel,
			int fullGuestScore, int concede) {

		// 主队+让球正负-客队比分,等于0 平，大于0 胜，小于0负
		int matchValue = fullHomeScorel + concede - fullGuestScore;
		if (matchValue == 0) {
			// 等于平
			return RaceResultEnum.DRAW;
		} else if (matchValue > 0) {
			return RaceResultEnum.WIN;
		} else {
			return RaceResultEnum.LOST;
		}

	}

	public static JczqSpfEnum getSpfRaceResult(int fullHomeScore,
			int fullGuestScore) {

		// 主队+让球正负-客队比分,等于0 平，大于0 胜，小于0负
		int matchValue = fullHomeScore - fullGuestScore;
		if (matchValue == 0) {
			// 等于平
			return JczqSpfEnum.SPF_DRAW;
		} else if (matchValue > 0) {
			return JczqSpfEnum.SPF_WIN;
		} else {
			return JczqSpfEnum.SPF_LOST;
		}

	}

	/**
	 * 获得比分的彩果 如果未开奖 返回null
	 * 
	 * @param jczqData
	 * @return
	 */
	public static JczqScoreEnum getBfRaceResult(int fullHomeScorel,
			int fullGuestScore) {

		// 循环遍历
		for (JczqScoreEnum tempEnum : JczqScoreEnum.values()) {
			// 如果当前枚举与开奖比分能匹配,则返回当前枚举
			if (StringUtils.equals(tempEnum.name(), "S_" + fullHomeScorel + "_"
					+ fullGuestScore)) {
				return tempEnum;
			}
		}
		// 如果执行到这里，标明比赛结果是胜其他，或负其他，或平其他
		// 主队+让球正负-客队比分,等于0 平，大于0 胜，小于0负
		int matchValue = fullHomeScorel - fullGuestScore;

		if (matchValue == 0) {
			// 如果是平
			return JczqScoreEnum.S_D_O;
		} else if (matchValue > 0) {
			// 如果是胜
			return JczqScoreEnum.S_W_O;
		} else {
			// 如果是负
			return JczqScoreEnum.S_L_O;
		}

	}

	/**
	 * 获得进球数彩果 如果未开奖 返回null
	 * 
	 * @param jczqData
	 * @return
	 */
	public static JczqGoalsEnum getJqsRaceResult(int fullHomeScorel,
			int fullGuestScore) {

		// 遍历枚举，与主队客队比分进行名称比较
		for (JczqGoalsEnum tempEnum : JczqGoalsEnum.values()) {
			// 临时枚举名称,G+主队进球数+客队进球数
			String tempEnumName = "G" + (fullHomeScorel + fullGuestScore);
			if (StringUtils.equals(tempEnum.name(), tempEnumName)) {
				// 如果匹配成功，则返回当前枚举
				return tempEnum;
			}
		}
		// 如果到这一步，表示匹配不成功，则比分是7+
		return JczqGoalsEnum.G7;

	}

	/**
	 * 获得半全场赛果 如果未开奖 返回null
	 * 
	 * @param jczqData
	 * @return
	 */
	public static JczqHalfFullRaceResultEnum getBqcRaceResult(
			int halfHomeScore, int halfGuestScore, int fullHomeScorel,
			int fullGuestScore) {

		// 最后彩果 = 半场彩果 + 全场彩果 如：半场彩果为0，全场彩果为3，那就是 0-3
		// 半场匹配值
		int halfMatchValue = halfHomeScore - halfGuestScore;
		String halfRaceResult = "";
		if (halfMatchValue == 0) {
			halfRaceResult = "D";
		} else if (halfMatchValue > 0) {
			halfRaceResult = "W";
		} else {
			halfRaceResult = "L";
		}
		// 全场匹配值
		int fullMatchValue = fullHomeScorel - fullGuestScore;
		String fullRaceResult = "";
		if (fullMatchValue == 0) {
			fullRaceResult = "D";
		} else if (fullMatchValue > 0) {
			fullRaceResult = "W";
		} else {
			fullRaceResult = "L";
		}
		// 转换并返回枚举
		return JczqHalfFullRaceResultEnum.valueOf(halfRaceResult
				+ fullRaceResult);

	}

}
