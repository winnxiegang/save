package com.android.xjq.bean.message;

import android.text.TextUtils;

import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.xjq.bean.JcSnapshotBean;
import com.android.xjq.bean.SubjectTag;
import com.android.xjq.model.comment.CommentObjectTypeEnum;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouyi on 2016/1/6.
 */
public class ReplyMeBean {

    private PaginatorBean paginator;

    private String nowDate;

    private List<MessagesBean> messages;

    // private HashMap<String, UserCommentBean.UserRacePredictBean> userRacePredictMap;

    private HashMap<String, JcSnapshotBean> jczqDataMap;

    private HashMap<String, JcSnapshotBean> jclqDataMap;

    private Map<String, List<SubjectTag>> subjectTagMap;

    public void operateData(){

        for (int i = 0; i < messages.size(); i++) {
            MessagesBean bean = messages.get(i);
            bean.setGmtCreate(TimeUtils.formatTime(nowDate, messages.get(i).getComment().getGmtCreate()));
            //二级回复:评论的回复，回复的回复
            if(TextUtils.equals(CommentObjectTypeEnum.COMMENT_REPLY_TO_ME.name(),bean.getMsgSubType().getName())){
                bean.setShowResource("回复"+bean.getComment().getLoginName()+"的评论："+bean.getComment().getContent());
                bean.setShowReply(bean.getCommentReply().getContent());
            }else{//一级回复
//                //对文章的评论
//                if(TextUtils.equals(bean.getSubject().getObjectType().getName(),"ARTICLE")){
//                    String showTitle = bean.getSubject().getTitle()!=null?bean.getSubject().getTitle():bean.getSubject().getSummary();
//                    bean.setShowResource("评论"+bean.getSubject().getLoginName()+"的文章：《"+showTitle+"》");
//
//                }else {//动态的评论
//                    bean.setShowResource("评论"+bean.getSubject().getLoginName()+"的动态："+bean.getSubject().getSummary());
//                }
                bean.setShowResource("评论"+bean.getSubject().getLoginName()+"的动态："+bean.getSubject().getSummary());
                bean.setShowReply(bean.getComment().getContent());
            }

        }
    }


    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public void setMessages(List<MessagesBean> messages) {
        this.messages = messages;
    }

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public String getNowDate() {
        return nowDate;
    }

    public List<MessagesBean> getMessages() {
        return messages;
    }


    public static class MessagesBean extends BaseTopicsBean {  //
        public NormalObject mainType;

        private boolean vip;

        private String userLogoUrl;

        private String senderName;

        private String gmtCreate;

        private NormalObject msgSubType;

        private String senderId;

        private NormalObject msgType;
        @Expose
        private String showReply;
        @Expose
        private String showResource;

        public String getShowReply() {
            return showReply;
        }

        public void setShowReply(String showReply) {
            this.showReply = showReply;
        }

        public String getShowResource() {
            return showResource;
        }

        public void setShowResource(String showResource) {
            this.showResource = showResource;
        }

        public void setVip(boolean vip) {
            this.vip = vip;
        }

        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public void setMsgType(NormalObject msgType) {
            this.msgType = msgType;
        }

        public boolean isVip() {
            return vip;
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }

        public String getSenderName() {
            return senderName;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public String getSenderId() {
            return senderId;
        }

        public NormalObject getMsgType() {
            return msgType;
        }

        public NormalObject getMsgSubType() {
            return msgSubType;
        }

        public void setMsgSubType(NormalObject msgSubType) {
            this.msgSubType = msgSubType;
        }
    }
}
