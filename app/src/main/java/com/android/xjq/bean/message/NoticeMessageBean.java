package com.android.xjq.bean.message;

/**
 * Created by zhouyi on 2016/1/7 16:31.
 */
public class NoticeMessageBean {


    private AccessCodeAndNewCountBean accessCodeAndNewCount;

    public void setAccessCodeAndNewCount(AccessCodeAndNewCountBean accessCodeAndNewCount) {
        this.accessCodeAndNewCount = accessCodeAndNewCount;
    }

    public AccessCodeAndNewCountBean getAccessCodeAndNewCount() {
        return accessCodeAndNewCount;
    }

    public static class AccessCodeAndNewCountBean {
        private int TOPIC;
        private int REPLY;
        private int MENTION;
        private int SUBSCRIBE;
        private int USER_MESSAGE_NOTICE;

        public void setTOPIC(int TOPIC) {
            this.TOPIC = TOPIC;
        }

        public void setREPLY(int REPLY) {
            this.REPLY = REPLY;
        }

        public void setMENTION(int MENTION) {
            this.MENTION = MENTION;
        }

        public void setSUBSCRIBE(int SUBSCRIBE) {
            this.SUBSCRIBE = SUBSCRIBE;
        }

        public int getTOPIC() {
            return TOPIC;
        }

        public int getREPLY() {
            return REPLY;
        }

        public int getMENTION() {
            return MENTION;
        }

        public int getSUBSCRIBE() {
            return SUBSCRIBE;
        }

        public int getUSER_MESSAGE_NOTICE() {
            return USER_MESSAGE_NOTICE;
        }

        public void setUSER_MESSAGE_NOTICE(int USER_MESSAGE_NOTICE) {
            this.USER_MESSAGE_NOTICE = USER_MESSAGE_NOTICE;
        }
    }
}
