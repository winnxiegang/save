package com.android.xjq.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.pullrecycler.ilayoutmanager.ILayoutManager;
import com.android.banana.pullrecycler.ilayoutmanager.MyLinearLayoutManager;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.PullRecycler;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;
import com.android.xjq.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2017/7/4.
 */
public abstract class BaseListFragment<T> extends BaseFragment implements onRefreshListener {

    protected List<T> mDatas = new ArrayList<>();
    protected int mCurPage = DEFAULT_PAGE;//默认初次加载页码为1
    protected ListAdapter mAdapter;
    protected PullRecycler mRecycler;
    protected ProgressBar mProgressBar;
    protected ImageView mLatestImg;
    public PaginatorBean mPaginator;
    private boolean mEnableLoadMore = false;
    private boolean mRefreshEnable = true;
    private ILayoutManager mILayoutManager;
    private View view;

    @Override
    protected View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_base_list, null);
            mRecycler = (PullRecycler) view.findViewById(R.id.pullRecycler);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mLatestImg = (ImageView) view.findViewById(R.id.latest_img);
            setUpRecyclerView();
            onSetUpView();
        }
        return view;
    }

    protected void initView(LayoutInflater inflater, ViewGroup parentView) {
        if (view == null) {
            View tempView = inflater.inflate(R.layout.fragment_base_list, parentView);
            mRecycler = (PullRecycler) tempView.findViewById(R.id.pullRecycler);
            mProgressBar = (ProgressBar) tempView.findViewById(R.id.progressBar);
            mLatestImg = (ImageView) tempView.findViewById(R.id.latest_img);
            setUpRecyclerView();
            onSetUpView();
            view = parentView;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    private void setUpRecyclerView() {
        mRecycler.setLayoutManger(getLayoutManager());
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null)
            mRecycler.addItemDecoration(itemDecoration);

        mAdapter = new ListAdapter(getContext(), mDatas, getItemLayoutRes(), getSupportMultiType());
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setOnRefreshListener(new onRefreshListener() {
            @Override
            public void onRefresh(boolean refresh) {
                if (refresh) {
                    mCurPage = 1;
                    mRecycler.setEnableLoadMore(mEnableLoadMore);
                }
                BaseListFragment.this.onRefresh(refresh);
            }
        });
        mRecycler.setRefresh();

    }

    protected ILayoutManager getLayoutManager() {
        if (mILayoutManager == null)
            mILayoutManager = new MyLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        return mILayoutManager;
    }


    protected abstract void onSetUpView();

    //想添加别的分割线 复写该方法
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        if (mRecycler == null) return;
        mRefreshEnable = refreshEnable;
        mRecycler.setEnableRefresh(refreshEnable);
    }

    public void setLoadMoreEnable(boolean enableLoadMore) {
        mEnableLoadMore = enableLoadMore;
    }

    public void loadCompleted(ArrayList<T> list) {
        if (mRecycler == null) return;
        mRecycler.showContentView();
        //刷新
        if (mCurPage == 1) {

            //空数据
            if ((list == null || list.size() == 0) && mDatas.size() == 0) {
                //设置空白页面,如果刷新之前有数据还是显示旧的数据
                mRecycler.showEmptyView();
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
        mRecycler.refreshCompleted();
        mCurPage++;
    }

    private void judeUI(List list) {
        //如果是第二页 而且返回的数据条目少于默认数量，则认为没有更多数据了，禁掉加载更多

        if (list == null || mPaginator != null && mPaginator.getPage() >= mPaginator.getPages()) {

            mRecycler.setEnableLoadMore(false);

            if (mDatas.size() > 0)//&& mPullRecycler.canScrollVertical()

                mRecycler.showOverLoadView();

            else if (mDatas.size() <= 0) {

                mRecycler.showEmptyView();
                mRecycler.hideOverLoadView();

            }
        }
    }

    public class ListAdapter extends MultiTypeSupportAdapter<T> {
        public ListAdapter(Context context, List<T> list, int layoutRes, MultiTypeSupport typeSupport) {
            super(context, list, layoutRes, typeSupport);
        }

        @Override
        public void onBindNormalHolder(ViewHolder holder, T item, int position) {
            onBindHolder(holder, item, position);
        }

        @Override
        public void onItemClick(View view, int position) {
            BaseListFragment.this.onItemClick(view, position);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            BaseListFragment.this.onItemLongClick(view, position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
    }

    public void onItemLongClick(View view, int position) {

    }

    public void onItemClick(View view, int position) {

    }

    public void resetPos() {
        mCurPage = 1;
    }

    public abstract void onRefresh(boolean refresh);

    //重载 可以自定义列表界面
    public int getContentViewLayoutRes() {
        return R.layout.base_widget_list;
    }

    //如果 是单一条目 那么复写这个方法 设置条目  layout
    public int getItemLayoutRes() {
        return 0;
    }

    //如果是多条目，那么复写这个方法，根据条目类型，给出条目布局
    public MultiTypeSupport getSupportMultiType() {
        /**
         * @see MultiTypeSupport
         *
         */
        return null;
    }

    public abstract void onBindHolder(ViewHolder holder, T item, int position);

}
