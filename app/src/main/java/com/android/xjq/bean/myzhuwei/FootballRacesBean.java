package com.android.xjq.bean.myzhuwei;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kokuma on 2017/11/6.
 */

public class FootballRacesBean {

    /**
     * success : true
     * nowDate : 2017-11-06 17:51:56
     * jumpLogin : false
     * footballRaceClientSimpleList : [{"raceStatus":{"name":"WAIT","message":"未赛"},"id":"2666676932207890000002162775","innerRaceId":25617234,"innerGuestTeamId":98566,"innerHomeTeamId":98559,"innerMatchId":31787,"matchName":"亚青U19","guestTeamName":"吉尔吉斯斯坦U19","homeTeamName":"阿联酋U19","halfHomeScore":-1,"halfGuestScore":-1,"fullGuestScore":-1,"fullHomeScore":-1,"gmtStart":"2017-11-06 14:00:00"},{"raceStatus":{"name":"WAIT","message":"未赛"},"id":"2666676932907890000002162776","innerRaceId":25617235,"innerGuestTeamId":98567,"innerHomeTeamId":98587,"innerMatchId":31787,"matchName":"亚青U19","guestTeamName":"韩国U19","homeTeamName":"东帝汶U19","halfHomeScore":-1,"halfGuestScore":-1,"fullGuestScore":-1,"fullHomeScore":-1,"gmtStart":"2017-11-06 14:00:00"},{"raceStatus":{"name":"WAIT","message":"未赛"},"id":"2666676931407890000002162774","innerRaceId":25617233,"innerGuestTeamId":98583,"innerHomeTeamId":98576,"innerMatchId":31787,"matchName":"亚青U19","guestTeamName":"老挝U19","homeTeamName":"中国澳门U19","halfHomeScore":-1,"halfGuestScore":-1,"fullGuestScore":-1,"fullHomeScore":-1,"gmtStart":"2017-11-06 13:00:00"},{"raceStatus":{"name":"WAIT","message":"未赛"},"id":"2666676930507890000009545088","innerRaceId":25617232,"innerGuestTeamId":98558,"innerHomeTeamId":103804,"innerMatchId":31787,"matchName":"亚青U19","guestTeamName":"泰国U19","homeTeamName":"蒙古U19","halfHomeScore":-1,"halfGuestScore":-1,"fullGuestScore":-1,"fullHomeScore":-1,"gmtStart":"2017-11-06 12:00:00"}]
     * matchNameAndIsFiveLeagueMap : {"亚青U19":false}
     */

    private boolean success;
    private String nowDate;
    private boolean jumpLogin;
    private List<FootballRaceClientSimpleListBean> footballRaceClientSimpleList;
    private HashMap<String, String> innerMatchIdAndMatchNameMap;
    private HashMap<String, String> matchGroupAndInnerMatchIdsMap;
    private List<Integer> multipleList;
    private HashMap<String, Long> matchNameAndInnerMatchIdMap;

    public HashMap<String, Long> getMatchNameAndInnerMatchIdMap() {
        return matchNameAndInnerMatchIdMap;
    }

    public void setMatchNameAndInnerMatchIdMap(HashMap<String, Long> matchNameAndInnerMatchIdMap) {
        this.matchNameAndInnerMatchIdMap = matchNameAndInnerMatchIdMap;
    }


    public List<Integer> getMultipleList() {
        return multipleList;
    }

    public void setMultipleList(List<Integer> multipleList) {
        this.multipleList = multipleList;
    }

    private Map<String, FootballRaceClientSimpleListBean.GameBoardBean> raceIdAndNearestGameBoardMap;

    private Map<String, List<BasketballRacesBean.SumInfoBean>> raceIdAndSumInfoMap;

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

    public Map<String, FootballRaceClientSimpleListBean.GameBoardBean> getRaceIdAndNearestGameBoardMap() {
        return raceIdAndNearestGameBoardMap;
    }

