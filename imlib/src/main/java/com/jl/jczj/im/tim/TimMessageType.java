package com.jl.jczj.im.tim;

/**
 * Created by qiaomu on 2017/11/21.
 * <p>
 * 系统消息推送过来的 类型字段注解
 */
    
public interface TimMessageType {
    //角色变更
    String GROUP_USER_ROLE_CHANGE_TEXT = "GROUP_USER_ROLE_CHANGE_TEXT";

    //取消禁言
    String GROUP_MESSAGE_FORBIDDEN_SENDER_REMOVE_TEXT = "GROUP_MESSAGE_FORBIDDEN_SENDER_REMOVE_TEXT";

    // 禁言
    String GROUP_MESSAGE_FORBIDDEN_SENDER_ADD_TEXT = "GROUP_MESSAGE_FORBIDDEN_SENDER_ADD_TEXT";

    //踢出群
    String USER_QUIT_GROUP_TEXT = "USER_QUIT_GROUP_TEXT";

    //邀请加入群
    String INVITED_TO_GROUP_TYPE = "INVITED_TO_GROUP_TYPE";
}
