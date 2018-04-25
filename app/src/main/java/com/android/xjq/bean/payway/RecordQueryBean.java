package com.android.xjq.bean.payway;

import java.util.ArrayList;

/**
 * Created by ajiao on 2018\3\7 0007.
 */

public class RecordQueryBean {
    /**
     * activityPrizeRecordList : [{"amount":0,"areaName":"德乙","channelAreaId":100091,"gmtCreate":"2018-03-07 20:43:10","masterAnchorId":"8201711018675831","masterAnchorUrl":"http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711018675831&timestamp=1520430384504","patronLoginName":"超凶的倩姐","prizeItemName":"广州机票一张"},{"amount":0,"areaName":"德乙","channelAreaId":100091,"gmtCreate":"2018-03-07 20:43:10","masterAnchorId":"8201711018675831","masterAnchorUrl":"http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711018675831&timestamp=1520430384504","patronLoginName":"超凶的倩姐","prizeItemName":"广州机票一张"},{"amount":0,"areaName":"德乙","channelAreaId":100091,"gmtCreate":"2018-03-07 21:09:10","masterAnchorId":"8201711018675831","masterAnchorUrl":"http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711018675831&timestamp=1520430384504","patronLoginName":"超凶的倩姐","prizeItemName":"广州双人五日游"},{"amount":0,"areaName":"德乙","channelAreaId":100091,"gmtCreate":"2018-03-07 21:09:10","masterAnchorId":"8201711018675831","masterAnchorUrl":"http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711018675831&timestamp=1520430384504","patronLoginName":"超凶的倩姐","prizeItemName":"广州双人五日游"}]
     * jumpLogin : false
     * nowDate : 2018-03-07 21:46:24
     * paginator : {"items":4,"itemsPerPage":20,"page":1,"pages":1}
     * success : true
     */

    private boolean jumpLogin;
    private String nowDate;
    private PaginatorBean paginator;
    private boolean success;
    private ArrayList<PrizeRecordBean> activityPrizeRecordList;

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

    public ArrayList<PrizeRecordBean> getPrizeRecordList() {
        return activityPrizeRecordList;
    }

    public void setActivityPrizeRecordList(ArrayList<PrizeRecordBean> activityPrizeRecordList) {
        this.activityPrizeRecordList = activityPrizeRecordList;
    }

    public static class PaginatorBean {
        /**
         * items : 4
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

    public static class PrizeRecordBean {
        /**
         * amount : 0.0
         * areaName : 德乙
         * channelAreaId : 100091
         * gmtCreate : 2018-03-07 20:43:10
         * masterAnchorId : 8201711018675831
         * masterAnchorUrl : http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711018675831&timestamp=1520430384504
         * patronLoginName : 超凶的倩姐
         * prizeItemName : 广州机票一张
         */

        private double amount;
        private String areaName;
        private int channelAreaId;
        private String gmtCreate;
        private String masterAnchorId;
        private String masterAnchorUrl;
        private String patronLoginName;
        private String prizeItemName;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public int getChannelAreaId() {
            return channelAreaId;
        }

        public void setChannelAreaId(int channelAreaId) {
            this.channelAreaId = channelAreaId;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getMasterAnchorId() {
            return masterAnchorId;
        }

        public void setMasterAnchorId(String masterAnchorId) {
            this.masterAnchorId = masterAnchorId;
        }

        public String getMasterAnchorUrl() {
            return masterAnchorUrl;
        }

        public void setMasterAnchorUrl(String masterAnchorUrl) {
            this.masterAnchorUrl = masterAnchorUrl;
        }

        public String getPatronLoginName() {
            return patronLoginName;
        }

        public void setPatronLoginName(String patronLoginName) {
            this.patronLoginName = patronLoginName;
        }

        public String getPrizeItemName() {
            return prizeItemName;
        }

        public void setPrizeItemName(String prizeItemName) {
            this.prizeItemName = prizeItemName;
        }
    }

}
