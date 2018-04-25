package com.android.banana.groupchat.bean;

import java.util.List;

/**
 * Created by zaozao on 2017/6/1.
 */

public class SpeakerSetData {

    private int groupId;

    private SpeakSetTypeBean groupMessagePermissionType;

    private List<SpeakSetTypeBean> groupMessgaePermissionTypeList;

    private List<ChatRoomMemberBean.GroupMemberSimpleBean> userInfoList;

    public int getGroupId() {
        return groupId;
    }

    public List<SpeakSetTypeBean> getGroupMessgaePermissionTypeList() {
        return groupMessgaePermissionTypeList;
    }

    public void setGroupMessgaePermissionTypeList(List<SpeakSetTypeBean> groupMessgaePermissionTypeList) {
        this.groupMessgaePermissionTypeList = groupMessgaePermissionTypeList;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public SpeakSetTypeBean getGroupMessagePermissionType() {
        return groupMessagePermissionType;
    }

    public void setGroupMessagePermissionType(SpeakSetTypeBean groupMessagePermissionType) {
        this.groupMessagePermissionType = groupMessagePermissionType;
    }

    public List<ChatRoomMemberBean.GroupMemberSimpleBean> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<ChatRoomMemberBean.GroupMemberSimpleBean> userInfoList) {
        this.userInfoList = userInfoList;
    }

}
