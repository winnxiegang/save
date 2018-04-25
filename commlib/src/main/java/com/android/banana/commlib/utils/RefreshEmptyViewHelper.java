package com.android.banana.commlib.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayout;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayoutDirection;


/**
 * Created by 周毅 on 2015/6/3 11:37.
 */
public class RefreshEmptyViewHelper implements SwipyRefreshLayout.OnRefreshListener {

    private OnRefreshListener mRefreshListener;

    private View mEmptyView;

    private TextView mEmptyTipTv;

    private SwipyRefreshLayout mRefreshView;

    private ImageView mEmptyIv;

    private ListView mListView;

    private RecyclerView mRecyclerView;

    private Builder mBuilder;

    public RefreshEmptyViewHelper(Activity activity, final OnRefreshListener listener, Drawable defaultDrawable) {

        this(activity, listener, defaultDrawable, null);

    }

    //关闭上拉加载和下拉刷新功能
    public void setSwipyRefreshNone(){

        mRefreshView.setDirection(SwipyRefreshLayoutDirection.NONE);
    }

    //打开上拉加载和下拉刷新功能
    public void setSwipyRefreshBoth(){

        mRefreshView.setDirection(SwipyRefreshLayoutDirection.BOTH);
    }

    public RefreshEmptyViewHelper(View view, final OnRefreshListener listener, Drawable defaultDrawable, String emptyTip) {

        if (view.findViewById(R.id.refreshLayout) == null || view.findViewById(R.id.recordEmptyView) == null) {

            throw new IllegalArgumentException("activity没有包含refreshLayout的ID View或者不包含emptyView的ID" + "View");

        }

        mRefreshListener = listener;

        mEmptyView = view.findViewById(R.id.recordEmptyView);

        mEmptyIv = (ImageView) view.findViewById(R.id.imageView);

        mEmptyTipTv = (TextView) view.findViewById(R.id.emptyTipTv);

        mRefreshView = (SwipyRefreshLayout) view.findViewById(R.id.refreshLayout);

        mListView = (ListView) view.findViewById(R.id.refreshListView);

        if (defaultDrawable != null) {

            mEmptyIv.setImageDrawable(defaultDrawable);

        }

        if (emptyTip != null) {

            mEmptyTipTv.setText(emptyTip);

        }

        mRefreshView.setDirection(SwipyRefreshLayoutDirection.BOTH);

        setRefreshLayout();

        mRefreshView.setOnRefreshListener(this);

        mListView.setEmptyView(mEmptyView);

        mEmptyTipTv.setVisibility(View.GONE);

        mEmptyIv.setVisibility(View.GONE);

        mEmptyView.setVisibility(View.GONE);

        mRefreshView.setVisibility(View.GONE);

    }

    public RefreshEmptyViewHelper(Activity activity, final OnRefreshListener listener, Drawable defaultDrawable, String emptyTip) {

        if (activity.findViewById(R.id.refreshLayout) == null || activity.findViewById(R.id.recordEmptyView) == null) {
            throw new IllegalArgumentException("activity没有包含refreshLayout的ID View或者不包含emptyView的ID" + "View");
        }

        this.mRefreshListener = listener;

        mEmptyView = activity.findViewById(R.id.recordEmptyView);

        mEmptyTipTv = (TextView) activity.findViewById(R.id.emptyTipTv);

        mEmptyIv = (ImageView) activity.findViewById(R.id.imageView);

        mRefreshView = (SwipyRefreshLayout) activity.findViewById(R.id.refreshLayout);

        mListView = (ListView) activity.findViewById(R.id.refreshListView);

        if (defaultDrawable != null) {

            mEmptyIv.setImageDrawable(defaultDrawable);

        }

        if (emptyTip != null) {

            mEmptyTipTv.setText(emptyTip);

        }

        mRefreshView.setDirection(SwipyRefreshLayoutDirection.BOTH);

        setRefreshLayout();

        mRefreshView.setOnRefreshListener(this);

       mListView.setEmptyView(mEmptyView);

        mEmptyTipTv.setVisibility(View.GONE);

        mEmptyIv.setVisibility(View.GONE);

        mEmptyView.setVisibility(View.GONE);

        mRefreshView.setVisibility(View.VISIBLE);

    }

