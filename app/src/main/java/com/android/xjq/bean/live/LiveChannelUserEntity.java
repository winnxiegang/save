package com.android.xjq.bean.live;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by lingjiu on 2018/2/24.
 */

public class LiveChannelUserEntity {
    private long userCount;
    private List<ChannelUserEntity.ChannelUserBean> channelAreaUserInfoList;
    private List<AnchorUserInfoBean> anchorUserInfoList;
    //主主播
    @Expose
    private AnchorUserInfoBean firstAnchorUserInfo;

    public AnchorUserInfoBean getFirstAnchorUserInfo() {
        if (firstAnchorUserInfo != null)
            return firstAnchorUserInfo;
        if (anchorUserInfoList != null && anchorUserInfoList.size() > 0) {
            for (LiveChannelUserEntity.AnchorUserInfoBean anchorUserInfoBean : anchorUserInfoList) {
                //主主播
                if (anchorUserInfoBean.isUserAnchor()) {
                    firstAnchorUserInfo = anchorUserInfoBean;
                }
            }
        }
        return firstAnchorUserInfo;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    public List<ChannelUserEntity.ChannelUserBean> getChannelAreaUserInfoList() {
        return channelAreaUserInfoList;
    }

    public void setChannelAreaUserInfoList(List<ChannelUserEntity.ChannelUserBean> channelAreaUserInfoList) {
        this.channelAreaUserInfoList = channelAreaUserInfoList;
    }

    public List<AnchorUserInfoBean> getAnchorUserInfoList() {
        return anchorUserInfoList;
    }

    public void setAnchorUserInfoList(List<AnchorUserInfoBean> anchorUserInfoList) {
        this.anchorUserInfoList = anchorUserInfoList;
    }

    public static class AnchorUserInfoBean {
        //主播id
        private long anchorId;
        //主播用户id
        private String userId;
        //主播用户名
        private String userName;
        //主播头像地址
        private String userLogoUrl;
        //是否为主主播
        private boolean userAnchor;
        //粉丝数
        private long followMyUserCount;
        //是否关注
        private boolean focus;

        public long getAnchorId() {
            return anchorId;
        }

        public void setAnchorId(long anchorId) {
            this.anchorId = anchorId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }

        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
        }

        public boolean isUserAnchor() {
            return userAnchor;
        }

        public void setUserAnchor(boolean userAnchor) {
            this.userAnchor = userAnchor;
        }

        public long getFollowMyUserCount() {
            return followMyUserCount;
        }

        public void setFollowMyUserCount(long followMyUserCount) {
            this.followMyUserCount = followMyUserCount;
        }

        public boolean isFocus() {
            return focus;
        }

        public void setFocus(boolean focus) {
            this.focus = focus;
        }
    }


}
