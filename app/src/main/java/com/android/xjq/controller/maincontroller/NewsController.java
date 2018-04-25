package com.android.xjq.controller.maincontroller;

import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.pullrecycler.ilayoutmanager.MyLinearLayoutManager;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.banana.pullrecycler.recyclerview.PullRecycler;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.NewsInfoBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页资讯
 */

public class NewsController extends BaseController4JCZJ<BaseActivity> implements HomeInterface<NewsInfoBean>, onRefreshListener {
    private PullRecycler mRecycler;
    private NewsAdapter mNewsAdapter;
    private HomeHttpHelper mHttpHelper = new HomeHttpHelper(this);
    private List<NewsInfoBean.NewsInfo> mNewsList = new ArrayList<>();
    private int mCurPager = 1;
    private boolean needLoadMore = true;

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_home_news);
    }

    @Override
    public void onSetUpView() {
        mRecycler = findViewOfId(R.id.pullRecycler);
        mRecycler.setOnRefreshListener(this);
        mRecycler.setItemAnimator(null);
        mRecycler.setLayoutManger(new MyLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.base_divider_list_10dp, 1, false));
        mNewsAdapter = new NewsAdapter(getContext(), mNewsList, R.layout.layout_subject_item_news, null);
        mRecycler.setAdapter(mNewsAdapter);
        mRecycler.setEnableLoadMore(true);
        mRecycler.setRefresh();

    }

    @Override
    public void onRefresh(boolean refresh) {
        if (refresh) {
            mCurPager = 1;
            mRecycler.setEnableLoadMore(true);
        }
        mHttpHelper.queryNewsListInfo(mCurPager);
    }

    @Override
    public void responseSuccessHttp(NewsInfoBean newsInfoBean, RequestContainer requestContainer) {
        boolean refreshing = mRecycler.isRefreshing();
        mRecycler.setRefreshing(false);
        mRecycler.showContentView();
        if (newsInfoBean == null || newsInfoBean.infoList == null || newsInfoBean.infoList.size() == 0) {
            refreshCompleted();
            return;
        }
        mCurPager++;
        if (refreshing) {
            mNewsList.clear();
        }
        mNewsList.addAll(newsInfoBean.infoList);

        PaginatorBean paginator = newsInfoBean.paginator;
        if (paginator != null && paginator.getPage() >= paginator.getPages()) {
            mRecycler.setEnableLoadMore(false);
            mRecycler.showOverLoadView();
        }
        mRecycler.refreshCompleted();
    }

    private void refreshCompleted() {
        if (mNewsAdapter.getItemCount() == 0) {
            mRecycler.showEmptyView();
        } else {
            mRecycler.showOverLoadView();
        }
        mRecycler.refreshCompleted();
    }

    @Override
    public void responseFalseHttp(JSONObject jo, RequestContainer requestContainer) {
        refreshCompleted();
    }
}
