package com.android.xjq.bean.payway;

import java.util.ArrayList;

/**
 * Created by ajiao on 2018\3\7 0007.
 */

public class PKBean {
    /**
     * jumpLogin : false
     * nowDate : 2018-03-07 19:42:03
     * paginator : {"items":1,"itemsPerPage":20,"page":1,"pages":1}
     * purchaseOrderSummaryClientSimpleList : [{"createrType":{"message":"用户主动创建","name":"USER_CREATE"},"entryBizId":"4000628290877907890000007041","entryValue":"能","gmtCreate":"2018-03-07 19:35:42","gmtModified":"2018-03-07 19:40:18","id":"4000629134485507890000002084","orderStatus":"WIN","payCount":1,"pkgCode":"SILVER_TREASURE_CHEST","prizeFee":119.86,"prizeStatus":{"message":"派奖结束","name":"PRIZE_FINISH"},"sourceId":"100022","title":"这次创建能否一发入魂？","totalFee":100,"totalPaidFee":100,"userAlias":"大鱼1","userId":"8201711218690389","userLogoUrl":"http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711018675831&timestamp=","win":true}]
     * success : true
     */

    private boolean jumpLogin;
    private String nowDate;
    private PaginatorBean paginator;
    private boolean success;
    private ArrayList<PurchaseListBean> purchaseOrderSummaryClientSimpleList;

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

    public ArrayList<PurchaseListBean> getPurchaseList() {
        return purchaseOrderSummaryClientSimpleList;
    }

    public void setPurchaseOrderSummaryClientSimpleList(ArrayList<PurchaseListBean> purchaseOrderSummaryClientSimpleList) {
        this.purchaseOrderSummaryClientSimpleList = purchaseOrderSummaryClientSimpleList;
    }

    public static class PaginatorBean {
        /**
         * items : 1
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

    public static class PurchaseListBean {
        /**
         * createrType : {"message":"用户主动创建","name":"USER_CREATE"}
         * entryBizId : 4000628290877907890000007041
         * entryValue : 能
         * gmtCreate : 2018-03-07 19:35:42
         * gmtModified : 2018-03-07 19:40:18
         * id : 4000629134485507890000002084
         * orderStatus : WIN
         * payCount : 1
         * pkgCode : SILVER_TREASURE_CHEST
         * prizeFee : 119.86
         * prizeStatus : {"message":"派奖结束","name":"PRIZE_FINISH"}
         * sourceId : 100022
         * title : 这次创建能否一发入魂？
         * totalFee : 100.0
         * totalPaidFee : 100.0
         * userAlias : 大鱼1
         * userId : 8201711218690389
         * userLogoUrl : http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711018675831&timestamp=
         * win : true
         */

        private CreaterTypeBean createrType;
        private String entryBizId;
        private String entryValue;
        private String gmtCreate;
        private String gmtModified;
        private String id;
        private String orderStatus;
        private int payCount;
        private String pkgCode;
        private double prizeFee;
        private PrizeStatusBean prizeStatus;
        private String sourceId;
        private String title;
        private double totalFee;
        private double totalPaidFee;
        private String userAlias;
        private String userId;
        private String userLogoUrl;
        private boolean win;

        public String getPkgImageUrl() {
            return pkgImageUrl;
        }

        public void setPkgImageUrl(String pkgImageUrl) {
            this.pkgImageUrl = pkgImageUrl;
        }

        private String pkgImageUrl;

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

        public String getPkgCode() {
            return pkgCode;
        }

        public void setPkgCode(String pkgCode) {
            this.pkgCode = pkgCode;
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

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getUserAlias() {
            return userAlias;
        }

        public void setUserAlias(String userAlias) {
            this.userAlias = userAlias;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }

        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
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

        public static class PrizeStatusBean {
            /**
             * message : 派奖结束
             * name : PRIZE_FINISH
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
