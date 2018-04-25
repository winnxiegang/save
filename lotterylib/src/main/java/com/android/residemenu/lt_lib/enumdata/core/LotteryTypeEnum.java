/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core;

import com.android.residemenu.lt_lib.enumdata.EnumBase;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * 彩种大类
 *
 * @author chenbug
 * @version $Id: LotteryTypeEnum.java, v 0.1 2011-2-10 下午08:23:34 chenbug Exp $
 */
public enum LotteryTypeEnum implements EnumBase {

    SAMPLE("所有彩种的样票", false, false, new LotterySubTypeEnum[]{LotterySubTypeEnum.SAMPLE_TC}, null, null),


    ZUCAI("足彩", true, true, new LotterySubTypeEnum[]{LotterySubTypeEnum.ZUCAI_BQC,
            LotterySubTypeEnum.ZUCAI_JQC, LotterySubTypeEnum.ZUCAI_R9, LotterySubTypeEnum.ZUCAI_SFC}, null,
            null),

    ZC("足彩", true, true, new LotterySubTypeEnum[]{LotterySubTypeEnum.ZUCAI_BQC,
            LotterySubTypeEnum.ZUCAI_JQC, LotterySubTypeEnum.ZUCAI_R9, LotterySubTypeEnum.ZUCAI_SFC}, null,
            null),

    JCZQ("竞彩足球", false, false, new LotterySubTypeEnum[]{LotterySubTypeEnum.JCZQ_BF,
            LotterySubTypeEnum.JCZQ_BQC, LotterySubTypeEnum.JCZQ_JQS, LotterySubTypeEnum.JCZQ_RQSPF,
            LotterySubTypeEnum.JCZQ_HHGG, LotterySubTypeEnum.JCZQ_SPF, LotterySubTypeEnum.JCZQ_GYJ}, null,
            null),

    JCLQ("竞彩篮球", false, false, new LotterySubTypeEnum[]{LotterySubTypeEnum.JCLQ_SF,
            LotterySubTypeEnum.JCLQ_SFC, LotterySubTypeEnum.JCLQ_RF, LotterySubTypeEnum.JCLQ_DX,
            LotterySubTypeEnum.JCLQ_HHGG}, null, null);

    private String message;

    private LotterySubTypeEnum[] subTypes;

    /**
     * 玩法大类
     */
    private Class<? extends Enum<?>> playTypeEnumClass;

    /**
     * 玩法子类
     */
    private Class<? extends Enum<?>> playSubTypeEnumClass;

    /**
     * 是否所有的彩种子类都自有期次
     */
    private boolean allLotterySubTypeHasSelfIssue;

    /**
     * 发送第三方订单时，是否检查期次
     */
    private boolean sendDrawOrdersCheckIssue;

    private LotteryTypeEnum(String message, boolean allLotterySubTypeHasSelfIssue,
                            boolean sendDrawOrdersCheckIssue, LotterySubTypeEnum[] subTypes,
                            Class<? extends Enum<?>> playTypeEnumClass, Class<? extends Enum<?>> playSubTypeEnumClass) {
        this.message = message;
        this.allLotterySubTypeHasSelfIssue = allLotterySubTypeHasSelfIssue;
        this.subTypes = subTypes;
        this.playTypeEnumClass = playTypeEnumClass;
        this.playSubTypeEnumClass = playSubTypeEnumClass;
        this.sendDrawOrdersCheckIssue = sendDrawOrdersCheckIssue;

    }

    public Class<? extends Enum<?>> playTypeEnumClass() {
        return playTypeEnumClass;
    }

    public Class<? extends Enum<?>> playSubTypeEnumClass() {
        return playSubTypeEnumClass;
    }

    public static LotteryTypeEnum messageOf(String message) {
        for (LotteryTypeEnum enumObj : LotteryTypeEnum.values()) {
            if (StringUtils.equals(enumObj.message(), message)) {
                return enumObj;
            }
        }
        return null;
    }

    /**
     *
     */
    public boolean allSubTypeHasSelfIssue() {
        return allLotterySubTypeHasSelfIssue;
    }

    /**
     *
     */
    public boolean hasSubTypes() {
        return ArrayUtils.getLength(subTypes) > 0;
    }

    public boolean sendDrawOrdersCheckIssue() {
        return sendDrawOrdersCheckIssue;
    }

    public static final LotteryTypeEnum safeValueOf(String name) {
        try {
            return LotteryTypeEnum.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }

    public LotterySubTypeEnum[] subTypes() {
        return this.subTypes;
    }

    /**
     * @param subType
     * @return
     */
    public static LotteryTypeEnum findBySubType(LotterySubTypeEnum subType) {
        for (LotteryTypeEnum enumObj : LotteryTypeEnum.values()) {
            if (ArrayUtils.contains(enumObj.subTypes, subType)) {
                return enumObj;
            }
        }
        return null;
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

        return null;
    }

}
