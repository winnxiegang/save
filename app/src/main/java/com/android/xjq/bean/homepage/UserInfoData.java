package com.android.xjq.bean.homepage;

import com.android.xjq.bean.UserMedalLevelBean;

import java.util.List;

/**
 * Created by zaozao on 2018/2/23.
 * 用处：个人主页头部数据
 */

public class UserInfoData {

    /**
     * canInitiatePrivateChat : false
     * follow : false
     * jumpLogin : false
     * loginName : 我是追光少年
     * nowDate : 2018-02-23 17:23:59
     * success : true
     * userAttentionNum : 0
     * userFollowsNum : 0
     * userId : 8201711278725840
     * channelAreaId
     * inChannelArea
     * userLogoUrl : http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711278725840&timestamp=
     */

    private boolean canInitiatePrivateChat;
    private boolean follow;
    private String loginName;
    private String userAttentionNum;
    private String userFollowsNum;
    private String userId;
    private String userLogoUrl;
    public int channelAreaId;
    public boolean inChannelArea;
    public List<UserMedalLevelBean> userMedalLevelList;

    public boolean isCanInitiatePrivateChat() {
        return canInitiatePrivateChat;
    }

    public void setCanInitiatePrivateChat(boolean canInitiatePrivateChat) {
        this.canInitiatePrivateChat = canInitiatePrivateChat;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }


    public String getUserAttentionNum() {
        return userAttentionNum;
    }

    public void setUserAttentionNum(String userAttentionNum) {
        this.userAttentionNum = userAttentionNum;
    }

    public String getUserFollowsNum() {
        return userFollowsNum;
    }

    public void setUserFollowsNum(String userFollowsNum) {
        this.userFollowsNum = userFollowsNum;
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
}
