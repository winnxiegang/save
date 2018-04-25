package com.android.residemenu.lt_lib.utils.jjc.jczq;


import com.android.residemenu.lt_lib.enumdata.LotterySelecterEnum;
import com.android.residemenu.lt_lib.enumdata.core.JczqGoalsEnum;
import com.android.residemenu.lt_lib.enumdata.core.JczqHalfFullRaceResultEnum;
import com.android.residemenu.lt_lib.enumdata.core.JczqHhdgEnum;
import com.android.residemenu.lt_lib.enumdata.core.JczqHhggEnum;
import com.android.residemenu.lt_lib.enumdata.core.JczqSpfEnum;
import com.android.residemenu.lt_lib.enumdata.core.RaceResultEnum;
import com.android.residemenu.lt_lib.enumdata.core.jczq.JczqScoreEnum;
import com.android.residemenu.lt_lib.model.jjc.GridData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 竞彩足球期次数据处理
 *
 * @author leslie
 * @version $Id: JczqGridDataUtil.java, v 0.1 2014年12月6日 上午11:37:01 leslie Exp $
 */
public final class JczqGridDataUtil {




    /**
     * 固定单关胜平负GridData数据
     */
    public static void gddg(List<GridData> gridList, JSONObject spfDgSp, JSONObject rqspfDgSp, boolean isRqspfSupport, boolean isSpfSupport)
            throws JSONException {


        int size = (!isRqspfSupport && isSpfSupport) ? 3 : 6;

        int index = (isRqspfSupport && !isSpfSupport) ? 3 : 0;

        for (int k = index; k < size; k++) {

            String title = null;

            GridData gridData = new GridData();

            gridData.setIndex(gridList.size());

            gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_GDDG.name());

            if (k % 3 == 0) {
                title = RaceResultEnum.WIN.message();
            } else if (k % 3 == 1) {
                title = RaceResultEnum.DRAW.message();
            } else {
                title = RaceResultEnum.LOST.message();
            }

            switch (k) {

                case 0:
                    gridData.setOdds(spfDgSp.getString(RaceResultEnum.WIN.name().toLowerCase()));
                    gridData.setOption(JczqHhdgEnum.SPF_WIN.name());
                    gridData.setPlayType(LotterySelecterEnum.JCZQ_SPF.name());
                    break;
                case 1:
                    gridData.setOdds(spfDgSp.getString(RaceResultEnum.DRAW.name().toLowerCase()));
                    gridData.setOption(JczqHhdgEnum.SPF_DRAW.name());
                    gridData.setPlayType(LotterySelecterEnum.JCZQ_SPF.name());
                    break;
                case 2:
                    gridData.setOdds(spfDgSp.getString(RaceResultEnum.LOST.name().toLowerCase()));
                    gridData.setOption(JczqHhdgEnum.SPF_LOST.name());
                    gridData.setPlayType(LotterySelecterEnum.JCZQ_SPF.name());
                    break;

                case 3:
                    gridData.setOdds(rqspfDgSp.getString(RaceResultEnum.WIN.name().toLowerCase()));
                    gridData.setOption(JczqHhdgEnum.RQSPF_WIN.name());
                    gridData.setPlayType(LotterySelecterEnum.JCZQ_RQSPF.name());
                    break;
                case 4:
                    gridData.setOdds(rqspfDgSp.getString(RaceResultEnum.DRAW.name().toLowerCase()));
                    gridData.setOption(JczqHhdgEnum.RQSPF_DRAW.name());
                    gridData.setPlayType(LotterySelecterEnum.JCZQ_RQSPF.name());
                    break;
                case 5:
                    gridData.setOdds(rqspfDgSp.getString(RaceResultEnum.LOST.name().toLowerCase()));
                    gridData.setOption(JczqHhdgEnum.RQSPF_LOST.name());
                    gridData.setPlayType(LotterySelecterEnum.JCZQ_RQSPF.name());
                    break;
            }



            gridData.setTitle(title);

            gridList.add(gridData);

        }
    }


    /**
     * 胜平负和让球胜平负数据
     *
     * @param gridList    每格数据
     * @param spfGgSp
     * @param spfEnable
     * @param rqspfGgSp
     * @param rqspfEnable
     * @throws JSONException
     */
    public static void ht(List<GridData> gridList, JSONObject spfGgSp,
                          boolean spfEnable, JSONObject rqspfGgSp, boolean rqspfEnable)
            throws JSONException {

        for (int k = 0; k < LotterySelecterEnum.JCZQ_RQSPF.getSize(); k++) {

            String title = null;

            GridData gridData = new GridData();

            gridData.setIndex(gridList.size());

            gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_RQSPF.name());

            if (k % 3 == 0) {
                title = RaceResultEnum.WIN.message();
            } else if (k % 3 == 1) {
                title = RaceResultEnum.DRAW.message();
            } else {
                title = RaceResultEnum.LOST.message();
            }

            if (k <= 2) {// 胜平负
                gridData.setEnabled(spfEnable);
                gridData.setPlayType(LotterySelecterEnum.JCZQ_SPF.name());
            } else {// 让球胜平负
                gridData.setEnabled(rqspfEnable);
                gridData.setPlayType(LotterySelecterEnum.JCZQ_RQSPF.name());
            }

            switch (k) {

                case 0:
                    gridData.setOdds(spfGgSp.getString(RaceResultEnum.WIN.name().toLowerCase()));
                    gridData.setOption(JczqSpfEnum.SPF_WIN.name());
                    gridData.setBonusOptimizationOption(JczqHhggEnum.SPF_WIN.name());
                    break;
                case 1:
                    gridData.setOdds(spfGgSp.getString(RaceResultEnum.DRAW.name().toLowerCase()));
                    gridData.setOption(JczqSpfEnum.SPF_DRAW.name());
                    gridData.setBonusOptimizationOption(JczqHhggEnum.SPF_DRAW.name());
                    break;
                case 2:
                    gridData.setOdds(spfGgSp.getString(RaceResultEnum.LOST.name().toLowerCase()));
                    gridData.setOption(JczqSpfEnum.SPF_LOST.name());
                    gridData.setBonusOptimizationOption(JczqHhggEnum.SPF_LOST.name());
                    break;
                case 3:
                    gridData.setOdds(rqspfGgSp.getString(RaceResultEnum.WIN.name().toLowerCase()));
                    gridData.setOption(RaceResultEnum.WIN.name());
                    gridData.setBonusOptimizationOption(JczqHhggEnum.RQSPF_WIN.name());
                    break;
                case 4:
                    gridData.setOdds(rqspfGgSp.getString(RaceResultEnum.DRAW.name().toLowerCase()));
                    gridData.setOption(RaceResultEnum.DRAW.name());
                    gridData.setBonusOptimizationOption(JczqHhggEnum.RQSPF_DRAW.name());
                    break;
                case 5:
                    gridData.setOdds(rqspfGgSp.getString(RaceResultEnum.LOST.name().toLowerCase()));
                    gridData.setOption(RaceResultEnum.LOST.name());
                    gridData.setBonusOptimizationOption(JczqHhggEnum.RQSPF_LOST.name());
                    break;

            }

            gridData.setTitle(title);

            gridList.add(gridData);
        }

    }

    public static void jqs(boolean isGDDG_JQS, List<GridData> gridList,
                           JSONObject jqsGgSp, boolean jqsEnable) throws JSONException {
        for (int k = 0; k < LotterySelecterEnum.JCZQ_JQS.getSize(); k++) {

            GridData gridData = new GridData();
            gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_JQS.name());
            gridData.setPlayType(LotterySelecterEnum.JCZQ_JQS.name());
            gridData.setIndex(gridList.size());
            gridData.setTitle(k + "");

            String key = "sp" + k;

            gridData.setEnabled(jqsEnable);

            switch (k) {

                case 0:
                    gridData.setOdds(jqsGgSp.getString(key));
                    if (isGDDG_JQS) {
                        gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(JczqGoalsEnum.G0).name());
                    } else {
                        gridData.setOption(JczqGoalsEnum.G0.name());
                        gridData.setBonusOptimizationOption(JczqHhggEnum.JQS_G0.name());
                    }

                    break;
                case 1:
                    gridData.setOdds(jqsGgSp.getString(key));
                    if (isGDDG_JQS) {
                        gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(
                                JczqGoalsEnum.G1).name());
                    } else {
                        gridData.setOption(JczqGoalsEnum.G1.name());
                        gridData.setBonusOptimizationOption(JczqHhggEnum.JQS_G1.name());
                    }
                    break;
                case 2:
                    gridData.setOdds(jqsGgSp.getString(key));
                    if (isGDDG_JQS) {
                        gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(
                                JczqGoalsEnum.G2).name());
                    } else {
                        gridData.setOption(JczqGoalsEnum.G2.name());
                        gridData.setBonusOptimizationOption(JczqHhggEnum.JQS_G2.name());
                    }
                    break;
                case 3:
                    gridData.setOdds(jqsGgSp.getString(key));
                    if (isGDDG_JQS) {
                        gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(
                                JczqGoalsEnum.G3).name());
                    } else {
                        gridData.setOption(JczqGoalsEnum.G3.name());
                        gridData.setBonusOptimizationOption(JczqHhggEnum.JQS_G3.name());
                    }
                    break;
                case 4:
                    gridData.setOdds(jqsGgSp.getString(key));
                    if (isGDDG_JQS) {
                        gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(
                                JczqGoalsEnum.G4).name());
                    } else {
                        gridData.setOption(JczqGoalsEnum.G4.name());
                        gridData.setBonusOptimizationOption(JczqHhggEnum.JQS_G4.name());
                    }
                    break;
                case 5:
                    gridData.setOdds(jqsGgSp.getString(key));
                    if (isGDDG_JQS) {
                        gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(
                                JczqGoalsEnum.G5).name());
                    } else {
                        gridData.setOption(JczqGoalsEnum.G5.name());
                        gridData.setBonusOptimizationOption(JczqHhggEnum.JQS_G5.name());
                    }
                    break;

                case 6:
                    gridData.setOdds(jqsGgSp.getString(key));
                    if (isGDDG_JQS) {
                        gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(
                                JczqGoalsEnum.G6).name());
                    } else {
                        gridData.setOption(JczqGoalsEnum.G6.name());
                        gridData.setBonusOptimizationOption(JczqHhggEnum.JQS_G6.name());
                    }
                    break;

                case 7:
                    gridData.setOdds(jqsGgSp.getString(key));
                    if (isGDDG_JQS) {
                        gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(
                                JczqGoalsEnum.G7).name());
                    } else {
                        gridData.setOption(JczqGoalsEnum.G7.name());
                        gridData.setBonusOptimizationOption(JczqHhggEnum.JQS_G7.name());
                    }
                    gridData.setTitle(k + "+");
                    break;

            }
            gridList.add(gridData);
        }

    }

    /**
     * 比分数据
     *
     * @param gridList
     * @param bfGgSp
     * @param bfEnable
     * @throws JSONException
     */
    public static void bf(boolean isGDDG_BF, List<GridData> gridList,
                          JSONObject bfGgSp, boolean bfEnable) throws JSONException {

        JczqScoreEnum[] jczqScoreEnums = JczqScoreEnum.values();

        for (JczqScoreEnum jczqScoreEnum : jczqScoreEnums) {

            String key = "sp" + jczqScoreEnum.homeScore()
                    + jczqScoreEnum.guestScore();

            if (jczqScoreEnum.homeScore() == 9
                    && jczqScoreEnum.guestScore() == 0) {

                key = "winOther";
            } else if (jczqScoreEnum.homeScore() == 9
                    && jczqScoreEnum.guestScore() == 9) {
                key = "drawOther";
            } else if (jczqScoreEnum.homeScore() == 0
                    && jczqScoreEnum.guestScore() == 9) {
                key = "lostOther";
            }

            GridData gridData = new GridData();
            gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_BF.name());
            gridData.setPlayType(LotterySelecterEnum.JCZQ_BF.name());
            gridData.setIndex(gridList.size());
            gridData.setOdds(bfGgSp.getString(key));
            if (isGDDG_BF) {
                gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(jczqScoreEnum).name());
            } else {
                gridData.setOption(jczqScoreEnum.name());
                gridData.setBonusOptimizationOption(JczqHhggEnum.valueOfRealResultEnumValue(jczqScoreEnum).name());
            }

            gridData.setTitle(jczqScoreEnum.message());
            gridData.setEnabled(bfEnable);
            gridList.add(gridData);

        }

    }

    /**
     * @param isGDDG_BQC
     * @param gridList
     * @param bqcGgSp
     * @param bqcEnable
     * @throws JSONException
     */
    public static void bqc(boolean isGDDG_BQC, List<GridData> gridList,
                           JSONObject bqcGgSp, boolean bqcEnable) throws JSONException {

        String ff = bqcGgSp.getString("ff");
        String fp = bqcGgSp.getString("fp");
        String fs = bqcGgSp.getString("fs");
        String pf = bqcGgSp.getString("pf");
        String pp = bqcGgSp.getString("pp");
        String ps = bqcGgSp.getString("ps");
        String sf = bqcGgSp.getString("sf");
        String sp = bqcGgSp.getString("sp");
        String ss = bqcGgSp.getString("ss");



        GridData gridData = new GridData();
        gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setPlayType(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setIndex(gridList.size());
        gridData.setOdds(ss);
        if (isGDDG_BQC) {
            gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(JczqHalfFullRaceResultEnum.WW).name());
        } else {
            gridData.setOption(JczqHalfFullRaceResultEnum.WW.name());
            gridData.setBonusOptimizationOption(JczqHhggEnum.BQC_WW.name());
        }

        gridData.setTitle(JczqHalfFullRaceResultEnum.WW.message());
        gridData.setEnabled(bqcEnable);
        gridList.add(gridData);

        gridData = new GridData();
        gridData.setEnabled(bqcEnable);
        gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setPlayType(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setIndex(gridList.size());
        gridData.setOdds(sp);
        if (isGDDG_BQC) {
            gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(JczqHalfFullRaceResultEnum.WD).name());
        } else {
            gridData.setOption(JczqHalfFullRaceResultEnum.WD.name());
            gridData.setBonusOptimizationOption(JczqHhggEnum.BQC_WD.name());
        }

        gridData.setTitle(JczqHalfFullRaceResultEnum.WD.message());
        gridList.add(gridData);

        gridData = new GridData();
        gridData.setEnabled(bqcEnable);
        gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setPlayType(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setIndex(gridList.size());
        gridData.setOdds(sf);
        if (isGDDG_BQC) {
            gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(JczqHalfFullRaceResultEnum.WL).name());
        } else {
            gridData.setOption(JczqHalfFullRaceResultEnum.WL.name());
            gridData.setBonusOptimizationOption(JczqHhggEnum.BQC_WL.name());
        }

        gridData.setTitle(JczqHalfFullRaceResultEnum.WL.message());
        gridList.add(gridData);

        gridData = new GridData();
        gridData.setEnabled(bqcEnable);
        gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setPlayType(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setIndex(gridList.size());
        gridData.setOdds(ps);
        if (isGDDG_BQC) {
            gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(JczqHalfFullRaceResultEnum.DW).name());
        } else {
            gridData.setOption(JczqHalfFullRaceResultEnum.DW.name());
            gridData.setBonusOptimizationOption(JczqHhggEnum.BQC_DW.name());
        }

        gridData.setTitle(JczqHalfFullRaceResultEnum.DW.message());
        gridList.add(gridData);

        gridData = new GridData();
        gridData.setEnabled(bqcEnable);
        gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setPlayType(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setIndex(gridList.size());
        gridData.setOdds(pp);
        if (isGDDG_BQC) {
            gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(JczqHalfFullRaceResultEnum.DD).name());
        } else {
            gridData.setOption(JczqHalfFullRaceResultEnum.DD.name());
            gridData.setBonusOptimizationOption(JczqHhggEnum.BQC_DD.name());
        }

        gridData.setTitle(JczqHalfFullRaceResultEnum.DD.message());
        gridList.add(gridData);

        gridData = new GridData();
        gridData.setEnabled(bqcEnable);
        gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setPlayType(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setIndex(gridList.size());
        gridData.setOdds(pf);
        if (isGDDG_BQC) {
            gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(JczqHalfFullRaceResultEnum.DL).name());

        } else {
            gridData.setOption(JczqHalfFullRaceResultEnum.DL.name());
            gridData.setBonusOptimizationOption(JczqHhggEnum.BQC_DL.name());
        }

        gridData.setTitle(JczqHalfFullRaceResultEnum.DL.message());
        gridList.add(gridData);

        gridData = new GridData();
        gridData.setEnabled(bqcEnable);
        gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setPlayType(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setIndex(gridList.size());
        gridData.setOdds(fs);
        if (isGDDG_BQC) {
            gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(JczqHalfFullRaceResultEnum.LW).name());
        } else {
            gridData.setOption(JczqHalfFullRaceResultEnum.LW.name());
            gridData.setBonusOptimizationOption(JczqHhggEnum.BQC_LW.name());
        }

        gridData.setTitle(JczqHalfFullRaceResultEnum.LW.message());
        gridList.add(gridData);

        gridData = new GridData();
        gridData.setEnabled(bqcEnable);
        gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setPlayType(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setIndex(gridList.size());
        gridData.setOdds(fp);
        if (isGDDG_BQC) {
            gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(JczqHalfFullRaceResultEnum.LD).name());
        } else {
            gridData.setOption(JczqHalfFullRaceResultEnum.LD.name());
            gridData.setBonusOptimizationOption(JczqHhggEnum.BQC_LD.name());
        }

        gridData.setTitle(JczqHalfFullRaceResultEnum.LD.message());
        gridList.add(gridData);

        gridData = new GridData();
        gridData.setEnabled(bqcEnable);
        gridData.setSelecterEnum(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setPlayType(LotterySelecterEnum.JCZQ_BQC.name());
        gridData.setIndex(gridList.size());
        gridData.setOdds(ff);
        if (isGDDG_BQC) {
            gridData.setOption(JczqHhdgEnum.valueOfRealResultEnumValue(JczqHalfFullRaceResultEnum.LL).name());
        } else {
            gridData.setOption(JczqHalfFullRaceResultEnum.LL.name());
            gridData.setBonusOptimizationOption(JczqHhggEnum.BQC_LL.name());
        }
        gridData.setTitle(JczqHalfFullRaceResultEnum.LL.message());
        gridList.add(gridData);

    }

}
