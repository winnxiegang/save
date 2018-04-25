package com.android.xjq.bean;

import java.util.List;

/**
 * Created by Pangxie on 2016/5/12 16:33.
 * 初次预测实体类数据
 *
 */
public class ForecastBean {

    private boolean predict;

    private UserRacePredictEntity userRacePredict;

    private JczqDataSupportPredictDataEntity jczqDataSupportPredictData;

    private boolean rqspfGg;

    private boolean spfGg;

    public UserRacePredictEntity getUserRacePredict() {
        return userRacePredict;
    }

    public void setUserRacePredict(UserRacePredictEntity userRacePredict) {
        this.userRacePredict = userRacePredict;
    }

    public void setJczqDataSupportPredictData(JczqDataSupportPredictDataEntity jczqDataSupportPredictData) {
        this.jczqDataSupportPredictData = jczqDataSupportPredictData;
    }

    public JczqDataSupportPredictDataEntity getJczqDataSupportPredictData() {
        return jczqDataSupportPredictData;
    }
    public boolean isPredict() {
        return predict;
    }

    public void setPredict(boolean predict) {
        this.predict = predict;
    }


    public static class JczqDataSupportPredictDataEntity {
        private int fullGuestScore;
        private int concede;
        private int innerRaceId;
        /**
         * message : 未赛
         * name : WAIT
         */

        private StatusEntity status;
        private String stopSellDate;
        private int innerHomeTeamId;
        private int halfHomeScore;
        /**
         * draw : 3.5
         * lost : 1.67
         * win : 4.1
         */

        private RqspfGgSpEntity rqspfGgSp;
        private int innerMatchId;
        private int fullHomeScore;
        private String guestTeamName;
        private int id;
        private int halfGuestScore;
        /**
         * draw : 3.1
         * lost : 3.6
         * win : 1.91
         */

        private SpfGgSpEntity spfGgSp;
        private int innerGuestTeamId;
        private String bizId;
        private String homeTeamName;
        /**
         * value : 0
         * message : 让平
         * name : RQSPF_DRAW
         */

        private List<SupportUserChooseOptionsEntity> supportUserChooseOptions;

        public void setFullGuestScore(int fullGuestScore) {
            this.fullGuestScore = fullGuestScore;
        }

        public void setConcede(int concede) {
            this.concede = concede;
        }

        public void setInnerRaceId(int innerRaceId) {
            this.innerRaceId = innerRaceId;
        }

        public void setStatus(StatusEntity status) {
            this.status = status;
        }

        public void setStopSellDate(String stopSellDate) {
            this.stopSellDate = stopSellDate;
        }

        public void setInnerHomeTeamId(int innerHomeTeamId) {
            this.innerHomeTeamId = innerHomeTeamId;
        }

        public void setHalfHomeScore(int halfHomeScore) {
            this.halfHomeScore = halfHomeScore;
        }

        public void setRqspfGgSp(RqspfGgSpEntity rqspfGgSp) {
            this.rqspfGgSp = rqspfGgSp;
        }

        public void setInnerMatchId(int innerMatchId) {
            this.innerMatchId = innerMatchId;
        }

        public void setFullHomeScore(int fullHomeScore) {
            this.fullHomeScore = fullHomeScore;
        }

