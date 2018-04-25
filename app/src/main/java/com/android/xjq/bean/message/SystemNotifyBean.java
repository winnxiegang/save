package com.android.xjq.bean.message;

import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.commlib.bean.NormalObject;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.xjq.model.message.SystemMessageSubTypeEnum;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhouyi on 2016/6/8 11:56.
 */
public class SystemNotifyBean{

    private PaginatorBean paginator; //PaginatorBean

    private List<NoticesBean> notices;

    private String nowDate;

   private HashMap<String, MessageParameterBean> messageParameterMap;


    public void operatorData() {
        for (NoticesBean bean : notices) {
            bean.setGmtCreate(TimeUtils.formatTime(nowDate, bean.getGmtCreate()));
            if (messageParameterMap != null) {
                bean.setMessageContent(messageParameterMap.get(bean.getId()));
                if(SystemMessageSubTypeEnum.saveValueOf(bean.getMsgSubType().getName()).equals(SystemMessageSubTypeEnum.USER_GUESS_PRIZE_NOTICE)){
                    bean.getMessageContent().setObjectType("USER_GUESS_PRIZE_NOTICE");
                }
            }
        }
    }

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }

    public List<NoticesBean> getNotices() {
        return notices;
    }

    public void setNotices(List<NoticesBean> notices) {
        this.notices = notices;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public HashMap<String, MessageParameterBean> getMessageParameterMap() {
        return messageParameterMap;
    }

    public void setMessageParameterMap(HashMap<String, MessageParameterBean> messageParameterMap) {
        this.messageParameterMap = messageParameterMap;
    }

    public static class MessageParameterBean {

        private String CHANNEL_AREA_ID;

        private String CHANNEL_AREA_NAME;

        private String amount;

        private String couponId;

        private String purchaseNo;

        private String sourceUserName;

        private String sourceUserId;

        private String objectType;

        private String objectId;

        private String couponNo;

        /**
         * 禁言通知
         */
        private String actionType;

        private String expiredDate;

        private String forbiddenReason;

        /**
         * 话题、评论删除通知
         */
        private String objectContent;

        private String COMMENT;

        private String SUBJECT;

        private String deleteReason;


        //用户信息审核通知
        private String  memo;
        private String  applyStatus;
        public  String getMemo() {
            return memo;
        }
        public void   setMemo(String memo) {
            this.memo = memo;
        }
        public String getApplyStatus() {
            return applyStatus;
        }
        public void   setApplyStatus(String applyStatus) {
            this.applyStatus = applyStatus;
        }

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getExpiredDate() {
            return expiredDate;
        }

        public void setExpiredDate(String expiredDate) {
            this.expiredDate = expiredDate;
        }

        public String getForbiddenReason() {
            return forbiddenReason;
        }

        public void setForbiddenReason(String forbiddenReason) {
            this.forbiddenReason = forbiddenReason;
        }

        public String getObjectContent() {
            if (objectContent != null && objectContent.length() > 10) {
                objectContent = objectContent.substring(0, 10) + "..";
            }
            return objectContent == null ? "" : objectContent;
        }

        public String getCHANNEL_AREA_ID() {
            return CHANNEL_AREA_ID;
        }

        public void setCHANNEL_AREA_ID(String CHANNEL_AREA_ID) {
            this.CHANNEL_AREA_ID = CHANNEL_AREA_ID;
        }

        public String getCHANNEL_AREA_NAME() {
            return CHANNEL_AREA_NAME;
        }

        public void setCHANNEL_AREA_NAME(String CHANNEL_AREA_NAME) {
            this.CHANNEL_AREA_NAME = CHANNEL_AREA_NAME;
        }

        public void setObjectContent(String objectContent) {
            this.objectContent = objectContent;
        }

        public String getCOMMENT() {
            return COMMENT;
        }

        public void setCOMMENT(String COMMENT) {
            this.COMMENT = COMMENT;
        }

        public String getSUBJECT() {
            return SUBJECT;
        }

        public void setSUBJECT(String SUBJECT) {
            this.SUBJECT = SUBJECT;
        }

        public String getDeleteReason() {
            return deleteReason;
        }

        public void setDeleteReason(String deleteReason) {
            this.deleteReason = deleteReason;
        }

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getCouponNo() {
            return couponNo;
        }

        public void setCouponNo(String couponNo) {
            this.couponNo = couponNo;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCouponId() {
            return couponId;
        }

        public void setCouponId(String couponId) {
            this.couponId = couponId;
        }

        public String getPurchaseNo() {
            return purchaseNo;
        }

        public void setPurchaseNo(String purchaseNo) {
            this.purchaseNo = purchaseNo;
        }

        public String getSourceUserName() {
            return sourceUserName;
        }

        public void setSourceUserName(String sourceUserName) {
            this.sourceUserName = sourceUserName;
        }

        public String getSourceUserId() {
            return sourceUserId;
        }

        public void setSourceUserId(String sourceUserId) {
            this.sourceUserId = sourceUserId;
        }
    }


    public static class NoticesBean {

        private String id;
        private String userLogoUrl;
        private boolean vip;
        private String senderName;
        private String gmtCreate;
        private NormalObject msgSubType;
        private String senderId;
        private NormalObject msgType;

        @Expose
        private MessageParameterBean messageContent;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }


        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public NormalObject getMsgSubType() {
            return msgSubType;
        }

        public void setMsgSubType(NormalObject msgSubType) {
            this.msgSubType = msgSubType;
        }

        public NormalObject getMsgType() {
            return msgType;
        }

        public void setMsgType(NormalObject msgType) {
            this.msgType = msgType;
        }

        public MessageParameterBean getMessageContent() {
            return messageContent;
        }

        public void setMessageContent(MessageParameterBean messageContent) {
            this.messageContent = messageContent;
        }

    }
}
