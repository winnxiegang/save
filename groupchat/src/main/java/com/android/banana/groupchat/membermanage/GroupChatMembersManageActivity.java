package com.android.banana.groupchat.membermanage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.swipyrefreshlayout.VpSwipeRefreshLayout;
import com.android.banana.groupchat.bean.ChatRoomMemberBean;
import com.android.banana.groupchat.chatenum.ChatRoomMemberLevelEnum;
import com.android.banana.groupchat.ilistener.onSelectClickListener;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.utils.DeleteGroupChatMemberDialog;
import com.android.banana.utils.KeyboardHelper;
import com.android.banana.view.ScrollListView;
import com.android.banana.view.TapLinearLayout;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zaozao on 2016/5/27.
 * 群聊成员管理（添加发言人，删除发言人，查看群成员）
 */
public class GroupChatMembersManageActivity extends BaseActivity implements OnHttpResponseListener, TapLinearLayout.onTapListener {

    public static final int LOOK_MEMBER_STATE = 0;//查看成员（区分管理员和普通用户）
    public static final int DELETE_MEMBER_STATE = 2;//  删除发言人
    public static final int ADD_MEMBER_STATE = 1;// 添加发言人

    ImageView backIv;
    TextView cancelTv;
    TextView okTv;
    LinearLayout searchLayout;
    TapLinearLayout layout;
    EditText searchEt;
    ImageView deleteTextIv;
    TextView searchTv;
    ScrollListView refreshListView;
    VpSwipeRefreshLayout refreshLayout;
    View myView;

    private boolean isDelete = false;

    private boolean showOkTv = true;

    private HttpRequestHelper requestHelper;

    private int currentOperateState = LOOK_MEMBER_STATE;

    private boolean isManager = false;

    private boolean isOwner = false;

    private boolean queryUserList = false;

    private int selectCount = 0;

    private String groupId;

    private boolean haveSearch = false;//是否搜索过（是否将memberList暂时存到saveMemberList）

    public boolean addManager = false;

    private GroupChatRoomMemberAdapter adapter;

    private int selectedPosition;

    //搜索成功之后将原来的数据放到此list中
    private ArrayList<ChatRoomMemberBean.GroupMemberSimpleBean> saveMemberList = new ArrayList<>();

    //进入页面获取的所有的成员列表
    private ArrayList<ChatRoomMemberBean.GroupMemberSimpleBean> memberList = new ArrayList<>();

    //普通成员列表
    private ArrayList<ChatRoomMemberBean.GroupMemberSimpleBean> normalMemberList = new ArrayList<>();

    //管理员列表
    List<ChatRoomMemberBean.GroupMemberSimpleBean> userRoleInfoSimpleList = new ArrayList<>();

    //上个页面带过来的已添加过用户
    private ArrayList<String> speakerUserIdList = new ArrayList<>();

    //搜索出来的用户
    private ChatRoomMemberBean.GroupMemberSimpleBean searchMember;

