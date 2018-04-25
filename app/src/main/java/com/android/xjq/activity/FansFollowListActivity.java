package com.android.xjq.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.groupchat.base.BaseListActivity;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.homepage.HomePageActivity;
import com.android.xjq.bean.UserFollowList;
import com.android.xjq.utils.SocialTools;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

/**
 * Created by qiaomu on 2018/2/5.
 * <p>
 * 承载 粉丝 和关注列表的页面
 */

public class FansFollowListActivity extends BaseListActivity<UserFollowList.UserFollow> implements IHttpResponseListener<UserFollowList> {
    //粉丝界面
    public static final int OPERATE_FANS = 0;
    //关注界面
    public static final int OPERATE_FOLLOW = 1;
    //默认会进入粉丝界面
    private int mCurOperate = OPERATE_FANS;

    private String userId;

    private WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);

    //---------------------------------------------------------------[OPERATE_FANS],[OPERATE_FOLLOW]
    public static void start2FansFollowListActivity(Context from, int operate, String userId) {
        Intent intent = new Intent(from, FansFollowListActivity.class);
        intent.putExtra("operate", operate);
        intent.putExtra("userId", userId);
        from.startActivity(intent);
    }

    @Override
    protected void initEnv() {
        mCurOperate = getIntent().getIntExtra("operate", OPERATE_FANS);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        setUpToolbar(mCurOperate == OPERATE_FANS ? R.string.title_fans : R.string.title_attention, -1, MODE_BACK);
        userId = getIntent().getStringExtra("userId");
        mPullRecycler.addItemDecoration(new DividerItemDecoration(this, R.drawable.base_divider_list));
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.item_fans_attention;
    }

    @Override
    public void onBindHolder(final ViewHolder holder, final UserFollowList.UserFollow userFollow, final int position) {
        holder.setImageByUrl(this, R.id.avart_image, userFollow.userLogoUrl)
                .setText(R.id.user_name, userFollow.userName)
                .setText(R.id.attention_count, "关注 " + userFollow.attentionNum + "   |   粉丝 " + userFollow.fansNum)
                .setChecked(R.id.attentionTv, userFollow.focus)
                .setOnClickListener(R.id.attentionTv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userFollow.focus) {
                            SocialTools.cancelAttention(userFollow.userId, "FOLLOWCANCEL", new AttentionCallback(holder, userFollow, position));
                        } else {
                            SocialTools.payAttention(userFollow.userId, new AttentionCallback(holder, userFollow, position));
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view, int pos) {
        HomePageActivity.startHomepageActivity(this, mDatas.get(pos).userId);
    }

    class AttentionCallback implements SocialTools.onSocialCallback {
        private ViewHolder mHolder;
        private UserFollowList.UserFollow mUserFollow;
        private int mPosition;

        public AttentionCallback(ViewHolder holder, UserFollowList.UserFollow userFollow, int position) {
            mHolder = holder;
            mUserFollow = userFollow;
            mPosition = position;
        }

        @Override
        public void onResponse(JSONObject response, boolean success) {
            mUserFollow.focus = !mUserFollow.focus;
            mHolder.setChecked(R.id.attentionTv, mUserFollow.focus);
            mAdapter.notifyItemChanged(mPosition);
        }
    }

    @Override
    public void onRefresh(boolean refresh) {
        RequestFormBody formBody = new RequestFormBody(mCurOperate == OPERATE_FANS ? XjqUrlEnum.USER_FANS_INFO : XjqUrlEnum.USER_ATTENTIONS_INFO, true);
        formBody.put("currentPage", mCurPage);
        formBody.put("userId", userId);
        mHttpHelper.startRequest(formBody);
    }

    @Override
    public void onSuccess(RequestContainer request, UserFollowList userFollowLists) {
        mPaginator = userFollowLists.paginator;
        loadCompleted(userFollowLists == null ? null : userFollowLists.userFollows);
        if (mAdapter.getItemCount() <= 0)
            mPullRecycler.showEmptyView(false, R.drawable.ic_fans_no_data, getString(R.string.fans_follow_empty), "");
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        loadCompleted(null);
        operateErrorResponseMessage(jsonObject);
    }
}
