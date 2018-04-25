package com.android.xjq.view.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;

/**
 * Created by lingjiu on 2017/8/8.
 */

public abstract class RecyclerViewScrollListener extends OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        if (visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && newState == recyclerView.SCROLL_STATE_IDLE) {
            isVisBottom(true);
        } else {
            isVisBottom(false);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int scollYDistance = getScollYDistance(recyclerView);
        super.onScrolled(recyclerView, dx, dy);
    }

    public abstract void isVisBottom(boolean isVisBottom);


    /**
     * @return 返回recycler Yd轴方向移动的距离
     */
    public int getScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }
}
