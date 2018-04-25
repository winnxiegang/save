package com.android.xjq.bean.live;

/**
 * Created by zhouyi on 2017/4/5.
 */

public class ChannelInfoBean {

    private ChannelInfo channelInfo;

    private AnchorUserInfoBean anchorUserInfo;

    private int channelNum;

    public ChannelInfo getChannelInfo() {
        return channelInfo;
    }

    public void setChannelInfo(ChannelInfo channelInfo) {
        this.channelInfo = channelInfo;
    }

    public AnchorUserInfoBean getAnchorUserInfo() {
        return anchorUserInfo;
    }

    public void setAnchorUserInfo(AnchorUserInfoBean anchorUserInfo) {
        this.anchorUserInfo = anchorUserInfo;
    }

    public int getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(int channelNum) {
        this.channelNum = channelNum;
    }

    public static class ChannelInfo {
        /**
         * id : 200003
         * channelCode : 200003
         * channelName : 200001
         * roomId : 100000
         * channelTypeId : 1
         * summary : 测试摘要
         * notice : 测试通知
         * anchorId : 100007
         * memo : 测试备注
         * channelTitle : 很凶的倩姐
         */

        private int id;
        private String channelCode;
        private String channelName;
        private int roomId;
        private int channelTypeId;
        private String summary;
        private String notice;
        private int anchorId;
        private String memo;
        private String channelTitle;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getChannelCode() {
            return channelCode;
        }

        public void setChannelCode(String channelCode) {
            this.channelCode = channelCode;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public int getChannelTypeId() {
            return channelTypeId;
        }

        public void setChannelTypeId(int channelTypeId) {
            this.channelTypeId = channelTypeId;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public int getAnchorId() {
            return anchorId;
        }

        public void setAnchorId(int anchorId) {
            this.anchorId = anchorId;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getChannelTitle() {
            return channelTitle;
        }

        public void setChannelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
        }
    }

    public static class AnchorUserInfoBean {
        /**
         * userId : 8201702240300010
         * loginName : 主播4
         * userLevel : lv1
         * avatarUrl : http://livemapi.huohongshe.net/userLogo.resource?userId=8201702240300010
         */

        private String userId;
        private String loginName;
        private String userLogoUrl;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }
        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
        }
    }

}
