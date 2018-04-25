package com.android.xjq.bean.live;

import java.util.List;

/**
 * 服务端改造使用新的推送消息
 * Created by zhouyi on 2017/5/11.
 */

public class NewLiveCommentBean {


    private String id;
    private int typeId;
    private String typeCode;
    private boolean systemDeleted;
    private String uniqueId;

    private String gmtCreate;
    private String gmtModified;
    private long messageSequence;
    private String sendUserId;
    private String sendUserLoginName;
    private String sendUserLogoUrl;
    private String groupId;
    private List<BodiesBean> bodies;
    private LiveCommentBean.UserPropertiesBean properties;

    public static class BodiesBean{

        public LiveCommentBean.ContentBean.BodyBean parameters;

        public LiveCommentBean.ContentBean.PropertiesBean properties;

        public String content;

        public String fontColor;

        public String getFontColor() {
            return fontColor;
        }

        public void setFontColor(String fontColor) {
            this.fontColor = fontColor;
        }

        public LiveCommentBean.ContentBean.BodyBean getParameters() {
            return parameters;
        }

        public void setParameters(LiveCommentBean.ContentBean.BodyBean parameters) {
            this.parameters = parameters;
        }

        public LiveCommentBean.ContentBean.PropertiesBean getProperties() {
            return properties;
        }

        public void setProperties(LiveCommentBean.ContentBean.PropertiesBean properties) {
            this.properties = properties;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public LiveCommentBean.UserPropertiesBean getProperties() {
        return properties;
    }

    public void setProperties(LiveCommentBean.UserPropertiesBean properties) {
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public boolean isSystemDeleted() {
        return systemDeleted;
    }

    public void setSystemDeleted(boolean systemDeleted) {
        this.systemDeleted = systemDeleted;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public long getMessageSequence() {
        return messageSequence;
    }

    public void setMessageSequence(long messageSequence) {
        this.messageSequence = messageSequence;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserLoginName() {
        return sendUserLoginName;
    }

    public void setSendUserLoginName(String sendUserLoginName) {
        this.sendUserLoginName = sendUserLoginName;
    }

    public String getSendUserLogoUrl() {
        return sendUserLogoUrl;
    }

    public void setSendUserLogoUrl(String sendUserLogoUrl) {
        this.sendUserLogoUrl = sendUserLogoUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<BodiesBean> getBodies() {
        return bodies;
    }

    public void setBodies(List<BodiesBean> bodies) {
        this.bodies = bodies;
    }
}
