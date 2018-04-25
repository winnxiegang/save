package com.android.banana.commlib.bean;

/**
 * Created by zaozao on 2017/12/4.
 */

public class JczjMedalBean {

    /**
     * userMedalUrl : http://jchapi.huored.net/userMedalPic.resource?byteName=PROFIT_RED_L1_L
     * configName : 百夫长
     * medalCode : PROFIT_RED
     * orderValue : 2
     * imageName : PROFIT_RED_L1
     * medalName : 带红勋章
     * configCode : L1
     * userId : 8201606224095767
     * calValue : 2
     * code : "bzzx_PROFIT_RED_M"
     */

    private String userMedalUrl;
    private String configName;
    private String medalCode;
    private int orderValue;
    private String imageName;
    private String medalName;
    private String configCode;
    private String userId;
    private int calValue;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUserMedalUrl(String userMedalUrl) {
        this.userMedalUrl = userMedalUrl;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public void setMedalCode(String medalCode) {
        this.medalCode = medalCode;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setMedalName(String medalName) {
        this.medalName = medalName;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCalValue(int calValue) {
        this.calValue = calValue;
    }

    public String getUserMedalUrl() {
        return userMedalUrl;
    }

    public String getConfigName() {
        return configName;
    }

    public String getMedalCode() {
        return medalCode;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public String getImageName() {
        return imageName;
    }

    public String getMedalName() {
        return medalName;
    }

    public String getConfigCode() {
        return configCode;
    }

    public String getUserId() {
        return userId;
    }

    public int getCalValue() {
        return calValue;
    }
}
