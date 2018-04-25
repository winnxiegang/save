package com.android.xjq.bean.recharge;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by lingjiu on 2017/6/14.
 */

public class DepositApplyBean {


    /**
     * jumpLogin : false
     * parameterMap : {"timestamp":"1497403601","sign":"A070C91EDD08971752A78F124F6836FA","partnerid":"1482468322","noncestr":"0aqxyoMhjrxBgfZjHUqvQSBQHnhTahr6","prepayid":"wx20170614092641271e2a3cb70989273535","package":"Sign=WXPay","appid":"wxf2a606073cef8a40"}
     * post : false
     * depositNo : 1880117061400760113
     * providerType : {"name":"WEIXINPAY","message":"微信支付","value":0}
     */

    private boolean jumpLogin;
//    private ParameterMapBean parameterMap;
    private boolean post;
    private String depositNo;
    private ProviderTypeBean providerType;
    private HashMap<String,String> parameterMap;
    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public HashMap<String, String> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(HashMap<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }

    public String getDepositNo() {
        return depositNo;
    }

    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public ProviderTypeBean getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderTypeBean providerType) {
        this.providerType = providerType;
    }

    public static class ParameterMapBean {
        /**
         * timestamp : 1497403601
         * sign : A070C91EDD08971752A78F124F6836FA
         * partnerid : 1482468322
         * noncestr : 0aqxyoMhjrxBgfZjHUqvQSBQHnhTahr6
         * prepayid : wx20170614092641271e2a3cb70989273535
         * package : Sign=WXPay
         * appid : wxf2a606073cef8a40
         */

        private String timestamp;
        private String sign;
        private String partnerid;
        private String noncestr;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String appid;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }
    }

    public static class ProviderTypeBean {
        /**
         * name : WEIXINPAY
         * message : 微信支付
         * value : 0
         */

        private String name;
        private String message;
        private int value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
