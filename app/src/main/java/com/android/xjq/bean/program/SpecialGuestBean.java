package com.android.xjq.bean.program;

import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.live.BaseOperator;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ajiao on 2018\2\9 0009.
 */

public class SpecialGuestBean implements BaseOperator {
    /**
     * guestInfoList : [{"attentionNum":0,"fansNum":0,"fidleMedal":{"currentMedalLevelConfigCode":"Lv0","currentMedalLevelConfigValue":0,"medalConfigCode":"GLOBAL_GRADE_MEDAL","userId":"8201712158726308"},"focus":false,"globalGradeMedal":{"currentMedalLevelConfigCode":"Lv0","currentMedalLevelConfigValue":0,"medalConfigCode":"GLOBAL_GRADE_MEDAL","userId":"8201712158726308"},"userName":"1w","userId":"8201712158726308","userLogoUrl":"http://mapi1.xjq.net/userLogoUrl.htm?userId=8201712158726308&timestamp=1513323962000"}]
     * jumpLogin : false
     * nowDate : 2018-02-23 16:50:04
     * success : true
     */

    private boolean jumpLogin;
    private String nowDate;
    private boolean success;
    private List<GuestInfoListBean> userInfoList;
    public HashMap<String, Integer> userIdMultipleMap;

    @Override
    public void operatorData() {
        if (userIdMultipleMap != null && userIdMultipleMap.size() > 0 &&
                userInfoList != null && userInfoList.size() > 0) {
            for (GuestInfoListBean userInfo : userInfoList) {
                if (userIdMultipleMap.containsKey(userInfo.userId)) {
                    userInfo.multiple = userIdMultipleMap.get(userInfo.userId);
                }
            }
        }
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

    public List<GuestInfoListBean> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<GuestInfoListBean> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public static class GuestInfoListBean {
        /**
         * attentionNum : 0
         * fansNum : 0
         * fidleMedal : {"currentMedalLevelConfigCode":"Lv0","currentMedalLevelConfigValue":0,"medalConfigCode":"GLOBAL_GRADE_MEDAL","userId":"8201712158726308"}
         * focus : false
         * globalGradeMedal : {"currentMedalLevelConfigCode":"Lv0","currentMedalLevelConfigValue":0,"medalConfigCode":"GLOBAL_GRADE_MEDAL","userId":"8201712158726308"}
         * userName : 1w
         * userId : 8201712158726308
         * userLogoUrl : http://mapi1.xjq.net/userLogoUrl.htm?userId=8201712158726308&timestamp=1513323962000
         */

        private int attentionNum;
        private int fansNum;
        private MedalBean fidleMedal;
        private boolean focus;
        private MedalBean globalGradeMedal;
        private String userName;
        private String userId;
        private String userLogoUrl;
        public List<UserMedalLevelBean> userMedalLevelList;
        @Expose
        public int multiple;

        public int getAttentionNum() {
            return attentionNum;
        }

        public void setAttentionNum(int attentionNum) {
            this.attentionNum = attentionNum;
        }

        public int getFansNum() {
            return fansNum;
        }

        public void setFansNum(int fansNum) {
            this.fansNum = fansNum;
        }

        public MedalBean getFidleMedal() {
            return fidleMedal;
        }

        public void setFidleMedal(MedalBean fidleMedal) {
            this.fidleMedal = fidleMedal;
        }

        public boolean isFocus() {
            return focus;
        }

        public void setFocus(boolean focus) {
            this.focus = focus;
        }

        public MedalBean getGlobalGradeMedal() {
            return globalGradeMedal;
        }

        public void setGlobalGradeMedal(MedalBean globalGradeMedal) {
            this.globalGradeMedal = globalGradeMedal;
        }

        public String getLoginName() {
            return userName;
        }

        public void setLoginName(String userName) {
            this.userName = userName;
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

        public static class MedalBean {
            /**
             * currentMedalLevelConfigCode : Lv0
             * currentMedalLevelConfigValue : 0
             * medalConfigCode : GLOBAL_GRADE_MEDAL
             * userId : 8201712158726308
             */

            private String currentMedalLevelConfigCode;
            private int currentMedalLevelConfigValue;
            private String medalConfigCode;
            private String userId;

            public String getCurrentMedalLevelConfigCode() {
                return currentMedalLevelConfigCode;
            }

            public void setCurrentMedalLevelConfigCode(String currentMedalLevelConfigCode) {
                this.currentMedalLevelConfigCode = currentMedalLevelConfigCode;
            }

            public int getCurrentMedalLevelConfigValue() {
                return currentMedalLevelConfigValue;
            }

            public void setCurrentMedalLevelConfigValue(int currentMedalLevelConfigValue) {
                this.currentMedalLevelConfigValue = currentMedalLevelConfigValue;
            }

            public String getMedalConfigCode() {
                return medalConfigCode;
            }

            public void setMedalConfigCode(String medalConfigCode) {
                this.medalConfigCode = medalConfigCode;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }

}
