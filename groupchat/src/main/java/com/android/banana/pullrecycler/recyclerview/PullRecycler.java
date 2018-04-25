package com.android.banana.pullrecycler.recyclerview;

/**
 * Created by mrs on 2017/5/11.
 */


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.pullrecycler.ilayoutmanager.ILayoutManager;
import com.android.banana.pullrecycler.multisupport.LookSpanSize;


/**
 * Created by mrs on 2017/5/11.
 */

public class PullRecycler extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout refreshLayout;
    private WrapRecyclerView recyclerView;
    private LoadFrameLayout loadFrameLayout;
    private ImageView emptyIv;
    private TextView emptyTipTv, emptyTipTitle;
    private onRefreshListener listener;
    public RecyclerView.Adapter adapter;
    private ILayoutManager iLayoutmanager;
    private boolean mEnableRefresh = true;

    private boolean smoothMove, pinTop;
    private int positionToTop;
    private int mResId;
    private CharSequence mTip;
    private CharSequence mTitleTip;

    public PullRecycler(@NonNull Context context) {
        super(context);
        setUpView(context);

    }

    public PullRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUpView(context);
    }

    public PullRecycler(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpView(context);
    }


    private void setUpView(Context context) {

        View container = LayoutInflater.from(context).inflate(R.layout.base_widget_pullrecycler, this, true);

        refreshLayout = (SwipeRefreshLayout) container.findViewById(R.id.SwipeRefreshLayout);
        recyclerView = (WrapRecyclerView) container.findViewById(R.id.recycleList);
        loadFrameLayout = (LoadFrameLayout) container.findViewById(R.id.loadFramLayout);
        emptyTipTitle = (TextView) loadFrameLayout.findViewById(R.id.emptyTipTitle);
        emptyIv = (ImageView) loadFrameLayout.findViewById(R.id.imageView);
        emptyTipTv = (TextView) loadFrameLayout.findViewById(R.id.emptyTipTv);

        setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (pinTop) {
                        pinTop = false;
                        scrollToTopIfPossible(positionToTop, smoothMove, true);
                    }
//                    refreshLayout.setEnabled(mEnableRefresh);
                }
                if (newState == RecyclerView.SCROLL_STATE_SETTLING || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    refreshLayout.setEnabled(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (onScrollerYListener != null) {
                    int scrollYDistance = getScrollYDistance();
                    float overScreenHeightMultiple = scrollYDistance / screenHeight;
                    onScrollerYListener.scrolledY(scrollYDistance);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    public void setOnScrollerYListener(OnScrolledYListener onScrollerYListener) {
        this.onScrollerYListener = onScrollerYListener;
    }

    private OnScrolledYListener onScrollerYListener;

    private float screenHeight = LibAppUtil.getScreenHeight(getContext());

    /**
     * @return 返回recycler Yd轴方向移动的距离
     */
    public int getScrollYDistance() {
        int position = iLayoutmanager.findFirstVisiblePosition();
        View firstVisiableChildView = iLayoutmanager.findViewOfPosition(position);
        int itemHeight = firstVisiableChildView == null ? 0 : firstVisiableChildView.getHeight();
        return (position) * itemHeight - (firstVisiableChildView == null ? 0 : firstVisiableChildView.getTop());
    }


    public void setEmptyView(int resId, CharSequence tip, CharSequence titleTip) {
        mResId = resId;
        mTip = tip;
        mTitleTip = titleTip;

        emptyIv.setImageResource(resId);
        emptyTipTitle.setText(titleTip);

        emptyTipTv.setText(tip);
        emptyTipTv.setTextSize(12);
    }


    public void showEmptyView(boolean showOriginImage, int resId, CharSequence tip, CharSequence titleTip) {

        if (!showOriginImage) {

            emptyIv.setVisibility(VISIBLE);
            emptyIv.setImageResource(resId);
            emptyTipTitle.setVisibility(VISIBLE);
            emptyTipTitle.setText(titleTip);
            loadFrameLayout.setVisibility(VISIBLE);
            loadFrameLayout.setBackgroundColor(Color.TRANSPARENT);
        }
        emptyTipTv.setText(tip);
        emptyTipTv.setTextSize(12);
        loadFrameLayout.showEmptyView();

    }


    public void showEmptyView() {
        showEmptyView(mTip);
    }

    public void showEmptyView(CharSequence emptyTip) {
        loadFrameLayout.setVisibility(VISIBLE);
        loadFrameLayout.showEmptyView();
        emptyTipTv.setText(emptyTip);
    }

    public void showContentView() {
        loadFrameLayout.setVisibility(GONE);
        refreshLayout.setVisibility(VISIBLE);
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    public void refreshCompleted() {
        if (isRefreshing())
            refreshLayout.setRefreshing(false);
        recyclerView.refreshCompleted();
    }


    public void setLayoutManger(ILayoutManager manger) {
        this.iLayoutmanager = manger;
        recyclerView.setMyLayoutManager(manger);
    }

    public ILayoutManager getLayoutManager() {
        return iLayoutmanager;
    }

    public boolean isStackFromEnd() {
        return iLayoutmanager == null ? false : iLayoutmanager.isStackFromEnd();
    }

    public void setHasFixedSize(boolean hasFix) {
        recyclerView.setHasFixedSize(hasFix);
    }

    public boolean isRefreshing() {
        return refreshLayout.isRefreshing();
    }

    public void addHeaderView(View view) {
        recyclerView.addHeaderView(view);
    }

    public void addFooterView(View view) {
        recyclerView.addFooterView(view);
    }

    public void removeHeaderView(View view) {
        recyclerView.removeHeaderView(view);
    }

    public void removeFooterView(View view) {
        recyclerView.removeFooterView(view);
    }

    @Override
    public void onRefresh() {
        if (listener != null)
            listener.onRefresh(true);
    }

    public void setOnRefreshListener(onRefreshListener listener) {
        this.listener = listener;
        recyclerView.setOnLoadMoreListener(listener);
    }

    public void setPostRefresh() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    public void setRefresh() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    /*是否允许加载更多*/
    public boolean isEnableLoadMore() {
        return recyclerView.isLoadMoreEnable();
    }

    /*设置是否允许加载更多
    * 设置adapter之后
    * */
    public void setEnableLoadMore(boolean enableLoadMore) {
        recyclerView.setLoadMoreEnable(enableLoadMore);
    }

    /*是否允许下拉刷新*/
    public void setEnableRefresh(boolean enableRefresh) {
        mEnableRefresh = enableRefresh;
        refreshLayout.setEnabled(enableRefresh);
    }

    /*设置是否正在成刷新*/
    public void setRefreshing(boolean refresh) {
        refreshLayout.setRefreshing(refresh);
    }

    /*添加装饰,可多次调用互不影响*/
    public void addItemDecoration(RecyclerView.ItemDecoration ItemDecoration) {
        recyclerView.addItemDecoration(ItemDecoration);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        recyclerView.setItemAnimator(animator);
    }

    public WrapRecyclerView getRecyclerView() {
        return recyclerView;
    }

    /*已经处理了  三种layoutManager的兼容 获取最后一条显示的postion*/
    public int getLastVisibleItemPosition() {
        return iLayoutmanager.findLastVisiblePosition();
    }

    public int getFirstVisibleItemPosition() {
        return iLayoutmanager.findFirstVisiblePosition();
    }

    /*设置 GridLayoutManager  或者 StaggeredGridLayoutManager模式下 不同条目所占的位置   要在设置适配器之前调用*/
    public void setLookSpan(LookSpanSize lookSpan) {
        if (lookSpan == null)
            return;
        recyclerView.setLookSpan(lookSpan);
    }

    /*显示没有更多加载内容的提示*/
    public void showOverLoadView() {
        recyclerView.showOverLoadView();
    }

    /*移除没有更多加载内容的提示*/
    public void hideOverLoadView() {
        recyclerView.hideOverLoadView();
    }

    /**
     * 检测垂直方向内容填充后是否可以滚动
     * <p>
     * 如果是在可获得View高度之前调用,那么得到的返回值将是-1
     *
     * @return true可是滚动 false当前内容填充不足以滚动
     * @doc return The adapter position of the last visible view or {@link RecyclerView#NO_POSITION} if
     * there aren't any visible items.
     * @see LinearLayoutManager#findLastVisibleItemPosition()
     */
    public boolean canScrollVertical() {
        if (adapter == null || adapter.getItemCount() == 0)
            return false;
        int lastVisibleItemPosition = getLastVisibleItemPosition();
        int firstVisibleItemPosition = getFirstVisibleItemPosition();
        if (lastVisibleItemPosition <= 0 || lastVisibleItemPosition == firstVisibleItemPosition)
            return false;
        return recyclerView.canScrollVertically(-1)
                || recyclerView.canScrollVertically(1);
    }


    /*键盘弹起时 ,根据IM消息规则 要不要滚动到最后一条*/
    public void scrollToPosition(boolean smooth) {
        if (adapter == null)
            return;
        int lastItemPosition = adapter.getItemCount() - 1;
        if (lastItemPosition < 0)
            return;
        if (smooth) {
            recyclerView.smoothScrollToPosition(lastItemPosition);
        } else {
            iLayoutmanager.scrollPositionWithOffset(lastItemPosition, 0);
        }
    }

    public void scrollToPosition(int pos) {
        recyclerView.scrollToPosition(pos);
    }

    public void keepPosition(final boolean refresh, final int countBeforeUpdate, boolean scrollDown, boolean active) {
        if (refresh) {
            int position = adapter.getItemCount() - countBeforeUpdate;
            if (position >= 0)
                recyclerView.scrollToPositionWithOffset(position, 0);
        }
        if (scrollDown) {
            scrollToPosition(true);
        }


        if (!isStackFromEnd() && !active) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (canScrollVertical())
                        iLayoutmanager.setStackFromEndIfPossible(true);
                }
            }, 300);
        }
    }

    public void scrollToTopIfPossible(final int position, boolean smooth, boolean pinTop) {
        int firstItem = iLayoutmanager.findFirstVisiblePosition();
        int lastItem = iLayoutmanager.findLastVisiblePosition();
        if (position < firstItem) {
            // 如果要跳转的位置在第一个可见项之前，则smoothScrollToPosition可以直接跳转
            if (smooth) {
                recyclerView.smoothScrollToPosition(position);
            } else {
                recyclerView.scrollToPosition(position);
            }
        } else if (position <= lastItem) {
            // 如果要跳转的位置在第一个可见项之后，且在最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < recyclerView.getChildCount()) {
                int top = recyclerView.getChildAt(movePosition).getTop();
                if (smooth) {
                    recyclerView.smoothScrollBy(0, top);
                } else {
                    recyclerView.scrollBy(0, top);
                }
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，进入上一个控制语句
            if (smooth) {
                recyclerView.smoothScrollToPosition(position);
            } else {
                recyclerView.scrollToPosition(position);
            }

            if (pinTop) {
                this.pinTop = pinTop;
                this.smoothMove = smooth;
                this.positionToTop = position;
            }

        }
    }

    public void setStackFromEnd(boolean stackFromEnd) {
        iLayoutmanager.setStackFromEndIfPossible(stackFromEnd);
    }

    public SparseArray<View> getHeaders() {
        return recyclerView.getHeaders();
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return refreshLayout;
    }


}
