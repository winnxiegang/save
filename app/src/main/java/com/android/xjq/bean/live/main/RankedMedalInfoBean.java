package com.android.xjq.bean.live.main;

import android.content.Context;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ajiao on 2017/10/10 0010.
 */

public class RankedMedalInfoBean {


    /**
     * response : {"jumpLogin":false,"medalLabelConfigContents":["胖胖"],"nowDate":"2017-10-10 15:17:27","rankMedalTotalSimpleList":[{"areaId":30024,"currentValue":24200,"id":"2431934016506480980052137098","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201703220430006","userMedalDetailId":"2431933807206480980052073283"},{"areaId":30024,"currentValue":10000,"id":"2440735021006480980055707594","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201706290740003","userMedalDetailId":"2440734016506480980055545288"},{"areaId":30024,"currentValue":10000,"id":"2433360016406480980053736122","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201703300470004","userMedalDetailId":"2433359405406480980056944415"},{"areaId":30024,"currentValue":5880,"id":"2432253015706480980053840582","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201709140910002","userMedalDetailId":"2432252808006480980059961311"},{"areaId":30024,"currentValue":800,"id":"2434000020406480980059830443","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201704200590004","userMedalDetailId":"2433999806006480980052956796"},{"areaId":30024,"currentValue":110,"id":"2432022018406480980055044977","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201703310470030","userMedalDetailId":"2432021206606480980059706570"},{"areaId":30024,"currentValue":10,"id":"2440760020906480980051104032","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201707210800002","userMedalDetailId":"2440760010306480980051906889"},{"areaId":30024,"currentValue":0,"id":"2441629531206480980051017563","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201704060510016","userMedalDetailId":"2441629358806480980056984025"},{"areaId":30024,"currentValue":140,"id":"2440511016106480980054469704","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201703020330001","userMedalDetailId":"2440510806606480980053493188"}],"success":true,"userIdAndLevelConfigCodeMap":{"8201703220430006":"Lv6","8201703300470004":"Lv5","8201703310470030":"Lv1","8201704060510016":"Lv1","8201704200590004":"Lv2","8201706290740003":"Lv5","8201707210800002":"Lv1","8201709140910002":"Lv4"},"userIdAndLoginNameMap":{"8201703020330001":"空凌破晓","8201703220430006":"夏夏","8201703300470004":"Goddedog","8201703310470030":"樂易","8201704060510016":"lingjiu_01","8201704200590004":"lingjiu_02","8201706290740003":"灯火文化","8201707210800002":"lidaoyuan","8201709140910002":"ajiao"}}
     */


    /**
     * jumpLogin : false
     * medalLabelConfigContents : ["胖胖"]
     * nowDate : 2017-10-10 15:17:27
     * rankMedalTotalSimpleList : [{"areaId":30024,"currentValue":24200,"id":"2431934016506480980052137098","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201703220430006","userMedalDetailId":"2431933807206480980052073283"},{"areaId":30024,"currentValue":10000,"id":"2440735021006480980055707594","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201706290740003","userMedalDetailId":"2440734016506480980055545288"},{"areaId":30024,"currentValue":10000,"id":"2433360016406480980053736122","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201703300470004","userMedalDetailId":"2433359405406480980056944415"},{"areaId":30024,"currentValue":5880,"id":"2432253015706480980053840582","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201709140910002","userMedalDetailId":"2432252808006480980059961311"},{"areaId":30024,"currentValue":800,"id":"2434000020406480980059830443","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201704200590004","userMedalDetailId":"2433999806006480980052956796"},{"areaId":30024,"currentValue":110,"id":"2432022018406480980055044977","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201703310470030","userMedalDetailId":"2432021206606480980059706570"},{"areaId":30024,"currentValue":10,"id":"2440760020906480980051104032","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201707210800002","userMedalDetailId":"2440760010306480980051906889"},{"areaId":30024,"currentValue":0,"id":"2441629531206480980051017563","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201704060510016","userMedalDetailId":"2441629358806480980056984025"},{"areaId":30024,"currentValue":140,"id":"2440511016106480980054469704","medalConfigId":1,"sequenceTrend":"DOWN","userId":"8201703020330001","userMedalDetailId":"2440510806606480980053493188"}]
     * success : true
     * userIdAndLevelConfigCodeMap : {"8201703220430006":"Lv6","8201703300470004":"Lv5","8201703310470030":"Lv1","8201704060510016":"Lv1","8201704200590004":"Lv2","8201706290740003":"Lv5","8201707210800002":"Lv1","8201709140910002":"Lv4"}
     * userIdAndLoginNameMap : {"8201703020330001":"空凌破晓","8201703220430006":"夏夏","8201703300470004":"Goddedog","8201703310470030":"樂易","8201704060510016":"lingjiu_01","8201704200590004":"lingjiu_02","8201706290740003":"灯火文化","8201707210800002":"lidaoyuan","8201709140910002":"ajiao"}
     */

