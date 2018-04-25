package com.android.xjq.bean.myzhuwei;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kokuma on 2017/11/4.
 */

public class ZhuweiDetailBean {

    private boolean success;
    private String nowDate;
    private boolean jumpLogin;
    private PurchaseOrderBean purchaseOrderSummaryClientSimple;
    private GameBoardBean gameBoard;
    private List<OrderFundBillDetailBean> orderFundBillDetailClientSimples;

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

    public PurchaseOrderBean getPurchaseOrderSummaryClientSimple() {
        return purchaseOrderSummaryClientSimple;
    }

    public void setPurchaseOrderSummaryClientSimple(PurchaseOrderBean purchaseOrderSummaryClientSimple) {
        this.purchaseOrderSummaryClientSimple = purchaseOrderSummaryClientSimple;
    }

    public GameBoardBean getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoardBean gameBoard) {
        this.gameBoard = gameBoard;
    }

    public List<OrderFundBillDetailBean> getOrderFundBillDetailClientSimples() {
        return orderFundBillDetailClientSimples;
    }

    public void setOrderFundBillDetailClientSimples(List<OrderFundBillDetailBean> orderFundBillDetailClientSimples) {
        this.orderFundBillDetailClientSimples = orderFundBillDetailClientSimples;
    }

    public static class PurchaseOrderBean {
        /**
         * id : test111
         * prizeStatus : {"name":"PRIZE_FINISH","message":"派奖结束"}
         * totalFee : 100.0
         * prizeFee : 500.0
         * payCount : 1
         * userId : 8201711028675850
         * raceType : FOOTBALL
         * createrType : {"name":"USER_CREATE","message":"用户主动创建"}
         * gmtCreate : 2017-11-02 11:03:21
         * gmtModified : 2017-11-02 11:03:21
         * entryBizId : 2641428348907890000005059763
         * entryValue : ["HOME_WIN"]
         * totalPaidFee : 10.0
         * userLogoUrl : http://mapi.xjq.net/userLogo.resource?userId=8201711028675850&mt=
         * userAlias : mm2
         * orderStatus : WIN
         */

        private String id;
        private PrizeStatusBean prizeStatus;
        private double totalFee;
        private double prizeFee;
        private int payCount;
        private String userId;
        private String raceType;
        private CreaterTypeBean createrType;
        private String gmtCreate;
        private String gmtModified;
        private String entryBizId;
        private double totalPaidFee;
        private String userLogoUrl;
        private String userAlias;
        private String orderStatus;
        private String entryValue;

        public ZhuweiDetailBean.GameBoardBean gameBoardBean;

        public GameBoardBean getGameBoardBean() {
            return gameBoardBean;
        }

        public void setGameBoardBean(GameBoardBean gameBoardBean) {
            this.gameBoardBean = gameBoardBean;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public PrizeStatusBean getPrizeStatus() {
            return prizeStatus;
        }

        public void setPrizeStatus(PrizeStatusBean prizeStatus) {
            this.prizeStatus = prizeStatus;
        }

        public double getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(double totalFee) {
            this.totalFee = totalFee;
        }

        public double getPrizeFee() {
            return prizeFee;
        }

        public void setPrizeFee(double prizeFee) {
            this.prizeFee = prizeFee;
        }

        public int getPayCount() {
            return payCount;
        }

        public void setPayCount(int payCount) {
            this.payCount = payCount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRaceType() {
            return raceType;
        }

        public void setRaceType(String raceType) {
            this.raceType = raceType;
        }

        public CreaterTypeBean getCreaterType() {
            return createrType;
        }

        public void setCreaterType(CreaterTypeBean createrType) {
            this.createrType = createrType;
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

        public String getEntryBizId() {
            return entryBizId;
        }

        public void setEntryBizId(String entryBizId) {
            this.entryBizId = entryBizId;
        }

        public double getTotalPaidFee() {
            return totalPaidFee;
        }

        public void setTotalPaidFee(double totalPaidFee) {
            this.totalPaidFee = totalPaidFee;
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }

        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
        }

        public String getUserAlias() {
            return userAlias;
        }

        public void setUserAlias(String userAlias) {
            this.userAlias = userAlias;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getEntryValue() {
            return entryValue;
        }

        public void setEntryValue(String entryValue) {
            this.entryValue = entryValue;
        }

        public static class PrizeStatusBean {
            /**
             * name : PRIZE_FINISH
             * message : 派奖结束
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

        public static class CreaterTypeBean {
            /**
             * name : USER_CREATE
             * message : 用户主动创建
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

    public static class GameBoardBean {
        /**
         * id : 2641428348907890000005059763
         * raceType : FOOTBALL
         * raceId : 2639611107607890000007908373
         * plate : -1.5
         * raceStageType : {"id":1,"code":"FULL","name":"全场","orderNum":1,"enabled":true,"gmtCreate":"2017-11-02 16:55:01","gmtModified":"2017-11-02 16:55:01"}
         * homeTeamName : 墨西哥美洲
         * guestTeamName : 克雷塔罗
         */

        private String id;
        private String raceType;
        private String raceId;
        private double plate;
        private RaceStageTypeBean raceStageType;
        private String homeTeamName;
        private String guestTeamName;
        private ArrayList<OrderCheerListEntity.GameBoardOption> gameBoardOptionEntries;

        public ArrayList<OrderCheerListEntity.GameBoardOption> getGameBoardOptionEntries() {
            return gameBoardOptionEntries;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRaceType() {
            return raceType;
        }

        public void setRaceType(String raceType) {
            this.raceType = raceType;
        }

        public String getRaceId() {
            return raceId;
        }

        public void setRaceId(String raceId) {
            this.raceId = raceId;
        }

        public double getPlate() {
            return plate;
        }

        public void setPlate(double plate) {
            this.plate = plate;
        }

        public RaceStageTypeBean getRaceStageType() {
            return raceStageType;
        }

        public void setRaceStageType(RaceStageTypeBean raceStageType) {
            this.raceStageType = raceStageType;
        }

        public String getHomeTeamName() {
            return homeTeamName;
        }

        public void setHomeTeamName(String homeTeamName) {
            this.homeTeamName = homeTeamName;
        }

        public String getGuestTeamName() {
            return guestTeamName;
        }

        public void setGuestTeamName(String guestTeamName) {
            this.guestTeamName = guestTeamName;
        }

        public static class RaceStageTypeBean {
            /**
             * id : 1
             * code : FULL
             * name : 全场
             * orderNum : 1
             * enabled : true
             * gmtCreate : 2017-11-02 16:55:01
             * gmtModified : 2017-11-02 16:55:01
             */

            private int id;
            private String code;
            private String name;
            private int orderNum;
            private boolean enabled;
            private String gmtCreate;
            private String gmtModified;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
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
        }
    }

    public static class OrderFundBillDetailBean {
        /**
         * imageUrl : http://mapi.xjq.net/giftImageUrl.htm?giftConfigId=24&timestamp=1509676319000
         * giftNo : 24
         * totalPaidFee : 500.0
         * prizeGoldFee : 500.0
         * prizeGiftFee : 0.0
         * payCount : 1
         */

        private String imageUrl;
        private int giftNo;
        private double totalPaidFee;
        private double prizeGoldFee;
        private double prizeGiftFee;
        private int payCount;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getGiftNo() {
            return giftNo;
        }

        public void setGiftNo(int giftNo) {
            this.giftNo = giftNo;
        }

        public double getTotalPaidFee() {
            return totalPaidFee;
        }

        public void setTotalPaidFee(double totalPaidFee) {
            this.totalPaidFee = totalPaidFee;
        }

        public double getPrizeGoldFee() {
            return prizeGoldFee;
        }

        public void setPrizeGoldFee(double prizeGoldFee) {
            this.prizeGoldFee = prizeGoldFee;
        }

        public double getPrizeGiftFee() {
            return prizeGiftFee;
        }

        public void setPrizeGiftFee(double prizeGiftFee) {
            this.prizeGiftFee = prizeGiftFee;
        }

        public int getPayCount() {
            return payCount;
        }

        public void setPayCount(int payCount) {
            this.payCount = payCount;
        }
    }
}
