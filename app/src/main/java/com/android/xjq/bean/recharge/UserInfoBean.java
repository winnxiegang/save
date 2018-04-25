package com.android.xjq.bean.recharge;

import java.util.HashMap;

/**
 * Created by lingjiu on 2017/6/8.
 */
public class UserInfoBean {
    /**
     * userId : 8201704060510016
     * loginName : lingjiu_01
     * thirdChannelAndLogoUrlMap : {}
     * userLevelPicUrl : http://livemapi.huohongshe.net/userLevelImg.resource?levelCode=lv5
     * avatarUrl : http://livemapi.huohongshe.net/userLogo.resource?userId=8201704060510016&timestamp=1491465028000
     */

    private String userId;
    private String loginName;
    private HashMap<String, String> thirdChannelAndLogoUrlMap;
    private String userLevelPicUrl;
    private String userLogoUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashMap<String, String> getThirdChannelAndLogoUrlMap() {
        return thirdChannelAndLogoUrlMap;
    }

    public void setThirdChannelAndLogoUrlMap(HashMap<String, String> thirdChannelAndLogoUrlMap) {
        this.thirdChannelAndLogoUrlMap = thirdChannelAndLogoUrlMap;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserLevelPicUrl() {
        return userLevelPicUrl;
    }

    public void setUserLevelPicUrl(String userLevelPicUrl) {
        this.userLevelPicUrl = userLevelPicUrl;
    }

    public String getUserLogoUrl() {
        return userLogoUrl;
    }

    public void setUserLogoUrl(String userLogoUrl) {
        this.userLogoUrl = userLogoUrl;
    }

    public static class ThirdChannelAndLogoUrlMapBean {
    }
}
