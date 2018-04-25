package com.android.banana.groupchat.bean;

/**
 * Created by kokuma on 2017/11/2.
 */

public class GroupBean {

    private boolean success;
    private GroupManageInfoBean groupSetPageInfo;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public GroupManageInfoBean getGroupSetPageInfo() {
        return groupSetPageInfo;
    }

    public void setGroupSetPageInfo(GroupManageInfoBean groupSetPageInfo) {
        this.groupSetPageInfo = groupSetPageInfo;
    }


}
