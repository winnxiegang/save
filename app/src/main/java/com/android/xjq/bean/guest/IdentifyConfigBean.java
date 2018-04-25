package com.android.xjq.bean.guest;

/**
 * Created by lingjiu on 2017/11/6.
 */

public class IdentifyConfigBean {

    /**
     * identityConfig : {"canReward":true,"code":"VIP","currencyType":{"message":"金币","name":"GOLD_COIN"},"effectiveTime":30,"effectiveTimeUnit":"day","enabled":true,"gmtCreate":"2017-11-01 19:56:27","gmtEffect":"2017-11-01 19:56:27","gmtModified":"2017-11-04 19:36:11","id":1,"payType":{"message":"包月","name":"BY_MONTH"},"price":2,"title":"贵宾"}
     * jumpLogin : false
     * nowDate : 2017-11-06 10:49:51
     * success : true
     */

    private IdentityConfigBean identityConfig;
    private boolean vip;
    private boolean jumpLogin;
    private String nowDate;
    private boolean success;

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public IdentityConfigBean getIdentityConfig() {
        return identityConfig;
    }

    public void setIdentityConfig(IdentityConfigBean identityConfig) {
        this.identityConfig = identityConfig;
    }

    public static class IdentityConfigBean {
        /**
         * canReward : true
         * code : VIP
         * currencyType : {"message":"金币","name":"GOLD_COIN"}
         * effectiveTime : 30
         * effectiveTimeUnit : day
         * enabled : true
         * gmtCreate : 2017-11-01 19:56:27
         * gmtEffect : 2017-11-01 19:56:27
         * gmtModified : 2017-11-04 19:36:11
         * id : 1
         * payType : {"message":"包月","name":"BY_MONTH"}
         * price : 2.0
         * title : 贵宾
         */

        private boolean canReward;
        private String code;
        private CurrencyTypeBean currencyType;
        private int effectiveTime;
        private String effectiveTimeUnit;
        private boolean enabled;
        private String gmtCreate;
        private String gmtEffect;
        private String gmtModified;
        private int id;
        private PayTypeBean payType;
        private double price;
        private String title;

        public boolean isCanReward() {
            return canReward;
        }

        public void setCanReward(boolean canReward) {
            this.canReward = canReward;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public CurrencyTypeBean getCurrencyType() {
            return currencyType;
        }

        public void setCurrencyType(CurrencyTypeBean currencyType) {
            this.currencyType = currencyType;
        }

        public int getEffectiveTime() {
            return effectiveTime;
        }

        public void setEffectiveTime(int effectiveTime) {
            this.effectiveTime = effectiveTime;
        }

        public String getEffectiveTimeUnit() {
            return effectiveTimeUnit;
        }

        public void setEffectiveTimeUnit(String effectiveTimeUnit) {
            this.effectiveTimeUnit = effectiveTimeUnit;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getGmtEffect() {
            return gmtEffect;
        }

        public void setGmtEffect(String gmtEffect) {
            this.gmtEffect = gmtEffect;
        }

        public String getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(String gmtModified) {
            this.gmtModified = gmtModified;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public PayTypeBean getPayType() {
            return payType;
        }

        public void setPayType(PayTypeBean payType) {
            this.payType = payType;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public static class CurrencyTypeBean {
            /**
             * message : 金币
             * name : GOLD_COIN
             */

            private String message;
            private String name;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class PayTypeBean {
            /**
             * message : 包月
             * name : BY_MONTH
             */

            private String message;
            private String name;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
