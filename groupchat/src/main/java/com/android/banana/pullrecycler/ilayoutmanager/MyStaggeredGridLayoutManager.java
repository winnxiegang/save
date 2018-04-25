package com.android.banana.pullrecycler.ilayoutmanager;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qiaomnu on 17/6/9.
 */
public class MyStaggeredGridLayoutManager extends StaggeredGridLayoutManager implements ILayoutManager {

    private boolean mScrollEnable=true;

    public MyStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return this;
    }

    @Override
    public int findLastVisiblePosition() {
        int[] positions = null;
        positions = findLastVisibleItemPositions(positions);
        return positions[0];
    }

    @Override
    public int findFirstVisiblePosition() {
        int[] positions = null;
        positions = findFirstVisibleItemPositions(positions);
        return positions[0];
    }

    @Override
    public void scrollPositionWithOffset(int position, int offset) {
        super.scrollToPositionWithOffset(position, offset);
    }

    @Override
    public PointF computeScrollVector4Position(int targetPosition) {
        return null;
    }

    @Override
    public boolean isStackFromEnd() {
        return false;
    }

    @Override
    public void setStackFromEndIfPossible(boolean stackFromEnd) {
    }

    @Override
    public View findViewOfPosition(int position) {
        return this.findViewByPosition(position);
    }

    @Override
    public void setScrollVerticalEnable(boolean scrollEnable) {
        mScrollEnable = scrollEnable;
    }

    @Override
    public boolean canScrollVertically() {
        return mScrollEnable ? super.canScrollVertically() : false;
    }
}
