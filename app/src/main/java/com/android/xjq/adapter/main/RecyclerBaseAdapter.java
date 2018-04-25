package com.android.xjq.adapter.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.banana.commlib.loadmore.CommonLoadMoreView;
import com.android.banana.commlib.loadmore.OnLoadMoreListener;
import com.android.banana.commlib.loadmore.OnRetryClickListener;

import java.util.List;

/**
 * Created by lingjiu on 2017/3/31.
 */

public abstract class RecyclerBaseAdapter<T> extends RecyclerView.Adapter {

    protected static final int ITEM_VIEW_TYPE_HEADER = 0;

    protected static final int ITEM_VIEW_TYPE_ITEM = 1;

    protected static final int ITEM_VIEW_TYPE_FOOTER = 2;

    protected static final int ITEM_VIEW_TYPE_EMPTY = 3;

    protected Context context;

    private boolean mLoadMoreEnable = true;

    protected List<T> list;

    private View header;

    private CommonLoadMoreView footer;

    private View mEmpty;

    private OnLoadMoreListener mOnLoadMoreListener;

    public RecyclerBaseAdapter(Context context, List<T> list, View header) {
        this.list = list;
        this.header = header;
        this.context = context;
        footer = new CommonLoadMoreView(context);
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {

        this.mLoadMoreEnable = loadMoreEnable;
        if (mLoadMoreEnable) {
            if (footer == null) {
                footer = new CommonLoadMoreView(context);
            }
        } else {
            footer = null;
        }
    }

    //返回View的类别
    @Override
    public int getItemViewType(int position) {
        if (header != null && isHeader(position)) {
            return ITEM_VIEW_TYPE_HEADER;
        }
        if (mEmpty != null && isEmpty(position)) {
            return ITEM_VIEW_TYPE_EMPTY;
        }
        if (footer != null && isFooter(position)) {
            return ITEM_VIEW_TYPE_FOOTER;
        }
        return ITEM_VIEW_TYPE_ITEM;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_VIEW_TYPE_HEADER:
                return new BaseViewHolder(header);
            case ITEM_VIEW_TYPE_FOOTER:
                return new BaseViewHolder(footer);
            case ITEM_VIEW_TYPE_EMPTY:
                return new BaseViewHolder(mEmpty);
            default:
//                return onCreateViewHolder(parent,viewType);
                return getViewHolder(parent.getContext());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //如果有header有footer,则返回,把设置数据留给外面
        if (isHeader(position)) {
            return;
        }
        if (isEmpty(position)) {
            return;
        }

        if (isFooter(position)) {
            if (mLoadMoreEnable && mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onLoadMore();
            }
            return;
        }

        //如果有header数据要-1
        setData(holder, position);
    }

    protected abstract BaseViewHolder getViewHolder(Context context);


    protected abstract void setData(RecyclerView.ViewHolder holder, int position);


    @Override
    public int getItemCount() {
        if (list == null || list.isEmpty()) {
            int headerCount = header == null ? 0 : 1;
            int emptyCount = isEmptyEnable() ? 1 : 0;
            return headerCount + emptyCount;
        } else {
            int headerCount = header == null ? 0 : 1;
            int loadMoreCount = footer == null ? 0 : 1;
            return headerCount + list.size() + loadMoreCount;
        }

    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(View view) {
            super(view);
        }

    }

    private boolean isEmptyEnable() {
        return mEmpty != null;
    }

    public void setEmptyView(View mEmpty) {
        this.mEmpty = mEmpty;
    }

    public boolean isHeader(int position) {
        if (header == null) {
            return false;
        }
        return position == 0;
    }

    public boolean isEmpty(int position) {
        if (mEmpty == null) {
            return false;
        } else if (list != null && list.size() > 0) {
            return false;
        }
        return position == (header == null ? 0 : 1);
    }

    public boolean isFooter(int position) {
        if (footer == null) {
            return false;
        }
        //return position == (list.size() + 1);
        return position == (list.size());
    }

    /**
     * 设置加载更多时触发的操作
     */
    public void setOnLoadMoreListener(final OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
        footer.setOnRetryClickListener(new OnRetryClickListener() {
            @Override
            public void OnRetryClick() {
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore();
                }
            }
        });
    }

    public void showLoadMoreLoading() {
        footer.showLoading();
    }

    public void showLoadMoreRetry() {
        footer.showRetry();
    }

    public void showLoadMoreEnd() {
        footer.showEnd();
    }

}
