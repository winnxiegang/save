/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core;


import com.android.residemenu.lt_lib.enumdata.EnumBase;

import com.android.residemenu.lt_lib.enumdata.core.jclq.JclqHhggPlayTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.jclq.JclqRfPlayTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.jczq.JclqDxPlayTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.jczq.JclqSfPlayTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.jczq.JclqSfcPlayTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.jczq.JczqBfPlayTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.jczq.JczqBqcPlayTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.jczq.JczqHhggPlayTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.jczq.JczqJqsPlayTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.jczq.JczqRqspfPlayTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.jczq.JczqSpfPlayTypeEnum;
import com.android.residemenu.lt_lib.enumdata.core.zucai.ZuCaiPlayTypeEnum;
import com.android.residemenu.lt_lib.utils.lang.StringUtils;

/**
 * 彩种类型
 *
 * @author chenbug
 * @version $Id: LotterySubTypeEnum, v 0.1 2011-2-10 下午08:23:34 chenbug Exp $
 */
public enum LotterySubTypeEnum implements EnumBase {

    SAMPLE_TC("体彩样票", null, null, false),

    ZUCAI_SFC("胜负彩", ZuCaiPlayTypeEnum.class, null, true),

    ZUCAI_JQC("进球彩", ZuCaiPlayTypeEnum.class, null, true),

    ZUCAI_BQC("半全场", ZuCaiPlayTypeEnum.class, null, true),

    ZUCAI_R9("任9", ZuCaiPlayTypeEnum.class, null, true),

    JCZQ_RQSPF("让球胜平负", JczqRqspfPlayTypeEnum.class, null, false),

    JCZQ_SPF("胜平负", JczqSpfPlayTypeEnum.class, null, false),

    JCZQ_BF("比分", JczqBfPlayTypeEnum.class, null, false),

    JCZQ_GDDG("固定单关", null, null, false),

    JCZQ_HHDG("固定单关", null, null, false),

    JCZQ_JQS("进球数", JczqJqsPlayTypeEnum.class, null, false),

    JCZQ_BQC("半全场", JczqBqcPlayTypeEnum.class, null, false),

    JCZQ_HHGG("混合过关", JczqHhggPlayTypeEnum.class, null, false),

    // 冠亚军
    JCZQ_GYJ("冠亚军", JczqGyjPlayTypeEnum.class, null, true),
    // 冠军
    JCZQ_GJ("冠军", null, null, true),

    JCLQ_SFC("胜分差", JclqSfcPlayTypeEnum.class, null, false),

    JCLQ_DX("大小", JclqDxPlayTypeEnum.class, null, false),

    JCLQ_SF("胜负", JclqSfPlayTypeEnum.class, null, false),

    JCLQ_RF("让分胜负", JclqRfPlayTypeEnum.class, null, false),

    JCLQ_HHGG("混合过关", JclqHhggPlayTypeEnum.class, null, false),;

    private String message;

    /**
     * 玩法大类
     */
    private Class<? extends Enum<?>> playTypeEnumClass;

    /**
     * 玩法子类
     */
    private Class<? extends Enum<?>> playSubTypeEnumClass;

    /**
     * 是否自有期次
     */
    private boolean hasSelfIssue;

    private LotterySubTypeEnum(String message, Class<? extends Enum<?>> playTypeEnumClass,
                               Class<? extends Enum<?>> playSubTypeEnumClass, boolean hasSelfIssue) {
        this.message = message;
        this.playTypeEnumClass = playTypeEnumClass;
        this.playSubTypeEnumClass = playSubTypeEnumClass;
        this.hasSelfIssue = hasSelfIssue;

    }

    public boolean hasSelfIssue() {
        return hasSelfIssue;
    }

    public static LotterySubTypeEnum messageOf(String message) {
        for (LotterySubTypeEnum enumObj : LotterySubTypeEnum.values()) {
            if (StringUtils.equals(enumObj.message(), message)) {
                return enumObj;
            }
        }
        return null;
    }

    public Class<? extends Enum<?>> playTypeEnumClass() {
        return playTypeEnumClass;
    }

    public Class<? extends Enum<?>> playSubTypeEnumClass() {
        return playSubTypeEnumClass;
    }

    public static final LotterySubTypeEnum safeValueOf(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        try {
            return LotterySubTypeEnum.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.bench.common.enums.EnumBase#message()
     */
    public String message() {

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
