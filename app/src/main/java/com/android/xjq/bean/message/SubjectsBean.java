package com.android.xjq.bean.message;

import com.android.banana.commlib.bean.NormalObject;

import com.android.xjq.bean.JcSnapshotBean;
import com.android.xjq.bean.order.PurchaseSnapshotBean;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by zhouyi on 2017/1/12 17:04.
 */

public class SubjectsBean {

    private int profitCopyCount;

    private String title;

    private String summary;

    private String content;

    private int replyCount;

    private int likeCount;

    private String gmtCreate;

    private String subjectId;

    private boolean liked;

    private String userName;

    private String loginName;

    private String createrId;

    private String userId;

    private String gmtModified;

    private boolean deleted;

    private int collectId;

    private String userLogoUrl;

    private boolean focus;

    private boolean vip;

    private String filterHtmlSummary;

    private boolean commentOff;

    @Expose boolean isMe;

    @Expose
    private boolean tempFollowed;

    @Expose
    private boolean tempEachFollowed;

    //private PropertiesBean properties;

    private NormalObject objectType;

    @Expose
    private String objectTypeName;

//    @Expose
//    private DiscoveryEnum discoveryEnum;

    @Expose
    private List<String> smallImageUrl;

    @Expose
    private List<String> midImageUrl;

    @Expose
    private JcSnapshotBean jcSnapshotBean;

    @Expose
    private PurchaseSnapshotBean purchaseSnapshotBean;

//    @Expose
//    private List<GroupChatMedalBean> userMedalBean;

    @Expose
    private boolean isElite;

    @Expose
    private boolean isHot;

    @Expose
    private String AnalysisCode;

    private boolean setTop;

    private String objectId;

    @Expose
    private boolean showDaiHong;

    @Expose
    private String jcdxType;

    @Expose
    private NormalObject PredictResultStatus;

    public boolean isTempEachFollowed() {
        return tempEachFollowed;
    }

    public void setTempEachFollowed(boolean tempEachFollowed) {
        this.tempEachFollowed = tempEachFollowed;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isShowDaiHong() {
        return showDaiHong;
    }

    public void setShowDaiHong(boolean showDaiHong) {
        this.showDaiHong = showDaiHong;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public String getJcdxType() {
        return jcdxType;
    }

    public void setJcdxType(String jcdxType) {
        this.jcdxType = jcdxType;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }


    public NormalObject getPredictResultStatus() {
        return PredictResultStatus;
    }

    public void setPredictResultStatus(NormalObject predictResultStatus) {
        PredictResultStatus = predictResultStatus;
    }

    public boolean isSetTop() {
        return setTop;
    }

    public void setSetTop(boolean setTop) {
        this.setTop = setTop;
    }

    public String getAnalysisCode() {
        return AnalysisCode;
    }

    public void setAnalysisCode(String analysisCode) {
        AnalysisCode = analysisCode;
    }

    public boolean isElite() {
        return isElite;
    }

    public void setElite(boolean elite) {
        isElite = elite;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

//    public List<GroupChatMedalBean> getUserMedalBean() {
//        return userMedalBean;
//    }
//
//    public void setUserMedalBean(List<GroupChatMedalBean> userMedalBean) {
//        this.userMedalBean = userMedalBean;
//    }

    public int getProfitCopyCount() {
        return profitCopyCount;
    }

    public void setProfitCopyCount(int profitCopyCount) {
        this.profitCopyCount = profitCopyCount;
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

    public void setContent(String content) {
        this.content = content;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount<0?0:likeCount;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getCollectId() {
        return collectId;
    }

    public void setCollectId(int collectId) {
        this.collectId = collectId;
    }

//    public DiscoveryEnum getDiscoveryEnum() {
//        return discoveryEnum;
//    }
//
//    public void setDiscoveryEnum(DiscoveryEnum discoveryEnum) {
//        this.discoveryEnum = discoveryEnum;
//    }

    public List<String> getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(List<String> smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public List<String> getMidImageUrl() {
        return midImageUrl;
    }

    public void setMidImageUrl(List<String> midImageUrl) {
        this.midImageUrl = midImageUrl;
    }

    public JcSnapshotBean getJcSnapshotBean() {
        return jcSnapshotBean;
    }

    public void setJcSnapshotBean(JcSnapshotBean jcSnapshotBean) {
        this.jcSnapshotBean = jcSnapshotBean;
    }

    public PurchaseSnapshotBean getPurchaseSnapshotBean() {
        return purchaseSnapshotBean;
    }

    public void setPurchaseSnapshotBean(PurchaseSnapshotBean purchaseSnapshotBean) {
        this.purchaseSnapshotBean = purchaseSnapshotBean;
    }

    public String getUserLogoUrl() {
        return userLogoUrl;
    }

    public void setUserLogoUrl(String userLogoUrl) {
        this.userLogoUrl = userLogoUrl;
    }


    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getObjectTypeName() {
        return objectTypeName;
    }

    public void setObjectTypeName(String objectTypeName) {
        this.objectTypeName = objectTypeName;
    }

//    public PropertiesBean getProperties() {
//        return properties;
//    }
//
//    public void setProperties(PropertiesBean properties) {
//        this.properties = properties;
//    }

    public NormalObject getObjectType() {
        return objectType;
    }

    public void setObjectType(NormalObject objectType) {
        this.objectType = objectType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean isTempFollowed() {
        return tempFollowed;
    }

    public void setTempFollowed(boolean tempFollowed) {
        this.tempFollowed = tempFollowed;
    }

    public String getFilterHtmlSummary() {
        return filterHtmlSummary;
    }

    public void setFilterHtmlSummary(String filterHtmlSummary) {
        this.filterHtmlSummary = filterHtmlSummary;
    }

    public boolean isCommentOff() {
        return commentOff;
    }

    public void setCommentOff(boolean commentOff) {
        this.commentOff = commentOff;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }


}
