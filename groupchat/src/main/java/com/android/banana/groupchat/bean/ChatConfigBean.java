package com.android.banana.groupchat.bean;

import android.text.TextUtils;

import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.groupchat.chatenum.ChatRoomMemberLevelEnum;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by mrs on 2017/4/12.
 */

public class ChatConfigBean {


    /********************************检查是否登录聊天室返回的 ********************************************************/
    //@Doc拉取聊天记录检查是否登录过
    public String id;
    public String authKey;
    public String signKey;
    public String identifyId;


    /*-----------------------------------------发消息 发红包 方案之前的权限检验接口----------------------------------*/
    public NormalObject error; //错误说明
    public String gmtExpired;
    // public boolean forbidden;       同下面群聊聊天室配置的字段
    // public boolean groupChatOpen;   同下面群聊聊天室配置的字段
    public boolean pushCoupon;
    public boolean pushImg;

    /*************************************进入群聊群聊聊天室配置***********************************************************/
    public ForbiddenAction forbiddenAction;//禁言forbidden==true,forbiddenAction!=null 是一段时间禁言，否则是永久禁言
    public boolean forbidden;
    public boolean showCoupon;
    public boolean groupChatOpen;
    public String userRoleCode;//NORMAL

    @SerializedName("groupSetPageInfoClientSimple")
    public GroupChatSimpleInfo groupInfo;
    public Map<String, String> userIdAndRoleCodeMap;
    //仅允许管理员或者部分人发言shezhi
    public NormalObject groupMessageSendAuthority;

    public boolean isManager() {
        return !TextUtils.equals(ChatRoomMemberLevelEnum.NORMAL.name(), userRoleCode);
    }

}
