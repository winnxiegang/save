package com.android.xjq.bean.live.main;

import com.android.xjq.bean.live.BaseOperator;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/3/31.
 */

public class SearchResultBean implements BaseOperator {

    private String searchText;
    private HashMap<String, String> channelIdAndUserIdMap;
    private List<ChannelInfoSimpleBean> channelInfoSimple;
    private List<ChannelInfoSimpleBean> channelUserInfoList;

    @Expose
    private List<ChannelInfoSimpleBean> searchResultList;

    @Override
    public void operatorData() {
        searchResultList = new ArrayList<>();
        if (channelUserInfoList != null && channelUserInfoList.size() > 0) {
            for (ChannelInfoSimpleBean channelInfoSimpleBean : channelUserInfoList) {
                //添加勋章图标
                List<String> userMedalList = new ArrayList<>();
                HashMap<String, String> thirdChannelAndLogoUrlMap = channelInfoSimpleBean.getThirdChannelAndLogoUrlMap();
                if (thirdChannelAndLogoUrlMap != null && thirdChannelAndLogoUrlMap.size() > 0) {
                    for (String key : thirdChannelAndLogoUrlMap.keySet()) {
                        userMedalList.add(thirdChannelAndLogoUrlMap.get(key));
                    }
                }
                if (channelInfoSimpleBean.getLevelImageUrl() != null) {
                    userMedalList.add(channelInfoSimpleBean.getLevelImageUrl());
                }
                channelInfoSimpleBean.setUserMedalList(userMedalList);

                //添加主播id
                if (channelInfoSimple != null && channelInfoSimple.size() > 0) {
                    for (ChannelInfoSimpleBean infoSimpleBean : channelInfoSimple) {
                        if (channelIdAndUserIdMap != null && channelIdAndUserIdMap.size() > 0) {
                            if (channelIdAndUserIdMap.containsKey(String.valueOf(infoSimpleBean.getId()))) {
                                if (channelInfoSimpleBean.getUserId().equals(channelIdAndUserIdMap.get(String.valueOf(infoSimpleBean.getId())))) {
                                    channelInfoSimpleBean.setAnchorId(infoSimpleBean.getAnchorId());
                                }
                            }
                        }

                    }
                }
            }

            //去除重复元素
            separateRepeatElement();

            searchResultList.addAll(channelUserInfoList);
        }
        if (channelInfoSimple != null && channelInfoSimple.size() > 0) {
            channelInfoSimple.get(0).setShowTitle(true);
            searchResultList.addAll(channelInfoSimple);
        }

    }

    private void separateRepeatElement() {
        for ( int i = 0 ; i < channelUserInfoList.size() - 1 ; i ++ ) {
            for ( int j = channelUserInfoList.size() - 1 ; j > i; j -- ) {
                if (channelUserInfoList.get(j).getUserId().equals(channelUserInfoList.get(i).getUserId())) {
                    channelUserInfoList.remove(j);
                }
            }
        }
        channelUserInfoList.get(0).setShowTitle(true);
    }


    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<ChannelInfoSimpleBean> getChannelUserInfoList() {
        return channelUserInfoList;
    }

    public void setChannelUserInfoList(List<ChannelInfoSimpleBean> channelUserInfoList) {
        this.channelUserInfoList = channelUserInfoList;
    }

    public HashMap<String, String> getChannelIdAndUserIdMap() {
        return channelIdAndUserIdMap;
    }

    public void setChannelIdAndUserIdMap(HashMap<String, String> channelIdAndUserIdMap) {
        this.channelIdAndUserIdMap = channelIdAndUserIdMap;
    }

    public List<ChannelInfoSimpleBean> getChannelInfoSimple() {
        return channelInfoSimple;
    }

    public void setChannelInfoSimple(List<ChannelInfoSimpleBean> channelInfoSimple) {
        this.channelInfoSimple = channelInfoSimple;
    }

    public List<ChannelInfoSimpleBean> getSearchResultList() {
        return searchResultList;
    }

    public void setSearchResultList(List<ChannelInfoSimpleBean> searchResultList) {
        this.searchResultList = searchResultList;
    }

    public static class ChannelInfoSimpleBean {

        private int anchorId;
        private boolean anchorPushStream;
        private String channelName;
        private String channelTitle;
        private int id;
        private int inChannelUsers;
        private String logoUrl;
        //主播部分
        private String levelCode;
        private String levelImageUrl;
        private HashMap<String, String> thirdChannelAndLogoUrlMap;
        private String userId;
        private String userLogoUrl;
        private String userName;
        @Expose
        private boolean showTitle;
        @Expose
        private List<String> userMedalList;


        public List<String> getUserMedalList() {
            return userMedalList;
        }

        public void setUserMedalList(List<String> userMedalList) {
            this.userMedalList = userMedalList;
        }

        public boolean isShowTitle() {
            return showTitle;
        }

        public void setShowTitle(boolean showTitle) {
            this.showTitle = showTitle;
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

        public HashMap<String, String> getThirdChannelAndLogoUrlMap() {
            return thirdChannelAndLogoUrlMap;
        }

        public void setThirdChannelAndLogoUrlMap(HashMap<String, String> thirdChannelAndLogoUrlMap) {
            this.thirdChannelAndLogoUrlMap = thirdChannelAndLogoUrlMap;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getAnchorId() {
            return anchorId;
        }

        public void setAnchorId(int anchorId) {
            this.anchorId = anchorId;
        }

        public boolean isAnchorPushStream() {
            return anchorPushStream;
        }

        public void setAnchorPushStream(boolean anchorPushStream) {
            this.anchorPushStream = anchorPushStream;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public String getChannelTitle() {
            return channelTitle;
        }

        public void setChannelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInChannelUsers() {
            return inChannelUsers;
        }

        public void setInChannelUsers(int inChannelUsers) {
            this.inChannelUsers = inChannelUsers;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }
    }


}