    public void updateEmptyView(int  drawableResId, String emptyTip){


        mEmptyIv.setImageResource(drawableResId);

        mEmptyTipTv.setText(emptyTip);

    }

    public RefreshEmptyViewHelper(View rootView, Builder builder) {

        if (rootView.findViewById(R.id.refreshLayout) == null || rootView.findViewById(R.id.recordEmptyView) == null) {
            throw new IllegalArgumentException("activity没有包含refreshLayout的ID View或者不包含emptyView的ID" + "View");
        }

        initView(rootView);

        mBuilder = builder;

        if (builder.listener != null) {

            mRefreshListener = builder.listener;

            mRefreshView.setOnRefreshListener(this);

        }

        if (builder.direction == null) {
            mRefreshView.setDirection(SwipyRefreshLayoutDirection.BOTH);
        } else {
            mRefreshView.setDirection(builder.direction);
        }
        if (builder.emptyDrawable != 0) {
            mEmptyIv.setImageResource(builder.emptyDrawable);
        }
        if (builder.emptyTip != null) {
            mEmptyTipTv.setText(builder.emptyTip);
        }

        if (builder.showEmptyView) {

            mListView.setEmptyView(mEmptyView);

            if (builder.initShowEmptyView) {
                setInitShowEmptyView(View.VISIBLE);
            } else {
                setInitShowEmptyView(View.GONE);
            }

        }
    }

    /**
     * 用于简单的各种adapterView刷新
     *
     * @param activity
     * @param direction
     * @param onRefreshListener
     */
    public RefreshEmptyViewHelper(Activity activity, SwipyRefreshLayoutDirection direction, OnRefreshListener onRefreshListener) {

        mRefreshView = (SwipyRefreshLayout) activity.findViewById(R.id.refreshLayout);

        setRefreshLayout();

        mEmptyView = activity.findViewById(R.id.recordEmptyView);

        mEmptyTipTv = (TextView) activity.findViewById(R.id.emptyTipTv);

        mEmptyIv = (ImageView) activity.findViewById(R.id.imageView);

        mRefreshView = (SwipyRefreshLayout) activity.findViewById(R.id.refreshLayout);

        mListView = (ListView) activity.findViewById(R.id.refreshListView);

        if (direction != null) {

            mRefreshView.setDirection(direction);
        }

        if (onRefreshListener != null) {

            this.mRefreshListener = onRefreshListener;

            mRefreshView.setOnRefreshListener(this);
        }

    }

    /**
     * 用于RecyclerView的刷新
     *
     * @param activity
     * @param direction
     * @param onRefreshListener
     */
    public RefreshEmptyViewHelper(Activity activity, SwipyRefreshLayoutDirection direction, RecyclerView.LayoutManager layoutManager, OnRefreshListener onRefreshListener, String emptyTip) {

        mRefreshView = (SwipyRefreshLayout) activity.findViewById(R.id.refreshLayout);

        setRefreshLayout();

        mEmptyView = activity.findViewById(R.id.recordEmptyView);

        mEmptyTipTv = (TextView) activity.findViewById(R.id.emptyTipTv);

        mEmptyIv = (ImageView) activity.findViewById(R.id.imageView);

        mRefreshView = (SwipyRefreshLayout) activity.findViewById(R.id.refreshLayout);

        mRecyclerView = (RecyclerView) activity.findViewById(R.id.refreshListView);

        if (direction != null) {

            mRefreshView.setDirection(direction);
        }

        if (layoutManager !=null) {

            mRecyclerView.setLayoutManager(layoutManager);
        }

        if (emptyTip != null) {

            mEmptyTipTv.setText(emptyTip);
        }

        if (onRefreshListener != null) {

            this.mRefreshListener = onRefreshListener;

            mRefreshView.setOnRefreshListener(this);
        }
    }

