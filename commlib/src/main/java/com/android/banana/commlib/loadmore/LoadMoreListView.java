package com.android.banana.commlib.loadmore;

/**
 * Created by lingjiu on 2017/3/31.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 上拉加载ListView
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    /**
     * 底部显示正在加载的页面
     */
    private CommonLoadMoreView footerView = null;

    private Context context;

    private OnLoadMoreListener onLoadMoreListener;

    private int firstVisibleItem;

    private boolean isReachBottom;

    public LoadMoreListView(Context context) {
        super(context);
        this.context = context;
        initListView();
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initListView();
    }

    public CommonLoadMoreView getFooterView() {
        return footerView;
    }


    /**
     * 初始化ListView
     */
    private void initListView() {

        // 为ListView设置滑动监听
        setOnScrollListener(this);
        // 去掉底部分割线
        setFooterDividersEnabled(false);
    }

    /**
     * 初始化上啦加载
     * <p>
     * 在setAdapter之前调用
     */
    public void initBottomView(boolean loadMoreEnable) {
        if (!loadMoreEnable) return;
        if (footerView == null) {

            footerView = new CommonLoadMoreView(this.context);
        }
        addFooterView(footerView);
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

        //当滑动到底部时
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && firstVisibleItem != 0 && isReachBottom) {
            if (onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMore();
            }

        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        if (footerView != null) {
            //判断可视Item是否能在当前页面完全显示
            if (visibleItemCount == totalItemCount) {
                // removeFooterView(footerView);
                if (visibleItemCount > 1) {
                    footerView.setVisibility(View.GONE);
//                    //首次加载如果第一页的条目较少,可视条目没有填充满，即立即加载下一页
//                    if (firstVisibleItem == 0 && !isFirstRequest) {
//                        isFirstRequest = true;
//                        onLoadMoreListener.onLoadMore();
//                    }
                } else {
                    footerView.setVisibility(View.GONE);//隐藏底部布局
                }
            } else {
                // addFooterView(footerView);
                footerView.setVisibility(View.VISIBLE);//显示底部布局
            }

            if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                View lastVisibleItemView = getChildAt(getChildCount() - 1);
                if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == getHeight()) {
                    isReachBottom = true;
                    Log.d("ListView", "##### 滚动到底部 ######");
                }
            } else {
                isReachBottom = false;
            }

        }
    }

    public void setOnLoadMoreListener(final OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        if (footerView == null) return;
        ((CommonLoadMoreView) footerView).setOnRetryClickListener(new OnRetryClickListener() {
            @Override
            public void OnRetryClick() {
                if (onLoadMoreListener != null) {

                    onLoadMoreListener.onLoadMore();
                }
            }
        });
    }

}