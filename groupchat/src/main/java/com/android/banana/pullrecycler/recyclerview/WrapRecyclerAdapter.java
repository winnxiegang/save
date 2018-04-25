package com.android.banana.pullrecycler.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.android.banana.pullrecycler.multisupport.LookSpanSize;
import com.android.banana.pullrecycler.multisupport.ViewHolder;


/**
 * Created by mrs on 2017/4/7.
 * <p>
 * <p>
 * 添加多头部 尾部 加载更多适配器
 * <p>
 * <p>
 * 配合WrapRecyclerView 使用
 */

public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = this.getClass().toString();
    private View loadMoreView;
    private boolean loadMoreEnable;
    private boolean mShowOverloadView;
    // 用来存放底部和头部View的集合  比Map要高效一些
    // 可以点击进入看一下官方的解释
    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFooterViews = new SparseArray<>();

    // 基本的头部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_HEADER = 10000000;
    // 基本的底部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_FOOTER = 20000000;

    private static int TYPE_LOAD_MORE = 3000000;

    // 列表的Adapter
    private RecyclerView.Adapter mAdapter;
    private LookSpanSize lookSpan;


    public WrapRecyclerAdapter(RecyclerView.Adapter adapter, View loadMoreView, LookSpanSize lookSpan) {
        this.loadMoreView = loadMoreView;
        this.mAdapter = adapter;
        this.lookSpan = lookSpan;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // viewType 可能就是 SparseArray 的key
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.get(viewType);
            return new ViewHolder(headerView);
        }

        if (isFooterViewType(viewType)) {
            View footerView = mFooterViews.get(viewType);
            return new ViewHolder(footerView);
        }
        if (isLoadMoreLayoutEnable() && viewType == TYPE_LOAD_MORE) {
            return new ViewHolder(loadMoreView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    private boolean isLoadMoreLayoutEnable() {
        return loadMoreEnable || mShowOverloadView;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position) || isFooterPosition(position) || ((isLoadMoreLayoutEnable()) && position == getItemCount() - 1)) {
            if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
            return;
        }

        // 计算一下位置
        position = position - mHeaderViews.size();
        mAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if ((isLoadMoreLayoutEnable()) && position == getItemCount() - 1 && mAdapter.getItemCount() > 0) {
            return TYPE_LOAD_MORE;
        }

        if (isHeaderPosition(position)) {
            // 直接返回position位置的key
            return mHeaderViews.keyAt(position);
        }

        if (isFooterPosition(position)) {
            // 直接返回position位置的key
            position = position - mHeaderViews.size() - mAdapter.getItemCount();
            return mFooterViews.keyAt(position);
        }

        // 返回列表Adapter的getItemViewType
        position = position - mHeaderViews.size();

        return mAdapter.getItemViewType(position);
    }

    private boolean isFooterViewType(int viewType) {
        int position = mFooterViews.indexOfKey(viewType);
        return position >= 0;
    }
    
    private boolean isHeaderViewType(int viewType) {
        if (mHeaderViews.size() == 0)
            return false;
        int position = mHeaderViews.indexOfKey(viewType);
        return position >= 0;
    }


    private boolean isFooterPosition(int position) {
        return mHeaderViews.size() == 0 ? false : position >= (mHeaderViews.size() + mAdapter.getItemCount());
        // + (loadMoreEnable ? 1 : 0);
    }

    private boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }

    @Override
    public int getItemCount() {
        // 条数三者相加 = 底部条数 + 头部条数 + Adapter的条数、
        int itemCount = mAdapter.getItemCount();
        int count = itemCount + mHeaderViews.size() + mFooterViews.size() + ((itemCount) > 0 && (mShowOverloadView || loadMoreEnable) ? 1 : 0);
        return count;
    }

    // 添加头部
    public void addHeaderView(View view) {
        int position = mHeaderViews.indexOfValue(view);
        if (position < 0) {
            mHeaderViews.put(BASE_ITEM_TYPE_HEADER++, view);
            notifyItemRangeInserted(0,0);
        }
    }

    public SparseArray<View> getHeaderViews() {
        return mHeaderViews;
    }

    public SparseArray<View> getFooterViews() {
        return mFooterViews;
    }

    // 添加底部
    public void addFooterView(View view) {
        int position = mFooterViews.indexOfValue(view);
        if (position < 0) {

            mFooterViews.put(BASE_ITEM_TYPE_FOOTER++, view);
        }
        notifyItemInserted(getItemCount());
    }

    // 移除头部
    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) return;
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    // 移除底部
    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index < 0)
            return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (mAdapter != null) {
            mAdapter.onViewRecycled(holder);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (mAdapter != null) {
            mAdapter.onDetachedFromRecyclerView(recyclerView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {   //  1   2
            final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter = isHeaderPosition(position)
                            || isFooterPosition(position)
                            || ((isLoadMoreLayoutEnable()) && position == getItemCount() - 1);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : (lookSpan == null ? 1 : lookSpan.getSpanSize(position));
                }
            });
        }
        if (mAdapter != null) {
            mAdapter.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }

        if (mAdapter != null) {
            mAdapter.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (mAdapter != null) {
            mAdapter.onViewDetachedFromWindow(holder);
        }
    }

    public void setLookSpan(LookSpanSize lookSpan) {
        this.lookSpan = lookSpan;
        notifyDataSetChanged();
    }

    public void setLoadMoreView(View view) {
        this.loadMoreView = view;
    }

    public void setLoadMoreEnable(boolean enable) {
        this.loadMoreEnable = enable;
    }

    public void showOverLoadView(boolean showOverloadView) {
        mShowOverloadView = showOverloadView;
    }
}
