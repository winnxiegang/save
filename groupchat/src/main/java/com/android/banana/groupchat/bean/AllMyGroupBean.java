package com.android.banana.groupchat.bean;

import java.util.List;

/**
 * Created by zaozao on 2017/11/8.
 */

public class AllMyGroupBean {

    /**
     * groupIdByUserJoinList : ["2690800925704610980052407565","2667972344204610980053344184","2682822029304610980056257272","2677432073604610980057303867","2694460546904610980052008117"]
     * jumpLogin : false
     * nowDate : 2017-11-08 21:03:09
     * payloadOrderByUserList : [{"enabled":true,"gmtCreate":"2017-11-08 21:01:58","gmtModified":"2017-11-08 21:01:58","id":"2694611861704610980057501209","orderValue":1510146118612,"payloadId":"2694460546904610980052008117","payloadType":"GROUP","userId":"8201710318665838"},{"enabled":true,"gmtCreate":"2017-11-08 21:01:23","gmtModified":"2017-11-08 21:01:23","id":"2694608333104610980056868800","orderValue":1510146083239,"payloadId":"2690800925704610980052407565","payloadType":"GROUP","userId":"8201710318665838"}]
     * success : true
     */

    private List<String> groupIdByUserJoinList;
    private List<PayloadBean> payloadOrderByUserList;

    public List<String> getGroupIdByUserJoinList() {
        return groupIdByUserJoinList;
    }

    public void setGroupIdByUserJoinList(List<String> groupIdByUserJoinList) {
        this.groupIdByUserJoinList = groupIdByUserJoinList;
    }

    public List<PayloadBean> getPayloadOrderByUserList() {
        return payloadOrderByUserList;
    }

    public void setPayloadOrderByUserList(List<PayloadBean> payloadOrderByUserList) {
        this.payloadOrderByUserList = payloadOrderByUserList;
    }

    public static class PayloadBean {
        /**
         * enabled : true
         * gmtCreate : 2017-11-08 21:01:58
         * gmtModified : 2017-11-08 21:01:58
         * id : 2694611861704610980057501209
         * orderValue : 1510146118612
         * payloadId : 2694460546904610980052008117
         * payloadType : GROUP
         * userId : 8201710318665838
         */

        private boolean enabled;
        private String gmtCreate;
        private String gmtModified;
        private String id;
        private long orderValue;
        private String payloadId;
        private String payloadType;
        private String userId;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getOrderValue() {
            return orderValue;
        }

        public void setOrderValue(long orderValue) {
            this.orderValue = orderValue;
        }

        public String getPayloadId() {
            return payloadId;
        }

        public void setPayloadId(String payloadId) {
            this.payloadId = payloadId;
        }

        public String getPayloadType() {
            return payloadType;
        }

        public void setPayloadType(String payloadType) {
            this.payloadType = payloadType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
