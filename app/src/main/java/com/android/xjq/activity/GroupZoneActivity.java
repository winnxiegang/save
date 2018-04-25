package com.android.xjq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.StatusBarCompat;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.utils.picasso.PicassoLoadCallback;
import com.android.banana.groupchat.base.BaseListActivity;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.SubjectComposeListBean2;
import com.android.xjq.bean.SubjectsComposeBean2;
import com.android.xjq.utils.SubjectUtils2;
import com.android.xjq.utils.live.BitmapBlurHelper;
import com.android.xjq.utils.singleVideo.SinglePlayCallback;
import com.android.xjq.utils.singleVideo.SinglePlayManager;
import com.android.xjq.utils.singleVideo.VideoViewHolder;

import org.json.JSONObject;

/**
 * Created by qiaomu on 2018/2/2.
 * <p>
 * 群空间
 */

public class GroupZoneActivity extends BaseListActivity<SubjectsComposeBean2> implements IHttpResponseListener<SubjectComposeListBean2>, SinglePlayCallback {

    private View mHeaderView;
    private WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);
    private String groupId, groupMemo, groupName, groupLogoUrl;
    private SinglePlayManager mSinglePlayManager = new SinglePlayManager();

    public static void startGroupZoneActivity(Context from, String groupId, String groupName, String groupMemo, String groupLogoUrl) {
        Intent intent = new Intent(from, GroupZoneActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupMemo", groupMemo);
        intent.putExtra("groupLogoUrl", groupLogoUrl);
        from.startActivity(intent);
    }

    @Override
    protected void initEnv() {
        groupId = getIntent().getStringExtra("groupId");
        groupName = getIntent().getStringExtra("groupName");
        groupMemo = getIntent().getStringExtra("groupMemo");
        groupLogoUrl = getIntent().getStringExtra("groupLogoUrl");

    }

    @Override
    public int getContentViewLayoutRes() {
        return R.layout.activity_group_zone;
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        setLoadMoreEnable(true);

        setUpToolbar(getString(R.string.group_zone_title), -1, MODE_BACK);
        inflaterHeader();

        mSinglePlayManager.attach(mPullRecycler.getRecyclerView(), this);
    }

    private void inflaterHeader() {
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.activity_group_zone_header, mPullRecycler, false);
        getLayoutManager().setScrollVerticalEnable(false);
        int start = getResources().getDimensionPixelOffset(R.dimen.titleBarHeight_40dp);
        mPullRecycler.getSwipeRefreshLayout().setProgressViewOffset(false, start, start * 2);
        mPullRecycler.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int color = ContextCompat.getColor(GroupZoneActivity.this, R.color.main_red);

                int headerScrollDistance = Math.abs(mHeaderView.getTop());
                if (headerScrollDistance > mToolbar.getHeight()) {
                    headerScrollDistance += mToolbar.getHeight();
                    headerScrollDistance = Math.min(headerScrollDistance, mHeaderView.getHeight());
                }
                int alpha = (int) (headerScrollDistance * 1.0f / mHeaderView.getHeight() * 255);
                mToolbar.setBackgroundColor(StatusBarCompat.alphaColor(color, alpha));
            }
        });

        ((TextView) mHeaderView.findViewById(R.id.title_name)).setText(groupName);
        ((TextView) mHeaderView.findViewById(R.id.title_memo)).setText(groupMemo);
        PicUtils.load(this, ((ImageView) mHeaderView.findViewById(R.id.title_portraitIv)), groupLogoUrl, R.drawable.user_default_logo);
        PicUtils.loadWithBitmap(this, groupLogoUrl, new PicassoLoadCallback() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap) {
                if (bitmap == null)
                    return;
                ((ImageView) mHeaderView.findViewById(R.id.title_portraitIv)).setImageBitmap(bitmap);
                Bitmap blur = BitmapBlurHelper.doBlur(GroupZoneActivity.this, bitmap, 4);
                mHeaderView.setBackground(new BitmapDrawable(blur));
            }
        });

        addHeaderView(mHeaderView);
    }

    @Override
    public void onRefresh(boolean refresh) {
        mSinglePlayManager.stopAllVideoHolder();
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.GROUP_TOPIC_QUERY, true);
        formBody.put("groupId", groupId);
        formBody.put("currentPage", mCurPage);
        mHttpHelper.startRequest(formBody);
    }

    @Override
    public MultiTypeSupport getSupportMultiType() {
        return new MultiTypeSupport<SubjectsComposeBean2>() {
            @Override
            public int getTypeLayoutRes(SubjectsComposeBean2 composeBean2, int pos) {
                return composeBean2.getLayoutViewType();
            }
        };
    }

    @Override
    public void onBindHolder(ViewHolder holder, SubjectsComposeBean2 composeBean2, int position) {
        SubjectUtils2.bindViewHolder(this, holder, composeBean2, position, this);
    }

    @Override
    public void onItemClick(View view, int pos) {
        SubjectUtils2.onItemClick(this, mDatas.get(pos));
    }

    @Override
    public void onSuccess(RequestContainer request, SubjectComposeListBean2 composeListBean2) {
        mPaginator = composeListBean2 == null ? null : composeListBean2.paginator;
        loadCompleted(composeListBean2 == null ? null : composeListBean2.topicSimpleList);
        if (mAdapter.getItemCount() <= 0) {
            mPullRecycler.showEmptyView(false, R.drawable.ic_group_zone_empty, "空空如也", "");
        }
        fixRecyclerViewAutoScroll();
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        loadCompleted(null);
        operateErrorResponseMessage(jsonObject);
        fixRecyclerViewAutoScroll();
    }

    private void fixRecyclerViewAutoScroll(){
        //解决recyclerview先添加头部后再 显示列表数据 会自动下滑
        mPullRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLayoutManager().setScrollVerticalEnable(true);
            }
        }, 16);
    }
    @Override
    public void onPlayStatusChanged(VideoViewHolder videoViewHolder, int playStatus) {
        mSinglePlayManager.onPlayStatusChanged(videoViewHolder, playStatus);
    }

    @Override
    protected void onPause() {
        mSinglePlayManager.stopAllVideoHolder();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mSinglePlayManager.stopAllVideoHolder();
        super.onStop();
    }
}
