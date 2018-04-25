package com.android.xjq.bean.scheduledetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zhouyi on 2016/7/14 17:28.
 */
public class JclqRankingInfoBean {

    /**
     * 球队ID
     */
    @SerializedName("ti")
    private long teamId;

    /**
     * 球队名
     */
    @SerializedName("tn")
    private String teamName;

    /**
     * 比赛场数
     */
    @SerializedName("mc")
    private int matchCount;

    /**
     * 比赛胜场数
     */
    @SerializedName("mw")
    private int matchWin;

    /**
     * 胜率
     */
    @SerializedName("wr")
    private String winRate;

    /**
     * 比赛负场数
     */
    @SerializedName("ml")
    private int matchLost;

    /**
     * 得分
     */
    @SerializedName("is")
    private int inScore;

    /**
     * 失分
     */
    @SerializedName("ls")
    private int lostScore;

    /**
     * 均得
     */
    @SerializedName("isa")
    private double inScoreAve;

    /**
     * 均失
     */
    @SerializedName("lsa")
    private double lostScoreAve;

    /**
     * 所在联盟
     */
    @SerializedName("div")
    private String division;

    /**
     * 球队排名
     */
    @SerializedName("tr")
    private int teamRank;

    /**
     * 连续场数{正数表示连胜，负数表示连败}
     */
    @SerializedName("hc")
    private int holdCount;

    @Expose
    private String holdCountStr;

    /**
     * 净得分
     */
    @Expose
    private String getScoreAve;

    @Expose
    private String teamRankCount;

    /**
     * 当前队伍是否是主队
     */
    @Expose
    private boolean showHome;

    /**
     * 当前队伍是否是客队
     */
    @Expose
    private boolean showGuest;

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public int getMatchWin() {
        return matchWin;
    }

    public void setMatchWin(int matchWin) {
        this.matchWin = matchWin;
    }

    public String getWinRate() {
        return winRate;
    }

    public void setWinRate(String winRate) {
        this.winRate = winRate;
    }

    public int getInScore() {
        return inScore;
    }

    public void setInScore(int inScore) {
        this.inScore = inScore;
    }

    public int getMatchLost() {
        return matchLost;
    }

    public void setMatchLost(int matchLost) {
        this.matchLost = matchLost;
    }

    public double getLostScoreAve() {
        return lostScoreAve;
    }

    public void setLostScoreAve(double lostScoreAve) {
        this.lostScoreAve = lostScoreAve;
    }

    public double getInScoreAve() {
        return inScoreAve;
    }

    public void setInScoreAve(double inScoreAve) {
        this.inScoreAve = inScoreAve;
    }

    public int getLostScore() {
        return lostScore;
    }

    public void setLostScore(int lostScore) {
        this.lostScore = lostScore;
    }



    public int getTeamRank() {
        return teamRank;
    }

    public void setTeamRank(int teamRank) {
        this.teamRank = teamRank;
    }

    public int getHoldCount() {
        return holdCount;
    }

    public void setHoldCount(int holdCount) {
        this.holdCount = holdCount;
    }

    public String getHoldCountStr() {
        return holdCountStr;
    }

    public void setHoldCountStr(String holdCountStr) {
        this.holdCountStr = holdCountStr;
    }

    public String getGetScoreAve() {
        return getScoreAve;
    }

    public void setGetScoreAve(String getScoreAve) {
        this.getScoreAve = getScoreAve;
    }

    public String getTeamRankCount() {
        return teamRankCount;
    }

    public void setTeamRankCount(String teamRankCount) {
        this.teamRankCount = teamRankCount;
    }

    public boolean isShowHome() {
        return showHome;
    }

    public void setShowHome(boolean showHome) {
        this.showHome = showHome;
    }

    public boolean isShowGuest() {
        return showGuest;
    }

    public void setShowGuest(boolean showGuest) {
        this.showGuest = showGuest;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
