package com.android.xjq.bean.live;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/4/25.
 */

public class ChannelUserEntity implements BaseOperator {


    private int micCount;
    private int userCount;
    private List<ChannelUserBean> channelUserInfoList;
    private int maxUserCount;
    private HashMap<String, List<String>> userIdAndManageRoleCodesMap;

    @Override
    public void operatorData() {
        //添加勋章图标
        if (channelUserInfoList != null && channelUserInfoList.size() > 0) {
            for (ChannelUserBean channelUserBean : channelUserInfoList) {

                List<String> userMedalList = new ArrayList<>();
                HashMap<String, String> thirdChannelAndLogoUrlMap = channelUserBean.getThirdChannelAndLogoUrlMap();
                if (thirdChannelAndLogoUrlMap != null && thirdChannelAndLogoUrlMap.size() > 0) {
                    for (String key : thirdChannelAndLogoUrlMap.keySet()) {
                        userMedalList.add(thirdChannelAndLogoUrlMap.get(key));
                    }
                }
                if (channelUserBean.getLevelImageUrl() != null) {
                    userMedalList.add(channelUserBean.getLevelImageUrl());
                }
                channelUserBean.setUserMedalList(userMedalList);

                if (userIdAndManageRoleCodesMap != null && userIdAndManageRoleCodesMap.size() > 0) {
                    if (userIdAndManageRoleCodesMap.containsKey(channelUserBean.getUserId())) {
                        List<String> horseList = userIdAndManageRoleCodesMap.get(channelUserBean.getUserId());
                        channelUserBean.setUserHorseList(horseList);
                    }
                }
            }
        }

        //添加游客
        /*if (channelUserInfoList == null) channelUserInfoList = new ArrayList<>();
        if (channelUserInfoList.size() < userCount) {
            int size = channelUserInfoList.size();
            int max = userCount > maxUserCount ? maxUserCount : userCount;
            for (int i = channelUserInfoList.size() + 1; i <= max; i++) {
                channelUserInfoList.add(new ChannelUserBean("游客" + (i - size)));
            }
        }*/
    }

    public void setUserIdAndManageRoleCodesMap(HashMap<String, List<String>> userIdAndManageRoleCodesMap) {
        this.userIdAndManageRoleCodesMap = userIdAndManageRoleCodesMap;
    }

    public int getMaxUserCount() {
        return maxUserCount;
    }

    public void setMaxUserCount(int maxUserCount) {
        this.maxUserCount = maxUserCount;
    }

    public int getMicCount() {
        return micCount;
    }

    public void setMicCount(int micCount) {
        this.micCount = micCount;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public List<ChannelUserBean> getChannelUserInfoList() {
        return channelUserInfoList;
    }

    public void setChannelUserInfoList(List<ChannelUserBean> channelUserInfoList) {
        this.channelUserInfoList = channelUserInfoList;
    }

    public static class ChannelUserBean {
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
        private String loginName;
        private HashMap<String, String> thirdChannelAndLogoUrlMap;
        private String userLogoUrl;
        @Expose
        private List<String> userMedalList;
        @Expose
        private List<String> userHorseList;

        public ChannelUserBean() {
        }

        public List<String> getUserHorseList() {
            return userHorseList;
        }

        public void setUserHorseList(List<String> userHorseList) {
            this.userHorseList = userHorseList;
        }

        public ChannelUserBean(String loginName) {
            this.loginName = loginName;
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

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }
    }
}
