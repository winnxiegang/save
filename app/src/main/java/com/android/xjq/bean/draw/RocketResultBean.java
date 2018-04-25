package com.android.xjq.bean.draw;

/**
 * Created by zaozao on 2018/3/8.
 */

public class RocketResultBean {

    /**
     * success : true
     * nowDate : 2018-03-08 20:42:44
     * jumpLogin : false
     * issueStatus : {"name":"NORMAL","message":"正常"}
     */

    private IssueStatusBean issueStatus;

    private String rocketResult;

    public String getRocketResult() {
        return rocketResult;
    }

    public void setRocketResult(String rocketResult) {
        this.rocketResult = rocketResult;
    }

    public IssueStatusBean getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(IssueStatusBean issueStatus) {
        this.issueStatus = issueStatus;
    }

    public static class IssueStatusBean {
        /**
         * name : NORMAL
         * message : 正常
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
