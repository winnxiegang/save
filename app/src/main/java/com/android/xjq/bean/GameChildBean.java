package com.android.xjq.bean;

import java.util.List;

/**
 * Created by ajiao on 2017/11/6 0006.
 */

public class GameChildBean {
    /**
     * success : true
     * nowDate : 2017-11-07 16:45:07
     * jumpLogin : false
     * gameBoardList : [{"id":"2660221463607890980017610661","raceType":"BASKETBALL","raceId":"2650349723207890980015447300","gameBoardOptionEntries":[{"id":"2660221464507890980012080329","boardId":"2660221463607890980017610661","optionCode":"GUEST_WIN","optionName":"客胜","enabled":true,"gmtCreate":"2017-11-04 21:30:14","gmtModified":"2017-11-04 21:30:14","totalFee":0,"totalPlayUserCount":0,"totalPlayOrderCount":0},{"id":"2660221464407890980012080328","boardId":"2660221463607890980017610661","optionCode":"HOME_WIN","optionName":"主胜","enabled":true,"gmtCreate":"2017-11-04 21:30:14","gmtModified":"2017-11-04 21:30:14","totalFee":0,"totalPlayUserCount":0,"totalPlayOrderCount":0}],"plate":0.5,"raceStageType":{"id":1,"code":"FULL","name":"全场","orderNum":1,"enabled":true,"gmtCreate":"2017-11-02 16:55:01","gmtModified":"2017-11-02 16:55:01"},"saleStatus":{"name":"PROGRESSING","message":"进行中"},"optionGroup":"RFSF","lotteryStatus":{"name":"INIT","message":"初始"},"prizeStatus":"WAIT"},{"id":"2660222263107890980012080330","raceType":"BASKETBALL","raceId":"2650349723207890980015447300","gameBoardOptionEntries":[{"id":"2660222263607890980017863674","boardId":"2660222263107890980012080330","optionCode":"GUEST_WIN","optionName":"客胜","enabled":true,"gmtCreate":"2017-11-04 21:30:22","gmtModified":"2017-11-04 21:30:22","totalFee":0,"totalPlayUserCount":0,"totalPlayOrderCount":0},{"id":"2660222263607890980017863673","boardId":"2660222263107890980012080330","optionCode":"HOME_WIN","optionName":"主胜","enabled":true,"gmtCreate":"2017-11-04 21:30:22","gmtModified":"2017-11-04 21:30:22","totalFee":0,"totalPlayUserCount":0,"totalPlayOrderCount":0}],"plate":1.5,"raceStageType":{"id":2,"code":"FIRST_HALF","name":"上半场","orderNum":2,"enabled":true,"gmtCreate":"2017-11-02 16:55:01","gmtModified":"2017-11-02 16:55:01"},"saleStatus":{"name":"PROGRESSING","message":"进行中"},"optionGroup":"RFSF","lotteryStatus":{"name":"INIT","message":"初始"},"prizeStatus":"WAIT"},{"id":"2660223178707890980017863675","raceType":"BASKETBALL","raceId":"2650349723207890980015447300","gameBoardOptionEntries":[{"id":"2660223179307890980012234173","boardId":"2660223178707890980017863675","optionCode":"GUEST_WIN","optionName":"客胜","enabled":true,"gmtCreate":"2017-11-04 21:30:31","gmtModified":"2017-11-04 21:30:31","totalFee":0,"totalPlayUserCount":0,"totalPlayOrderCount":0},{"id":"2660223179207890980012234172","boardId":"2660223178707890980017863675","optionCode":"HOME_WIN","optionName":"主胜","enabled":true,"gmtCreate":"2017-11-04 21:30:31","gmtModified":"2017-11-04 21:30:31","totalFee":0,"totalPlayUserCount":0,"totalPlayOrderCount":0}],"plate":1.5,"raceStageType":{"id":3,"code":"SECOND_HALF","name":"下半场","orderNum":3,"enabled":true,"gmtCreate":"2017-11-02 16:55:01","gmtModified":"2017-11-02 16:55:01"},"saleStatus":{"name":"PROGRESSING","message":"进行中"},"optionGroup":"RFSF","lotteryStatus":{"name":"INIT","message":"初始"},"prizeStatus":"WAIT"}]
     */
    private boolean success;
    private String nowDate;
    private boolean jumpLogin;
    private List<GameBoardListBean> gameBoardList;
    private RaceDataSimple raceDataSimple;
    private List<Integer> multipleList;

