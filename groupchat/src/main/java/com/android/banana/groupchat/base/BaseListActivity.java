package com.android.banana.groupchat.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.banana.R;
import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.pullrecycler.Conf;
import com.android.banana.pullrecycler.ilayoutmanager.ILayoutManager;
import com.android.banana.pullrecycler.ilayoutmanager.MyLinearLayoutManager;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.PullRecycler;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrs on 2017/4/6.
 */

public abstract class BaseListActivity<T> extends BaseActivity4Jczj {

    /**
     * 说明:
     * <p>
     * 这是一个通用的 可以快速搭建 列表界面的 BaseListActivity
     * 同时，支持刷新，加载更多，添加多headerview{@link BaseListActivity#addHeaderView}}，footerview{@link BaseListActivity#addFooterView(View)}}
     * <p>
     * <p>
     * * 使用实例
     * public class TestActivity extends BaseListActivity<JavaBean> {
     * private ArrayList<JavaBean> list = new ArrayList<>();
     *
     * @Override protected void onActivityInit() {
     * addHeaderView(LayoutInflater.from(this).inflate(R.layout.base_widget_load_more, mRecycleList, false));
     * addHeaderView(LayoutInflater.from(this).inflate(R.layout.dialog_more_select, mRecycleList, false));
     * }
     * @Override public int getItemLayoutRes() {
     * return android.R.layout.simple_list_item_1;
     * }
     * @Override public void onBindHolder(ViewHolder holder, JavaBean item, int position) {
     * //链式调用
     * holder.setText(android.R.id.text1, JavaBean.desc)
     * .setImageUrl(android.R.id.imageview, JavaBean.picUrl);
     * .setImageUrl(android.R.id.imageview1, JavaBean.picUrl1);
     * .setImageUrl(android.R.id.imageview2, JavaBean.picUrl2);
     * }
     * @Override public void onRefresh() {
     * mCurPage = 1;
     * loadCompleted(getDatas(true));
     * }
     * @Override public void onLoadMore() {
     * loadCompleted(getDatas(false));
     * }
     * <p>
     * <p>
     * 1.实现列表单布局  请复写
     * @see BaseListActivity#getItemLayoutRes()
     * <p>
     * 2.实现多条目布局，同样支持,请复写
     * @see BaseListActivity#getSupportMultiType()
     * <p>
     * 3.默认一页加载数量是20
     * @see Conf#DEFAULT_LIST_ITEM
     * <p>
     * 4.在刷新或者加载更多回调之后调用
     * @see BaseListActivity#loadCompleted(ArrayList)
     * <p>
     */

    protected ArrayList<T> mDatas = new ArrayList<>();
    public RecyclerView.Adapter mAdapter;

    protected int mCurPage = 1;//默认初次加载页码为1
    public PullRecycler mPullRecycler;
    public PaginatorBean mPaginator;
    private boolean mEnableLoadMore = true;
    private boolean mRefreshEnable = true;
    private ILayoutManager mILayoutManager;

    @Override
    protected void setUpContentView() {
        setContentView(getContentViewLayoutRes(), "", -1, MODE_BACK);
        mPullRecycler = (PullRecycler) findViewById(R.id.PullRecycler);

    }

    public int getContentViewLayoutRes() {
        return R.layout.base_widget_list;
    }

    @Override
    protected void setUpView() {
        mPullRecycler.setLayoutManger(getLayoutManager());

        if (getItemDecoration() != null)
            mPullRecycler.addItemDecoration(getItemDecoration());

        mAdapter = new ListAdapter(this, mDatas, getItemLayoutRes(), getSupportMultiType());

        mPullRecycler.setAdapter(mAdapter);

        mPullRecycler.setOnRefreshListener(new onRefreshListener() {
            @Override
            public void onRefresh(boolean refresh) {
                if (refresh) {
                    mCurPage = 1;
                    mPullRecycler.setEnableLoadMore(mEnableLoadMore);
                }

                BaseListActivity.this.onRefresh(refresh);
            }
        });

        mPullRecycler.setEnableLoadMore(true);

        mPullRecycler.setEnableRefresh(true);

        mPullRecycler.setRefresh();
    }

    @Override
    protected void setUpData() {

    }

    protected ILayoutManager getLayoutManager() {
        if (mILayoutManager == null)
            mILayoutManager = new MyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        return mILayoutManager;
    }


    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }


    public void loadCompleted(ArrayList<T> list) {
        mPullRecycler.showContentView();
        //刷新
        if (mCurPage == 1) {

            //空数据
            if ((list == null || list.size() == 0) && mDatas.size() == 0) {
                //设置空白页面,如果刷新之前有数据还是显示旧的数据
                mPullRecycler.showEmptyView();
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
        mPullRecycler.refreshCompleted();
        mCurPage++;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        mRefreshEnable = refreshEnable;
        mPullRecycler.setEnableRefresh(refreshEnable);
    }

    public void setLoadMoreEnable(boolean enableLoadMore) {
        mEnableLoadMore = enableLoadMore;
    }

    private void judeUI(List list) {
        //如果是第二页 而且返回的数据条目少于默认数量，则认为没有更多数据了，禁掉加载更多

        if (list == null || mPaginator != null && mPaginator.getPage() >= mPaginator.getPages()) {

            mPullRecycler.setEnableLoadMore(false);

            if (mDatas.size() > 0)//&& mPullRecycler.canScrollVertical()

                mPullRecycler.showOverLoadView();

            else if (mDatas.size() <= 0) {

                mPullRecycler.showEmptyView();
                mPullRecycler.hideOverLoadView();

            }
        } else if (mDatas.size() <= 0) {

            mPullRecycler.showEmptyView();
            mPullRecycler.hideOverLoadView();

        }
    }

    private class ListAdapter extends MultiTypeSupportAdapter<T> {
        public ListAdapter(Context context, ArrayList<T> list, int layoutRes, MultiTypeSupport typeSupport) {
            super(context, list, layoutRes, typeSupport);
        }

        @Override
        public void onBindNormalHolder(ViewHolder holder, T t, final int position) {
            BaseListActivity.this.onBindHolder(holder, t, position);
        }

        @Override
        public void onItemClick(View view, int position) {
            BaseListActivity.this.onItemClick(view, position);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            BaseListActivity.this.onItemLongClick(view, position);
        }

    }

    //如果 是单一条目 那么复写这个方法 设置条目  layout
    public int getItemLayoutRes() {
        return 0;
    }

    //如果是多条目，那么复写这个方法，根据条目类型，给出条目布局
    public MultiTypeSupport getSupportMultiType() {
        /**
         * @see MultiTypeSupport
         */
        return null;
    }

    public void addHeaderView(View view) {
        // 先设置Adapter然后才能添加，这里是仿照ListView的处理方式
        if (mPullRecycler != null) {
            mPullRecycler.addHeaderView(view);
        }
    }

    public void addFooterView(View view) {
        if (mPullRecycler != null) {
            mPullRecycler.addFooterView(view);
        }
    }

    public void removeHeaderView(View view) {
        if (mPullRecycler != null) {
            mPullRecycler.removeHeaderView(view);
        }
    }

    public void removeFooterView(View view) {
        if (mPullRecycler != null) {
            mPullRecycler.removeFooterView(view);
        }
    }

    public void onItemClick(View view, int pos) {
    }

    public void onItemLongClick(View view, int pos) {
    }

    public abstract void onBindHolder(ViewHolder holder, T t, int position);

    public abstract void onRefresh(boolean refresh);
}
