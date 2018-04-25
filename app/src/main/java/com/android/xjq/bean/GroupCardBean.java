package com.android.xjq.bean;

import java.util.List;

/**
 * Created by qiaomu on 2018/3/1.
 */

public class GroupCardBean {

    /**
     * groupId : 2856705977504610980055441344
     * userId : 8201711278725840
     * userLogoUrl : http://mapi1.xjq.net/userLogoUrl.htm?userId=8201711278725840&timestamp=1519870529000
     * userName : 我是追光少年
     * focus : false
     * groupName : 追光少年的群聊哟
     * forbidden : false
     */

    public String groupId;
    public String userId;
    public String userLogoUrl;
    public String userName;
    public boolean focus;
    public String groupName;
    public boolean forbidden;
    public String nickName;
    public boolean canInitiatePrivateChat;
    public  List<UserMedalLevelBean> userMedalLevelList;
}
