package com.android.banana.commlib.bean.liveScoreBean;

import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.liveScore.livescoreEnum.FtRaceStatusEnum;
import com.android.banana.commlib.utils.TimeUtils;
import com.google.gson.annotations.Expose;

import org.json.JSONObject;

/**
 * Created by zhouyi on 2016/5/6 17:13.
 */
public class RaceDetailQueryBean {

    @Expose
    private String homeLogoUrl;

    @Expose
    private String guestLogoUrl;

    private String nowDate;

    private JczqDataBean jczqData;

    private JczqDataBean jclqData;

    public JczqDataBean getJczqData() {
        return jczqData;
    }

    public void setJczqData(JczqDataBean jczqData) {
        this.jczqData = jczqData;
    }


    public String getHomeLogoUrl() {
        return homeLogoUrl;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }

    public String getGuestLogoUrl() {
        return guestLogoUrl;
    }

    public void setGuestLogoUrl(String guestLogoUrl) {
        this.guestLogoUrl = guestLogoUrl;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public JczqDataBean getJclqData() {
        return jclqData;
    }

    public void setJclqData(JczqDataBean jclqData) {
        this.jclqData = jclqData;
    }

    public static class JczqDataBean {
        public int sectionCount;
        
        private int fullGuestScore;
        private String startDate;
        public String nowDate;
        private int concede;
        private String issueNo;

        private NormalObject status;
        private String gameNo;
        private boolean prized;
        private int halfHomeScore;
        private int innerHomeTeamId;
        private int fullHomeScore;
        private String gameName;
        private String guestTeamName;
        private int id;
        private int halfGuestScore;
        private int innerGuestTeamId;
        private boolean late;
        private String bizId;
        private String homeTeamName;
        private int innerRaceId;
        private int innerMatchId;

        private boolean innerTeamReverse;

        public String getHasStartTime() {
            return hasStartTime;
        }

        public void setHasStartTime(String hasStartTime) {
            this.hasStartTime = hasStartTime;
        }

        private String hasStartTime;

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

        public void setNowDate(String nowDate) {
            this.nowDate = nowDate;
        }

        public int getConcede() {
            return concede;
        }

        public void setConcede(int concede) {
            this.concede = concede;
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

        public int getInnerHomeTeamId() {
            return innerHomeTeamId;
        }

        public void setInnerHomeTeamId(int innerHomeTeamId) {
            this.innerHomeTeamId = innerHomeTeamId;
        }

        public int getFullHomeScore() {
            return fullHomeScore;
        }

        public void setFullHomeScore(int fullHomeScore) {
            this.fullHomeScore = fullHomeScore;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getGuestTeamName() {
            return guestTeamName;
        }

        public void setGuestTeamName(String guestTeamName) {
            this.guestTeamName = guestTeamName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public String getBizId() {
            return bizId;
        }

        public void setBizId(String bizId) {
            this.bizId = bizId;
        }

        public String getHomeTeamName() {
            return homeTeamName;
        }

        public void setHomeTeamName(String homeTeamName) {
            this.homeTeamName = homeTeamName;
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

        public int getInnerMatchId() {
            return innerMatchId;
        }

        public void setInnerMatchId(int innerMatchId) {
            this.innerMatchId = innerMatchId;
        }

        public boolean isInnerTeamReverse() {
            return innerTeamReverse;
        }

        public void setInnerTeamReverse(boolean innerTeamReverse) {
            this.innerTeamReverse = innerTeamReverse;
        }

        /**
         * @param jcData
         * @param nowDate
         * @return
         * @see JczqMatchLiveBean#JczqMatchLiveBean(JSONObject)
         */
        public String getHasStartTime(JczqDataBean jcData, String nowDate, boolean isLqRace) {
            long betweenTime = TimeUtils.caculateMnisDiff(nowDate, jcData.getStartDate());
            if (isLqRace) {
                return betweenTime + "";
            }
            if (FtRaceStatusEnum.saveValueOf(jcData.getStatus().getName()) == FtRaceStatusEnum.PLAY_S) {
                if (betweenTime + 45 > 90) {
                    return "90+";
                } else {
                    return String.valueOf(betweenTime + 45);
                }
            } else if (FtRaceStatusEnum.saveValueOf(jcData.getStatus().getName()) == FtRaceStatusEnum.PLAY_F) {
                if (betweenTime > 45) {
                    return "45+";
                } else {
                    return String.valueOf(betweenTime);
                }
            }
            //这边为了解决一个服务端的一个问题：下半场开始的前两分钟内可能拿不到下半场比赛开始时间。这是就显示46'
            else if (FtRaceStatusEnum.saveValueOf(jcData.getStatus().getName()) == FtRaceStatusEnum.PLAY_S) {
                return 46 + "";
            }
            return String.valueOf(betweenTime);
        }

    }
}
