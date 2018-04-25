package com.android.banana.groupchat.bean;

/**
 * Created by kokuma on 2017/10/31.
 */

public class GroupUserApplyJoinSimpleBean {
    /**
     * 申请记录id
     */
    /**
     * id
     */
    private String applyId;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 组id
     */
    private String groupId;

    /**
     * 审核状态
     */
    private MessageTypeEnum aduitStatus;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 头像地址
     */
    private String userLogoUrl;
    /**
     * 加V
     */
    private boolean vip;
    /**
     * 粉丝数
     */
    private Long fansNum;
    /**
     * 关注数
     */
    private Long attentionNum;
    /**
     * 累计金额
     */
    private double totalPrizeFee;
    /**
     * 创建时间
     */
    private String gmtCreate;



    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public MessageTypeEnum getAduitStatus() {
        return aduitStatus;
    }

    public void setAduitStatus(MessageTypeEnum aduitStatus) {
        this.aduitStatus = aduitStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogoUrl() {
        return userLogoUrl;
    }

    public void setUserLogoUrl(String userLogoUrl) {
        this.userLogoUrl = userLogoUrl;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public Long getFansNum() {
        return fansNum;
    }

    public void setFansNum(Long fansNum) {
        this.fansNum = fansNum;
    }

    public Long getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(Long attentionNum) {
        this.attentionNum = attentionNum;
    }

    public double getTotalPrizeFee() {
        return totalPrizeFee;
    }

    public void setTotalPrizeFee(double totalPrizeFee) {
        this.totalPrizeFee = totalPrizeFee;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }


}
