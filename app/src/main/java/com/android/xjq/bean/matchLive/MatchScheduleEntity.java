package com.android.xjq.bean.matchLive;

import com.android.xjq.bean.MatchScheduleBean;
import com.android.xjq.bean.matchschedule.ChannelAreaBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/11/7.
 */

public class MatchScheduleEntity {
    private List<MatchScheduleBean> basketballRaceClientSimpleList;
    private List<MatchScheduleBean> footballRaceClientSimpleList;
    private HashMap<String, List<ChannelAreaBean>> raceIdAndChannelAreaMap;
    private HashMap<String, Long> matchNameAndInnerMatchIdMap;
    private HashMap<String, String> matchGroupAndInnerMatchIdsMap;

    public HashMap<String, Long> getMatchNameAndInnerMatchIdMap() {
        return matchNameAndInnerMatchIdMap;
    }

    public void setMatchNameAndInnerMatchIdMap(HashMap<String, Long> matchNameAndInnerMatchIdMap) {
        this.matchNameAndInnerMatchIdMap = matchNameAndInnerMatchIdMap;
    }


    public HashMap<String, List<ChannelAreaBean>> getRaceIdAndChannelAreaMap() {
        return raceIdAndChannelAreaMap;
    }

    public void setRaceIdAndChannelAreaMap(HashMap<String, List<ChannelAreaBean>> raceIdAndChannelAreaMap) {
        this.raceIdAndChannelAreaMap = raceIdAndChannelAreaMap;
    }

    public HashMap<String, String> getMatchGroupAndInnerMatchIdsMap() {
        return matchGroupAndInnerMatchIdsMap;
    }

    public void setMatchGroupAndInnerMatchIdsMap(HashMap<String, String> matchGroupAndInnerMatchIdsMap) {
        this.matchGroupAndInnerMatchIdsMap = matchGroupAndInnerMatchIdsMap;
    }

    public List<MatchScheduleBean> getBasketballRaceClientSimpleList() {
        return basketballRaceClientSimpleList;
    }

    public void setBasketballRaceClientSimpleList(List<MatchScheduleBean> basketballRaceClientSimpleList) {
        this.basketballRaceClientSimpleList = basketballRaceClientSimpleList;
    }

    public List<MatchScheduleBean> getFootballRaceClientSimpleList() {
        return footballRaceClientSimpleList;
    }

    public void setFootballRaceClientSimpleList(List<MatchScheduleBean> footballRaceClientSimpleList) {
        this.footballRaceClientSimpleList = footballRaceClientSimpleList;
    }


}
