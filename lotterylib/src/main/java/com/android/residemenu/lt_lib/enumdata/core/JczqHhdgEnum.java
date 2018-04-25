/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core;

import com.android.residemenu.lt_lib.enumdata.EnumBase;
import com.android.residemenu.lt_lib.enumdata.core.jczq.JczqScoreEnum;
import com.android.residemenu.lt_lib.utils.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * 
 * @author cold
 * 
 * @version $Id: JczqHhggOptionEnum.java, v 0.1 2013-6-1 下午04:59:05 cold Exp $
 */
public enum JczqHhdgEnum implements HhggEnum {

	RQSPF_WIN("让胜", RaceResultEnum.WIN, LotterySubTypeEnum.JCZQ_RQSPF),

	RQSPF_DRAW("让平", RaceResultEnum.DRAW, LotterySubTypeEnum.JCZQ_RQSPF),

	RQSPF_LOST("让负", RaceResultEnum.LOST, LotterySubTypeEnum.JCZQ_RQSPF),

	SPF_WIN("胜", JczqSpfEnum.SPF_WIN, LotterySubTypeEnum.JCZQ_SPF),

	SPF_DRAW("平", JczqSpfEnum.SPF_DRAW, LotterySubTypeEnum.JCZQ_SPF),

	SPF_LOST("负", JczqSpfEnum.SPF_LOST, LotterySubTypeEnum.JCZQ_SPF),

	BF_S_1_0("1:0", JczqScoreEnum.S_1_0, LotterySubTypeEnum.JCZQ_BF),

	BF_S_2_0("2:0", JczqScoreEnum.S_2_0, LotterySubTypeEnum.JCZQ_BF),

	BF_S_2_1("2:1", JczqScoreEnum.S_2_1, LotterySubTypeEnum.JCZQ_BF),

	BF_S_3_0("3:0", JczqScoreEnum.S_3_0, LotterySubTypeEnum.JCZQ_BF),

	BF_S_3_1("3:1", JczqScoreEnum.S_3_1, LotterySubTypeEnum.JCZQ_BF),

	BF_S_3_2("3:2", JczqScoreEnum.S_3_2, LotterySubTypeEnum.JCZQ_BF),

	BF_S_4_0("4:0", JczqScoreEnum.S_4_0, LotterySubTypeEnum.JCZQ_BF),

	BF_S_4_1("4:1", JczqScoreEnum.S_4_1, LotterySubTypeEnum.JCZQ_BF),

	BF_S_4_2("4:2", JczqScoreEnum.S_4_2, LotterySubTypeEnum.JCZQ_BF),

	BF_S_5_0("5:0", JczqScoreEnum.S_5_0, LotterySubTypeEnum.JCZQ_BF),

	BF_S_5_1("5:1", JczqScoreEnum.S_5_1, LotterySubTypeEnum.JCZQ_BF),

	BF_S_5_2("5:2", JczqScoreEnum.S_5_2, LotterySubTypeEnum.JCZQ_BF),

	BF_S_W_O("胜其他", JczqScoreEnum.S_W_O, LotterySubTypeEnum.JCZQ_BF),

	BF_S_0_0("0:0", JczqScoreEnum.S_0_0, LotterySubTypeEnum.JCZQ_BF),

	BF_S_1_1("1:1", JczqScoreEnum.S_1_1, LotterySubTypeEnum.JCZQ_BF),

	BF_S_2_2("2:2", JczqScoreEnum.S_2_2, LotterySubTypeEnum.JCZQ_BF),

	BF_S_3_3("3:3", JczqScoreEnum.S_3_3, LotterySubTypeEnum.JCZQ_BF),

	BF_S_D_O("平其他", JczqScoreEnum.S_D_O, LotterySubTypeEnum.JCZQ_BF),

	BF_S_0_1("0:1", JczqScoreEnum.S_0_1, LotterySubTypeEnum.JCZQ_BF),

	BF_S_0_2("0:2", JczqScoreEnum.S_0_2, LotterySubTypeEnum.JCZQ_BF),

	BF_S_1_2("1:2", JczqScoreEnum.S_1_2, LotterySubTypeEnum.JCZQ_BF),

	BF_S_0_3("0:3", JczqScoreEnum.S_0_3, LotterySubTypeEnum.JCZQ_BF),

	BF_S_1_3("1:3", JczqScoreEnum.S_1_3, LotterySubTypeEnum.JCZQ_BF),

	BF_S_2_3("2:3", JczqScoreEnum.S_2_3, LotterySubTypeEnum.JCZQ_BF),

	BF_S_0_4("0:4", JczqScoreEnum.S_0_4, LotterySubTypeEnum.JCZQ_BF),

	BF_S_1_4("1:4", JczqScoreEnum.S_1_4, LotterySubTypeEnum.JCZQ_BF),

	BF_S_2_4("2:4", JczqScoreEnum.S_2_4, LotterySubTypeEnum.JCZQ_BF),

