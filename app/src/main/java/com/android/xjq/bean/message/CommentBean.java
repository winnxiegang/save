package com.android.xjq.bean.message;

import android.text.SpannableStringBuilder;

import com.android.banana.commlib.bean.NormalObject;

import com.google.gson.annotations.Expose;

/**
 * 球爆中评论Json的Bean文件
 * Created by zhouyi on 2017/1/12 16:56.
 */

public class CommentBean {

    private String content;

    private boolean userDeleted;

    private int replyCount;

    private int likeCount;

    private String gmtCreate;

    private int floor;

    private String userId;

    private boolean liked;

    private String loginName;

    private boolean systemDeleted;

    private String commentId;

    private NormalObject commentObjectType;

    private String summary;

    private String filterHtmlContent;

    @Expose
    private SpannableStringBuilder predictSpanMessage;

    @Expose
    private String predictMessage;

    @Expose
    private NormalObject predictStatus;

    public String getPredictMessage() {
        return predictMessage;
    }

    public void setPredictMessage(String predictMessage) {
        this.predictMessage = predictMessage;
    }

    public NormalObject getPredictStatus() {
        return predictStatus;
    }

    public void setPredictStatus(NormalObject predictStatus) {
        this.predictStatus = predictStatus;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserDeleted(boolean userDeleted) {
        this.userDeleted = userDeleted;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setUserName(String userName) {
        this.loginName = userName;
    }

    public void setSystemDeleted(boolean systemDeleted) {
        this.systemDeleted = systemDeleted;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public boolean isUserDeleted() {
        return userDeleted;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public int getFloor() {
        return floor;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isLiked() {
        return liked;
    }

    public String getUserName() {
        return loginName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isSystemDeleted() {
        return systemDeleted;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public NormalObject getCommentObjectType() {
        return commentObjectType;
    }

    public void setCommentObjectType(NormalObject commentObjectType) {
        this.commentObjectType = commentObjectType;
    }

    public String getFilterHtmlContent() {
        return filterHtmlContent;
    }

    public void setFilterHtmlContent(String filterHtmlContent) {
        this.filterHtmlContent = filterHtmlContent;
    }

    public SpannableStringBuilder getPredictSpanMessage() {
        return predictSpanMessage;
    }

    public void setPredictSpanMessage(SpannableStringBuilder predictSpanMessage) {
        this.predictSpanMessage = predictSpanMessage;
    }
}
