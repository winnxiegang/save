package com.android.banana.groupchat.groupchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.banana.R;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.groupchat.base.BaseListActivity;
import com.android.banana.groupchat.bean.GroupJoinListBean;
import com.android.banana.groupchat.groupchat.chat.GroupChatActivity;
import com.android.banana.groupchat.groupchat.groupcreat.FamilyVerifyCommandActivity;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

/**
 * Created by qiaomu on 2018/2/23.
 * <p>
 * 已加入的群聊列表
 */

public class GroupJoinListActivity extends BaseListActivity<GroupJoinListBean.GroupJoinBean> implements IHttpResponseListener<GroupJoinListBean> {
    private WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);

    public static void startGroupJoinListActivity(Context from) {
        Intent intent = new Intent(from, GroupJoinListActivity.class);
        from.startActivity(intent);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(this, R.drawable.base_divider_list);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        setLoadMoreEnable(false);
        setUpToolbar(R.string.title_group_join, R.menu.menu_add, MODE_BACK);
        View searchView = LayoutInflater.from(this).inflate(R.layout.layout_search, mPullRecycler, false);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupJoinListActivity.this, SearchAndJoinGroupActivity.class));
            }
        });
        ImageView searchIv = (ImageView)searchView.findViewById(R.id.search_iv);
        Drawable mutate = ContextCompat.getDrawable(this, R.drawable.icon_search_main_white).mutate();
        DrawableCompat.setTint(mutate, Color.parseColor("#80666666"));
        searchIv.setImageDrawable(mutate);

        mPullRecycler.addHeaderView(searchView);
        mPullRecycler.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.item_group_chat_notification;
    }

    @Override
    public void onBindHolder(ViewHolder holder, GroupJoinListBean.GroupJoinBean groupJoinBean, int position) {
        holder.setImageByUrl(this, R.id.chatRoomPhotoIv, groupJoinBean.groupLogoUrl)
                .setText(R.id.chatRoomName, groupJoinBean.name)
                .setText(R.id.userCountTv, groupJoinBean.userCount > 0 ? "(" + groupJoinBean.userCount + ")人" : "")
                .setText(R.id.timeTv, "")
                .setText(R.id.newNotificationTv, groupJoinBean.memo)
                .setViewVisibility(R.id.applyResultTv, View.GONE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        startActivity(new Intent(this, FamilyVerifyCommandActivity.class));
        return super.onMenuItemClick(item);
    }

    @Override
    public void onItemClick(View view, int pos) {
        GroupJoinListBean.GroupJoinBean groupJoinBean = mDatas.get(pos);
        GroupChatActivity.start2GroupChatActivity(this, groupJoinBean.id, groupJoinBean.name, groupJoinBean.groupLogoUrl);
    }

    @Override
    public void onRefresh(boolean refresh) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.USER_JOINED_GROUP_INFO_QUERY, true);
        formBody.put("currentPage", mCurPage);
        mHttpHelper.startRequest(formBody);
    }


    @Override
    public void onSuccess(RequestContainer request, GroupJoinListBean groupJoinListBean) {
        mPaginator = groupJoinListBean.paginator;
        loadCompleted(groupJoinListBean == null ? null : groupJoinListBean.groupChatList);
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        loadCompleted(null);
        operateErrorResponseMessage(jsonObject);
    }
}
