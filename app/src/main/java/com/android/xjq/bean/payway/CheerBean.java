package com.android.xjq.bean.payway;

import java.util.ArrayList;

/**
 * Created by ajiao on 2018\1\29 0029.
 */

public class CheerBean {

    private boolean jumpLogin;
    private String nowDate;
    private PaginatorBean paginator;
    private boolean success;
    private ArrayList<PurchaseOrderBean> purchaseOrderSummaryClientSimpleList;

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<PurchaseOrderBean> getPurchaseOrderList() {
        return purchaseOrderSummaryClientSimpleList;
    }

    public void setPurchaseOrderSummaryClientSimpleList(ArrayList<PurchaseOrderBean> purchaseOrderSummaryClientSimpleList) {
        this.purchaseOrderSummaryClientSimpleList = purchaseOrderSummaryClientSimpleList;
    }

    public static class PaginatorBean {
        /**
         * items : 3
         * itemsPerPage : 20
         * page : 1
         * pages : 1
         */

        private int items;
        private int itemsPerPage;
        private int page;
        private int pages;

        public int getItems() {
            return items;
        }

        public void setItems(int items) {
            this.items = items;
        }

        public int getItemsPerPage() {
            return itemsPerPage;
        }

        public void setItemsPerPage(int itemsPerPage) {
            this.itemsPerPage = itemsPerPage;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }
    }

    public static class PurchaseOrderBean {
        /**
         * createrType : {"message":"用户主动创建","name":"USER_CREATE"}
         * entryBizId : 4000625884096907890970012285
         * entryValue : GUEST_WIN
         * gameBoardClientSimple : {"guestTeamName":"索非亚列夫斯基","homeTeamName":"查洛摩利","id":"4000625884096907890970012285","innerGuestTeamId":0,"innerHomeTeamId":0,"plate":-10.5,"raceId":"4000625881257707890970011605","raceStageType":{"code":"FULL","enabled":true,"gmtCreate":"2017-11-02 16:55:01","gmtModified":"2017-11-11 20:56:53","id":1,"name":"全场","orderNum":1},"raceType":"FOOTBALL","sourceId":"4000625881257707890970011605"}
         * gmtCreate : 2018-03-07 14:14:00
         * gmtModified : 2018-03-07 14:14:00
         * id : 4000627204030207890970018937
         * orderStatus : PROGRESSING
         * payCount : 1
         * prizeFee : 0.0
         * prizeStatus : {"message":"初始","name":"INIT"}
         * totalFee : 500.0
         * totalPaidFee : 500.0
         * userId : 8201711218690389
         * win : false
         */

        private CreaterTypeBean createrType;
        private String entryBizId;
        private String entryValue;
        private GameBoardClientSimpleBean gameBoardClientSimple;
        private String gmtCreate;
        private String gmtModified;
        private String id;
        private String orderStatus;
        private int payCount;
        private double prizeFee;
        private PrizeStatusBean prizeStatus;
        private double totalFee;
        private double totalPaidFee;
        private String userId;
        private boolean win;

        public CreaterTypeBean getCreaterType() {
            return createrType;
        }

        public void setCreaterType(CreaterTypeBean createrType) {
            this.createrType = createrType;
        }

        public String getEntryBizId() {
            return entryBizId;
        }

        public void setEntryBizId(String entryBizId) {
            this.entryBizId = entryBizId;
        }

        public String getEntryValue() {
            return entryValue;
        }

        public void setEntryValue(String entryValue) {
            this.entryValue = entryValue;
        }

        public GameBoardClientSimpleBean getGameBoardClientSimple() {
            return gameBoardClientSimple;
        }

        public void setGameBoardClientSimple(GameBoardClientSimpleBean gameBoardClientSimple) {
            this.gameBoardClientSimple = gameBoardClientSimple;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getPayCount() {
            return payCount;
        }

        public void setPayCount(int payCount) {
            this.payCount = payCount;
        }

        public double getPrizeFee() {
            return prizeFee;
        }

        public void setPrizeFee(double prizeFee) {
            this.prizeFee = prizeFee;
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

        public double getTotalPaidFee() {
            return totalPaidFee;
        }

        public void setTotalPaidFee(double totalPaidFee) {
            this.totalPaidFee = totalPaidFee;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public boolean isWin() {
            return win;
        }

        public void setWin(boolean win) {
            this.win = win;
        }

        public static class CreaterTypeBean {
            /**
             * message : 用户主动创建
             * name : USER_CREATE
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

        public static class GameBoardClientSimpleBean {
            /**
             * guestTeamName : 索非亚列夫斯基
             * homeTeamName : 查洛摩利
             * id : 4000625884096907890970012285
             * innerGuestTeamId : 0
             * innerHomeTeamId : 0
             * plate : -10.5
             * raceId : 4000625881257707890970011605
             * raceStageType : {"code":"FULL","enabled":true,"gmtCreate":"2017-11-02 16:55:01","gmtModified":"2017-11-11 20:56:53","id":1,"name":"全场","orderNum":1}
             * raceType : FOOTBALL
             * sourceId : 4000625881257707890970011605
             */

            private String guestTeamName;
            private String homeTeamName;
            private String id;
            private int innerGuestTeamId;
            private int innerHomeTeamId;
            private double plate;
            private String raceId;
            private RaceStageTypeBean raceStageType;
            private String raceType;
            private String sourceId;

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

            public double getPlate() {
                return plate;
            }

            public void setPlate(double plate) {
                this.plate = plate;
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

            public String getSourceId() {
                return sourceId;
            }

            public void setSourceId(String sourceId) {
                this.sourceId = sourceId;
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
        }

        public static class PrizeStatusBean {
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
    }

}
