package com.android.banana.groupchat.bean;

/**
 * Created by qiaomu on 2017/11/3.
 */

public class SimpleChatConfigBean {


    /**
     * success : true
     * nowDate : 2017-11-04 15:51:36
     * jumpLogin : false
     * soleId : 2650439317004610980058414991
     * id : 2650439331204610980052781800
     * authKey : 2017110415513669132ec3e2c54f13b4364cb158ffee6b
     * gmtExpired : 2017-11-05 15:51:36
     * signKey : cdae13573fd5401596b80f6da6989469
     * identifyId : 201711041551369b0eb7681b734ad9a1e3380c3d7cf3c7
     * canCoinReward : true
     * userLogoUrlPrefix 头像地址
     */

    public boolean success;
    public String nowDate;
    public boolean jumpLogin;
    public String soleId;
    public String id;//==authid
    public String authKey;
    public String gmtExpired;
    public String signKey;
    public String identifyId;//聊天室认证id
    public boolean canCoinReward;//私聊是否显示打赏图标
    public String userLogoUrlPrefix;//头像地址
}
