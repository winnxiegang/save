package com.android.xjq.bean.login;

import java.util.List;

/**
 * Created by zhouyi on 2017/3/30.
 */

public class UserLoginBean {

    private boolean jumpLogin;
    private OneAuthPlatformLoginBean oneAuthPlatformLogin;
    private UserSelectInfoBean userSelectInfo;
    private int remainPasswordTryCount;

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public OneAuthPlatformLoginBean getOneAuthPlatformLogin() {
        return oneAuthPlatformLogin;
    }

    public void setOneAuthPlatformLogin(OneAuthPlatformLoginBean oneAuthPlatformLogin) {
        this.oneAuthPlatformLogin = oneAuthPlatformLogin;
    }

    public UserSelectInfoBean getUserSelectInfo() {
        return userSelectInfo;
    }

    public void setUserSelectInfo(UserSelectInfoBean userSelectInfo) {
        this.userSelectInfo = userSelectInfo;
    }

    public int getRemainPasswordTryCount() {
        return remainPasswordTryCount;
    }

    public void setRemainPasswordTryCount(int remainPasswordTryCount) {
        this.remainPasswordTryCount = remainPasswordTryCount;
    }

    public static class OneAuthPlatformLoginBean {

        private String oneAuthId;
        private String loginKey;
        private String signKey;
        private String cryptKey;

        public String getOneAuthId() {
            return oneAuthId;
        }

        public void setOneAuthId(String oneAuthId) {
            this.oneAuthId = oneAuthId;
        }

        public String getLoginKey() {
            return loginKey;
        }

        public void setLoginKey(String loginKey) {
            this.loginKey = loginKey;
        }

        public String getSignKey() {
            return signKey;
        }

        public void setSignKey(String signKey) {
            this.signKey = signKey;
        }

        public String getCryptKey() {
            return cryptKey;
        }

        public void setCryptKey(String cryptKey) {
            this.cryptKey = cryptKey;
        }
    }

    public static class UserSelectInfoBean {


        private String defaultUserId;
        private List<UserSimplesBean> userSimples;

        public String getDefaultUserId() {
            return defaultUserId;
        }

        public void setDefaultUserId(String defaultUserId) {
            this.defaultUserId = defaultUserId;
        }

        public List<UserSimplesBean> getUserSimples() {
            return userSimples;
        }

        public void setUserSimples(List<UserSimplesBean> userSimples) {
            this.userSimples = userSimples;
        }

    }

}
