package com.android.banana.groupchat.bean;

/**
 * Created by zaozao on 2017/11/6.
 */

public class NoticeParamBean {

    /**
     * ADUIT_STATUS : AGREEED
     * APPLY_ID : 2648131526704610970012942184
     * GROUP_CHAT_ID : 2647942657500200970023032965
     * GROUP_CHAT_NAME : 我是一个群聊
     * GROUP_ID : 2647942855904610970014051609
     * USER_LOGO : http://jchapi3.huored.net/userLogoUrl.htm?userId=8201710316945890&mt=1509437393000
     */

    private String ADUIT_STATUS; //审核状态
    private String APPLY_ID;     //申请id
    private String GROUP_CHAT_ID;//groupChatId
    private String GROUP_CHAT_NAME;//groupChatName
    private String GROUP_ID;     //groupId
    private String USER_LOGO;     //userLogo
    private int GROUP_USER_COUNT;  //userCount
    private String GROUP_LOGO_URL;
    /**
     * GROUP_ID : 1705261717260461980039153788
     * INVITE_TITLE : 测试邀请通知(群聊通知的内容)
     */

    private String INVITE_TITLE;

    private String INVITE_ID;

    public String getINVITE_ID() {
        return INVITE_ID;
    }

    public void setINVITE_ID(String INVITE_ID) {
        this.INVITE_ID = INVITE_ID;
    }

    public String getINVITE_TITLE() {
        return INVITE_TITLE;
    }

    public void setINVITE_TITLE(String INVITE_TITLE) {
        this.INVITE_TITLE = INVITE_TITLE;
    }

    public String getADUIT_STATUS() {
        return ADUIT_STATUS;
    }

    public void setADUIT_STATUS(String ADUIT_STATUS) {
        this.ADUIT_STATUS = ADUIT_STATUS;
    }

    public String getAPPLY_ID() {
        return APPLY_ID;
    }

    public void setAPPLY_ID(String APPLY_ID) {
        this.APPLY_ID = APPLY_ID;
    }

    public String getGROUP_CHAT_ID() {
        return GROUP_CHAT_ID;
    }

    public void setGROUP_CHAT_ID(String GROUP_CHAT_ID) {
        this.GROUP_CHAT_ID = GROUP_CHAT_ID;
    }

    public String getGROUP_CHAT_NAME() {
        return GROUP_CHAT_NAME;
    }

    public void setGROUP_CHAT_NAME(String GROUP_CHAT_NAME) {
        this.GROUP_CHAT_NAME = GROUP_CHAT_NAME;
    }

    public String getGROUP_ID() {
        return GROUP_ID;
    }

    public void setGROUP_ID(String GROUP_ID) {
        this.GROUP_ID = GROUP_ID;
    }

    public String getUSER_LOGO() {
        return USER_LOGO;
    }

    public void setUSER_LOGO(String USER_LOGO) {
        this.USER_LOGO = USER_LOGO;
    }

    public int getGROUP_USER_COUNT() {
        return GROUP_USER_COUNT;
    }

    public void setGROUP_USER_COUNT(int GROUP_USER_COUNT) {
        this.GROUP_USER_COUNT = GROUP_USER_COUNT;
    }

    public String getGROUP_LOGO_URL() {
        return GROUP_LOGO_URL;
    }

    public void setGROUP_LOGO_URL(String GROUP_LOGO_URL) {
        this.GROUP_LOGO_URL = GROUP_LOGO_URL;
    }
}