    //点完成时选中的用户列表
    private ArrayList<ChatRoomMemberBean.GroupMemberSimpleBean> addMemberList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_chat_members_manage);

        backIv = findViewOfId(R.id.backIv);

        cancelTv = findViewOfId(R.id.cancelTv);

        okTv = findViewOfId(R.id.okTv);

        searchLayout = findViewOfId(R.id.searchLayout);

        layout = findViewOfId(R.id.layout);

        searchEt = findViewOfId(R.id.searchEt);

        deleteTextIv = findViewOfId(R.id.deleteTextIv);

        searchTv = findViewOfId(R.id.searchTv);

        refreshListView = findViewOfId(R.id.refreshListView);

        refreshLayout = findViewOfId(R.id.refreshLayout);

        myView = findViewOfId(R.id.myView);

        searchEt.setOnTouchListener(onTouchListener);

        Intent intent = getIntent();

        if (intent.getStringArrayListExtra("userIdList") != null) {

            speakerUserIdList = intent.getStringArrayListExtra("userIdList");
        }

        groupId = intent.getStringExtra("groupId");

        isManager = Boolean.parseBoolean(intent.getStringExtra("isManager"));

        isOwner = Boolean.parseBoolean(intent.getStringExtra("isOwner"));

        addManager = Boolean.parseBoolean(intent.getStringExtra("addManager"));

        currentOperateState = intent.getIntExtra("operateType", 0);

        if (currentOperateState == ADD_MEMBER_STATE) {

            setTitleBar(true, "添加发言人", null);
        } else {
            if (addManager) {
                setTitleBar(true, "添加管理员", null);
            } else {
                setTitleBar(true, "聊天室成员", backClickListener);
                queryUserList = true;
            }
        }
        initView();
    }


    private void initView() {
        initial();

        requestHelper = new HttpRequestHelper(this, this);

        refreshListView = (ScrollListView) findViewById(R.id.refreshListView);

        refreshListView.setDivider(null);

        setRefreshLayout();

        adapter = new GroupChatRoomMemberAdapter(this, memberList);

        refreshListView.setAdapter(adapter);

        adapter.setOnSelectListener(selectListener);

        layout.setonTapListener(this);

        setView();

        getChatRoomMember(true);
    }


    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            okTv.setVisibility(View.GONE);
            backIv.setVisibility(View.GONE);
            searchTv.setVisibility(View.VISIBLE);
            cancelTv.setVisibility(View.VISIBLE);
            mRefreshEmptyViewHelper.setSwipyRefreshNone();
            myView.setVisibility(View.VISIBLE);
            return false;
        }
    };

    public void deleteInputText(View v) {
        searchEt.setText("");
    }

    public void toSearch(View v) {
        String searchUserName = searchEt.getText().toString();
        if (!TextUtils.isEmpty(searchUserName)) {
            searchChatRoomMember(searchUserName);
            KeyboardHelper.hideSoftInput(searchEt);
        }
    }


    View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (isDelete) {
                Intent intent = new Intent();

                intent.putExtra("memberCount", memberList.size() + "人");

                setResult(RESULT_OK, intent);
            }
            finish();
        }
    };


    //     @OnClick({R.id.cancelTv,R.id.myView})//搜索状态下点击取消和灰色布局
    public void cancelSearch() {
        searchEt.clearFocus();
        searchEt.setText("");
        if (showOkTv) {
            okTv.setVisibility(View.VISIBLE);
        }
        myView.setVisibility(View.GONE);
        backIv.setVisibility(View.VISIBLE);
        searchTv.setVisibility(View.GONE);
        cancelTv.setVisibility(View.GONE);

        mRefreshEmptyViewHelper.setSwipyRefreshBoth();

        if (haveSearch) {//只有搜索过才需要执行

            searchAndCancelHandle();
        }
        if (currentOperateState != ADD_MEMBER_STATE) {
            currentOperateState = LOOK_MEMBER_STATE;
        }
        setView();

        adapter.notifyDataSetChanged();
    }

    /**
     * 搜索之后点击取消
     */
    private void searchAndCancelHandle() {
        if (refreshLayout.getVisibility() == View.GONE) {
            mRefreshEmptyViewHelper.updateEmptyView(R.drawable.icon_no_content_about_chat_system_reply, "暂无成员");
            refreshLayout.setVisibility(View.VISIBLE);
        }

        haveSearch = false;
        if (searchMember != null && searchMember.isOpen()) {
            memberList.get(0).setOpen(false);
            adapter.notifyDataSetChanged();
        }
        memberList.clear();

        memberList.addAll(saveMemberList);

        saveMemberList.clear();//点击取消的时候将保存的数据还给memberList然后清空
        updateMemberList();
        adapter.notifyDataSetChanged();
    }

    /**
     * 搜索出来之后aaaa
     * 如果用户删除了，点击取消的时候需要和之前保存的list匹配一下，如果存在，则移除
     * 或者用户，选中，未选中，就是状态改变了，这个时候点击取消，需要更新一下页面
     */
    private void updateMemberList() {
        if (searchMember != null) {
            for (int i = 0; i < memberList.size(); i++) {
                if (memberList.get(i).getUserId().equals(searchMember.getUserId())) {

                    memberList.get(i).setSelected(searchMember.isSelected());

                    if (searchMember.isSearchAndDelete()) {
                        memberList.remove(i);
                    }
                }
            }
        }
    }

    //     @OnClick(R.id.okTv)
    public void ok() {
        switch (currentOperateState) {
            case LOOK_MEMBER_STATE://查看成员时，点击“删除成员”
                currentOperateState = DELETE_MEMBER_STATE;
                break;
            case DELETE_MEMBER_STATE://点击完成
                currentOperateState = LOOK_MEMBER_STATE;
                break;
            case ADD_MEMBER_STATE://点击“完成（5）”
                addClickComplete();
                break;
        }
        setView();

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 点击完成的时候执行，将发言人添加到发言设置页
     */
    private void addClickComplete() {
        if (selectCount > 0) {
            for (int i = 0; i < memberList.size(); i++) {
                if (memberList.get(i).isSelected()) {
                    addMemberList.add(memberList.get(i));
                }
            }
            Intent intent = new Intent();

            intent.putExtra("memberList", addMemberList);

            setResult(RESULT_OK, intent);

            finish();
        } else {
            ToastUtil.showLong(this.getApplicationContext(), "你还未选发言人哦");
        }
    }

    public static void startGroupChatMembersManageActivity(Activity activity, String groupId, boolean isManager,boolean isOwner) {

        startGroupChatMembersManageActivity(activity, groupId, isManager,isOwner, false);
    }

    public static void startGroupChatMembersManageActivity(Activity activity, String groupId, boolean isManager,boolean isOwner, boolean addManager) {

        Intent intent = new Intent();

        intent.putExtra("groupId", groupId);

        intent.putExtra("isManager", isManager + "");

        intent.putExtra("addManager", addManager + "");

        intent.putExtra("isOwner", isOwner + "");

        intent.setClass(activity, GroupChatMembersManageActivity.class);

        activity.startActivityForResult(intent, 1000);

    }

    onSelectClickListener selectListener = new onSelectClickListener() {
        @Override
        public void onSelectClick(View v, final int position, boolean deDelete) {
            selectedPosition = position;
            if (!deDelete) {//点击删除
                if (addManager && currentOperateState == LOOK_MEMBER_STATE) {

                    addRoomMember();
                } else if (currentOperateState == ADD_MEMBER_STATE) {
                    if (!memberList.get(position).isHaveAddComplete()) {
                        if (memberList.get(position).isSelected()) {
                            memberList.get(position).setSelected(false);
                            selectCount--;
                        } else {
                            memberList.get(position).setSelected(true);
                            selectCount++;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    okTv.setText("完成(" + selectCount + ")");

                } else {
                    boolean canSecretChat = false;
                    //自己和自己不能私聊，有一方是群主和管理员才可以
                    if (isManager || memberList.get(position).isShowIdentify()
                            && (!TextUtils.equals(LoginInfoHelper.getInstance().getUserId(), memberList.get(position).getUserId()))) {
                        canSecretChat = true;
                    }
                    Intent intent = new Intent("com.android.xjq.userinfo");
                    intent.putExtra("userId", memberList.get(position).getUserId());
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("isAdmin", isManager);
                    intent.putExtra("isOwner", isOwner);
                    intent.putExtra("mClickObjectRoleCode", memberList.get(position).getLevelCode());
                    startActivity(intent);
                }
            } else {//滑动点击删除
                deleteMember(position);
            }
        }

        @Override
        public void deleteSuccess(int position) {

            if (haveSearch) {
                searchMember.setSearchAndDelete(true);
                okTv.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.GONE);
                mRefreshEmptyViewHelper.closeRefresh();
            }
            if (memberList.get(selectedPosition).isShowIdentifyTitle() && selectedPosition != memberList.size() - 1) {
                memberList.get(selectedPosition + 1).setShowIdentifyTitle(true);
            }
            memberList.remove(selectedPosition);
            isDelete = true;
            adapter.notifyDataSetChanged();
        }

    };

    private void deleteMember(int position) {
        new DeleteGroupChatMemberDialog(GroupChatMembersManageActivity.this, new DeleteGroupChatMemberDialog.SelectListener() {
            @Override
            public void select(boolean selected) {
                deleteChatRoomMember(selected);
            }
        });
    }

    /**
     * 根据operateType显示
     * 给adapter设置operateType
     */
    private void setView() {
//        adapter.setDelete(false);
        switch (currentOperateState) {
            case LOOK_MEMBER_STATE://查看成员时

                if (addManager) {//添加管理员
                    okTv.setVisibility(View.GONE);
                    showOkTv = false;
                } else {
                    if (isManager) {
                        refreshListView.setSwipe2Dimiss(true);
                        okTv.setText("删除成员");
                        adapter.setDelete(true);
                    } else {
                        okTv.setVisibility(View.GONE);
                        showOkTv = false;
                    }
                }
                break;
            case DELETE_MEMBER_STATE://
                if (memberList.size() > 0) {
                    okTv.setText("完成");
                }
                break;
            case ADD_MEMBER_STATE:
                okTv.setText("完成(" + selectCount + ")");
                break;
        }
        adapter.setOperateType(currentOperateState);
    }


    private void searchRequestSuccess(JSONObject jo) {

        ChatRoomMemberBean chatRoomMemberBean = new Gson().fromJson(jo.toString(), ChatRoomMemberBean.class);

        searchMember = chatRoomMemberBean.getUserInfoSimple();

        for (int i = 0; i < memberList.size(); i++) {
            if (searchMember.getUserId().equals(memberList.get(i).getUserId())) {
                if (memberList.get(i).isHaveAddComplete()) {
                    searchMember.setHaveAddComplete(true);
                }
                if (memberList.get(i).isShowIdentify()) {
                    searchMember.setShowIdentify(true);
                }
                if (memberList.get(i).isSelected()) {
                    searchMember.setSelected(true);
                }
            }
        }
        if (showOkTv) {
            okTv.setVisibility(View.VISIBLE);
        }

        searchMember.setUserName(searchMember.getLoginName());

        saveCurrentList();

        memberList.add(0, searchMember);

        adapter.notifyDataSetChanged();

    }

    /**
     * 搜索之后，将当前显示的成员列表信息放到saveMemberList中
     * 将memberList清空，以便于存放搜索后的结果
     */
    private void saveCurrentList() {
        myView.setVisibility(View.GONE);

        if (saveMemberList.size() == 0) {//只保存最初的成员列表，否则如果连续多次搜索，会出问题
            saveMemberList.addAll(memberList);
        }
        memberList.clear();
    }

    private void requestSuccessMemberQuery(JSONObject jo) {

        mRefreshEmptyViewHelper.closeRefresh();

        ChatRoomMemberBean chatRoomMemberBean = new Gson().fromJson(jo.toString(), ChatRoomMemberBean.class);

        chatRoomMemberBean.operatorData();

        maxPages = chatRoomMemberBean.getPaginator().getPages();

        normalMemberList.clear();

        if (mRequestType == REFRESH) {
            memberList.clear();
            userRoleInfoSimpleList.clear();
            if (normalMemberList.size() > 0) {
                normalMemberList.clear();
            }
        }
        if (chatRoomMemberBean.getUserRoleInfoSimpleList() != null && mRequestType == REFRESH) {

            userRoleInfoSimpleList.addAll(chatRoomMemberBean.getUserRoleInfoSimpleList());

            for (int i = 0; i < userRoleInfoSimpleList.size(); i++) {

                if (speakerUserIdList.contains(userRoleInfoSimpleList.get(i).getUserId())) {//已经被选过的做个标记
                    userRoleInfoSimpleList.get(i).setHaveAddComplete(true);
                }
            }
            memberList.addAll(userRoleInfoSimpleList);
        }

        if (chatRoomMemberBean.getGroupMemberClientSimpleList() != null) {

            List<ChatRoomMemberBean.GroupMemberSimpleBean> list = chatRoomMemberBean.getGroupMemberClientSimpleList();

            for (int i = 0; i < list.size(); i++) {

                if (speakerUserIdList.contains(list.get(i).getUserId())) {//已经被选过的做个标记
                    list.get(i).setHaveAddComplete(true);
                }

                if ("NORMAL".equals(list.get(i).getLevelCode())) {
                    normalMemberList.add(list.get(i));
                }

            }
            //添加管理员，或者查看群成员
            if (currentOperateState == LOOK_MEMBER_STATE) {
                memberList.addAll(normalMemberList);
            } else {

                memberList.addAll(list);
            }

        }

        if (currentPage > 1) {//可能用户搜索出来添加进去的，在第二页以后，当上拉加载出来之后也要灰色掉

            updateMemberList();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void executorSuccess(RequestContainer request, JSONObject jo) {
        JczjURLEnum jczjURLEnum = (JczjURLEnum) request.getRequestEnum();
        switch (jczjURLEnum) {
            case GROUP_MEMBER_QUERY:
                requestSuccessMemberQuery(jo);
                break;
            case GROUP_USER_JOINED_QUERY:
                searchRequestSuccess(jo);
                break;
            case CHAT_MEMBER_REMOVE_RELATION_GROUP:
                selectListener.deleteSuccess(selectedPosition);
                break;
            case GROUP_USER_ROLE_ADD:
                ToastUtil.showLong(getApplicationContext(), "添加成功");
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public void executorFalse(RequestContainer request, JSONObject jo) {
        mRefreshEmptyViewHelper.closeRefresh();
        JczjURLEnum jczjURLEnum = (JczjURLEnum) request.getRequestEnum();
        switch (jczjURLEnum) {
            case CHAT_MEMBER_REMOVE_RELATION_GROUP:
                deleteFailed(jo);
                break;
            case GROUP_USER_JOINED_QUERY:
                saveCurrentList();
                refreshLayout.setVisibility(View.GONE);
                mRefreshEmptyViewHelper.updateEmptyView((R.drawable.icon_search_not_found), "未寻找到目标");
                adapter.notifyDataSetChanged();
                break;
            case GROUP_MEMBER_QUERY:
            case GROUP_USER_ROLE_ADD:
                try {
                    operateErrorResponseMessage(jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void executorFailed(RequestContainer request) {
        mRefreshEmptyViewHelper.closeRefresh();
    }

    @Override
    public void executorFinish() {
        mRefreshEmptyViewHelper.closeRefresh();
    }


    /**
     * 删除成员失败
     *
     * @param jo
     */
    private void deleteFailed(JSONObject jo) {
        try {
            ErrorBean errorBean = new ErrorBean(jo);

            String errorName = errorBean.getError().getName();

            if ("GROUP_USER_NOT_JOINED".equals(errorName)) {
                //用户未加入群聊
//                LibAppUtil.showTip(context, "删除成功", R.drawable.icon_subscribe_success);
                selectListener.deleteSuccess(selectedPosition);
            } else {
                ((BaseActivity) this).operateErrorResponseMessage(jo);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


    /**
     * 聊天室成员查询
     */
    private void getChatRoomMember(boolean showDialog) {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.GROUP_MEMBER_QUERY, true);

        map.put("groupId", groupId);

        map.put("currentPage", currentPage + "");

        map.put("queryUserList", queryUserList);

        requestHelper.startRequest(map, showDialog);
    }

    /**
     * 搜索
     */
    private void searchChatRoomMember(String userName) {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.GROUP_USER_JOINED_QUERY, true);

        map.put("groupId", groupId);

        map.put("userName", userName);

        requestHelper.startRequest(map, true);

        haveSearch = true;
    }

    /**
     * 删除成员
     */
    private void addRoomMember() {
        RequestFormBody map = new RequestFormBody(JczjURLEnum.GROUP_USER_ROLE_ADD, true);

        map.put("groupId", groupId);

        map.put("userId", memberList.get(selectedPosition).getUserId());

        map.put("roleCodes", ChatRoomMemberLevelEnum.GROUP_ADMIN.name());

        requestHelper.startRequest(map, true);
    }


    /**
     * 删除成员
     */
    private void deleteChatRoomMember(boolean selected) {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.CHAT_MEMBER_REMOVE_RELATION_GROUP, true);

        map.put("groupId", groupId);

        map.put("userId", memberList.get(selectedPosition).getUserId());

        map.put("relationGroupType", "FAMLIY_CHAT");

//        map.put("forbidden",selected+"");//是否拉入黑名单

        requestHelper.startRequest(map, true);
    }

    /**
     * 设置刷新布局
     */
    private void setRefreshLayout() {

        mRefreshEmptyViewHelper = new RefreshEmptyViewHelper(this, new RefreshEmptyViewHelper.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mRequestType = REFRESH;

                currentPage = DEFAULT_PAGE;

                getChatRoomMember(false);
            }

            @Override
            public void onLoadMore() {

                mRequestType = LOAD_MORE;

                if (currentPage >= maxPages) {
                    ToastUtil.showLong(GroupChatMembersManageActivity.this.getApplicationContext(), "没有更多了");

                    mRefreshEmptyViewHelper.closeRefresh();
                } else {
                    currentPage++;

                    getChatRoomMember(false);
                }
            }

            @Override
            public void onEmptyRefresh() {

            }
        }, getResources().getDrawable(R.drawable.icon_search_not_found), "暂无成员");
    }

    @Override
    public void onTouch(float x, float y) {
        Rect outRect = new Rect();
        searchLayout.getGlobalVisibleRect(outRect);
        if (!outRect.contains((int) x, (int) y)) {
            KeyboardHelper.hideSoftInput(searchLayout);
        }
    }


    private void initial() {

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSearch();
            }
        });

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSearch();
            }
        });

        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok();
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    deleteTextIv.setVisibility(View.GONE);
                } else {
                    deleteTextIv.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
