package com.android.banana.commlib.bean.liveScoreBean;

import com.android.banana.commlib.bean.NormalObject;
import com.google.gson.annotations.SerializedName;

/**
 * 竞彩篮球比分直播数据
 * Created by zhouyi on 2016/7/18 17:45.
 */
public class JclqDynamicData {

    /**
     * 比赛ID
     */
    @SerializedName("id")
    private long id;


    /**
     * 比赛状态
     */
    @SerializedName("status")
    private NormalObject status;

    /**
     * 主队比分
     */
    @SerializedName("hs")
    private int homeScore;

    /**
     * 客队比分
     */
    @SerializedName("gs")
    private int guestScore;

    /**
     * 主队上半场比分
     */
    @SerializedName("hhs")
    private int halfHomeScore;

    /**
     * 客队上半场比分
     */
    @SerializedName("hgs")
    private int halfGuestScore;

    /**
     * 第1节主队分
     */
    @SerializedName("hss1")
    private String homeScoreSection1;

    /**
     * 第2节主队分
     */
    @SerializedName("hss2")
    private String homeScoreSection2;

    /**
     * 第3节主队分
     */
    @SerializedName("hss3")
    private String homeScoreSection3;

    /**
     * 第4节主队分
     */
    @SerializedName("hss4")
    private String homeScoreSection4;

    /**
     * 第1节客队分
     */
    @SerializedName("gss1")
    private String guestScoreSection1;

    /**
     * 第2节客队分
     */
    @SerializedName("gss2")
    private String guestScoreSection2;

    /**
     * 第3节客队分
     */
    @SerializedName("gss3")
    private String guestScoreSection3;

    /**
     * 第4节客队分
     */
    @SerializedName("gss4")
    private String guestScoreSection4;

    /**
     * 超时第1节主队分
     */
    @SerializedName("hots1")
    private String homeOverTimeScore1;

    /**
     * 超时第2节主队分
     */
    @SerializedName("hots2")
    private String homeOverTimeScore2;

    /**
     * 超时第3节主队分
     */
    @SerializedName("hots3")
    private String homeOverTimeScore3;

    /**
     * 超时第4节主队分
     */
    @SerializedName("hots4")
    private String homeOverTimeScore4;

    /**
     * 超时第1节客队分
     */
    @SerializedName("gots1")
    private String guestOverTimeScore1;

    /**
     * 超时第2节客队分
     */
    @SerializedName("gots2")
    private String guestOverTimeScore2;

    /**
     * 超时第3节客队分
     */
    @SerializedName("gots3")
    private String guestOverTimeScore3;

    /**
     * 超时第4节主队分
     */
    @SerializedName("gots4")
    private String guestOverTimeScore4;

    /**
     * 比分即时更新时间
     */
    @SerializedName("cst")
    private String changeScoreTimestamp;

    /**
     * 剩余时间
     */
    @SerializedName("rt")
    private String remainTime;

    /**
     * 加时次数
     */
    @SerializedName("ovc")
    private int overTimeCount;

    /**
     * 开赛时间
     */
    @SerializedName("start")

    private String gmtStart;

    /**
     * 节数量
     */
    @SerializedName("sc")
    private int sectionCount = -1;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSectionCount() {
        return sectionCount;
    }

    public void setSectionCount(int sectionCount) {
        this.sectionCount = sectionCount;
    }

    public NormalObject getStatus() {
        return status;
    }

