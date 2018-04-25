package com.android.xjq.bean.userInfo;

import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.groupchat.bean.medal.GroupChatMedalBean;
import com.android.xjq.bean.UserMedalLevelBean;

import java.util.List;

/**
 * Created by zhouyi on 2015/10/10 15:30.
 */
public class UserInfoClientBean {

    private UserInfoClientEntity userInfo;
    private String nowDate;
    private NormalObject identityStatus;
    private GroupChatMedalBean coatArmorDetail;
    private String myFollowers;
    private String followMyUsers;
    public boolean identifySetted;
    private List<UserMedalLevelBean> userMedalLevelList;

    public String getMyFollowers() {
        return myFollowers;
    }

    public String getFollowMyUsers() {
        return followMyUsers;
    }


    public List<UserMedalLevelBean> getUserMedalLevelList() {
        return userMedalLevelList;
    }

    public GroupChatMedalBean getCoatArmorDetail() {
        return coatArmorDetail;
    }

    public void setCoatArmorDetail(GroupChatMedalBean coatArmorDetail) {
        this.coatArmorDetail = coatArmorDetail;
    }

    public NormalObject getIdentityStatus() {
        return identityStatus;
    }

    public void setIdentityStatus(NormalObject identityStatus) {
        this.identityStatus = identityStatus;
    }

    public UserInfoClientEntity getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoClientEntity userInfo) {
        this.userInfo = userInfo;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getNowDate() {
        return nowDate;
    }

    public static class UserInfoClientEntity {

        private boolean accountPasswordSet;
        private boolean canLogin;
        private String cell;
        private boolean cellValidate;
        private boolean identifySetted;
        private String loginName;
        private boolean loginPasswordSet;
        private String realName;
        private String userId;
        private String userLogoUrl;
        private String certNo;

        public String getCertNo() {
            return certNo;
        }

        public void setCertNo(String certNo) {
            this.certNo = certNo;
        }

        public boolean isAccountPasswordSet() {
            return accountPasswordSet;
        }

        public void setAccountPasswordSet(boolean accountPasswordSet) {
            this.accountPasswordSet = accountPasswordSet;
        }

        public boolean isCanLogin() {
            return canLogin;
        }

        public void setCanLogin(boolean canLogin) {
            this.canLogin = canLogin;
        }

        public String getCell() {
            return cell;
        }

        public void setCell(String cell) {
            this.cell = cell;
        }

        public boolean isCellValidate() {
            return cellValidate;
        }

        public void setCellValidate(boolean cellValidate) {
            this.cellValidate = cellValidate;
        }

        public boolean isIdentifySetted() {
            return identifySetted;
        }

        public void setIdentifySetted(boolean identifySetted) {
            this.identifySetted = identifySetted;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public boolean isLoginPasswordSet() {
            return loginPasswordSet;
        }

        public void setLoginPasswordSet(boolean loginPasswordSet) {
            this.loginPasswordSet = loginPasswordSet;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
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
}
