package com.android.xjq.bean.guest;

import com.android.xjq.bean.live.BaseOperator;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by lingjiu on 2017/7/31.
 */

public class SeatApplyInfoBean implements BaseOperator {


    /**
     * success : true
     * nowDate : 2017-07-31 11:14:30
     * jumpLogin : false
     * applyUserName : lingjiu_01
     * seatPrice : 1.0
     * seatIssues : [{"id":6084,"channelId":200042,"issueNo":20170801,"enabled":true,"gmtStart":"2017-08-01 00:00:00","gmtEnd":"2017-08-01 23:59:59","status":{"name":"NORMAL","message":"正常状态"},"applyCount":0},{"id":6119,"channelId":200042,"issueNo":20170802,"enabled":true,"gmtStart":"2017-08-02 00:00:00","gmtEnd":"2017-08-02 23:59:59","status":{"name":"NORMAL","message":"正常状态"},"applyCount":0},{"id":6150,"channelId":200042,"issueNo":20170803,"enabled":true,"gmtStart":"2017-08-03 00:00:00","gmtEnd":"2017-08-03 23:59:59","status":{"name":"NORMAL","message":"正常状态"},"applyCount":0},{"id":6181,"channelId":200042,"issueNo":20170804,"enabled":true,"gmtStart":"2017-08-04 00:00:00","gmtEnd":"2017-08-04 23:59:59","status":{"name":"NORMAL","message":"正常状态"},"applyCount":0}]
     * maxApplyCount : 200
     * enableTime : 7
     * appliable : false
     */

    private String applyUserName;
    private int maxApplyCount;
    private double seatPrice;
    private int enableTime;
    private boolean appliable;
    private List<SeatIssuesBean> seatIssues;
    //已申请的座位列表
    private List<String> appliedIssueIds;

    @Override
    public void operatorData() {

        if (seatIssues != null && seatIssues.size() > 0) {
            for (SeatIssuesBean seatIssue : seatIssues) {
                if (appliedIssueIds != null && appliedIssueIds.size() > 0) {
                    if (appliedIssueIds.contains(String.valueOf(seatIssue.getId()))) {
                        seatIssue.setAppliedIssue(true);
                    }
                }
                seatIssue.setIssueFull(seatIssue.getApplyCount() == maxApplyCount);
            }
        }
    }

    public List<String> getAppliedIssueIds() {
        return appliedIssueIds;
    }

    public void setAppliedIssueIds(List<String> appliedIssueIds) {
        this.appliedIssueIds = appliedIssueIds;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public double getSeatPrice() {
        return seatPrice;
    }

    public void setSeatPrice(double seatPrice) {
        this.seatPrice = seatPrice;
    }

    public int getMaxApplyCount() {
        return maxApplyCount;
    }

    public void setMaxApplyCount(int maxApplyCount) {
        this.maxApplyCount = maxApplyCount;
    }

    public int getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(int enableTime) {
        this.enableTime = enableTime;
    }

    public boolean isAppliable() {
        return appliable;
    }

    public void setAppliable(boolean appliable) {
        this.appliable = appliable;
    }

    public List<SeatIssuesBean> getSeatIssues() {
        return seatIssues;
    }

    public void setSeatIssues(List<SeatIssuesBean> seatIssues) {
        this.seatIssues = seatIssues;
    }

    public static class SeatIssuesBean {
        /**
         * id : 6084
         * channelId : 200042
         * issueNo : 20170801
         * enabled : true
         * gmtStart : 2017-08-01 00:00:00
         * gmtEnd : 2017-08-01 23:59:59
         * status : {"name":"NORMAL","message":"正常状态"}
         * applyCount : 0
         */

        private int id;
        private int channelId;
        private int issueNo;
        private boolean enabled;
        private String gmtStart;
        private String gmtEnd;
        private StatusBean status;
        private int applyCount;
        @Expose
        private boolean isAppliedIssue;
        @Expose
        private boolean isIssueFull;
        @Expose
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public boolean issueFull() {
            return isIssueFull;
        }

        public void setIssueFull(boolean issueFull) {
            isIssueFull = issueFull;
        }

        public boolean isAppliedIssue() {
            return isAppliedIssue;
        }

        public void setAppliedIssue(boolean appliedIssue) {
            isAppliedIssue = appliedIssue;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getChannelId() {
            return channelId;
        }

        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }

        public int getIssueNo() {
            return issueNo;
        }

        public void setIssueNo(int issueNo) {
            this.issueNo = issueNo;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getGmtStart() {
            return gmtStart;
        }

        public void setGmtStart(String gmtStart) {
            this.gmtStart = gmtStart;
        }

        public String getGmtEnd() {
            return gmtEnd;
        }

        public void setGmtEnd(String gmtEnd) {
            this.gmtEnd = gmtEnd;
        }

        public StatusBean getStatus() {
            return status;
        }

        public void setStatus(StatusBean status) {
            this.status = status;
        }

        public int getApplyCount() {
            return applyCount;
        }

        public void setApplyCount(int applyCount) {
            this.applyCount = applyCount;
        }

        public static class StatusBean {
            /**
             * name : NORMAL
             * message : 正常状态
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
