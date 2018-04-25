package com.android.banana.groupchat.bean;


import android.text.TextUtils;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.bean.BaseOperator;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.groupchat.chatenum.GroupChatMessageType;
import com.android.banana.groupchat.chatenum.NoticeTypeEnum;
import com.android.library.Utils.LogUtils;
import com.jl.jczj.im.MessageType;
import com.jl.jczj.im.bean.ChatMsgBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zaozao on 2017/6/2.
 */

public class GroupChatMessageBean implements BaseOperator {

    //群聊通知
    private SystemMessageBean systemMessage;
    //未读消息数量
    private int systemUnReadMessageCount;
    //群聊信息，
    private HashMap<String, GroupInfoBean> groupIdAndChatRoomInfoClientSimpleMap;
    //系统消息对应的map
    private HashMap<String, NoticeParamBean> messageParameterMap;

    private HashMap<String, Integer> groupIdAndUserCountMap;
    //未读消息List
    private List<MessageListBean> entries;

    private String nowDate;

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    @Override
    public void operatorData() {

        if (entries != null && entries.size() > 0) {
            for (int i = 0; i < entries.size(); i++) {
                MessageListBean bean = entries.get(i);
                setSecretMessageBean(bean);
            }
        }

        if (systemMessage != null) {

            MessageListBean systemMessageBean = new MessageListBean();

            systemMessageBean.setLastMessageCreateTime(TimeUtils.stringToDate(systemMessage.getGmtCreate(), null));

            systemMessageBean.setOriginGmtCreate(systemMessage.getGmtCreate());

            systemMessageBean.setGmtCreate(TimeUtils.formatMessageTime(nowDate, systemMessage.getGmtCreate()));

            systemMessageBean.setRoomName("群聊通知");

            systemMessageBean.setSystemMessage(true);

            systemMessageBean.setGroupChatMessageType(GroupChatMessageType.SYSTEM_MESSAGE.name());

            systemMessageBean.setUnreadMessageCount(systemUnReadMessageCount);
            if (messageParameterMap != null) {
                if (messageParameterMap.containsKey(systemMessage.getId())) {
                    NoticeParamBean noticeParamBean = messageParameterMap.get(systemMessage.getId());
                    systemMessageBean.setGroupId(noticeParamBean.getGROUP_ID());
                    systemMessageBean.setGroupIdAndType(LoginInfoHelper.getInstance().getUserId() + "_" + noticeParamBean.getGROUP_ID() + "_" + GroupChatMessageType.SYSTEM_MESSAGE.name());
                    String content = "";
                    switch (NoticeTypeEnum.safeValueOf(systemMessage.getMsgSubType())) {
                        case GROUP_CHAT_APPLY_JOIN_NOTICE://同意按钮，拒绝按钮，等待
                            content = systemMessage.getSenderName() + "  申请加入群聊  ";
                            break;
                        case GROUP_CREATE_SUCCESS:
                            content = "建群成功,点击进入";
                            break;
                        case GROUP_CHAT_JOIN_ADUIT_NOTICE://,已同意  已拒绝
                            if ("AGREEED".equals(noticeParamBean.getADUIT_STATUS())) {
                                content = "管理员同意您加入";
                            } else if ("REFUSED".equals(noticeParamBean.getADUIT_STATUS())) {
                                content = "管理员拒绝您加入";
                            }
                            break;
                        case GROUP_CHAT_USER_REMOVE_NOTICE://("群聊用户被移除群通知"),
                            content = "您已被移除";
                            systemMessageBean.setDeleteFromGroup(true);//设置被移除标签
                            break;
                        case GROUP_CHAT_INVITE_JOIN_NOTICE://("您已被邀请进入该群"),
                            content = systemMessage.getSenderName() + "  邀请您加入  ";
                            break;
                    }
                    systemMessageBean.setLastMessageContent(content + noticeParamBean.getGROUP_CHAT_NAME());
                }
            }
            if (entries == null) {

                entries = new ArrayList<>();
            }
            entries.add(systemMessageBean);
        }
    }

    private void setSecretMessageBean(MessageListBean bean) {
        if (bean.getLatestMessage() == null)
            return;

        bean.setRoomName(bean.getLatestMessage().sendUserLoginName);

        bean.setGroupChatMessageType(GroupChatMessageType.SECRET_CHAT.name());

        bean.setGroupIdAndType(LoginInfoHelper.getInstance().getUserId() + "_" + bean.getSoleId() + "_" + GroupChatMessageType.SECRET_CHAT.name());

        bean.setLastMessageCreateTime(TimeUtils.stringToDate(bean.getLatestMessage().gmtCreate, null));

        bean.setOriginGmtCreate(bean.getLatestMessage().gmtCreate);

        bean.setSendUserId(bean.getLatestMessage().sendUserId);

        bean.setSendUserLoginName(bean.getLatestMessage().sendUserLoginName);

        bean.setGmtCreate(TimeUtils.formatMessageTime(nowDate, bean.getLatestMessage().gmtCreate));

        bean.setLastMessageContent(getMessageContent(bean.getLatestMessage()));
        //"http://mapi.xjq.net/userLogoUrl.htm?userId=8201711068675884&mt=1509972179000"
        bean.setPhotoUrl(com.android.banana.commlib.http.AppParam.API_DOMAIN + AppParam.USER_LOGO_URL + bean.getLatestMessage().sendUserLogoUrl);
        LogUtils.e("kkkk", "拼接的——————" + bean.getPhotoUrl());
    }

