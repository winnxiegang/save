package com.android.banana.groupchat.bean;

import com.android.banana.commlib.bean.NormalObject;

/**
 * Created by qiaomu on 2017/6/21.
 */

public class ForbiddenAction {


    /**
     * id : 1031115
     * actionType : {"name":"RACE_CHAT","message":"比赛聊天"}
     * enabled : true
     * gmtCreate : 2017-06-21 10:08:56
     * gmtModified : 2017-06-21 10:08:56
     * gmtStart : 2017-06-21 10:08:56
     * totalMinutes : 30
     * gmtExpired : 2017-06-21 10:38:56
     * forbiddenReason : 额的四大
     * objectId : 8201606274175767
     * objectType : {"name":"USER","message":"会员"}
     */

    public int id;
    public NormalObject actionType;
    public boolean enabled;
    public String gmtCreate;
    public String gmtModified;
    public String gmtStart;
    public int totalMinutes;
    public String gmtExpired;
    public String forbiddenReason;
    public String objectId;
    public NormalObject objectType;

}
