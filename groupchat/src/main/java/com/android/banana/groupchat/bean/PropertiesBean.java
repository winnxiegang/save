package com.android.banana.groupchat.bean;

/**
 * Created by zhouyi on 2017/1/12 17:05.
 */

public class PropertiesBean {
    private String purchaseNo;

    private String bizId;

    private String jczqBizId;

    private String jclqBizId;

    private String[] smallImageUrl;

    private String[] midImageUrl;

    private String subjectFirstCardType;

    private String articleRelationPredictId;

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getArticleRelationPredictId() {
        return articleRelationPredictId;
    }

    public void setArticleRelationPredictId(String articleRelationPredictId) {
        this.articleRelationPredictId = articleRelationPredictId;
    }

    public String getJczqBizId() {
        return jczqBizId;
    }

    public String getSubjectFirstCardType() {
        return subjectFirstCardType;
    }

    public void setSubjectFirstCardType(String subjectFirstCardType) {
        this.subjectFirstCardType = subjectFirstCardType;
    }

    public void setJczqBizId(String jczqBizId) {
        this.jczqBizId = jczqBizId;
    }

    public String getJclqBizId() {
        return jclqBizId;
    }

    public void setJclqBizId(String jclqBizId) {
        this.jclqBizId = jclqBizId;
    }

    public String[] getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String[] smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String[] getMidImageUrl() {
        return midImageUrl;
    }

    public void setMidImageUrl(String[] midImageUrl) {
        this.midImageUrl = midImageUrl;
    }
}
