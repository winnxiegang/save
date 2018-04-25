/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata;


import com.android.residemenu.lt_lib.enumdata.core.HhggEnum;
import com.android.residemenu.lt_lib.enumdata.core.LotterySubTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.jclq.JclqDxEnum;
import com.android.residemenu.lt_lib.enumdata.core.jclq.JclqRaceResultEnum;
import com.android.residemenu.lt_lib.enumdata.core.jclq.JclqSfcEnum;
import com.android.residemenu.lt_lib.utils.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cold
 *
 * @version $Id: JczqHhggOptionEnum.java, v 0.1 2013-6-1 下午04:59:05 cold Exp $
 */
public enum JclqHhggEnum implements HhggEnum {

    RF_WIN("主胜", JclqRaceResultEnum.WIN, LotterySubTypeEnum.JCLQ_RF),

    RF_LOST("主负", JclqRaceResultEnum.LOST, LotterySubTypeEnum.JCLQ_RF),

    DX_BIG("大", JclqDxEnum.BIG, LotterySubTypeEnum.JCLQ_DX),

    DX_SMALL("小", JclqDxEnum.SMALL, LotterySubTypeEnum.JCLQ_DX),

    SFC_G1B5("客胜(1-5)", JclqSfcEnum.G1B5, LotterySubTypeEnum.JCLQ_SFC),

    SFC_G6B10("客胜(6-10)", JclqSfcEnum.G6B10, LotterySubTypeEnum.JCLQ_SFC),

    SFC_G11B15("客胜(11-15)", JclqSfcEnum.G11B15, LotterySubTypeEnum.JCLQ_SFC),

    SFC_G16B20("客胜(16-20)", JclqSfcEnum.G16B20, LotterySubTypeEnum.JCLQ_SFC),

    SFC_G21B25("客胜(21-25)", JclqSfcEnum.G21B25, LotterySubTypeEnum.JCLQ_SFC),

    SFC_G26("客胜(26+)", JclqSfcEnum.G26, LotterySubTypeEnum.JCLQ_SFC),

    SFC_H1B5("主胜(1-5)", JclqSfcEnum.H1B5, LotterySubTypeEnum.JCLQ_SFC),

    SFC_H6B10("主胜(6-10)", JclqSfcEnum.H6B10, LotterySubTypeEnum.JCLQ_SFC),

    SFC_H11B15("主胜(11-15)", JclqSfcEnum.H11B15, LotterySubTypeEnum.JCLQ_SFC),

    SFC_H16B20("主胜(16-20)", JclqSfcEnum.H16B20, LotterySubTypeEnum.JCLQ_SFC),

    SFC_H21B25("主胜(21-25)", JclqSfcEnum.H21B25, LotterySubTypeEnum.JCLQ_SFC),

    SFC_H26("主胜(26+)", JclqSfcEnum.H26, LotterySubTypeEnum.JCLQ_SFC),

    SF_WIN("主胜", JclqRaceResultEnum.WIN, LotterySubTypeEnum.JCLQ_SF),

    SF_LOST("主负", JclqRaceResultEnum.LOST, LotterySubTypeEnum.JCLQ_SF), ;
    private int value;

    private LotterySubTypeEnum subType;

    private String message;

    private EnumBase realResultEnum;

    private static final String[] defaultSplits = new String[] { "@", "#", "$" };

    private JclqHhggEnum(String message, EnumBase realResultEnum, LotterySubTypeEnum subType) {
        this.message = message;
        this.subType = subType;
        this.realResultEnum = realResultEnum;
    }

    public static JclqHhggEnum valueOfRealResultEnumValue(EnumBase realResultEnum) {
        for (JclqHhggEnum option : JclqHhggEnum.values()) {
            if (option.realResultEnum == realResultEnum)
                return option;
        }
        return null;
    }



    public static JclqHhggEnum safeValueOfByPalyOption(LotterySubTypeEnum subType, EnumBase realResultEnum) {

        for (JclqHhggEnum option : JclqHhggEnum.values()) {
            if (option.subType == subType && option.realResultEnum == realResultEnum) {
                return option;
            }
        }
        return null;
    }

    public static List<JclqHhggEnum> valueOfSubTypeRealNames(LotterySubTypeEnum subType, List<String> options) {
        List<JclqHhggEnum> returnList = new ArrayList<JclqHhggEnum>();
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

    /**
     * 根据玩法，与真实的枚举名称获得枚举
     *
     * @param subType
     * @param option
     * @return
     */
    public static JclqHhggEnum valueOfSubTypeRealName(LotterySubTypeEnum subType, String option) {
        for (JclqHhggEnum hhggEnum : JclqHhggEnum.values()) {
            if (hhggEnum.subType == subType && StringUtils.equals(hhggEnum.realResultEnum.name(), option)) {
                return hhggEnum;
            }
        }
        return null;
    }




    public static List<JclqHhggEnum> valueOfRealResultEnumValues(List<EnumBase> realResultEnums) {
        List<JclqHhggEnum> returnList = new ArrayList<JclqHhggEnum>();
        for (EnumBase enumBase : realResultEnums) {
            returnList.add(valueOfRealResultEnumValue(enumBase));
        }
        return returnList;

    }

}
