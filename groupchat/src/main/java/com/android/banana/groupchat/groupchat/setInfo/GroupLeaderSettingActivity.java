package com.android.banana.groupchat.groupchat.setInfo;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.R;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.view.swipyrefreshlayout.VpSwipeRefreshLayout;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.groupchat.bean.ChatRoomMemberBean.GroupMemberSimpleBean;
import com.android.banana.groupchat.bean.GroupManagersBean;
import com.android.banana.groupchat.groupchat.adapter.GroupLeaderAdapter;
import com.android.banana.groupchat.membermanage.GroupChatMembersManageActivity;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.view.ScrollListView;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kokuma on 2017/10/27.
 * item_leader_set  item_leader_add
 * 侧滑参照  GroupChatMembersManageActivity
 */

public class GroupLeaderSettingActivity extends BaseActivity4Jczj implements IHttpResponseListener,View.OnClickListener {

    TextView tvListTitle;
    ScrollListView scrollListView;
    VpSwipeRefreshLayout refreshLayout;
    TextView  finishTv;


    private String groupId = "" ;
    private WrapperHttpHelper httpHelper;

    GroupManagersBean  managersBean;
    List<GroupMemberSimpleBean> listMember;
    ArrayList<String> listId ;
    GroupLeaderAdapter adapter;
    int MAX_COUNT;
    final String txtToast = "管理员最大数量为";

    boolean isOverCount(){
        if(listMember.size()-1>=MAX_COUNT){
            return true;
        }
        return  false;
    }

    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(groupId)) {
            return;
        }
        int id = view.getId();
        if (id==R.id.finishTv) {
            adapter.isOpenCheckbox = !adapter.isOpenCheckbox;
            adapter.notifyDataSetChanged();
            if(adapter.isOpenCheckbox){
                finishTv.setText("删除成员");
            }else {
                finishTv.setText("编辑");
            }
        }else if (id == R.id.backIv) {
            finish();
        }else {
            GroupMemberSimpleBean bean  = null;
            if(view.getTag() instanceof GroupMemberSimpleBean){
                bean =  (GroupMemberSimpleBean) view.getTag();
            }
            if (bean != null) {
                toDelete(bean);
            }else {
                if(isOverCount()){
                    Toast.makeText(this,txtToast+MAX_COUNT,Toast.LENGTH_SHORT).show();
                    return;
                }
                GroupChatMembersManageActivity.startGroupChatMembersManageActivity(this, groupId, true,true,true);//默认是群主，这里进去的用不到这个判断
            }
        }
    }

    void toDelete(GroupMemberSimpleBean bean) {
        if (TextUtils.isEmpty(groupId)||bean==null) {
            return;
        }

        RequestFormBody container = new RequestFormBody(JczjURLEnum.GROUP_USER_ROLE_DELETE, true);
        container.put("groupId", groupId);
        container.put("userId", bean.getUserId());
        container.put("roleCode", "GROUP_ADMIN");
        container.setGenericClaz(GroupManagersBean.class);
        httpHelper.startRequest(container, true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("kk","添加成功刷新界面");
        if(requestCode==1000&&resultCode==RESULT_OK){
            getLeaders();
        }
    }

    @Override
    public void onSuccess(RequestContainer request, Object result) {
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        if (requestEnum == JczjURLEnum.GROUP_USER_ROLE_AUTHORIZATION_QUERY && result != null) {
            GroupManagersBean bean = (GroupManagersBean) result;
            listMember.clear();
            listId.clear();
            if (bean!=null) {
                managersBean = bean;
                MAX_COUNT =managersBean.getGroupManagerAuthorizationMaxCount();
                listMember.addAll(managersBean.getUserInfoList());
                for(GroupMemberSimpleBean bean1 : listMember){
                    if (bean1!=null) {
                        listId.add(bean1.getUserId());
                    }
                }
                tvListTitle.setText("管理员（"+listMember.size()+"／"+MAX_COUNT+"）");
              listMember.add(null);
                LogUtils.i("xxl","listMember-size-"+listMember.size());
                adapter.notifyDataSetChanged();
            }
        }else if (requestEnum == JczjURLEnum.GROUP_USER_ROLE_DELETE && result != null) {
            getLeaders();
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {

    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_group_set_leader);
    }

    @Override
    protected void setUpView() {
         tvListTitle = (TextView) findViewById(R.id.tvListTitle);
         scrollListView = (ScrollListView) findViewById(R.id.scrollListView);
         refreshLayout = (VpSwipeRefreshLayout) findViewById(R.id.refreshLayout);

         finishTv = (TextView) findViewById(R.id.finishTv);
        finishTv.setText("编辑");
        finishTv.setOnClickListener(this);

    }

    @Override
    protected void setUpData() {
        Intent intent = getIntent();
        if (intent!=null) {
            groupId = intent.getStringExtra("groupId");
        }
        ((TextView)findViewById(R.id.titleTv)).setText(getResources().getString(R.string.group_set_leader));
        ImageView backIv = (ImageView) findViewById(R.id.backIv);
        backIv.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(this);
        finishTv.setVisibility(View.VISIBLE);

        httpHelper = new WrapperHttpHelper(this);
        listMember = new ArrayList<>();
        listId = new ArrayList<>();

        adapter = new GroupLeaderAdapter(this,listMember);
        adapter.onClickListener =  this ;
        adapter.listId = listId;
        scrollListView.setSwipe2Dimiss(true);
        scrollListView.setDivider(null);
        scrollListView.setAdapter(adapter);

        getLeaders();
    }

    void getLeaders(){
        if (TextUtils.isEmpty(groupId)) {
            return;
        }
        RequestFormBody container = new RequestFormBody(JczjURLEnum.GROUP_USER_ROLE_AUTHORIZATION_QUERY, true);
        container.put("groupId", groupId);
        container.setGenericClaz(GroupManagersBean.class);

        httpHelper.startRequest(container, true);
    }



}
