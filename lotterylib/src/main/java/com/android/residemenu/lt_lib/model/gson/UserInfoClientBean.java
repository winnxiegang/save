package com.android.residemenu.lt_lib.model.gson;

/**
 * Created by zhouyi on 2015/10/10 15:30.
 */
public class UserInfoClientBean {



    private boolean success;
    private boolean validateIdentity;
    private UserInfoClientEntity userInfoClient;
    private String nowDate;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setValidateIdentity(boolean validateIdentity) {
        this.validateIdentity = validateIdentity;
    }

    public void setUserInfoClient(UserInfoClientEntity userInfoClient) {
        this.userInfoClient = userInfoClient;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean getSuccess() {
        return success;
    }

    public boolean getValidateIdentity() {
        return validateIdentity;
    }

    public UserInfoClientEntity getUserInfoClient() {
        return userInfoClient;
    }

    public String getNowDate() {
        return nowDate;
    }

    public static class UserInfoClientEntity {

        private boolean loginPasswordSet;
        private CertTypeEntity certType;
        private String cell;
        private boolean qqValidate;
        private boolean accountPasswordSet;
        private boolean emailValidate;
        private String defaultAccountNo;
        private String nickName;
        private String userId;
        private String certNo;
        private boolean canLogin;
        private String realName;
        private boolean cellValidate;
        private String loginName;

        public void setLoginPasswordSet(boolean loginPasswordSet) {
            this.loginPasswordSet = loginPasswordSet;
        }

        public void setCertType(CertTypeEntity certType) {
            this.certType = certType;
        }

        public void setCell(String cell) {
            this.cell = cell;
        }

        public void setQqValidate(boolean qqValidate) {
            this.qqValidate = qqValidate;
        }

        public void setAccountPasswordSet(boolean accountPasswordSet) {
            this.accountPasswordSet = accountPasswordSet;
        }

        public void setEmailValidate(boolean emailValidate) {
            this.emailValidate = emailValidate;
        }

        public void setDefaultAccountNo(String defaultAccountNo) {
            this.defaultAccountNo = defaultAccountNo;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setCertNo(String certNo) {
            this.certNo = certNo;
        }

        public void setCanLogin(boolean canLogin) {
            this.canLogin = canLogin;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public void setCellValidate(boolean cellValidate) {
            this.cellValidate = cellValidate;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public boolean getLoginPasswordSet() {
            return loginPasswordSet;
        }

        public CertTypeEntity getCertType() {
            return certType;
        }

        public String getCell() {
            return cell;
        }

        public boolean getQqValidate() {
            return qqValidate;
        }

        public boolean getAccountPasswordSet() {
            return accountPasswordSet;
        }

        public boolean getEmailValidate() {
            return emailValidate;
        }

        public String getDefaultAccountNo() {
            return defaultAccountNo;
        }

        public String getNickName() {
            return nickName;
        }

        public String getUserId() {
            return userId;
        }

        public String getCertNo() {
            return certNo;
        }

        public boolean getCanLogin() {
            return canLogin;
        }

        public String getRealName() {
            return realName;
        }

        public boolean getCellValidate() {
            return cellValidate;
        }

        public String getLoginName() {
            return loginName;
        }

        public static class CertTypeEntity {
            /**
             * value : 0
             * message : 身份证
             * name : IDENTITY_CARD
             */

            private int value;
            private String message;
            private String name;

            public void setValue(int value) {
                this.value = value;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getValue() {
                return value;
            }

            public String getMessage() {
                return message;
            }

            public String getName() {
                return name;
            }
        }
    }
}
