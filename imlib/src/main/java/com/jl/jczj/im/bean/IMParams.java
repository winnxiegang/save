package com.jl.jczj.im.bean;

import java.io.Serializable;

/**
 * Created by qiaomu on 2017/6/16.
 */

public class IMParams implements Serializable {
    public static String groupCode;//群聊中返回的 ，比分直播用objectId_objectType拼接
      /*enter_operate接口返回*/

    public static String identifyId;

    /*login_invalidate返回*/

    public static String authId;

    /*不是很明白这个字段是什么意思,但是IM用得到*/
    public static String authKey;

    public static String groupId;
    /*登陆者id*/
    public static String userId;


    /*登陆者的loginKey，也是竞彩之家的签名key*/
    public static String loginKey;

    /*IM签名key*/
    public static String signKey;

    public static void initParams(String authId_, String authKey_,
                                  String groupId_, String userId_,
                                  String loginKey_,
                                  String signKey_,
                                  String identifyId_) {
        authId = authId_;
        authKey = authKey_;
        groupId = groupId_;
        userId = userId_;
        loginKey = loginKey_;
        signKey = signKey_;
        identifyId = identifyId_;
    }
}
