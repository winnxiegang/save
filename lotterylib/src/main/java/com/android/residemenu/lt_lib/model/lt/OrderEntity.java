package com.android.residemenu.lt_lib.model.lt;


import com.android.residemenu.lt_lib.model.jjc.EntityOption;
import com.android.residemenu.lt_lib.model.jjc.PrizedResults;
import com.android.residemenu.lt_lib.model.jjc.RaceData;

import java.util.Map;

/**
 * 投注内容
 *
 * @author leslie
 * @version $Id: OrderDetails.java, v 0.1 2014年5月15日 下午2:44:57 leslie Exp $
 */
public class OrderEntity extends RaceData {

    private boolean dan;

    /**
     * 该场比赛是否开奖
     */
    private boolean prized;

    /**
     * 彩果
     */
    private PrizedResults prizedResults;

    /**
     * 投注选项
     */
    private Map<String, Map<String, EntityOption>> entityOptionMap;

   /* *//**
     * 比赛状态
     *//*
    private RaceStatus raceStatus;*/

    /**
     * 让球数
     */
    private int concede;

    /**
     * 冠亚军彩果
     */
    private String raceResult;

    /**
     * 赔率
     */
    private String odds;

    private boolean showRed;

    public OrderEntity() {

    }

    public boolean isDan() {
        return dan;
    }

    public void setDan(boolean dan) {
        this.dan = dan;
    }

    public boolean isPrized() {
        return prized;
    }

    public void setPrized(boolean prized) {
        this.prized = prized;
    }

    public PrizedResults getPrizedResults() {
        return prizedResults;
    }

    public void setPrizedResults(PrizedResults prizedResults) {
        this.prizedResults = prizedResults;
    }

    // public RaceStatus getRaceStatus() {
    // return raceStatus;
    // }
    //
    // public void setRaceStatus(RaceStatus raceStatus) {
    // this.raceStatus = raceStatus;
    // }

    public int getConcede() {
        return concede;
    }

    public void setConcede(int concede) {
        this.concede = concede;
    }

    public Map<String, Map<String, EntityOption>> getEntityOptionMap() {
        return entityOptionMap;
    }

    public void setEntityOptionMap(Map<String, Map<String, EntityOption>> entityOptionMap) {
        this.entityOptionMap = entityOptionMap;
    }

    public String getRaceResult() {
        return raceResult;
    }

    public void setRaceResult(String raceResult) {
        this.raceResult = raceResult;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public boolean isShowRed() {
        return showRed;
    }

    public void setShowRed(boolean showRed) {
        this.showRed = showRed;
    }
}
