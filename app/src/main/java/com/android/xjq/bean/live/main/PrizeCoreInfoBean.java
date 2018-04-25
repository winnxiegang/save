package com.android.xjq.bean.live.main;

import java.util.List;

/**
 * Created by lingjiu on 2017/4/17.
 */

public class PrizeCoreInfoBean {


    /**
     * detailUrl : http://livemweb.huohongshe.net/prizecore/queryActiveIssuesInfo.htm
     * issueList : [{"checkContinuePrize":"SPECIAL","drawType":{"message":"根据开奖结果和期次开奖时间","name":"BY_PRIZE_RESULT_AND_ISSUE_GMT_PRIZE"},"gmtCreate":"2017-04-17 04:45:00","gmtModified":"2017-04-17 10:16:50","gmtStartPrize":"2017-04-17 09:44:50","gmtStartSell":"2017-04-17 09:44:50","gmtStopPrize":"2017-04-17 10:44:50","gmtStopSell":"2017-04-17 10:44:50","id":7057,"issueAmount":3880,"issueNo":"MVP_170417_00010","issueStatus":{"message":"初始状态","name":"INIT"},"issueType":"MVP","previousCumulativeAmount":7510,"prized":true,"properties":{}},{"checkContinuePrize":"SPECIAL","drawType":{"message":"根据开奖结果和期次开奖时间","name":"BY_PRIZE_RESULT_AND_ISSUE_GMT_PRIZE"},"gmtCreate":"2017-04-17 04:45:00","gmtModified":"2017-04-17 09:44:52","gmtStartPrize":"2017-04-17 09:44:50","gmtStartSell":"2017-04-17 09:44:50","gmtStopPrize":"2017-04-17 10:44:50","gmtStopSell":"2017-04-17 10:44:50","id":7056,"issueAmount":27500,"issueNo":"WORLD_CUP_170417_00010","issueStatus":{"message":"初始状态","name":"INIT"},"issueType":"WORLD_CUP","previousCumulativeAmount":27500,"prized":true,"properties":{}}]
     */

    private String detailUrl;
    private List<IssueListBean> issueList;

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public List<IssueListBean> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<IssueListBean> issueList) {
        this.issueList = issueList;
    }

    public static class IssueListBean {
        /**
         * checkContinuePrize : SPECIAL
         * drawType : {"message":"根据开奖结果和期次开奖时间","name":"BY_PRIZE_RESULT_AND_ISSUE_GMT_PRIZE"}
         * gmtCreate : 2017-04-17 04:45:00
         * gmtModified : 2017-04-17 10:16:50
         * gmtStartPrize : 2017-04-17 09:44:50
         * gmtStartSell : 2017-04-17 09:44:50
         * gmtStopPrize : 2017-04-17 10:44:50
         * gmtStopSell : 2017-04-17 10:44:50
         * id : 7057
         * issueAmount : 3880.0
         * issueNo : MVP_170417_00010
         * issueStatus : {"message":"初始状态","name":"INIT"}
         * issueType : MVP
         * previousCumulativeAmount : 7510.0
         * prized : true
         * properties : {}
         */

        private String checkContinuePrize;
        private DrawTypeBean drawType;
        private String gmtCreate;
        private String gmtModified;
        private String gmtStartPrize;
        private String gmtStartSell;
        private String gmtStopPrize;
        private String gmtStopSell;
        private int id;
        private double issueAmount;
        private String issueNo;
        private IssueStatusBean issueStatus;
        private String issueType;
        private double previousCumulativeAmount;
        private boolean prized;
        private PropertiesBean properties;

        public String getCheckContinuePrize() {
            return checkContinuePrize;
        }

        public void setCheckContinuePrize(String checkContinuePrize) {
            this.checkContinuePrize = checkContinuePrize;
        }

        public DrawTypeBean getDrawType() {
            return drawType;
        }

        public void setDrawType(DrawTypeBean drawType) {
            this.drawType = drawType;
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

        public String getGmtStartPrize() {
            return gmtStartPrize;
        }

        public void setGmtStartPrize(String gmtStartPrize) {
            this.gmtStartPrize = gmtStartPrize;
        }

        public String getGmtStartSell() {
            return gmtStartSell;
        }

        public void setGmtStartSell(String gmtStartSell) {
            this.gmtStartSell = gmtStartSell;
        }

        public String getGmtStopPrize() {
            return gmtStopPrize;
        }

        public void setGmtStopPrize(String gmtStopPrize) {
            this.gmtStopPrize = gmtStopPrize;
        }

        public String getGmtStopSell() {
            return gmtStopSell;
        }

        public void setGmtStopSell(String gmtStopSell) {
            this.gmtStopSell = gmtStopSell;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getIssueAmount() {
            return issueAmount;
        }

        public void setIssueAmount(double issueAmount) {
            this.issueAmount = issueAmount;
        }

        public String getIssueNo() {
            return issueNo;
        }

        public void setIssueNo(String issueNo) {
            this.issueNo = issueNo;
        }

        public IssueStatusBean getIssueStatus() {
            return issueStatus;
        }

        public void setIssueStatus(IssueStatusBean issueStatus) {
            this.issueStatus = issueStatus;
        }

        public String getIssueType() {
            return issueType;
        }

        public void setIssueType(String issueType) {
            this.issueType = issueType;
        }

        public double getPreviousCumulativeAmount() {
            return previousCumulativeAmount;
        }

        public void setPreviousCumulativeAmount(double previousCumulativeAmount) {
            this.previousCumulativeAmount = previousCumulativeAmount;
        }

        public boolean isPrized() {
            return prized;
        }

        public void setPrized(boolean prized) {
            this.prized = prized;
        }

        public PropertiesBean getProperties() {
            return properties;
        }

        public void setProperties(PropertiesBean properties) {
            this.properties = properties;
        }

        public static class DrawTypeBean {
            /**
             * message : 根据开奖结果和期次开奖时间
             * name : BY_PRIZE_RESULT_AND_ISSUE_GMT_PRIZE
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

        public static class IssueStatusBean {
        }

        public static class PropertiesBean {
        }
    }
}
