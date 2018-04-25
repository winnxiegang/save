package com.jl.jczj.im;

import com.jl.jczj.im.tim.TimMessageType;

/**
 * Created by mrs on 2017/5/4.
 */

public interface MessageType extends TimMessageType {

    /*发送时的MessageType类型字段*/
    String TXT = "TEXT";//发送出的文本类型
    String CUSTOM = "CUSTOM";//发送出的自定义类型
    String IMAGE = "IMAGE";//发送出的图片类型
    String FILE = "FILE";//发送出的文件类型
    String SOUND = "SOUND";//发送出的音频类型
    String LOCATION = "LOCATION";//发送出的地址类型


    /*接收到的消息类型字段*/
    String NORMAL = "NORMAL";//普通文本接收到的类型
    String IMAGE_VIEWABLE = "IMAGE_VIEWABLE";//用户发出，接收到的图片
    String COUPON_NOTIFICATION = "COUPON_FETCH_SUCCESS_NOTICE_TEXT";//抢到的红包通知
    String COUPON_CREATE_SUCCESS_NOTICE_TEXT = "COUPON_CREATE_SUCCESS_NOTICE_TEXT";//  红包发送成功消息
    String COIN_REWARD_NOTICE = "COIN_REWARD_NOTICE";//打赏

    String TRANSMIT_SUBJECT_TEXT = "TRANSMIT_SUBJECT_TEXT";//转发
    String TRANSMIT_PK_GAME_BOARD_TEXT = "TRANSMIT_PK_GAME_BOARD_TEXT";//转发PK
    String TRANSMIT_HAND_SPEED_TEXT = "TRANSMIT_HAND_SPEED_TEXT";//转发的极限手速

    String GROUP_MESSAGE_DELETE_TEXT = "GROUP_MESSAGE_DELETE_TEXT";//群主删除消息
}
