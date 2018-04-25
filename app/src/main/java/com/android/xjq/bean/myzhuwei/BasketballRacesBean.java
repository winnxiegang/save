package com.android.xjq.bean.myzhuwei;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kokuma on 2017/11/6.
 */

public class BasketballRacesBean {

    private boolean success;
    private String nowDate;
    private boolean jumpLogin;
    private Map<String, FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean> raceIdAndNearestGameBoardMap;
    private List<String> matchNameList;
    private List<BasketballRaceClientSimpleListBean> basketballRaceClientSimpleList;
    private Map<String, List<SumInfoBean>> raceIdAndSumInfoMap;
    private HashMap<String, String> innerMatchIdAndMatchNameMap;
    private HashMap<String, String> matchGroupAndInnerMatchIdsMap;
    private HashMap<String, Long> matchNameAndInnerMatchIdMap;

    public HashMap<String, Long> getMatchNameAndInnerMatchIdMap() {
        return matchNameAndInnerMatchIdMap;
    }

    public void setMatchNameAndInnerMatchIdMap(HashMap<String, Long> matchNameAndInnerMatchIdMap) {
        this.matchNameAndInnerMatchIdMap = matchNameAndInnerMatchIdMap;
    }

    private List<Integer> multipleList;

    public List<Integer> getMultipleList() {
        return multipleList;
    }

    public void setMultipleList(List<Integer> multipleList) {
        this.multipleList = multipleList;
    }

    public Map<String, FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean> getRaceIdAndNearestGameBoardMap() {
        return raceIdAndNearestGameBoardMap;
    }

    public void setRaceIdAndNearestGameBoardMap(Map<String, FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean> raceIdAndNearestGameBoardMap) {
        this.raceIdAndNearestGameBoardMap = raceIdAndNearestGameBoardMap;
    }

    public HashMap<String, String> getInnerMatchIdAndMatchNameMap() {
        return innerMatchIdAndMatchNameMap;
    }

    public void setInnerMatchIdAndMatchNameMap(HashMap<String, String> innerMatchIdAndMatchNameMap) {
        this.innerMatchIdAndMatchNameMap = innerMatchIdAndMatchNameMap;
    }

    public HashMap<String, String> getMatchGroupAndInnerMatchIdsMap() {
        return matchGroupAndInnerMatchIdsMap;
    }

    public void setMatchGroupAndInnerMatchIdsMap(HashMap<String, String> matchGroupAndInnerMatchIdsMap) {
        this.matchGroupAndInnerMatchIdsMap = matchGroupAndInnerMatchIdsMap;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }


    public Map<String, List<SumInfoBean>> getRaceIdAndSumInfoMap() {
        return raceIdAndSumInfoMap;
    }

    public void setRaceIdAndSumInfoMap(Map<String, List<SumInfoBean>> raceIdAndSumInfoMap) {
        this.raceIdAndSumInfoMap = raceIdAndSumInfoMap;
    }

    public List<String> getMatchNameList() {
        return matchNameList;
    }

    public void setMatchNameList(List<String> matchNameList) {
        this.matchNameList = matchNameList;
    }

    public List<BasketballRaceClientSimpleListBean> getBasketballRaceClientSimpleList() {
        return basketballRaceClientSimpleList;
    }

    public void setBasketballRaceClientSimpleList(List<BasketballRaceClientSimpleListBean> basketballRaceClientSimpleList) {
        this.basketballRaceClientSimpleList = basketballRaceClientSimpleList;
    }


    public static class SumInfoBean {
        /**
         * optionCode : HOME_WIN
         * sumFee : 100.0
         */

        private String optionCode;
        private double sumFee;

        public String getOptionCode() {
            return optionCode;
        }

        public void setOptionCode(String optionCode) {
            this.optionCode = optionCode;
        }

        public double getSumFee() {
            return sumFee;
        }

        public void setSumFee(double sumFee) {
            this.sumFee = sumFee;
        }
    }


    public static class BasketballRaceClientSimpleListBean {
        public FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean gameBoardBean;
        public List<BasketballRacesBean.SumInfoBean> sumInfoList;