	BF_S_0_5("0:5", JczqScoreEnum.S_0_5, LotterySubTypeEnum.JCZQ_BF),

	BF_S_1_5("1:5", JczqScoreEnum.S_1_5, LotterySubTypeEnum.JCZQ_BF),

	BF_S_2_5("2:5", JczqScoreEnum.S_2_5, LotterySubTypeEnum.JCZQ_BF),

	BF_S_L_O("负其他", JczqScoreEnum.S_L_O, LotterySubTypeEnum.JCZQ_BF),

	BQC_WW("胜胜", JczqHalfFullRaceResultEnum.WW, LotterySubTypeEnum.JCZQ_BQC),

	BQC_WD("胜平", JczqHalfFullRaceResultEnum.WD, LotterySubTypeEnum.JCZQ_BQC),

	BQC_WL("胜负", JczqHalfFullRaceResultEnum.WL, LotterySubTypeEnum.JCZQ_BQC),

	BQC_DW("平胜", JczqHalfFullRaceResultEnum.DW, LotterySubTypeEnum.JCZQ_BQC),

	BQC_DD("平平", JczqHalfFullRaceResultEnum.DD, LotterySubTypeEnum.JCZQ_BQC),

	BQC_DL("平负", JczqHalfFullRaceResultEnum.DL, LotterySubTypeEnum.JCZQ_BQC),

	BQC_LW("负胜", JczqHalfFullRaceResultEnum.LW, LotterySubTypeEnum.JCZQ_BQC),

	BQC_LD("负平", JczqHalfFullRaceResultEnum.LD, LotterySubTypeEnum.JCZQ_BQC),

	BQC_LL("负负", JczqHalfFullRaceResultEnum.LL, LotterySubTypeEnum.JCZQ_BQC),

	JQS_G0("0球", JczqGoalsEnum.G0, LotterySubTypeEnum.JCZQ_JQS),

	JQS_G1("1球", JczqGoalsEnum.G1, LotterySubTypeEnum.JCZQ_JQS),

	JQS_G2("2球", JczqGoalsEnum.G2, LotterySubTypeEnum.JCZQ_JQS),

	JQS_G3("3球", JczqGoalsEnum.G3, LotterySubTypeEnum.JCZQ_JQS),

	JQS_G4("4球", JczqGoalsEnum.G4, LotterySubTypeEnum.JCZQ_JQS),

	JQS_G5("5球", JczqGoalsEnum.G5, LotterySubTypeEnum.JCZQ_JQS),

	JQS_G6("6球", JczqGoalsEnum.G6, LotterySubTypeEnum.JCZQ_JQS),

	JQS_G7("7球+", JczqGoalsEnum.G7, LotterySubTypeEnum.JCZQ_JQS), ;

	;

	private int value;

	private LotterySubTypeEnum subType;

	private String message;

	private EnumBase realResultEnum;

	 

	private JczqHhdgEnum(String message, EnumBase realResultEnum, LotterySubTypeEnum subType) {
		this.message = message;
		this.subType = subType;
		this.realResultEnum = realResultEnum;
	}

	public static JczqHhdgEnum valueOfRealResultEnumValue(EnumBase realResultEnum) {
		for (JczqHhdgEnum option : JczqHhdgEnum.values()) {
			if (option.realResultEnum == realResultEnum)
				return option;
		}
		return null;
	}

	public static List<JczqHhdgEnum> valueOfRealResultEnumValues(List<EnumBase> realResultEnums) {
		List<JczqHhdgEnum> returnList = new ArrayList<JczqHhdgEnum>();
		for (EnumBase enumBase : realResultEnums) {
			returnList.add(valueOfRealResultEnumValue(enumBase));
		}
		return returnList;
	}

	/**
	 * 根据玩法，与真实的枚举名称获得枚举
	 * 
	 * @param subType
	 * @param option
	 * @return
	 */
	public static JczqHhdgEnum valueOfSubTypeRealName(LotterySubTypeEnum subType, String option) {
		for (JczqHhdgEnum hhdgEnum : JczqHhdgEnum.values()) {
			if (hhdgEnum.subType == subType && StringUtils.equals(hhdgEnum.realResultEnum.name(), option)) {
				return hhdgEnum;
			}
		}
		return null;
	}

	public static List<JczqHhdgEnum> valueOfSubTypeRealNames(LotterySubTypeEnum subType, List<String> options) {
		List<JczqHhdgEnum> returnList = new ArrayList<JczqHhdgEnum>();
		for (String otpion : options) {
			returnList.add(valueOfSubTypeRealName(subType, otpion));
		}
		return returnList;

	}

	public LotterySubTypeEnum subType() {
		return this.subType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bench.common.enums.EnumBase#message()
	 */
	public String message() {
		// TODO Auto-generated method stub
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bench.common.enums.EnumBase#value()
	 */
	public Number value() {
		// TODO Auto-generated method stub
		return value;
	}

	public EnumBase realResultEnum() {
		// TODO Auto-generated method stub
		return realResultEnum;
	}
}
