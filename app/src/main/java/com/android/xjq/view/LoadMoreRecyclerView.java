package com.android.xjq.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;


/**
 * Created by DaChao on 2017/12/5.
 */

public class LoadMoreRecyclerView extends RecyclerView {

    public static final int LOAD_MORE_STYLE_FOOT = 0, LOAD_MORE_STYLE_HEAD = 1;

    private boolean loadMoreEnable = true;
    private boolean noData;
    private boolean isLoadMoreShowing = false;
    private boolean isLoading = false;
    private int loadMoreStyle = LOAD_MORE_STYLE_FOOT;
    private LayoutManager layoutManager;
    private OnLoadMoreListener onLoadMoreListener;

    private OnScrollListener onScrollListener = new OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if ((dx == 0 && dy == 0) || onLoadMoreListener == null || noData || !loadMoreEnable || isLoading) {
                return;
            }
            layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                //线性布局
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (dy < 0) {
                    //纵向向下滑
                    if (loadMoreStyle == LOAD_MORE_STYLE_HEAD) {
                        int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
                        if (firstPosition <= 1 && firstPosition > -1) {
                            isLoading = true;
                            showLoadMore(true);
                            LoadMoreRecyclerView.this.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onLoadMoreListener.onLoadMore();
                                }
                            }, 500);
                        }
                    }
                } else if (dy > 0) {
                    //纵向向上滑
                    if (loadMoreStyle == LOAD_MORE_STYLE_FOOT) {
                        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                        if (lastPosition >= recyclerView.getAdapter().getItemCount() - 2) {
                            isLoading = true;
                            showLoadMore(true);
                            LoadMoreRecyclerView.this.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onLoadMoreListener.onLoadMore();
                                }
                            }, 500);
                        }
                    }
                }
            }
        }
    };

    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (loadMoreStyle == LOAD_MORE_STYLE_HEAD && isLoadMoreShowing) {
                positionStart++;
            }
            getAdapter().notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (loadMoreStyle == LOAD_MORE_STYLE_HEAD && isLoadMoreShowing) {
                positionStart++;
            }
            getAdapter().notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (loadMoreStyle == LOAD_MORE_STYLE_HEAD && isLoadMoreShowing) {
                positionStart++;
            }
            getAdapter().notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (loadMoreStyle == LOAD_MORE_STYLE_HEAD && isLoadMoreShowing) {
                positionStart++;
            }
            getAdapter().notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (loadMoreStyle == LOAD_MORE_STYLE_HEAD && isLoadMoreShowing) {
                fromPosition++;
                toPosition++;
            }
            getAdapter().notifyItemMoved(fromPosition, toPosition);
        }
    };


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public LoadMoreRecyclerView(Context context) {
        super(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * 设置加载更多的样式，默认是底部样式
     *
     * @param loadMoreStyle 可设置底部样式{@link #LOAD_MORE_STYLE_FOOT}和在顶部的样式{@link #LOAD_MORE_STYLE_HEAD}
     */
    public void setLoadMoreStyle(int loadMoreStyle) {
        this.loadMoreStyle = loadMoreStyle;
    }

    /**
     * 设置是否启用加载更多的功能
     *
     * @param enable 设置为true表示使用加载更多的特性；设置为false后该recyclerView和原生recyclerView无异
     */
    public void setLoadMoreEnable(boolean enable) {
        if (enable != loadMoreEnable) {
            loadMoreEnable = enable;
            if (getAdapter() != null) {
                getAdapter().notifyDataSetChanged();
            }
        }
    }

    /**
     * 设置加载更多显示重试
     */
    public void setLoadMoreError() {
        if (loadMoreEnable && isLoadMoreShowing && getAdapter() != null) {
            ((LoadMoreAdapter) getAdapter()).setLoadMoreStatus(LoadMoreViewHolder.STATUS_ERROR);
        }
    }

    /**
     * 设置已经没有更多数据
     *
     * @param noData 设置true后加载更多布局将显示已经加载完毕的提示，不会再显示加载进度条。
     *               同时将不再执行OnLoadMore回调。
     */
    public void setNoData(boolean noData) {
        if (!this.noData && noData) {
            isLoading = false;
            this.noData = true;
            if (isLoadMoreShowing) {
                ((LoadMoreAdapter) getAdapter()).setLoadMoreStatus(LoadMoreViewHolder.STATUS_NO_MORE);
            }
        } else if (this.noData && !noData) {
            this.noData = false;
            if (isLoadMoreShowing) {
                ((LoadMoreAdapter) getAdapter()).setLoadMoreStatus(LoadMoreViewHolder.STATUS_LOADING);
            }
        }
    }


    /**
     * 此次加载更多完成后调用，不需调用notify方法
     *
     * @param insertedItemCount 加载后新增加的数据条数
     */
    public void setLoadMoreFinished(int insertedItemCount) {
        if (isLoading) {
            isLoading = false;
            if (insertedItemCount > 0) {
                if (loadMoreStyle == LOAD_MORE_STYLE_HEAD) {
                    getAdapter().notifyItemRangeInserted(0, insertedItemCount);
                } else if (loadMoreStyle == LOAD_MORE_STYLE_FOOT) {
                    getAdapter().notifyItemRangeInserted(getAdapter().getItemCount() - 1, insertedItemCount);
                }
            }
        }
    }


    /**
     * 设置底部加载更多的监听事件
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        onLoadMoreListener = listener;
    }


    /**
     * 获取是否正在显示加载更多布局。
     *
     * @return 如果adapter中包含loadMore item(尽管当前没有滚动到loadMore item导致其不可见)，返回true。
     * 否则返回false。
     */
    public boolean isLoadMoreShowing() {
        return isLoadMoreShowing;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(updateAdapter(adapter));
    }

    /**
     * 修改adapter，增加底部加载更多
     */
    private LoadMoreAdapter updateAdapter(Adapter adapter) {
        adapter.registerAdapterDataObserver(observer);
        return new LoadMoreAdapter(adapter);
    }

    /**
     * 设置是否显示加载更多布局
     */
    private void showLoadMore(boolean show) {
        if (show && !isLoadMoreShowing) {
            isLoadMoreShowing = true;
            if (loadMoreStyle == LOAD_MORE_STYLE_HEAD) {
                getAdapter().notifyItemInserted(0);
            } else if (loadMoreStyle == LOAD_MORE_STYLE_FOOT) {
                getAdapter().notifyItemInserted(getAdapter().getItemCount());
            }
        } else if (!show && isLoadMoreShowing) {
            isLoadMoreShowing = false;
            if (loadMoreStyle == LOAD_MORE_STYLE_HEAD) {
                getAdapter().notifyItemRemoved(0);
            } else if (loadMoreStyle == LOAD_MORE_STYLE_FOOT) {
                getAdapter().notifyItemRemoved(getAdapter().getItemCount());
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    private class LoadMoreAdapter extends Adapter<ViewHolder> {

        private final int TYPE_LOAD_MORE = 10001;

        private Adapter originalAdapter;
        private LoadMoreViewHolder loadMoreViewHolder;

        LoadMoreAdapter(Adapter adapter) {
            originalAdapter = adapter;
        }

        @Override
        public int getItemViewType(int position) {
            if (loadMoreEnable && isLoadMoreShowing) {
                if (loadMoreStyle == LOAD_MORE_STYLE_FOOT) {
                    if (position == getItemCount() - 1) {
                        return TYPE_LOAD_MORE;
                    } else {
                        return originalAdapter.getItemViewType(position);
                    }
                } else {
                    if (position == 0) {
                        return TYPE_LOAD_MORE;
                    } else {
                        return originalAdapter.getItemViewType(position - 1);
                    }
                }
            } else {
                return originalAdapter.getItemViewType(position);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_LOAD_MORE) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(56));
                linearLayout.setLayoutParams(params);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER);
                loadMoreViewHolder = new LoadMoreViewHolder(linearLayout);
                return loadMoreViewHolder;
            }
            return originalAdapter.onCreateViewHolder(parent, viewType);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (getItemViewType(position) != TYPE_LOAD_MORE) {
                int originalPosition = position;
                if (isLoadMoreShowing && loadMoreStyle == LOAD_MORE_STYLE_HEAD) {
                    originalPosition--;
                }
                originalAdapter.onBindViewHolder(holder, originalPosition);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            if (getItemViewType(position) != TYPE_LOAD_MORE) {
                int originalPosition = position;
                if (isLoadMoreShowing && loadMoreStyle == LOAD_MORE_STYLE_HEAD) {
                    originalPosition--;
                }
                originalAdapter.onBindViewHolder(holder, originalPosition, payloads);
            }
        }

        @Override
        public int getItemCount() {
            if (loadMoreEnable && isLoadMoreShowing) {
                return originalAdapter.getItemCount() + 1;
            }
            return originalAdapter.getItemCount();
        }


        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            recyclerView.addOnScrollListener(onScrollListener);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            recyclerView.removeOnScrollListener(onScrollListener);
        }

        /**
         * 设置显示加载更多失败
         */
        void setLoadMoreStatus(int status) {
            loadMoreViewHolder.setStatus(status);
        }

    }

    private class LoadMoreViewHolder extends ViewHolder {

        static final int STATUS_LOADING = 0, STATUS_NO_MORE = 1, STATUS_ERROR = 2;
        private static final String STR_NO_MORE = "没有更多了", STR_CLICK_TO_RETRY = "加载失败，点击重试";
        private int status = -1;
        TextView textView;
        ProgressBar progressBar;

        LoadMoreViewHolder(View itemView) {
            super(itemView);
            textView = new TextView(getContext());
            ((ViewGroup) itemView).addView(textView);
            textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            textView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            textView.setText(STR_NO_MORE);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStatus(STATUS_LOADING);
                    onLoadMoreListener.onLoadMore();
                }
            });
            textView.setClickable(false);

            progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
            ((ViewGroup) itemView).addView(progressBar);
            progressBar.getLayoutParams().width = dp2px(30);
            progressBar.getLayoutParams().height = dp2px(30);

            setStatus(STATUS_LOADING);
        }

        /**
         * 设定显示状态
         */
        void setStatus(int status) {
            if (this.status == status) {
                return;
            }
            this.status = status;
            switch (status) {
                case STATUS_LOADING:
                    textView.setVisibility(GONE);
                    textView.setClickable(false);
                    progressBar.setVisibility(VISIBLE);
                    break;
                case STATUS_NO_MORE:
                    textView.setText(STR_NO_MORE);
                    textView.setVisibility(VISIBLE);
                    textView.setClickable(false);
                    progressBar.setVisibility(GONE);
                    break;
                case STATUS_ERROR:
                    textView.setText(STR_CLICK_TO_RETRY);
                    textView.setVisibility(VISIBLE);
                    textView.setClickable(true);
                    progressBar.setVisibility(GONE);
                    break;
            }
        }

    }

}
