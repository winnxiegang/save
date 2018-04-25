package com.android.banana.groupchat.bean;

/**
 * Created by zaozao on 2017/6/12.
 */

public class GroupInfoBean {
    /**
     * enabled : true
     * gmtCreate : 2017-05-04 11:47:52
     * gmtModified : 2017-05-04 11:47:52
     * groupCode : BT_SEASON_RACE_5059620
     * chatRoomName : 比分直播-竞篮-5059620
     * id : 1705041147460461980031290435
     * messageQueryByUserEnabled : false
     * messageSendByUserEnabled : false
     * notice : 比分直播-周四302 美国职业篮球联盟 犹他爵士 VS 金州勇士
     * numberId : 10
     * ownerId : 8201211190000629
     * ownerType : RACE_LIVE_CHANNEL
     * sourceId : 5059620
     * sourceType : BT_SEASON_RACE
     * typeId : 1
     * uniqueId : BT_SEASON_RACE_5059620
     */

    public boolean enabled;
    public String gmtCreate;
    public String gmtModified;
//    public String groupCode;
    public String id;
    public boolean messageQueryByUserEnabled;
    public boolean messageSendByUserEnabled;
    public String notice;
    public int numberId;
    public String ownerId;
    public String ownerType;
    public String sourceId;
    public String sourceType;
    public int typeId;
    public String uniqueId;
    public String chatRoomUrl;
    public String chatRoomName;
    public String chatRoomId;
    public int couponNum;

    public String getChatRoomUrl() {
        return chatRoomUrl;
    }

    public void setChatRoomUrl(String chatRoomUrl) {
        this.chatRoomUrl = chatRoomUrl;
    }


    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMessageQueryByUserEnabled() {
        return messageQueryByUserEnabled;
    }

    public void setMessageQueryByUserEnabled(boolean messageQueryByUserEnabled) {
        this.messageQueryByUserEnabled = messageQueryByUserEnabled;
    }

    public boolean isMessageSendByUserEnabled() {
        return messageSendByUserEnabled;
    }

    public void setMessageSendByUserEnabled(boolean messageSendByUserEnabled) {
        this.messageSendByUserEnabled = messageSendByUserEnabled;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getNumberId() {
        return numberId;
    }

    public void setNumberId(int numberId) {
        this.numberId = numberId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
