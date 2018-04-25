package com.android.banana.groupchat.bean;

import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by qiaomu on 2017/6/6.
 */

public class ChatRoomInfo {
    /**
     * success : true
     * nowDate : 2017-06-06 17:14:21
     * jumpLogin : false
     * chatRoomSetPageInfoClientSimple : {"chatRoomName":"圈子-COUPON_FT_SEASON_RACE-5203060","toipcName":"篮球比赛","notice":"测试创建接口1111111111","count":0,"userLevelCode":"NORMAL","permissionType":"ONLY_SPECIAL_USER_AND_MANAGER"}
     */

    public boolean success;
    public String nowDate;
    @SerializedName("chatRoomSetPageInfoClientSimple")
    public ChatRoomSetInfo chatRoomSetInfo;

    /*用户身份类型*/
    @Target(ElementType.PARAMETER)
    @StringDef({UserLevel.LEVEL_NORMAL, UserLevel.LEVEL_MANAGER})
    @interface UserLevel {
        String LEVEL_NORMAL = "NORMAL";
        String LEVEL_MANAGER = "MANAGER";
    }
    /*是否是管理员*/
    public boolean isManager() {
        if (chatRoomSetInfo.userLevelCode != null && TextUtils.equals(chatRoomSetInfo.userLevelCode, UserLevel.LEVEL_MANAGER))
            return true;
        return false;
    }

    public String getMemberCount() {
        return chatRoomSetInfo.count + "人";
    }

    public String getPermissionType(){
        if (chatRoomSetInfo==null||chatRoomSetInfo.permissionType==null)
            return "";
        return chatRoomSetInfo.permissionType.message;
    }


    public static class ChatRoomSetInfo {
        /**
         * chatRoomName : 圈子-COUPON_FT_SEASON_RACE-5203060
         * toipcName : 篮球比赛
         * notice : 测试创建接口1111111111
         * count : 0
         * userLevelCode : NORMAL
         * permissionType : ONLY_SPECIAL_USER_AND_MANAGER
         */
        public String chatRoomName;
        public String topicName;
        public String notice;
        public int count;
        public String userLevelCode;
        public PermissionType permissionType;
        public boolean sticked;
    }

    public static class PermissionType {
        public String name;
        public String message;
    }
}
