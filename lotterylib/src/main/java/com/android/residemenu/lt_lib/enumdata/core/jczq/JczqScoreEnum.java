/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.android.residemenu.lt_lib.enumdata.core.jczq;


import com.android.residemenu.lt_lib.enumdata.EnumBase;
import com.android.residemenu.lt_lib.utils.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * 竞猜足球比分枚举,注意,value1、value2、message值不能随意修改
 *
 * @author chenbug
 *
 * @version $Id: JczqScoreEnum.java, v 0.1 2011-3-2 下午03:28:29 chenbug Exp $
 */
public enum JczqScoreEnum implements EnumBase {

    S_1_0(1, 0, "1:0"),

    S_2_0(2, 0, "2:0"),

    S_2_1(2, 1, "2:1"),

    S_3_0(3, 0, "3:0"),

    S_3_1(3, 1, "3:1"),

    S_3_2(3, 2, "3:2"),

    S_4_0(4, 0, "4:0"),

    S_4_1(4, 1, "4:1"),

    S_4_2(4, 2, "4:2"),

    S_5_0(5, 0, "5:0"),

    S_5_1(5, 1, "5:1"),

    S_5_2(5, 2, "5:2"),

    S_W_O(9, 0, "胜其他"),

    S_0_0(0, 0, "0:0"),

    S_1_1(1, 1, "1:1"),

    S_2_2(2, 2, "2:2"),

    S_3_3(3, 3, "3:3"),

    S_D_O(9, 9, "平其他"),

    S_0_1(0, 1, "0:1"),

    S_0_2(0, 2, "0:2"),

    S_1_2(1, 2, "1:2"),

    S_0_3(0, 3, "0:3"),

    S_1_3(1, 3, "1:3"),

    S_2_3(2, 3, "2:3"),

    S_0_4(0, 4, "0:4"),

    S_1_4(1, 4, "1:4"),

    S_2_4(2, 4, "2:4"),

    S_0_5(0, 5, "0:5"),

    S_1_5(1, 5, "1:5"),

    S_2_5(2, 5, "2:5"),

    S_L_O(0, 9, "负其他"), ;



    private int homeScore;

    private int guestScore;

    private String message;

    private JczqScoreEnum(int homeScore, int guestScore, String message) {
        this.homeScore = homeScore;
        this.guestScore = guestScore;
        this.message = message;
    }

    /**
     * @param homeScore
     * @param guestScore
     * @return
     */
    public static JczqScoreEnum scoreOf(int homeScore, int guestScore) {
        for (JczqScoreEnum scoreEnum : JczqScoreEnum.values()) {
            if (scoreEnum.homeScore == homeScore && scoreEnum.guestScore == guestScore) {
                return scoreEnum;
            }
        }

        return null;
    }

    /**
     * 根据比分获取枚举<br>
     * 11=S_1_1<br>
     * 09=S_L_O<br>
     *
     * @param score
     * @return
     */
    public static JczqScoreEnum scoreOf(String score) {
        int homeScore = Integer.parseInt(score.substring(0, 1));
        int guestScore = Integer.parseInt(score.substring(1, 2));
        return scoreOf(homeScore, guestScore);
    }

    /**
     * @param message
     * @return
     */
    public static JczqScoreEnum messageOf(String message) {
        for (JczqScoreEnum scoreEnum : JczqScoreEnum.values()) {
            if (StringUtils.equals(scoreEnum.message(), message)) {
                return scoreEnum;
            }
        }

        //log.error("无法识别的message=" + message);
        return null;
    }

    public static JczqScoreEnum safeValueOf(String name) {
        for (JczqScoreEnum jczqScoreEnum : JczqScoreEnum.values()) {
            if (StringUtils.equals(jczqScoreEnum.name(), name)) {
                return jczqScoreEnum;
            }
        }

        //log.error("无法识别的name=" + name);
        return null;
    }

    public static List<JczqScoreEnum> returnWinEnums() {
        List<JczqScoreEnum> returnList = new ArrayList<JczqScoreEnum>();
        for (JczqScoreEnum jczqScoreEnum : JczqScoreEnum.values()) {
            if(jczqScoreEnum.homeScore() > jczqScoreEnum.guestScore()){
                returnList.add(jczqScoreEnum);
            }
        }
        return returnList;
    }
    public static List<JczqScoreEnum> returnLostEnums() {
        List<JczqScoreEnum> returnList = new ArrayList<JczqScoreEnum>();
        for (JczqScoreEnum jczqScoreEnum : JczqScoreEnum.values()) {
            if(jczqScoreEnum.homeScore() < jczqScoreEnum.guestScore()){
                returnList.add(jczqScoreEnum);
            }
        }
        return returnList;
    }

    public static List<JczqScoreEnum> returnDrawEnums() {
        List<JczqScoreEnum> returnList = new ArrayList<JczqScoreEnum>();
        for (JczqScoreEnum jczqScoreEnum : JczqScoreEnum.values()) {
            if (jczqScoreEnum.homeScore() == jczqScoreEnum.guestScore()) {
                returnList.add(jczqScoreEnum);
            }
        }
        return returnList;
    }

    public int homeScore() {
        return homeScore;
    }

    public int guestScore() {
        return guestScore;
    }

    public String message() {
        // TODO Auto-generated method stub
        return message;
    }

    public Number value() {
        // TODO Auto-generated method stub
        return null;
    }

    public String standardMessage() {
        return message;
    }

}