    public void setRaceIdAndNearestGameBoardMap(Map<String, FootballRaceClientSimpleListBean.GameBoardBean> raceIdAndNearestGameBoardMap) {
        this.raceIdAndNearestGameBoardMap = raceIdAndNearestGameBoardMap;
    }


    public Map<String, List<BasketballRacesBean.SumInfoBean>> getRaceIdAndSumInfoMap() {
        return raceIdAndSumInfoMap;
    }

    public void setRaceIdAndSumInfoMap(Map<String, List<BasketballRacesBean.SumInfoBean>> raceIdAndSumInfoMap) {
        this.raceIdAndSumInfoMap = raceIdAndSumInfoMap;
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

    public List<FootballRaceClientSimpleListBean> getFootballRaceClientSimpleList() {
        return footballRaceClientSimpleList;
    }

    public void setFootballRaceClientSimpleList(List<FootballRaceClientSimpleListBean> footballRaceClientSimpleList) {
        this.footballRaceClientSimpleList = footballRaceClientSimpleList;
    }

    public static class MatchNameAndIsFiveLeagueMapBean {
        /**
         * 亚青U19 : false
         */

        private boolean 亚青U19;

        public boolean is亚青U19() {
            return 亚青U19;
        }

        public void set亚青U19(boolean 亚青U19) {
            this.亚青U19 = 亚青U19;
        }
    }

    public static class FootballRaceClientSimpleListBean {
        public GameBoardBean gameBoardBean;
        public List<BasketballRacesBean.SumInfoBean> sumInfoList;

        private RaceStatusBean raceStatus;
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

        public static class GameBoardBean {

            /**
             * gameBoardId : 4000620367524707890970016640
             * gmtCreate : 2018-03-06 19:14:35
             * gmtGamePause : 2018-03-06 22:14:30
             * gmtGameStart : 2018-03-06 19:14:35
             * leftGameBoardOptionEntry : {"betFormImageUrl":"http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=26&timestamp=1513044137000","betFormName":"打鼓","betFormNo":"26","betFormSingleFee":1000,"boardId":"4000620367524707890970016640","currentUserTotalMultiple":0,"id":"4000620367527607890970018146","optionCode":"HOME_WIN","optionName":"主胜","totalFee":0,"totalMultiple":0,"totalPaidFee":0,"totalPlayUserCount":0}
             * lotteryStatus : {"message":"初始","name":"INIT"}
             * maxOptionCount : 1
             * minOptionCount : 1
             * optionGroup : RQSF
             * plate : -10.5
             * prizeStatus : {"message":"等待派奖","name":"WAIT"}
             * raceId : 4000620362931207890970017825
             * raceStageType : {"code":"FULL","enabled":true,"gmtCreate":"2017-11-02 16:55:01","gmtModified":"2017-11-11 20:56:53","id":1,"name":"全场","orderNum":1}
             * raceType : FOOTBALL
             * rightGameBoardOptionEntry : {"betFormImageUrl":"http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=25&timestamp=1513044124000","betFormName":"号角","betFormNo":"25","betFormSingleFee":500,"boardId":"4000620367524707890970016640","currentUserTotalMultiple":0,"id":"4000620367527807890970012171","optionCode":"GUEST_WIN","optionName":"客胜","totalFee":0,"totalMultiple":0,"totalPaidFee":0,"totalPlayUserCount":0}
             * saleStatus : {"message":"进行中","name":"PROGRESSING"}
             */
            public String homeName;
            public String guestName;
            private String gameBoardId;
            private String gmtCreate;
            private String gmtGamePause;
            private String gmtGameStart;
            private GameBoardOptionEntry leftGameBoardOptionEntry;
            private LotteryStatusBean lotteryStatus;
            private int maxOptionCount;
            private int minOptionCount;
            private String optionGroup;
            private double plate;
            private PrizeStatusBean prizeStatus;
            private String raceId;
            private RaceStageTypeBean raceStageType;
            private String raceType;
            private GameBoardOptionEntry rightGameBoardOptionEntry;
            private SaleStatusBean saleStatus;

            public String getGameBoardId() {
                return gameBoardId;
            }

            public void setGameBoardId(String gameBoardId) {
                this.gameBoardId = gameBoardId;
            }

            public String getGmtCreate() {
                return gmtCreate;
            }

            public void setGmtCreate(String gmtCreate) {
                this.gmtCreate = gmtCreate;
            }

            public String getGmtGamePause() {
                return gmtGamePause;
            }

            public void setGmtGamePause(String gmtGamePause) {
                this.gmtGamePause = gmtGamePause;
            }

            public String getGmtGameStart() {
                return gmtGameStart;
            }

            public void setGmtGameStart(String gmtGameStart) {
                this.gmtGameStart = gmtGameStart;
            }

            public GameBoardOptionEntry getLeftGameBoardOptionEntry() {
                return leftGameBoardOptionEntry;
            }

            public void setLeftGameBoardOptionEntry(GameBoardOptionEntry leftGameBoardOptionEntry) {
                this.leftGameBoardOptionEntry = leftGameBoardOptionEntry;
            }

            public LotteryStatusBean getLotteryStatus() {
                return lotteryStatus;
            }

            public void setLotteryStatus(LotteryStatusBean lotteryStatus) {
                this.lotteryStatus = lotteryStatus;
            }

            public int getMaxOptionCount() {
                return maxOptionCount;
            }

            public void setMaxOptionCount(int maxOptionCount) {
                this.maxOptionCount = maxOptionCount;
            }

            public int getMinOptionCount() {
                return minOptionCount;
            }

            public void setMinOptionCount(int minOptionCount) {
                this.minOptionCount = minOptionCount;
            }

            public String getOptionGroup() {
                return optionGroup;
            }

            public void setOptionGroup(String optionGroup) {
                this.optionGroup = optionGroup;
            }

            public double getPlate() {
                return plate;
            }

            public void setPlate(double plate) {
                this.plate = plate;
            }

            public PrizeStatusBean getPrizeStatus() {
                return prizeStatus;
            }

            public void setPrizeStatus(PrizeStatusBean prizeStatus) {
                this.prizeStatus = prizeStatus;
            }

            public String getRaceId() {
                return raceId;
            }

            public void setRaceId(String raceId) {
                this.raceId = raceId;
            }

            public RaceStageTypeBean getRaceStageType() {
                return raceStageType;
            }

            public void setRaceStageType(RaceStageTypeBean raceStageType) {
                this.raceStageType = raceStageType;
            }

            public String getRaceType() {
                return raceType;
            }

            public void setRaceType(String raceType) {
                this.raceType = raceType;
            }

            public GameBoardOptionEntry getRightGameBoardOptionEntry() {
                return rightGameBoardOptionEntry;
            }

            public void setRightGameBoardOptionEntry(GameBoardOptionEntry rightGameBoardOptionEntry) {
                this.rightGameBoardOptionEntry = rightGameBoardOptionEntry;
            }

            public SaleStatusBean getSaleStatus() {
                return saleStatus;
            }

            public void setSaleStatus(SaleStatusBean saleStatus) {
                this.saleStatus = saleStatus;
            }

            public static class GameBoardOptionEntry {
                /**
                 * betFormImageUrl : http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=26&timestamp=1513044137000
                 * betFormName : 打鼓
                 * betFormNo : 26
                 * betFormSingleFee : 1000.0
                 * boardId : 4000620367524707890970016640
                 * currentUserTotalMultiple : 0
                 * id : 4000620367527607890970018146
                 * optionCode : HOME_WIN
                 * optionName : 主胜
                 * totalFee : 0.0
                 * totalMultiple : 0
                 * totalPaidFee : 0.0
                 * totalPlayUserCount : 0
                 */

                private String betFormImageUrl;
                private String betFormName;
                private String betFormNo;
                private double betFormSingleFee;
                private String boardId;
                private int currentUserTotalMultiple;
                private String id;
                private String optionCode;
                private String optionName;
                private double totalFee;
                private int totalMultiple;
                private double totalPaidFee;
                private int totalPlayUserCount;

                public String getBetFormImageUrl() {
                    return betFormImageUrl;
                }

                public void setBetFormImageUrl(String betFormImageUrl) {
                    this.betFormImageUrl = betFormImageUrl;
                }

                public String getBetFormName() {
                    return betFormName;
                }

                public void setBetFormName(String betFormName) {
                    this.betFormName = betFormName;
                }

                public String getBetFormNo() {
                    return betFormNo;
                }

                public void setBetFormNo(String betFormNo) {
                    this.betFormNo = betFormNo;
                }

                public double getBetFormSingleFee() {
                    return betFormSingleFee;
                }

                public void setBetFormSingleFee(double betFormSingleFee) {
                    this.betFormSingleFee = betFormSingleFee;
                }

                public String getBoardId() {
                    return boardId;
                }

                public void setBoardId(String boardId) {
                    this.boardId = boardId;
                }

                public int getCurrentUserTotalMultiple() {
                    return currentUserTotalMultiple;
                }

                public void setCurrentUserTotalMultiple(int currentUserTotalMultiple) {
                    this.currentUserTotalMultiple = currentUserTotalMultiple;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getOptionCode() {
                    return optionCode;
                }

                public void setOptionCode(String optionCode) {
                    this.optionCode = optionCode;
                }

                public String getOptionName() {
                    return optionName;
                }

                public void setOptionName(String optionName) {
                    this.optionName = optionName;
                }

                public double getTotalFee() {
                    return totalFee;
                }

                public void setTotalFee(double totalFee) {
                    this.totalFee = totalFee;
                }

                public int getTotalMultiple() {
                    return totalMultiple;
                }

                public void setTotalMultiple(int totalMultiple) {
                    this.totalMultiple = totalMultiple;
                }

                public double getTotalPaidFee() {
                    return totalPaidFee;
                }

                public void setTotalPaidFee(double totalPaidFee) {
                    this.totalPaidFee = totalPaidFee;
                }

                public int getTotalPlayUserCount() {
                    return totalPlayUserCount;
                }

                public void setTotalPlayUserCount(int totalPlayUserCount) {
                    this.totalPlayUserCount = totalPlayUserCount;
                }
            }

            public static class LotteryStatusBean {
                /**
                 * message : 初始
                 * name : INIT
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

            public static class PrizeStatusBean {
                /**
                 * message : 等待派奖
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

            public static class RaceStageTypeBean {
                /**
                 * code : FULL
                 * enabled : true
                 * gmtCreate : 2017-11-02 16:55:01
                 * gmtModified : 2017-11-11 20:56:53
                 * id : 1
                 * name : 全场
                 * orderNum : 1
                 */

                private String code;
                private boolean enabled;
                private String gmtCreate;
                private String gmtModified;
                private int id;
                private String name;
                private int orderNum;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public boolean isEnabled() {
                    return enabled;
                }

                public void setEnabled(boolean enabled) {
                    this.enabled = enabled;
                }

                public String getGmtCreate() {
                    return gmtCreate;
                }

                public void setGmtCreate(String gmtCreate) {
                    this.gmtCreate = gmtCreate;
                }

                public String getGmtModified() {
                    return gmtModified;
                }

                public void setGmtModified(String gmtModified) {
                    this.gmtModified = gmtModified;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getOrderNum() {
                    return orderNum;
                }

                public void setOrderNum(int orderNum) {
                    this.orderNum = orderNum;
                }
            }

            public static class SaleStatusBean {
                /**
                 * message : 进行中
                 * name : PROGRESSING
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
        }
    }
}
