package com.android.xjq.controller.live;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.SpannableStringHelper;
import com.android.banana.pullrecycler.ilayoutmanager.MyGridLayoutManager;
import com.android.banana.pullrecycler.recyclerview.PullRecycler;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;
import com.android.httprequestlib.RequestContainer;
import com.android.jjx.sdk.utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.adapter.live.LiveWallAdapter;
import com.android.xjq.bean.liveWall.LiveWallInfoEntity;
import com.android.xjq.bean.liveWall.TopicSimpleListBean;
import com.etiennelawlor.imagegallery.library.view.GridSpacesItemDecoration;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2018/1/30.
 */

public class LiveWallController extends BaseLiveController<LiveActivity> implements onRefreshListener {

    private ViewHolder mViewHolder;
    private RecyclerView.Adapter mAdapter;
    private int srcContentHeight;
    private static final long ANIM_DURATION = 500L;
    private int mCurPage = 1;
    private PaginatorBean mPaginator;
    private List<TopicSimpleListBean> mDatas = new ArrayList<>();

    public LiveWallController(LiveActivity context) {
        super(context);
    }

    @Override
    public void init(View view) {
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder(view);
        }
        initView();
    }

    private void initView() {

        MyGridLayoutManager manager = new MyGridLayoutManager(context, 2);
        mViewHolder.recycler.setLayoutManger(manager);
        mViewHolder.recycler.setEmptyView(R.drawable.icon_no_content_live_wall, "",
                SpannableStringHelper.changeTextColor(context.getString(R.string.live_wall_empty), Color.WHITE));
        mAdapter = new LiveWallAdapter(context, mDatas, R.layout.item_live_wall_recyclerview, null);
        mViewHolder.recycler.setAdapter(mAdapter);
        mViewHolder.recycler.setOnRefreshListener(new onRefreshListener() {
            @Override
            public void onRefresh(boolean refresh) {
                if (refresh) {
                    mCurPage = 1;
                    mViewHolder.recycler.setEnableLoadMore(true);
                }
                LiveWallController.this.onRefresh(refresh);
            }
        });
        mViewHolder.recycler.addItemDecoration(new GridSpacesItemDecoration((int) DimensionUtils.dpToPx(2, context), (int) DimensionUtils.dpToPx(8, context), 2));
        // mViewHolder.recycler.setPostRefresh();
        mViewHolder.expandTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int destHeight = DimensionUtils.getScreenHeight(context);
                if (!isExpand && srcContentHeight == 0) {
                    srcContentHeight = mViewHolder.liveWallContent.getHeight();
                }
                if (!isExpand) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mViewHolder.liveWallContent.getLayoutParams();
                    params.topMargin = destHeight - srcContentHeight;
                    mViewHolder.liveWallContainer.removeView(mViewHolder.liveWallContent);
                    ((ViewGroup) context.getWindow().getDecorView()).addView(mViewHolder.liveWallContent);
                }
                startAnimation(srcContentHeight, destHeight);
            }
        });

    }

    private boolean isExpand;

    private void startAnimation(final int srcHeight, final int destHeight) {
        ValueAnimator viewAnimator = ValueAnimator.ofInt(srcHeight, destHeight);
        viewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mViewHolder.liveWallContent.getLayoutParams();
                float valuePercent = isExpand ? animatedFraction : 1 - animatedFraction;
                params.topMargin = (int) (valuePercent * (destHeight - srcHeight));
                mViewHolder.liveWallContent.setLayoutParams(params);
                LogUtils.e(TAG, "animatedFraction=" + animatedFraction + "    params.topMargin=" + params.topMargin);
            }
        });

        viewAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isExpand) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mViewHolder.liveWallContent.getLayoutParams();
                    params.topMargin = 0;
                    ((ViewGroup) context.getWindow().getDecorView()).removeView(mViewHolder.liveWallContent);
                    mViewHolder.liveWallContainer.addView(mViewHolder.liveWallContent);
                    mViewHolder.expandTv.setText(context.getString(R.string.expand_more));
                } else {
                    mViewHolder.expandTv.setText(context.getString(R.string.pack_up));
                }
                isExpand = !isExpand;
            }
        });
        viewAnimator.setDuration(ANIM_DURATION);
        viewAnimator.setInterpolator(new OvershootInterpolator(0.8f));
        viewAnimator.start();
    }

    @Override
    public void responseSuccessHttp(RequestContainer requestContainer, JSONObject jsonObject) {
        LiveWallInfoEntity liveWallInfoEntity = new Gson().fromJson(jsonObject.toString(), LiveWallInfoEntity.class);
        mPaginator = liveWallInfoEntity == null ? null : liveWallInfoEntity.paginator;
        loadCompleted(liveWallInfoEntity == null ? null : liveWallInfoEntity.topicSimpleList);

    }

    private void loadCompleted(ArrayList<TopicSimpleListBean> list) {
        mViewHolder.recycler.showContentView();
        //刷新
        if (mCurPage == 1) {
            //空数据
            if ((list == null || list.size() == 0) && mDatas.size() == 0) {
                //设置空白页面,如果刷新之前有数据还是显示旧的数据
                mViewHolder.recycler.showEmptyView();
            } else if (list != null && list.size() > 0) {
                mDatas.clear();
                mDatas.addAll(list);
            }
        } else {
            //加载更多
            if (list != null && list.size() > 0)
                mDatas.addAll(list);
        }
        judeUI(list);
        mViewHolder.recycler.refreshCompleted();
        mCurPage++;
    }

    private void judeUI(List list) {
        //如果是第二页 而且返回的数据条目少于默认数量，则认为没有更多数据了，禁掉加载更多
        if (list == null || mPaginator != null && mPaginator.getPage() >= mPaginator.getPages()) {
            mViewHolder.recycler.setEnableLoadMore(false);
            if (mDatas.size() > 0)//&& mPullRecycler.canScrollVertical()
                mViewHolder.recycler.showOverLoadView();
            else if (mDatas.size() <= 0) {
                mViewHolder.recycler.showEmptyView();
                mViewHolder.recycler.hideOverLoadView();
            }
        }
    }

    @Override
    public void setView() {
        onRefresh(true);
    }

    @Override
    public void onHiddenChanged(boolean isHide) {
        if (!isHide) {
            onRefresh(true);
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh(boolean refresh) {
        if (refresh) {
            mCurPage = 1;
        }
        context.getLiveHttpRequestController().queryLiveWallList(mCurPage);
    }


    static class ViewHolder {
        @BindView(R.id.recyclerView)
        PullRecycler recycler;
        @BindView(R.id.expandTv)
        TextView expandTv;
        @BindView(R.id.liveWallContainer)
        FrameLayout liveWallContainer;
        @BindView(R.id.liveWallContent)
        LinearLayout liveWallContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}


