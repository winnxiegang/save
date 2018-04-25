package com.android.xjq.bean;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.xjq.bean.matchschedule.ChannelAreaBean;
import com.android.xjq.presenters.CommonReqHelper;
import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.List;

/**
 * Created by ajiao on 2017/11/2 0002.
 */

public class MatchScheduleBean {

    /**
     * fullGuestScore : -1
     * fullHomeScore : -1
     * gmtStart : 2017-11-06 10:30:00
     * guestScoreSection1 : 0
     * guestScoreSection2 : 0
     * guestScoreSection3 : 0
     * guestScoreSection4 : 0
     * guestTeamName : 灰熊
     * halfGuestScore : -1
     * halfHomeScore : -1
     * homeScoreSection1 : 0
     * homeScoreSection2 : 0
     * homeScoreSection3 : 0
     * homeScoreSection4 : 0
     * homeTeamName : 湖人
     * id : 2650349723507890980015447400
     * innerGuestTeamId : 6812
     * innerHomeTeamId : 6863
     * innerMatchId : 401
     * innerRaceId : 434381
     * matchName : NBA
     * raceStatus : {"message":"未赛","name":"WAIT"}
     * sectionCount : 4
     */
    public static final String TYPE_HOME = "TYPE_HOME";
    public static final String TYPE_GUEST = "TYPE_GUEST";
    public static final String HEAD_FOOT_URL = "http://ft.huored.net/team/logo/";
    public static final String HEAD_BASKET_URL = "http://bt.huored.net/team/logo/";
    //type=h是主队，type=g是客队
    public static final String FOOTER_HOME_URL = ".resource?type=h";
    public static final String FOOTER_GUEST_URL = ".resource?type=g";

    private String homeIconUrl;
    private String guestIconUrl;

    private boolean moreChannelArea;
    private String color;
    private boolean existedOtherGameBoard;
    private int raceIdAndChannelAreaCount;
    private boolean saleCount;
    private int fullGuestScore;
    private int fullHomeScore;
    private String gmtStart;
    private int guestScoreSection1;
    private int guestScoreSection2;
    private int guestScoreSection3;
    private int guestScoreSection4;
    private String guestTeamName;
    private int halfGuestScore;
    private int halfHomeScore;
    private int homeScoreSection1;
    private int homeScoreSection2;
    private int homeScoreSection3;
    private int homeScoreSection4;
    private String homeTeamName;
    private String id;
    private int innerGuestTeamId;
    private int innerHomeTeamId;
    private int innerMatchId;
    private int innerRaceId;
    private String matchName;
    private RaceBean raceStatus;
    private int sectionCount;
    private String leaveTime = "";
    private String serverCurrentTime = "";

    //足球 红黄牌
    private int hr;
    private int hy;
    private int gr;
    private int gy;
    /**
     * 上半场开始时间
     */
    private String fst;

    /**
     * 下半场开始时间
     */
    private String sst;

    @Expose
    //节目信息
    private List<ChannelAreaBean> channelAreaBeanList;

    public boolean isMoreChannelArea() {
        return moreChannelArea;
    }

    public void setMoreChannelArea(boolean moreChannelArea) {
        this.moreChannelArea = moreChannelArea;
    }
    public String getHomeIconUrl() {
        return homeIconUrl;
    }

    public void setHomeIconUrl(String homeIconUrl) {
        this.homeIconUrl = homeIconUrl;
    }

    public String getGuestIconUrl() {
        return guestIconUrl;
    }

    public void setGuestIconUrl(String guestIconUrl) {
        this.guestIconUrl = guestIconUrl;
    }


    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getHy() {
        return hy;
    }

    public void setHy(int hy) {
        this.hy = hy;
    }

    public int getGr() {
        return gr;
    }

    public void setGr(int gr) {
        this.gr = gr;
    }

    public int getGy() {
        return gy;
    }

    public void setGy(int gy) {
        this.gy = gy;
    }

    public String getFst() {
        return fst;
    }

    public void setFst(String fst) {
        this.fst = fst;
    }

    public String getSst() {
        return sst;
    }

