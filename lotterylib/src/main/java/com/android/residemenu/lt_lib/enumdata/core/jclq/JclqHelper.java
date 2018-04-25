/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core.jclq;


/**
 * 
 * @author chenbug
 * 
 * @version $Id: JclqHelper, v 0.1 2011-2-24 下午04:27:19 chenbug Exp $
 */
public class JclqHelper {

	public static JclqRaceResultEnum getRfRaceResult(double rfScore,
			int homeScore, int guestScore) {
		// 主队+让球正负-客队比分,等于0 平，大于0 胜，小于0负
		double matchValue = homeScore + rfScore - guestScore;
		if (matchValue > 0) {
			return JclqRaceResultEnum.WIN;
		} else {
			return JclqRaceResultEnum.LOST;
		}
	}

	public static JclqRaceResultEnum getSfRaceResult(int homeScore,
			int guestScore) {
		// 如果主队比分 大于 客队比分
		if (homeScore > guestScore) {
			return JclqRaceResultEnum.WIN;
		} else {
			return JclqRaceResultEnum.LOST;
		}
	}

	public static JclqDxEnum getDxRaceResult(double setScore, int homeScore,
			int guestScore) {

		// 主队+客队 是否大于 让分
		// 匹配值 = 主队进总数 +客队进球数
		int machValue = homeScore + guestScore;
		// 是否 大于 预设总分
		if (machValue > setScore) {
			return JclqDxEnum.BIG;
		} else {
			return JclqDxEnum.SMALL;
		}

	}

	public static JclqSfcEnum getSfcRaceResult(int homeScore, int guestScore) {

		// 如果主队满场比分 大于 客队，表示主胜
		if (homeScore > guestScore) {
			int value = homeScore - guestScore;
			for (JclqSfcEnum jclqSfcEnum : JclqSfcEnum.homeEnum()) {
				// 查看是否落在档期中

				if (value >= jclqSfcEnum.min() && value <= jclqSfcEnum.max()) {
					return jclqSfcEnum;
				}
			}
		} else {
			int value = guestScore - homeScore;
			for (JclqSfcEnum jclqSfcEnum : JclqSfcEnum.guestEnum()) {
				// 查看是否落在档期中

				if (value >= jclqSfcEnum.min() && value <= jclqSfcEnum.max()) {
					return jclqSfcEnum;
				}
			}
		}

		return null;
	}

}