    public void setStatus(NormalObject status) {
        this.status = status;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public int getHalfHomeScore() {
        return halfHomeScore;
    }

    public void setHalfHomeScore(int halfHomeScore) {
        this.halfHomeScore = halfHomeScore;
    }

    public int getHalfGuestScore() {
        return halfGuestScore;
    }

    public void setHalfGuestScore(int halfGuestScore) {
        this.halfGuestScore = halfGuestScore;
    }

    public String getHomeScoreSection1() {
        return homeScoreSection1;
    }

    public void setHomeScoreSection1(String homeScoreSection1) {
        this.homeScoreSection1 = homeScoreSection1;
    }

    public String getHomeScoreSection2() {
        return homeScoreSection2;
    }

    public void setHomeScoreSection2(String homeScoreSection2) {
        this.homeScoreSection2 = homeScoreSection2;
    }

    public String getHomeScoreSection3() {
        return homeScoreSection3;
    }

    public void setHomeScoreSection3(String homeScoreSection3) {
        this.homeScoreSection3 = homeScoreSection3;
    }

    public String getHomeScoreSection4() {
        return homeScoreSection4;
    }

    public void setHomeScoreSection4(String homeScoreSection4) {
        this.homeScoreSection4 = homeScoreSection4;
    }

    public String getGuestScoreSection1() {
        return guestScoreSection1;
    }

    public void setGuestScoreSection1(String guestScoreSection1) {
        this.guestScoreSection1 = guestScoreSection1;
    }

    public String getGuestScoreSection2() {
        return guestScoreSection2;
    }

    public void setGuestScoreSection2(String guestScoreSection2) {
        this.guestScoreSection2 = guestScoreSection2;
    }

    public String getGuestScoreSection3() {
        return guestScoreSection3;
    }

    public void setGuestScoreSection3(String guestScoreSection3) {
        this.guestScoreSection3 = guestScoreSection3;
    }

    public String getGuestScoreSection4() {
        return guestScoreSection4;
    }

    public void setGuestScoreSection4(String guestScoreSection4) {
        this.guestScoreSection4 = guestScoreSection4;
    }

    public String getHomeOverTimeScore1() {
        return homeOverTimeScore1;
    }

    public void setHomeOverTimeScore1(String homeOverTimeScore1) {
        this.homeOverTimeScore1 = homeOverTimeScore1;
    }

    public String getHomeOverTimeScore2() {
        return homeOverTimeScore2;
    }

    public void setHomeOverTimeScore2(String homeOverTimeScore2) {
        this.homeOverTimeScore2 = homeOverTimeScore2;
    }

    public String getHomeOverTimeScore3() {
        return homeOverTimeScore3;
    }

    public void setHomeOverTimeScore3(String homeOverTimeScore3) {
        this.homeOverTimeScore3 = homeOverTimeScore3;
    }

    public String getHomeOverTimeScore4() {
        return homeOverTimeScore4;
    }

    public void setHomeOverTimeScore4(String homeOverTimeScore4) {
        this.homeOverTimeScore4 = homeOverTimeScore4;
    }

    public String getGuestOverTimeScore1() {
        return guestOverTimeScore1;
    }

    public void setGuestOverTimeScore1(String guestOverTimeScore1) {
        this.guestOverTimeScore1 = guestOverTimeScore1;
    }

    public String getGuestOverTimeScore2() {
        return guestOverTimeScore2;
    }

    public void setGuestOverTimeScore2(String guestOverTimeScore2) {
        this.guestOverTimeScore2 = guestOverTimeScore2;
    }

    public String getGuestOverTimeScore3() {
        return guestOverTimeScore3;
    }

    public void setGuestOverTimeScore3(String guestOverTimeScore3) {
        this.guestOverTimeScore3 = guestOverTimeScore3;
    }

    public String getGuestOverTimeScore4() {
        return guestOverTimeScore4;
    }

    public void setGuestOverTimeScore4(String guestOverTimeScore4) {
        this.guestOverTimeScore4 = guestOverTimeScore4;
    }

    public String getChangeScoreTimestamp() {
        return changeScoreTimestamp;
    }

    public void setChangeScoreTimestamp(String changeScoreTimestamp) {
        this.changeScoreTimestamp = changeScoreTimestamp;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public int getOverTimeCount() {
        return overTimeCount;
    }

    public void setOverTimeCount(int overTimeCount) {
        this.overTimeCount = overTimeCount;
    }

    public String getGmtStart() {
        return gmtStart;
    }

    public void setGmtStart(String gmtStart) {
        this.gmtStart = gmtStart;
    }
}