    public void setSst(String sst) {
        this.sst = sst;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<ChannelAreaBean> getChannelAreaBeanList() {
        return channelAreaBeanList;
    }

    public void setChannelAreaBeanList(List<ChannelAreaBean> channelAreaBeanList) {
        this.channelAreaBeanList = channelAreaBeanList;
    }

    public boolean isExistedOtherGameBoard() {
        return existedOtherGameBoard;
    }

    public void setExistedOtherGameBoard(boolean existedOtherGameBoard) {
        this.existedOtherGameBoard = existedOtherGameBoard;
    }

    public int getRaceIdAndChannelAreaCount() {
        return raceIdAndChannelAreaCount;
    }

    public void setRaceIdAndChannelAreaCount(int raceIdAndChannelAreaCount) {
        this.raceIdAndChannelAreaCount = raceIdAndChannelAreaCount;
    }

    public boolean isSaleCount() {
        return saleCount;
    }

    public void setSaleCount(boolean saleCount) {
        this.saleCount = saleCount;
    }

    public String getServerCurrentTime() {
        return serverCurrentTime;
    }

    public void setServerCurrentTime(String serverCurrentTime) {
        this.serverCurrentTime = serverCurrentTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }
    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getGmtFormatedTime (String time) {
        Date date = TimeUtils.stringToDate(time, "");
        return TimeUtils.dateToString2(date, TimeUtils.MATCH_FORMAT);
    }

    public int getFullGuestScore() {
        return fullGuestScore;
    }

    public void setFullGuestScore(int fullGuestScore) {
        this.fullGuestScore = fullGuestScore;
    }

    public int getFullHomeScore() {
        return fullHomeScore;
    }

    public void setFullHomeScore(int fullHomeScore) {
        this.fullHomeScore = fullHomeScore;
    }

    public String getGmtStart() {
        return gmtStart;
    }

    public void setGmtStart(String gmtStart) {
        this.gmtStart = gmtStart;
    }

    public int getGuestScoreSection1() {
        return guestScoreSection1;
    }

    public void setGuestScoreSection1(int guestScoreSection1) {
        this.guestScoreSection1 = guestScoreSection1;
    }

    public int getGuestScoreSection2() {
        return guestScoreSection2;
    }

    public void setGuestScoreSection2(int guestScoreSection2) {
        this.guestScoreSection2 = guestScoreSection2;
    }

    public int getGuestScoreSection3() {
        return guestScoreSection3;
    }

    public void setGuestScoreSection3(int guestScoreSection3) {
        this.guestScoreSection3 = guestScoreSection3;
    }

    public int getGuestScoreSection4() {
        return guestScoreSection4;
    }

    public void setGuestScoreSection4(int guestScoreSection4) {
        this.guestScoreSection4 = guestScoreSection4;
    }

    public String getGuestTeamName() {
        return guestTeamName;
    }

    public void setGuestTeamName(String guestTeamName) {
        this.guestTeamName = guestTeamName;
    }

    public int getHalfGuestScore() {
        return halfGuestScore;
    }

    public void setHalfGuestScore(int halfGuestScore) {
        this.halfGuestScore = halfGuestScore;
    }

    public int getHalfHomeScore() {
        return halfHomeScore;
    }

    public void setHalfHomeScore(int halfHomeScore) {
        this.halfHomeScore = halfHomeScore;
    }

    public int getHomeScoreSection1() {
        return homeScoreSection1;
    }

    public void setHomeScoreSection1(int homeScoreSection1) {
        this.homeScoreSection1 = homeScoreSection1;
    }

    public int getHomeScoreSection2() {
        return homeScoreSection2;
    }

    public void setHomeScoreSection2(int homeScoreSection2) {
        this.homeScoreSection2 = homeScoreSection2;
    }

    public int getHomeScoreSection3() {
        return homeScoreSection3;
    }

    public void setHomeScoreSection3(int homeScoreSection3) {
        this.homeScoreSection3 = homeScoreSection3;
    }

    public int getHomeScoreSection4() {
        return homeScoreSection4;
    }

    public void setHomeScoreSection4(int homeScoreSection4) {
        this.homeScoreSection4 = homeScoreSection4;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getInnerGuestTeamId() {
        return innerGuestTeamId;
    }

    public void setInnerGuestTeamId(int innerGuestTeamId) {
        this.innerGuestTeamId = innerGuestTeamId;
    }

    public int getInnerHomeTeamId() {
        return innerHomeTeamId;
    }

    public void setInnerHomeTeamId(int innerHomeTeamId) {
        this.innerHomeTeamId = innerHomeTeamId;
    }

    public int getInnerMatchId() {
        return innerMatchId;
    }

    public void setInnerMatchId(int innerMatchId) {
        this.innerMatchId = innerMatchId;
    }

    public int getInnerRaceId() {
        return innerRaceId;
    }

    public void setInnerRaceId(int innerRaceId) {
        this.innerRaceId = innerRaceId;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public RaceBean getRaceStatus() {
        return raceStatus;
    }

    public void setRaceStatus(RaceBean raceStatus) {
        this.raceStatus = raceStatus;
    }

    public int getSectionCount() {
        return sectionCount;
    }

    public void setSectionCount(int sectionCount) {
        this.sectionCount = sectionCount;
    }

    public static class RaceBean {
        /**
         * message : 未赛
         * name : WAIT
         */
        private String message;
        private String name;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public static String getTeamIconUrl(int id, String type, int ballType) {
        String header = ballType == CommonReqHelper.MATCH_TYPE_FOOTBALL ? HEAD_FOOT_URL : HEAD_BASKET_URL;
        String footer = TYPE_HOME.endsWith(type) ? FOOTER_HOME_URL : FOOTER_GUEST_URL;
        return header + id + footer;
    }
}
