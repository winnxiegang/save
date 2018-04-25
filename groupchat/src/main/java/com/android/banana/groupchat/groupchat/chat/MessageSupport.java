package com.android.banana.groupchat.groupchat.chat;

import android.text.TextUtils;


import com.android.banana.R;
import com.jl.jczj.im.MessageType;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.jl.jczj.im.bean.ChatMsgBody;

/**
 * Created by mrs on 2017/5/5.
 */

public class MessageSupport implements MultiTypeSupport<ChatMsgBody> {
    private String mLoginUserId;

    public MessageSupport(String userId) {
        this.mLoginUserId = userId;
    }

    @Override
    public int getTypeLayoutRes(ChatMsgBody data, int pos) {

        boolean isSendBySelf = TextUtils.equals(mLoginUserId, data.sendUserId);

        if (TextUtils.equals(data.typeCode, MessageType.NORMAL)) {
            boolean isNormal = TextUtils.isEmpty(data.bodies.get(0).imageId);

            if (isNormal) {
                //用户发出 收到的文本消息
                return isSendBySelf ? R.layout.layout_chat_text_right : R.layout.layout_chat_text_left;

            } else {
                //下拉历史的图片消息  将typeCode改为MessageType.IMAGE_VIEWABLE;，是为了跟推送过来，和自己发送图片消息类型的
                //typeCode  保持一致
                data.typeCode = MessageType.IMAGE_VIEWABLE;
                return isSendBySelf ? R.layout.layout_right_img : R.layout.layout_left_img;
            }
        } else if (TextUtils.equals(data.typeCode, MessageType.COUPON_CREATE_SUCCESS_NOTICE_TEXT)) {
            //用户发出 收到的红包
            return isSendBySelf ? R.layout.layout_chat_coupon_right : R.layout.layout_chat_coupon_left;
        } else if (TextUtils.equals(data.typeCode, MessageType.IMAGE_VIEWABLE)) {
            //用户发出，收到推送的图片文件
            return isSendBySelf ? R.layout.layout_right_img : R.layout.layout_left_img;

        } else if (TextUtils.equals(data.typeCode, MessageType.TRANSMIT_SUBJECT_TEXT)) {
            //话题转发
            return isSendBySelf ? R.layout.layout_chat_transform_right : R.layout.layout_chat_transform_left;

        } else if (TextUtils.equals(data.typeCode, MessageType.TRANSMIT_PK_GAME_BOARD_TEXT)) {
            //直播间pk转发
            return isSendBySelf ? R.layout.layout_chat_pk_right : R.layout.layout_chat_pk_left;

        } else if (TextUtils.equals(data.typeCode, MessageType.TRANSMIT_HAND_SPEED_TEXT)) {
            //直播间极限手速转发
            return isSendBySelf ? R.layout.layout_chat_hand_speed_right : R.layout.layout_chat_hand_speed_left;
        }
        //默认是系统红包通知消息
        else if (TextUtils.equals(data.typeCode, MessageType.COUPON_NOTIFICATION))
            return R.layout.layout_chat_coupon_system;

            //打赏
        else if (TextUtils.equals(data.typeCode, MessageType.COIN_REWARD_NOTICE))
            return R.layout.layout_chat_coupon_reward;

        return 0;
    }
}