    public SwipyRefreshLayout getRefreshView() {
        return mRefreshView;
    }

    private void setInitShowEmptyView(int visibility) {

        mEmptyTipTv.setVisibility(visibility);

        mEmptyIv.setVisibility(visibility);

        mEmptyView.setVisibility(visibility);

        mRefreshView.setVisibility(visibility);

    }

    private void initView(View rootView) {

        mEmptyView = rootView.findViewById(R.id.recordEmptyView);

        mEmptyTipTv = (TextView) rootView.findViewById(R.id.emptyTipTv);

        mEmptyIv = (ImageView) rootView.findViewById(R.id.imageView);

        mRefreshView = (SwipyRefreshLayout) rootView.findViewById(R.id.refreshLayout);

        mListView = (ListView) rootView.findViewById(R.id.refreshListView);

        setRefreshLayout();

    }

    private void setRefreshLayout() {
        mRefreshView.setColorSchemeResources(R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow);
    }

    public ListView getListView() {
        return mListView;
    }

    /**
     * 设置刷新失败.则继续显示空界面
     */
    private void setRefreshListEmpty() {

        mEmptyView.setVisibility(View.VISIBLE);

        mEmptyIv.setVisibility(View.VISIBLE);

        mEmptyTipTv.setVisibility(View.VISIBLE);

    }

    public void closeRefresh() {

        if (mRefreshView != null && mRefreshView.isRefreshing()) {
            mRefreshView.setRefreshing(false);
        }
        boolean ff = dataIsEmpty();
        if (ff) {
            setRefreshComplete();
        } else {
            setRefreshListEmpty();
        }

    }

    private boolean dataIsEmpty() {
        if (mListView != null) {
            return mListView.getAdapter().getCount() > 0;
        } else if (mRecyclerView !=null){
            return mRecyclerView.getAdapter().getItemCount()>0;
        }

        return  false;
    }

    private void setRefreshComplete() {

        mEmptyView.setVisibility(View.GONE);

        mRefreshView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            mRefreshListener.onLoadMore();
        } else if (direction == SwipyRefreshLayoutDirection.TOP) {
            mRefreshListener.onRefresh();
        }
    }


    public void setEmptyView(Drawable updateDrawable, String emptyTip) {

        this.mEmptyTipTv.setText(emptyTip);

        this.mEmptyIv.setImageDrawable(updateDrawable);

    }

    public static interface OnRefreshListener {

        public void onRefresh();

        public void onLoadMore();

        //在空界面点击刷新事件
        public void onEmptyRefresh();
    }


    public static class Builder {

        //是否显示指定的空界面
        private boolean showEmptyView;

        private String emptyTip;

        private int emptyDrawable;

        private SwipyRefreshLayoutDirection direction;

        private OnRefreshListener listener;

        //是否初始化就显示空界面,正常情况下，首次进入当前Activity,如果设置ListView设置了EmptyView。则默认就会显示出来。
        //由于有些时候不想要一进来就显示EmptyView。而是在数据加载失败了或者从服务端获取数据为空的情况下显示Empty。则需要将
        //Empty里面的所有元素都设置为GONE。
        private boolean initShowEmptyView;

        public Builder setShowEmptyView(boolean showEmptyView) {

            this.showEmptyView = showEmptyView;

            return this;

        }

        public Builder setEmptyTip(String emptyTip) {

            this.emptyTip = emptyTip;

            return this;

        }

        public Builder setEmptyDrawable(@DrawableRes int emptyDrawable) {

            this.emptyDrawable = emptyDrawable;

            return this;

        }

        public Builder setDirection(SwipyRefreshLayoutDirection direction) {

            this.direction = direction;

            return this;

        }

        public Builder setListener(OnRefreshListener listener) {

            this.listener = listener;

            return this;

        }

        public Builder setInitShowEmptyView(boolean initShowEmptyView) {

            this.initShowEmptyView = initShowEmptyView;

            return this;

        }
    }


}
