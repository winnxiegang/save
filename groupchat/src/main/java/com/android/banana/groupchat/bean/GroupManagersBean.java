package com.android.banana.groupchat.bean;

import java.util.List;

/**
 * Created by kokuma on 2017/11/1.
 */

public class GroupManagersBean  {

    private List<ChatRoomMemberBean.GroupMemberSimpleBean> userInfoList;
    // 置长老人数的最大配置
    private int groupManagerAuthorizationMaxCount;


    public List<ChatRoomMemberBean.GroupMemberSimpleBean> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<ChatRoomMemberBean.GroupMemberSimpleBean> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public int getGroupManagerAuthorizationMaxCount() {
        return groupManagerAuthorizationMaxCount;
    }

    public void setGroupManagerAuthorizationMaxCount(int groupManagerAuthorizationMaxCount) {
        this.groupManagerAuthorizationMaxCount = groupManagerAuthorizationMaxCount;
    }





}
