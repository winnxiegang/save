package com.android.banana.groupchat.bean;

import com.android.banana.commlib.bean.BaseOperator;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.groupchat.chatenum.NoticeTypeEnum;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zaozao on 2017/5/31.
 */

public class GroupChatNotificationBean implements BaseOperator {

    private PaginatorBean paginator;

    private List<NoticesBean> notices;

    private String nowDate;

    private HashMap<String, NoticeParamBean> messageParameterMap;

    @Override
    public void operatorData() {
        if (notices != null && notices.size() > 0) {
            for (int i = 0; i < notices.size(); i++) {
                NoticesBean noticesBean = notices.get(i);
                if (messageParameterMap.containsKey(noticesBean.getId())) {
                    NoticeParamBean noticeParamBean = messageParameterMap.get(noticesBean.getId());
                    noticesBean.setMessageParamBean(noticeParamBean);
                }
                noticesBean.setGmtCreate(TimeUtils.formatMessageTime(nowDate, noticesBean.getGmtCreate()));

                switch (NoticeTypeEnum.safeValueOf(noticesBean.getMsgSubType().getName())) {
                    case GROUP_CHAT_APPLY_JOIN_NOTICE://同意按钮，拒绝按钮，等待
                        noticesBean.setUser(true);//不是用户就是群聊，就要显示用户数
                        noticesBean.setNoticeContent("申请加入群聊" + noticesBean.messageParamBean.getGROUP_CHAT_NAME());

                        if ("AGREEED".equals(notices.get(i).messageParamBean.getADUIT_STATUS())) {
                            noticesBean.setShowResult(true);
                            noticesBean.setApplyResult("已同意");
                        } else if ("REFUSED".equals(noticesBean.messageParamBean.getADUIT_STATUS())) {
                            noticesBean.setShowResult(true);
                            noticesBean.setApplyResult("已拒绝");
                        } else if ("WAIT_ADUIT".equals(noticesBean.messageParamBean.getADUIT_STATUS())) {
                            noticesBean.setShowButton(true);
                        }
                        break;
                    case GROUP_CREATE_SUCCESS:
                        noticesBean.setNoticeContent("建群成功,点击进入该群");
                        noticesBean.setClickToGroupChat(true);
                        break;
                    case GROUP_CHAT_JOIN_ADUIT_NOTICE://,已同意  已拒绝

                        if ("AGREEED".equals(noticesBean.messageParamBean.getADUIT_STATUS())) {

                            noticesBean.setNoticeContent("管理员同意您的加入请求");
                            noticesBean.setClickToGroupChat(true);
                        } else if ("REFUSED".equals(noticesBean.messageParamBean.getADUIT_STATUS())) {
                            noticesBean.setNoticeContent("管理员拒绝您的加入请求");
                        }
                        break;
                    case GROUP_CHAT_USER_REMOVE_NOTICE://("群聊用户被移除群通知"),
                        noticesBean.setNoticeContent("您已被移除本群");
                        break;
                    case GROUP_CHAT_INVITE_JOIN_NOTICE://("您已被邀请进入该群"),
                        NoticeParamBean paramBean = noticesBean.getMessageParamBean();
                        noticesBean.setNoticeContent(noticesBean.getSenderName() + "  " + (paramBean == null ? "已邀请您加入本群" : paramBean.getINVITE_TITLE()));
                        noticesBean.setClickToGroupChat(true);
                        break;
                }
            }
        }
    }

    public class NoticesBean {

        /**
         * gmtCreate : 2017-11-02 10:40:45
         * id : 219571110
         * msgSubType : {"message":"群聊用户加入审核通知","name":"GROUP_CHAT_JOIN_ADUIT_NOTICE"}
         * msgType : {"message":"家族","name":"GROUP_CHAT"}
         * senderId : 8201606224095767
         */

        private String gmtCreate;
        private String id;
        private NormalObject msgSubType;
        private NormalObject msgType;
        private String senderId;
        private String senderName = "";
        public NoticeParamBean messageParamBean;
        @Expose
        private String noticeContent;
        @Expose
        private boolean user;
        @Expose
        private boolean showButton;
        @Expose
        private boolean showResult;
        @Expose
        private String applyResult;
        @Expose
        private boolean clickToGroupChat = false;

        public boolean isClickToGroupChat() {
            return clickToGroupChat;
        }

        public void setClickToGroupChat(boolean clickToGroupChat) {
            this.clickToGroupChat = clickToGroupChat;
        }

        public NoticeParamBean getMessageParamBean() {
            return messageParamBean;
        }

        public void setMessageParamBean(NoticeParamBean messageParamBean) {
            this.messageParamBean = messageParamBean;
        }

        public boolean isShowButton() {
            return showButton;
        }

        public void setShowButton(boolean showButton) {
            this.showButton = showButton;
        }

        public String getApplyResult() {
            return applyResult;
        }

        public void setApplyResult(String applyResult) {
            this.applyResult = applyResult;
        }

        public boolean isShowResult() {
            return showResult;
        }

        public void setShowResult(boolean showResult) {
            this.showResult = showResult;
        }

        public String getNoticeContent() {
            return noticeContent;
        }

        public void setNoticeContent(String noticeContent) {
            this.noticeContent = noticeContent;
        }

        public boolean isUser() {
            return user;
        }

        public void setUser(boolean user) {
            this.user = user;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }
    }

    public HashMap<String, NoticeParamBean> getMessageParameterMap() {
        return messageParameterMap;
    }

    public void setMessageParameterMap(HashMap<String, NoticeParamBean> messageParameterMap) {
        this.messageParameterMap = messageParameterMap;
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


}