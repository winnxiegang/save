package com.android.banana.groupchat.groupchat.setInfo;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.android.banana.R;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayout;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayoutDirection;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.groupchat.bean.GroupUserApplyJoinSimpleBean;
import com.android.banana.groupchat.bean.GroupUserStayAuditQueryBean;
import com.android.banana.groupchat.groupchat.widget.MemberCheckListView;
import com.android.banana.http.JczjURLEnum;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by kokuma on 2017/10/27.
 */

public class GroupMemberCheckActivity extends BaseActivity4Jczj implements IHttpResponseListener,View.OnClickListener {
    private WrapperHttpHelper httpHelper;
    private String groupId = "", groupChatId;
    private GroupUserStayAuditQueryBean stayAuditQueryBean;
    private int currentPage =1;
    MemberCheckListView listviewMember;

    //private int currentPage;
    SwipyRefreshLayout refreshLayout;


    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(groupId)) {
            return;
        }
        int id = view.getId();
        GroupUserApplyJoinSimpleBean bean  = (GroupUserApplyJoinSimpleBean) view.getTag();
        if (id == R.id.tvRefuse) {
            toRefuseOrAgree(bean,false);
        } else if (id == R.id.tvAgree) {
            toRefuseOrAgree(bean,true);
        }

    }

    private void toRefuseOrAgree(GroupUserApplyJoinSimpleBean bean,boolean isAgree) {
        if (TextUtils.isEmpty(groupId)||TextUtils.isEmpty(groupChatId)) {
            return;
        }

        RequestFormBody container = new RequestFormBody(JczjURLEnum.USER_MANAGE_GROUP_JOINED_OPERATION, true);
        container.put("groupId", groupId);
        container.put("groupChatId", groupChatId);
        container.put("applyId", bean.getApplyId());
        container.put("aduitStatus",isAgree?"AGREEED":"REFUSED");
        container.setGenericClaz(GroupUserStayAuditQueryBean.class);

        httpHelper.startRequest(container, true);
    }

    @Override
    public void onSuccess(RequestContainer request, Object result) {
        stopRefresh();
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        if (requestEnum == JczjURLEnum.GROUP_USER_STAY_AUDIT_QUERY && result != null) {
            GroupUserStayAuditQueryBean bean = (GroupUserStayAuditQueryBean) result;
            stayAuditQueryBean = bean;
            if (stayAuditQueryBean!=null) {
                if(stayAuditQueryBean.getPaginator()!=null&&stayAuditQueryBean.getPaginator().getPages()<currentPage){
                    return;
                }
                if (currentPage==1) {
                    listviewMember.getData().clear();
                }
                listviewMember.update_add(stayAuditQueryBean.getGroupUserApplyJoinClientSimpleList());
            }
        }else if (requestEnum == JczjURLEnum.USER_MANAGE_GROUP_JOINED_OPERATION && result != null){
//            GroupUserStayAuditQueryBean messageParamBean = (GroupUserStayAuditQueryBean) result;
//            if (messageParamBean!=null&&messageParamBean.isSuccess()) {
//                //
//            }
            getCheckingMembers();
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        stopRefresh();
    }


    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_member_check);
    }

    @Override
    protected void setUpView() {
        refreshLayout = (SwipyRefreshLayout) findViewById(R.id.refreshLayout);
        listviewMember = (MemberCheckListView) findViewById(R.id.listviewMember);
        listviewMember.onClickListener = this;
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    currentPage =1;
                    getCheckingMembers();
                }else if (direction == SwipyRefreshLayoutDirection.BOTTOM){
                    currentPage++;
                    getCheckingMembers();
                }
            }
        });
    }

    @Override
    protected void setUpData() {
        currentPage =1;
        Intent intent = getIntent();
        if (intent!=null) {
            groupChatId = intent.getStringExtra("groupChatId");
            groupId = intent.getStringExtra("groupId");
        }
        setUpToolbar(getString(R.string.group_check), -1, MODE_BACK);

        httpHelper = new WrapperHttpHelper(this);
        getCheckingMembers();
    }

    void getCheckingMembers(){
        if (TextUtils.isEmpty(groupId)) {
            return;
        }else {
            LogUtils.i("xxl","groupId=="+groupId);
        }
        RequestFormBody container = new RequestFormBody(JczjURLEnum.GROUP_USER_STAY_AUDIT_QUERY, true);
        container.put("groupId", groupId);
        container.put("currentPage", currentPage);
        container.setGenericClaz(GroupUserStayAuditQueryBean.class);

        httpHelper.startRequest(container, true);
    }

    public void stopRefresh() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
            refreshLayout.clearAnimation();
        }
    }

}
