package com.android.xjq.bean.live.main;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/4/6.
 */

public class ChannelUserBean {

    /**
     * levelCode : lv9
     * levelImageUrl : http://livemapi.huohongshe.net/userLevelImg.resource?levelCode=lv9
     * thirdChannelList : []
     * userId : 8201703290460008
     * userName : 苏氏二货
     */

    private String levelCode;
    private String levelImageUrl;
    private String userId;
    private String userName;
    private HashMap<String,String> thirdChannelAndLogoUrlMap;
    private String userLogoUrl;
    @Expose
    private List<String>  userMedalList;

    public ChannelUserBean(){}

    public ChannelUserBean(String userName){
        this.userName = userName;
    }


    public HashMap<String, String> getThirdChannelAndLogoUrlMap() {
        return thirdChannelAndLogoUrlMap;
    }

    public void setThirdChannelAndLogoUrlMap(HashMap<String, String> thirdChannelAndLogoUrlMap) {
        this.thirdChannelAndLogoUrlMap = thirdChannelAndLogoUrlMap;
    }

    public List<String> getUserMedalList() {
        return userMedalList;
    }

    public void setUserMedalList(List<String> userMedalList) {
        this.userMedalList = userMedalList;
    }

    public String getUserLogoUrl() {
        return userLogoUrl;
    }

    public void setUserLogoUrl(String userLogoUrl) {
        this.userLogoUrl = userLogoUrl;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getLevelImageUrl() {
        return levelImageUrl;
    }

    public void setLevelImageUrl(String levelImageUrl) {
        this.levelImageUrl = levelImageUrl;
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

}