    private boolean jumpLogin;
    private String nowDate;
    private boolean success;
    private HashMap<String, String> userIdAndLevelConfigCodeMap;
    private HashMap<String, String> userIdAndLoginNameMap;
    private List<String> medalLabelConfigContents;
    private List<RankMedalTotalSimpleListBean> rankMedalTotalSimpleList;
    private boolean anchorHasFanMedal;

    public void operate(Context context) {
        if (rankMedalTotalSimpleList != null && rankMedalTotalSimpleList.size() > 0) {
            for (int i = 0; i < rankMedalTotalSimpleList.size(); i++) {
                RankMedalTotalSimpleListBean rankMedalTotalSimpleListBean = rankMedalTotalSimpleList.get(i);
                String name = medalLabelConfigContents.size() > 0 ? medalLabelConfigContents.get(0) : "";
                rankMedalTotalSimpleListBean.setMedalLabelName(name);
                String userId = rankMedalTotalSimpleListBean.getUserId();
                if (userIdAndLevelConfigCodeMap.containsKey(userId)) {
                    String levelCode = userIdAndLevelConfigCodeMap.get(userId);
                    String resName = "icon_fans_medal_" + levelCode + "";
                    int resId = context.getResources().getIdentifier(resName.toLowerCase(), "drawable", context.getPackageName());
                    rankMedalTotalSimpleListBean.setLevelResId(resId);
                }
                if (userIdAndLoginNameMap.containsKey(userId)) {
                    rankMedalTotalSimpleListBean.setName(userIdAndLoginNameMap.get(userId));
                }

            }
        }

    }


    public boolean isAnchorHasFanMedal() {
        return anchorHasFanMedal;
    }

    public void setAnchorHasFanMedal(boolean anchorHasFanMedal) {
        this.anchorHasFanMedal = anchorHasFanMedal;
    }

    public HashMap<String, String> getUserIdAndLevelConfigCodeMap() {
        return userIdAndLevelConfigCodeMap;
    }

    public void setUserIdAndLevelConfigCodeMap(HashMap<String, String> userIdAndLevelConfigCodeMap) {
        this.userIdAndLevelConfigCodeMap = userIdAndLevelConfigCodeMap;
    }

    public HashMap<String, String> getUserIdAndLoginNameMap() {
        return userIdAndLoginNameMap;
    }

    public void setUserIdAndLoginNameMap(HashMap<String, String> userIdAndLoginNameMap) {
        this.userIdAndLoginNameMap = userIdAndLoginNameMap;
    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getMedalLabelConfigContents() {
        return medalLabelConfigContents;
    }

    public void setMedalLabelConfigContents(List<String> medalLabelConfigContents) {
        this.medalLabelConfigContents = medalLabelConfigContents;
    }

    public List<RankMedalTotalSimpleListBean> getRankMedalTotalSimpleList() {
        return rankMedalTotalSimpleList;
    }

    public void setRankMedalTotalSimpleList(List<RankMedalTotalSimpleListBean> rankMedalTotalSimpleList) {
        this.rankMedalTotalSimpleList = rankMedalTotalSimpleList;
    }

    public static class RankMedalTotalSimpleListBean {
        /**
         * areaId : 30024
         * currentValue : 24200
         * id : 2431934016506480980052137098
         * medalConfigId : 1
         * sequenceTrend : DOWN
         * userId : 8201703220430006
         * userMedalDetailId : 2431933807206480980052073283
         */

        private int areaId;
        private int currentValue;
        private String id;
        private int medalConfigId;
        private String sequenceTrend = "";
        private String userId;
        private String userMedalDetailId;
        @Expose
        private String medalLabelName;
        @Expose
        private int levelResId;
        @Expose
        private String name;

        public String getMedalLabelName() {
            return medalLabelName;
        }

        public void setMedalLabelName(String medalLabelName) {
            this.medalLabelName = medalLabelName;
        }

        public int getLevelResId() {
            return levelResId;
        }

        public void setLevelResId(int levelResId) {
            this.levelResId = levelResId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public int getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(int currentValue) {
            this.currentValue = currentValue;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getMedalConfigId() {
            return medalConfigId;
        }

        public void setMedalConfigId(int medalConfigId) {
            this.medalConfigId = medalConfigId;
        }

        public String getSequenceTrend() {
            return sequenceTrend;
        }

        public void setSequenceTrend(String sequenceTrend) {
            this.sequenceTrend = sequenceTrend;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserMedalDetailId() {
            return userMedalDetailId;
        }

        public void setUserMedalDetailId(String userMedalDetailId) {
            this.userMedalDetailId = userMedalDetailId;
        }
    }

}
