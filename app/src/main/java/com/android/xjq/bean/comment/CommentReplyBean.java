package com.android.xjq.bean.comment;

import com.android.banana.commlib.bean.JczjMedalBean;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by zhouyi on 2017/1/12 16:56.
 */

public class CommentReplyBean {
    private String content;

    private String loginName;

    private String commentReplyId;

    private boolean userDeleted;

    private boolean systemDeleted;

    private String gmtCreate;

    private String userId;

    @Expose
    private boolean louZhu;

    @Expose
    private String replyName;

    /**
     * 定位到指定的层
     */
    @Expose
    private boolean showBlack;

    /**
     * 勋章信息
     */
    @Expose
    private List<JczjMedalBean> userMedalBeanList;

    public List<JczjMedalBean> getUserMedalBeanList() {
        return userMedalBeanList;
    }

    public void setUserMedalBeanList(List<JczjMedalBean> userMedalBeanList) {
        this.userMedalBeanList = userMedalBeanList;
    }

    public boolean isSystemDeleted() {
        return systemDeleted;
    }

    public void setSystemDeleted(boolean systemDeleted) {
        this.systemDeleted = systemDeleted;
    }

    public boolean isUserDeleted() {
        return userDeleted;
    }

    public void setUserDeleted(boolean userDeleted) {
        this.userDeleted = userDeleted;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentReplyId() {
        return commentReplyId;
    }

    public void setCommentReplyId(String commentReplyId) {
        this.commentReplyId = commentReplyId;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isLouZhu() {
        return louZhu;
    }

    public void setLouZhu(boolean louZhu) {
        this.louZhu = louZhu;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public boolean isShowBlack() {
        return showBlack;
    }

    public void setShowBlack(boolean showBlack) {
        this.showBlack = showBlack;
    }
}
