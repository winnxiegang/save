package com.jl.jczj.im.service;

import com.android.httprequestlib.BaseRequestHttpName;

/**
 * Created by zhouyi on 2017/5/27.
 */

public enum IMUrlEnum implements BaseRequestHttpName {

    //进入房间之前先检查是否登陆过
    USER_AUTH_LOGIN_CHAT_VALIDATE("USER_AUTH_LOGIN_CHAT_VALIDATE"),

    //消息发送
    GROUP_USER_ENTRY_BASED_MESSAGE_SEND("GROUP_USER_ENTRY_BASED_MESSAGE_SEND"),


    //用户进入房间
    GROUP_USER_ENTRY_ENTER("GROUP_USER_ENTRY_ENTER"),

    //信息查询
    GROUP_MESSAGE_QUERY_BY_USER("GROUP_MESSAGE_QUERY_BY_USER"),

    //用户离开
    GROUP_USER_ENTRY_LEAVE("GROUP_USER_ENTRY_LEAVE"),


    //发送消息之前的权限检测
    USER_AUTHORITY_OR_CHAT_STATE_QUERY("USER_AUTHORITY_OR_CHAT_STATE_QUERY"),

    //退出聊天室
    GROUP_USER_QUIT("GROUP_USER_QUIT"),

    @Deprecated
    GROUP_MESSAGE_DELETE("GROUP_MESSAGE_DELETE");

    private String name;

    private String url;

    IMUrlEnum(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
