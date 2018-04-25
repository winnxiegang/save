package com.android.xjq.adapter.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.xjq.XjqApplication;
import com.android.xjq.R;
import com.android.xjq.model.RefreshLayoutDirection;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.loadmore.OnLoadMoreListener;

import lumenghz.com.pullrefresh.PullToRefreshView;

import static com.android.xjq.model.RefreshLayoutDirection.BOTH;

/**
 * Created by lingjiu on 2017/3/30.
 */

public class RefreshHelper implements OnLoadMoreListener, PullToRefreshView.OnRefreshListener {

    private RecyclerBaseAdapter baseRecyclerAdapter;

    private RecyclerView recyclerView;

    private PullToRefreshView refreshLayout;

    private OnRefreshListener onRefreshListener;

    private RefreshLayoutDirection direction;

    public RefreshHelper(View view, RefreshLayoutDirection direction, final OnRefreshListener listener) {

        if (view.findViewById(R.id.refreshLayout) == null) {

            throw new IllegalArgumentException("activity没有包含refreshLayout的ID");
        }

        refreshLayout = ((PullToRefreshView) view.findViewById(R.id.refreshLayout));

        recyclerView = ((RecyclerView) view.findViewById(R.id.recyclerView));

        baseRecyclerAdapter = ((RecyclerBaseAdapter) recyclerView.getAdapter());

        if (baseRecyclerAdapter == null) {

            throw new IllegalStateException("必须先设置adapter");
        }

        if (direction != null) {
            this.direction = direction;
        } else {
            this.direction = BOTH;
        }

        if (listener != null) {
            onRefreshListener = listener;
        }

        baseRecyclerAdapter.setOnLoadMoreListener(this);

        refreshLayout.setOnRefreshListener(this);

        setRefresh();
    }

    private void setRefresh() {
        switch (direction) {
            case BOTH:
                refreshLayout.setEnabled(true);

                baseRecyclerAdapter.setLoadMoreEnable(true);
                break;
            case BOTTOM:
                refreshLayout.setEnabled(false);

                baseRecyclerAdapter.setLoadMoreEnable(true);
                break;
            case TOP:
                refreshLayout.setEnabled(true);

                baseRecyclerAdapter.setLoadMoreEnable(false);
                break;

        }

    }

    public void closeRefresh(String requestType) {
        if (requestType == AppParam.REFRESH_DATA) {

            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMore() {
        baseRecyclerAdapter.showLoadMoreLoading();
       /* if (checkNetworkState()) {

            baseRecyclerAdapter.showLoadMoreLoading();
        } else {

            baseRecyclerAdapter.showLoadMoreRetry();
        }
*/
        if (onRefreshListener != null) {

            onRefreshListener.onLoadMore();
        }
    }

    private boolean checkNetworkState() {

        boolean flag = false;
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) XjqApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }

        return flag;
    }

    @Override
    public void onRefresh() {
        if (onRefreshListener != null) {

            onRefreshListener.onRefresh();
        }
    }


    public static interface OnRefreshListener {

        public void onRefresh();

        public void onLoadMore();
    }

}