    public List<Integer> getMultipleList() {
        return multipleList;
    }

    public void setMultipleList(List<Integer> multipleList) {
        this.multipleList = multipleList;
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

    public List<GameBoardListBean> getGameBoardList() {
        return gameBoardList;
    }

    public void setGameBoardList(List<GameBoardListBean> gameBoardList) {
        this.gameBoardList = gameBoardList;
    }

    public RaceDataSimple getRaceDataSimple() {
        return raceDataSimple;
    }

    public void setRaceDataSimple(RaceDataSimple raceDataSimple) {
        this.raceDataSimple = raceDataSimple;
    }

    public static class RaceDataSimple {

        /**
         * existedOtherGameBoard : false
         * gmtStart : 2017-11-13 12:00:00
         * guestTeamName : 华城FC
         * homeTeamName : 清州城
         * id : 2734320234807890980015021542
         * innerMatchId : 31695
         * innerRaceId : 25627993
         * matchName : 球会友谊
         * raceStatus : {"message":"待定","name":"DEC"}
         */

        private boolean existedOtherGameBoard;
        private String gmtStart;
        private String guestTeamName;
        private String homeTeamName;
        private String id;
        private int innerMatchId;
        private int innerRaceId;
        private String matchName;
        private RaceStatusBean raceStatus;

        public boolean isExistedOtherGameBoard() {
            return existedOtherGameBoard;
        }

        public void setExistedOtherGameBoard(boolean existedOtherGameBoard) {
            this.existedOtherGameBoard = existedOtherGameBoard;
        }

        public String getGmtStart() {
            return gmtStart;
        }

        public void setGmtStart(String gmtStart) {
            this.gmtStart = gmtStart;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public RaceStatusBean getRaceStatus() {
            return raceStatus;
        }

        public void setRaceStatus(RaceStatusBean raceStatus) {
            this.raceStatus = raceStatus;
        }

        public static class RaceStatusBean {
            /**
             * message : 待定
             * name : DEC
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

    public static class GameBoardListBean {
        /**
         * gameBoardId : 4000558989319707890000001348
         * gmtCreate : 2018-02-27 16:44:52
         * gmtGamePause : 2018-02-28 03:00:13
         * gmtGameStart : 2018-02-27 16:44:53
         * leftGameBoardOptionEntry : {"betFormImageUrl":"http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=23&timestamp=1513044096000","betFormName":"鼓掌","betFormNo":"23","betFormSingleFee":100,"boardId":"4000558989319707890000001348","currentUserTotalMultiple":0,"id":"4000558989321707890000002329","optionCode":"HOME_WIN","optionName":"主胜","totalFee":0,"totalMultiple":0,"totalPaidFee":0,"totalPlayUserCount":0}
         * lotteryStatus : {"message":"初始","name":"INIT"}
         * maxOptionCount : 1
         * minOptionCount : 1
         * optionGroup : RFSF
         * plate : -112.5
         * prizeStatus : {"message":"等待派奖","name":"WAIT"}
         * raceId : 4000558950544307890980011493
         * raceStageType : {"code":"FIRST_SECTION","enabled":true,"gmtCreate":"2017-11-02 16:55:01","gmtModified":"2017-11-11 20:56:53","id":4,"name":"第一节","orderNum":2}
         * raceType : BASKETBALL
         * rightGameBoardOptionEntry : {"betFormImageUrl":"http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=24&timestamp=1513044113000","betFormName":"助威棒","betFormNo":"24","betFormSingleFee":200,"boardId":"4000558989319707890000001348","currentUserTotalMultiple":0,"id":"4000558989321707890000006718","optionCode":"GUEST_WIN","optionName":"客胜","totalFee":0,"totalMultiple":0,"totalPaidFee":0,"totalPlayUserCount":0}
         * saleStatus : {"message":"暂停","name":"PAUSE"}
         */

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
             * betFormImageUrl : http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=23&timestamp=1513044096000
             * betFormName : 鼓掌
             * betFormNo : 23
             * betFormSingleFee : 100.0
             * boardId : 4000558989319707890000001348
             * currentUserTotalMultiple : 0
             * id : 4000558989321707890000002329
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
             * code : FIRST_SECTION
             * enabled : true
             * gmtCreate : 2017-11-02 16:55:01
             * gmtModified : 2017-11-11 20:56:53
             * id : 4
             * name : 第一节
             * orderNum : 2
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
             * message : 暂停
             * name : PAUSE
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