        private RaceStatusBean raceStatus;
        private int sectionCount;
        private int homeScoreSection1;
        private int guestScoreSection1;
        private int homeScoreSection2;
        private int guestScoreSection2;
        private int homeScoreSection3;
        private int guestScoreSection3;
        private int homeScoreSection4;
        private int guestScoreSection4;
        private String id;
        private int innerRaceId;
        private int innerGuestTeamId;
        private int innerHomeTeamId;
        private int innerMatchId;
        private String matchName;
        private String guestTeamName;
        private String homeTeamName;
        private int halfHomeScore;
        private int halfGuestScore;
        private int fullGuestScore;
        private int fullHomeScore;
        private String gmtStart;
        private boolean existedOtherGameBoard;
        private boolean existedDefaultGameBoard;
        private boolean gameBoardAllPrized;

        public boolean isGameBoardAllPrized() {
            return gameBoardAllPrized;
        }

        public void setGameBoardAllPrized(boolean gameBoardAllPrized) {
            this.gameBoardAllPrized = gameBoardAllPrized;
        }


        public boolean isExistedDefaultGameBoard() {
            return existedDefaultGameBoard;
        }

        public void setExistedDefaultGameBoard(boolean existedDefaultGameBoard) {
            this.existedDefaultGameBoard = existedDefaultGameBoard;
        }

        public boolean isExistedOtherGameBoard() {
            return existedOtherGameBoard;
        }

        public void setExistedOtherGameBoard(boolean existedOtherGameBoard) {
            this.existedOtherGameBoard = existedOtherGameBoard;
        }

        public RaceStatusBean getRaceStatus() {
            return raceStatus;
        }

        public void setRaceStatus(RaceStatusBean raceStatus) {
            this.raceStatus = raceStatus;
        }

        public int getSectionCount() {
            return sectionCount;
        }

        public void setSectionCount(int sectionCount) {
            this.sectionCount = sectionCount;
        }

        public int getHomeScoreSection1() {
            return homeScoreSection1;
        }

        public void setHomeScoreSection1(int homeScoreSection1) {
            this.homeScoreSection1 = homeScoreSection1;
        }

        public int getGuestScoreSection1() {
            return guestScoreSection1;
        }

        public void setGuestScoreSection1(int guestScoreSection1) {
            this.guestScoreSection1 = guestScoreSection1;
        }

        public int getHomeScoreSection2() {
            return homeScoreSection2;
        }

        public void setHomeScoreSection2(int homeScoreSection2) {
            this.homeScoreSection2 = homeScoreSection2;
        }

        public int getGuestScoreSection2() {
            return guestScoreSection2;
        }

        public void setGuestScoreSection2(int guestScoreSection2) {
            this.guestScoreSection2 = guestScoreSection2;
        }

        public int getHomeScoreSection3() {
            return homeScoreSection3;
        }

        public void setHomeScoreSection3(int homeScoreSection3) {
            this.homeScoreSection3 = homeScoreSection3;
        }

        public int getGuestScoreSection3() {
            return guestScoreSection3;
        }

        public void setGuestScoreSection3(int guestScoreSection3) {
            this.guestScoreSection3 = guestScoreSection3;
        }

        public int getHomeScoreSection4() {
            return homeScoreSection4;
        }

        public void setHomeScoreSection4(int homeScoreSection4) {
            this.homeScoreSection4 = homeScoreSection4;
        }

        public int getGuestScoreSection4() {
            return guestScoreSection4;
        }

        public void setGuestScoreSection4(int guestScoreSection4) {
            this.guestScoreSection4 = guestScoreSection4;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getInnerRaceId() {
            return innerRaceId;
        }

        public void setInnerRaceId(int innerRaceId) {
            this.innerRaceId = innerRaceId;
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

        public String getMatchName() {
            return matchName;
        }

        public void setMatchName(String matchName) {
            this.matchName = matchName;
        }

        public String getGuestTeamName() {
            return guestTeamName;
        }

        public void setGuestTeamName(String guestTeamName) {
            this.guestTeamName = guestTeamName;
        }

        public String getHomeTeamName() {
            return homeTeamName;
        }

        public void setHomeTeamName(String homeTeamName) {
            this.homeTeamName = homeTeamName;
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

        public static class RaceStatusBean {
            /**
             * name : WAIT
             * message : 未赛
             */

            private String name;
            private String message;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }
    }
}