    /**
     * 不同类型消息显示内容
     *
     * @param bean
     */
    public String getMessageContent(ChatMsgBody bean) {
        String content = "";
        try {
            String sender = bean.sendUserNickName != null ? bean.sendUserNickName : bean.sendUserLoginName;
            if (bean.bodies != null && bean.bodies.size() > 0) {
                if (bean.typeCode.equals(MessageType.COUPON_CREATE_SUCCESS_NOTICE_TEXT)) {//发红包
                    content = "[红包]" + bean.bodies.get(0).parameters.title;
                } else if (bean.typeCode.equals(MessageType.COUPON_NOTIFICATION)) {//抢红包
                    String sendUserName = bean.bodies.get(0).parameters.sendUserName;
                    String receiveUserName = bean.bodies.get(0).parameters.receiveUserName;
                    String amount = bean.bodies.get(0).parameters.amount;
                    content = receiveUserName + "抢到" + sendUserName + amount + "礼金";
                } else if (bean.typeCode.equals(MessageType.COIN_REWARD_NOTICE)) {//打赏
                    String receiveUserLoginName = null;
                    String sendUserId = bean.sendUserId;
                    String amount = "";
                    ChatMsgBody.Properties properties = bean.bodies.get(0).properties;
                    if (properties != null) {
                        amount = properties.rewardAmount;
                        receiveUserLoginName = properties.receiveUserLoginName;
                    }
                    if (TextUtils.equals(sendUserId, LoginInfoHelper.getInstance().getUserId())) {
                        content = "你打赏了" + receiveUserLoginName + amount + "金币";
                    } else {
                        content = sender + "打赏了你" + amount + "金币";
                    }
                } else if (bean.typeCode.equals(MessageType.IMAGE_VIEWABLE)) {//图片

                    content = sender + ":[图片]";
                } else if (TextUtils.equals(bean.typeCode, MessageType.TRANSMIT_SUBJECT_TEXT)) {
                    //转发类型
                    ChatMsgBody.Body.Parameters parameters = bean.bodies.get(0).parameters;

                    String showMemo =( (!TextUtils.isEmpty(parameters.title) &&!TextUtils.equals("",parameters.title))?  parameters.title:
                            (parameters.summary!=null&&(!TextUtils.equals("",parameters.summary))? parameters.summary:parameters.memo));
                    content = sender + ":[转发]" + showMemo;
                    if(content==null){
                        if( TextUtils.equals(bean.typeCode, MessageType.TRANSMIT_PK_GAME_BOARD_TEXT)){
                            content =  sender + ":[转发]pk";
                        }
                        else if(TextUtils.equals(bean.typeCode, MessageType.TRANSMIT_HAND_SPEED_TEXT)){
                            content = sender + ":[转发]极限手速";
                        }
                    }
                }
                else if( TextUtils.equals(bean.typeCode, MessageType.TRANSMIT_PK_GAME_BOARD_TEXT)){
                    content =  sender + ":[转发]pk";
                }
                else if(TextUtils.equals(bean.typeCode, MessageType.TRANSMIT_HAND_SPEED_TEXT)){
                    content = sender + ":[转发]极限手速";
                }
                else {
                    if (bean.bodies.get(0).content == null) {
                        if( bean.bodies.get(0).imageId!=null){
                            content = sender + ":[图片]";
                        }else{
                            content = bean.typeCode + "content为空";
                        }
                    } else {
                        content = sender + ":" + bean.bodies.get(0).content;
                    }
                }
            }
        } catch (Exception e) {
        }
        return content;
    }

    public HashMap<String, Integer> getGroupIdAndUserCountMap() {
        return groupIdAndUserCountMap;
    }

    public void setGroupIdAndUserCountMap(HashMap<String, Integer> groupIdAndUserCountMap) {
        this.groupIdAndUserCountMap = groupIdAndUserCountMap;
    }

    public List<MessageListBean> getEntries() {
        return entries;
    }

    public void setEntries(List<MessageListBean> entries) {
        this.entries = entries;
    }

    public SystemMessageBean getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(SystemMessageBean systemMessage) {
        this.systemMessage = systemMessage;
    }
}
