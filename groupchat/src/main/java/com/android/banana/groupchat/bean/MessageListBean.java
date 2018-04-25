package com.android.banana.groupchat.bean;

import com.google.gson.annotations.Expose;
import com.jl.jczj.im.bean.ChatMsgBody;

import java.util.Date;
import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;


/**
 * Created by zaozao on 2017/6/12.
 */
//public class MessageListBean extends RealmObject {
@RealmClass
public class MessageListBean implements RealmModel {
    private String groupId;
    private long orderValueByUser;//0代表未置顶
    private int unreadMessageCount;//未读消息数

    //私聊对应的一些特殊数据
    private String sendUserLoginName;
    private String sendUserId;
    private String soleId;


    @Expose
    private String userId;
    @PrimaryKey@Expose
    private String groupIdAndType;//userId_groupId_messageType
    @Ignore
    private ChatMsgBody latestMessage;
    @Ignore
    private boolean open;
    private String roomName;//群聊名称
    private String photoUrl;//头像
    private boolean showCoupon;//是否显示红包
    private String gmtCreate;//消息的发表时间
    private String originGmtCreate;//之前的消息时间
    private Date lastMessageCreateTime;//最后一条消息发表日期，排序用
    private String lastMessageContent;//最后一条消息内容
    private String memberNum;//成员数
    private String groupChatMessageType;
    private boolean systemMessage;//是不是群聊系统通知
    @Ignore
    private boolean deleteFromGroup;
    @Expose@Ignore
    private boolean replyMe;//回复我的
    @Expose@Ignore
    private boolean appSystemMessage;//通知

    public boolean isReplyMe() {
        return replyMe;
    }

    public void setReplyMe(boolean replyMe) {
        this.replyMe = replyMe;
    }

    public boolean isAppSystemMessage() {
        return appSystemMessage;
    }

    public void setAppSystemMessage(boolean appSystemMessage) {
        this.appSystemMessage = appSystemMessage;
    }

    public String getGroupIdAndType() {
        return groupIdAndType;
    }

    public void setGroupIdAndType(String groupIdAndType) {
        this.groupIdAndType = groupIdAndType;
    }

    public String getGroupChatMessageType() {
        return groupChatMessageType;
    }

    public void setGroupChatMessageType(String groupChatMessageType) {
        this.groupChatMessageType = groupChatMessageType;
    }

    public boolean isDeleteFromGroup() {
        return deleteFromGroup;
    }

    public void setDeleteFromGroup(boolean deleteFromGroup) {
        this.deleteFromGroup = deleteFromGroup;
    }

    public String getSendUserLoginName() {
        return sendUserLoginName;
    }

    public void setSendUserLoginName(String sendUserLoginName) {
        this.sendUserLoginName = sendUserLoginName;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(String memberNum) {
        this.memberNum = memberNum;
    }

    public String getOriginGmtCreate() {
        return originGmtCreate;
    }

    public void setOriginGmtCreate(String originGmtCreate) {
        this.originGmtCreate = originGmtCreate;
    }

    public Date getLastMessageCreateTime() {
        return lastMessageCreateTime;
    }

    public boolean isSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(boolean systemMessage) {
        this.systemMessage = systemMessage;
    }

    public void setLastMessageCreateTime(Date lastMessageCreateTime) {
        this.lastMessageCreateTime = lastMessageCreateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isShowCoupon() {
        return showCoupon;
    }

    public void setShowCoupon(boolean showCoupon) {
        this.showCoupon = showCoupon;
    }

    public long getOrderValueByUser() {
        return orderValueByUser;
    }

    public void setOrderValueByUser(long orderValueByUser) {
        this.orderValueByUser = orderValueByUser;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public ChatMsgBody getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(ChatMsgBody latestMessage) {
        this.latestMessage = latestMessage;
    }

    public String getSoleId() {
        return soleId;
    }

    public void setSoleId(String soleId) {
        this.soleId = soleId;
    }
}
