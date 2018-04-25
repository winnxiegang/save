package com.android.xjq.bean;

import com.android.banana.commlib.bean.NormalObject;

/**
 * Created by zhouyi on 2017/1/12 17:04.
 */

public class JcSnapshotBean {

    private String gameShortName;

    private int innerMatchId;

    private String guestTeamShortName;

    private String homeTeamShortName;

    private int fullGuestScore;

    private String startDate;

    private String issueNo;

    private int concede;

    private String gameNo;

    private boolean prized;

    private int innerHomeTeamId;

    private int halfHomeScore;

    private int fullHomeScore;

    private String guestTeamName;

    private String id;

    private int halfGuestScore;

    private int innerGuestTeamId;

    private boolean late;

    private String homeTeamName;

    private String bizId;

    private String gameName;

    private NormalObject status;

    private int innerRaceId;

    private String matchType;

    private SubjectTag subjectTag;

    private String raceId;

    private String raceAwardResult;//竞彩大学新增，新鲜事会返回

    public SubjectTag getSubjectTag() {
        return subjectTag;
    }

    public void setSubjectTag(SubjectTag subjectTag) {
        this.subjectTag = subjectTag;
    }

    public String getGameShortName() {
        return gameShortName;
    }

    public void setGameShortName(String gameShortName) {
        this.gameShortName = gameShortName;
    }

    public int getInnerMatchId() {
        return innerMatchId;
    }


    public String getRaceAwardResult() {
        return raceAwardResult;
    }

    public void setRaceAwardResult(String raceAwardResult) {
        this.raceAwardResult = raceAwardResult;
    }

    public void setInnerMatchId(int innerMatchId) {
        this.innerMatchId = innerMatchId;
    }

    public String getGuestTeamShortName() {
        return guestTeamShortName;
    }

    public void setGuestTeamShortName(String guestTeamShortName) {
        this.guestTeamShortName = guestTeamShortName;
    }

    public String getHomeTeamShortName() {
        return homeTeamShortName;
    }

    public void setHomeTeamShortName(String homeTeamShortName) {
        this.homeTeamShortName = homeTeamShortName;
    }

    public int getFullGuestScore() {
        return fullGuestScore;
    }

    public void setFullGuestScore(int fullGuestScore) {
        this.fullGuestScore = fullGuestScore;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }

    public String getGameNo() {
        return gameNo;
    }

    public void setGameNo(String gameNo) {
        this.gameNo = gameNo;
    }

    public int getConcede() {
        return concede;
    }

    public void setConcede(int concede) {
        this.concede = concede;
    }

    public boolean isPrized() {
        return prized;
    }

    public void setPrized(boolean prized) {
        this.prized = prized;
    }

    public int getHalfHomeScore() {
        return halfHomeScore;
    }

    public void setHalfHomeScore(int halfHomeScore) {
        this.halfHomeScore = halfHomeScore;
    }

    public int getFullHomeScore() {
        return fullHomeScore;
    }

    public void setFullHomeScore(int fullHomeScore) {
        this.fullHomeScore = fullHomeScore;
    }

    public int getInnerHomeTeamId() {
        return innerHomeTeamId;
    }

    public void setInnerHomeTeamId(int innerHomeTeamId) {
        this.innerHomeTeamId = innerHomeTeamId;
    }

    public String getGuestTeamName() {
        return guestTeamName;
    }

    public void setGuestTeamName(String guestTeamName) {
        this.guestTeamName = guestTeamName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHalfGuestScore() {
        return halfGuestScore;
    }

    public void setHalfGuestScore(int halfGuestScore) {
        this.halfGuestScore = halfGuestScore;
    }

    public int getInnerGuestTeamId() {
        return innerGuestTeamId;
    }

    public void setInnerGuestTeamId(int innerGuestTeamId) {
        this.innerGuestTeamId = innerGuestTeamId;
    }

    public boolean isLate() {
        return late;
    }

    public void setLate(boolean late) {
        this.late = late;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public NormalObject getStatus() {
        return status;
    }

    public void setStatus(NormalObject status) {
        this.status = status;
    }

    public int getInnerRaceId() {
        return innerRaceId;
    }

    public void setInnerRaceId(int innerRaceId) {
        this.innerRaceId = innerRaceId;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }
}
