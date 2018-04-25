package com.android.banana.groupchat.bean;

/**
 * Created by zaozao on 2017/6/12.
 * 群聊通知
 */

public class SystemMessageBean {
    /**
     * deleted : false
     * gmtCreate : 2017-06-08 23:09:07
     * gmtModified : 2017-06-08 23:09:07
     * id : 205280833
     * messageSubTypeId : 90001
     * senderId : 10029
     */
    private String senderName;
    private boolean deleted;
    private String gmtCreate;
    private String gmtModified;
    private String id;
    private int messageSubTypeId;
    private String msgSubType;
    private String msgType;
    private String senderId;

    public String getMsgSubType() {
        return msgSubType;
    }

    public void setMsgSubType(String msgSubType) {
        this.msgSubType = msgSubType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMessageSubTypeId() {
        return messageSubTypeId;
    }

    public void setMessageSubTypeId(int messageSubTypeId) {
        this.messageSubTypeId = messageSubTypeId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