        public void setGuestTeamName(String guestTeamName) {
            this.guestTeamName = guestTeamName;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setHalfGuestScore(int halfGuestScore) {
            this.halfGuestScore = halfGuestScore;
        }

        public void setSpfGgSp(SpfGgSpEntity spfGgSp) {
            this.spfGgSp = spfGgSp;
        }

        public void setInnerGuestTeamId(int innerGuestTeamId) {
            this.innerGuestTeamId = innerGuestTeamId;
        }

        public void setBizId(String bizId) {
            this.bizId = bizId;
        }

        public void setHomeTeamName(String homeTeamName) {
            this.homeTeamName = homeTeamName;
        }

        public void setSupportUserChooseOptions(List<SupportUserChooseOptionsEntity> supportUserChooseOptions) {
            this.supportUserChooseOptions = supportUserChooseOptions;
        }

        public int getFullGuestScore() {
            return fullGuestScore;
        }

        public int getConcede() {
            return concede;
        }

        public int getInnerRaceId() {
            return innerRaceId;
        }

        public StatusEntity getStatus() {
            return status;
        }

        public String getStopSellDate() {
            return stopSellDate;
        }

        public int getInnerHomeTeamId() {
            return innerHomeTeamId;
        }

        public int getHalfHomeScore() {
            return halfHomeScore;
        }

        public RqspfGgSpEntity getRqspfGgSp() {
            return rqspfGgSp;
        }

        public int getInnerMatchId() {
            return innerMatchId;
        }

        public int getFullHomeScore() {
            return fullHomeScore;
        }

        public String getGuestTeamName() {
            return guestTeamName;
        }

        public int getId() {
            return id;
        }

        public int getHalfGuestScore() {
            return halfGuestScore;
        }

        public SpfGgSpEntity getSpfGgSp() {
            return spfGgSp;
        }

        public int getInnerGuestTeamId() {
            return innerGuestTeamId;
        }

        public String getBizId() {
            return bizId;
        }

        public String getHomeTeamName() {
            return homeTeamName;
        }

        public List<SupportUserChooseOptionsEntity> getSupportUserChooseOptions() {
            return supportUserChooseOptions;
        }

        public static class StatusEntity {
            private String message;
            private String name;

            public void setMessage(String message) {
                this.message = message;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMessage() {
                return message;
            }

            public String getName() {
                return name;
            }
        }

        public static class RqspfGgSpEntity {
            private double draw;
            private double lost;
            private double win;

            public void setDraw(double draw) {
                this.draw = draw;
            }

            public void setLost(double lost) {
                this.lost = lost;
            }

            public void setWin(double win) {
                this.win = win;
            }

            public double getDraw() {
                return draw;
            }

            public double getLost() {
                return lost;
            }

            public double getWin() {
                return win;
            }
        }

        public static class SpfGgSpEntity {
            private double draw;
            private double lost;
            private double win;

            public void setDraw(double draw) {
                this.draw = draw;
            }

            public void setLost(double lost) {
                this.lost = lost;
            }

            public void setWin(double win) {
                this.win = win;
            }

            public double getDraw() {
                return draw;
            }

            public double getLost() {
                return lost;
            }

            public double getWin() {
                return win;
            }
        }

        public static class SupportUserChooseOptionsEntity {
            private int value;
            private String message;
            private String name;

            public void setValue(int value) {
                this.value = value;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getValue() {
                return value;
            }

            public String getMessage() {
                return message;
            }

            public String getName() {
                return name;
            }
        }
    }

    public static class UserRacePredictEntity {
        private String gmtModified;
        /**
         * message : 评论
         * name : COMMENT
         */

        private SourceTypeEntity sourceType;
        /**
         * RQSPF_DRAW : 3.8
         */

        private OptionSpMapEntity optionSpMap;
        private String gmtCreate;
        /**
         * message : 待计算
         * name : READY_CALCULATE
         */

        private PredictStatusEntity predictStatus;
        /**
         * sp_RQSPF_DRAW : 3.8
         * rqPlate : -1
         */

        private PropertiesEntity properties;
        private String sourceId;
        private String gmtRace;
        /**
         * message : 竞彩足球
         * name : JCZQ
         */

        private RaceTypeEntity raceType;
        private int id;
        private int raceId;
        private String userId;
        private int rqPlate;
        private int predictModeId;
        private List<?> awardResultList;
        /**
         * value : 0
         * message : 让平
         * name : RQSPF_DRAW
         */

        private List<OptionsEntity> options;

        public void setGmtModified(String gmtModified) {
            this.gmtModified = gmtModified;
        }

        public void setSourceType(SourceTypeEntity sourceType) {
            this.sourceType = sourceType;
        }

        public void setOptionSpMap(OptionSpMapEntity optionSpMap) {
            this.optionSpMap = optionSpMap;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public void setPredictStatus(PredictStatusEntity predictStatus) {
            this.predictStatus = predictStatus;
        }

        public void setProperties(PropertiesEntity properties) {
            this.properties = properties;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public void setGmtRace(String gmtRace) {
            this.gmtRace = gmtRace;
        }

        public void setRaceType(RaceTypeEntity raceType) {
            this.raceType = raceType;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setRaceId(int raceId) {
            this.raceId = raceId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setRqPlate(int rqPlate) {
            this.rqPlate = rqPlate;
        }

        public void setPredictModeId(int predictModeId) {
            this.predictModeId = predictModeId;
        }

        public void setAwardResultList(List<?> awardResultList) {
            this.awardResultList = awardResultList;
        }

        public void setOptions(List<OptionsEntity> options) {
            this.options = options;
        }

        public String getGmtModified() {
            return gmtModified;
        }

        public SourceTypeEntity getSourceType() {
            return sourceType;
        }

        public OptionSpMapEntity getOptionSpMap() {
            return optionSpMap;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public PredictStatusEntity getPredictStatus() {
            return predictStatus;
        }

        public PropertiesEntity getProperties() {
            return properties;
        }

        public String getSourceId() {
            return sourceId;
        }

        public String getGmtRace() {
            return gmtRace;
        }

        public RaceTypeEntity getRaceType() {
            return raceType;
        }

        public int getId() {
            return id;
        }

        public int getRaceId() {
            return raceId;
        }

        public String getUserId() {
            return userId;
        }

        public int getRqPlate() {
            return rqPlate;
        }

        public int getPredictModeId() {
            return predictModeId;
        }

        public List<?> getAwardResultList() {
            return awardResultList;
        }

        public List<OptionsEntity> getOptions() {
            return options;
        }

        public static class SourceTypeEntity {
            private String message;
            private String name;

            public void setMessage(String message) {
                this.message = message;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMessage() {
                return message;
            }

            public String getName() {
                return name;
            }
        }

        public static class OptionSpMapEntity {
            private double RQSPF_DRAW;

            public void setRQSPF_DRAW(double RQSPF_DRAW) {
                this.RQSPF_DRAW = RQSPF_DRAW;
            }

            public double getRQSPF_DRAW() {
                return RQSPF_DRAW;
            }
        }

        public static class PredictStatusEntity {
            private String message;
            private String name;

            public void setMessage(String message) {
                this.message = message;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMessage() {
                return message;
            }

            public String getName() {
                return name;
            }
        }

        public static class PropertiesEntity {

            private String sp_RQSPF_DRAW;
            private String sp_RQSPF_WIN;
            private String sp_RQSPF_LOST;
            private String sp_SPF_DRAW;
            private String sp_SPF_WIN;
            private String sp_SPF_LOST;
            private String rqPlate;

            public void setSp_RQSPF_DRAW(String sp_RQSPF_DRAW) {
                this.sp_RQSPF_DRAW = sp_RQSPF_DRAW;
            }

            public void setRqPlate(String rqPlate) {
                this.rqPlate = rqPlate;
            }

            public String getSp_RQSPF_DRAW() {
                return sp_RQSPF_DRAW;
            }

            public String getRqPlate() {
                return rqPlate;
            }

            public String getSp_RQSPF_WIN() {
                return sp_RQSPF_WIN;
            }

            public void setSp_RQSPF_WIN(String sp_RQSPF_WIN) {
                this.sp_RQSPF_WIN = sp_RQSPF_WIN;
            }

            public String getSp_RQSPF_LOST() {
                return sp_RQSPF_LOST;
            }

            public void setSp_RQSPF_LOST(String sp_RQSPF_LOST) {
                this.sp_RQSPF_LOST = sp_RQSPF_LOST;
            }

            public String getSp_SPF_DRAW() {
                return sp_SPF_DRAW;
            }

            public void setSp_SPF_DRAW(String sp_SPF_DRAW) {
                this.sp_SPF_DRAW = sp_SPF_DRAW;
            }

            public String getSp_SPF_WIN() {
                return sp_SPF_WIN;
            }

            public void setSp_SPF_WIN(String sp_SPF_WIN) {
                this.sp_SPF_WIN = sp_SPF_WIN;
            }

            public String getSp_SPF_LOST() {
                return sp_SPF_LOST;
            }

            public void setSp_SPF_LOST(String sp_SPF_LOST) {
                this.sp_SPF_LOST = sp_SPF_LOST;
            }
        }

        public static class RaceTypeEntity {

            private String message;

            private String name;

            public void setMessage(String message) {
                this.message = message;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMessage() {
                return message;
            }

            public String getName() {
                return name;
            }
        }

        public static class OptionsEntity {
            private int value;
            private String message;
            private String name;

            public void setValue(int value) {
                this.value = value;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getValue() {
                return value;
            }

            public String getMessage() {
                return message;
            }

            public String getName() {
                return name;
            }
        }
    }

    public boolean isRqspfGg() {
        return rqspfGg;
    }

    public void setRqspfGg(boolean rqspfGg) {
        this.rqspfGg = rqspfGg;
    }

    public boolean isSpfGg() {
        return spfGg;
    }

    public void setSpfGg(boolean spfGg) {
        this.spfGg = spfGg;
    }
}
