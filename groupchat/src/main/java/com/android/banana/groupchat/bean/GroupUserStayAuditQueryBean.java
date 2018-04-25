package com.android.banana.groupchat.bean;

import com.android.banana.commlib.bean.PaginatorBean;

import java.util.List;

/**
 * Created by kokuma on 2017/10/31.
 */

public class GroupUserStayAuditQueryBean {
    private List<GroupUserApplyJoinSimpleBean> groupUserApplyJoinClientSimpleList;
    private PaginatorBean paginator;
    private boolean success;

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean isSuccess() {
        return success;
    }

    public List<GroupUserApplyJoinSimpleBean> getGroupUserApplyJoinClientSimpleList() {
        return groupUserApplyJoinClientSimpleList;
    }

    public void setGroupUserApplyJoinClientSimpleList(List<GroupUserApplyJoinSimpleBean> groupUserApplyJoinClientSimpleList) {
        this.groupUserApplyJoinClientSimpleList = groupUserApplyJoinClientSimpleList;
    }


    public PaginatorBean getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }


}
