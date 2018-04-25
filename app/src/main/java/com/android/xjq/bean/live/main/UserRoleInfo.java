package com.android.xjq.bean.live.main;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/6/8.
 */
public class UserRoleInfo {

    private String userId;
    private String loginName;
    private String userLevelPicUrl;
    private String avatarUrl;
    private HashMap<String, String> thirdChannelAndLogoUrlMap;
    /**
     * 是否为当前的角色
     */
    @Expose
    private boolean isCurrentRole;

    /**
     * 是否为默认的角色
     */
    @Expose
    private boolean isDefaultRole;
    @Expose
    private List<String> userMedalList;

    public List<String> getUserMedalList() {
        return userMedalList;
    }

    public void setUserMedalList(List<String> userMedalList) {
        this.userMedalList = userMedalList;
    }

    public HashMap<String, String> getThirdChannelAndLogoUrlMap() {
        return thirdChannelAndLogoUrlMap;
    }

    public void setThirdChannelAndLogoUrlMap(HashMap<String, String> thirdChannelAndLogoUrlMap) {
        this.thirdChannelAndLogoUrlMap = thirdChannelAndLogoUrlMap;
    }

    public boolean isCurrentRole() {
        return isCurrentRole;
    }

    public void setCurrentRole(boolean currentRole) {
        isCurrentRole = currentRole;
    }

    public boolean isDefaultRole() {
        return isDefaultRole;
    }

    public void setDefaultRole(boolean defaultRole) {
        isDefaultRole = defaultRole;
    }


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

    public String getUserLevelPicUrl() {
        return userLevelPicUrl;
    }

    public void setUserLevelPicUrl(String userLevelPicUrl) {
        this.userLevelPicUrl = userLevelPicUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
