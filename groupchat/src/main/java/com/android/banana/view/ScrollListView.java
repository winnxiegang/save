package com.android.banana.view;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by zhouyi on 2016/1/17 10:55.
 * 支持侧滑删除
 */
public class ScrollListView extends ListView implements AbsListView.OnScrollListener {

    private OnMyScrollChangeListener listener;

    private GestureDetectorCompat mGestureDetectorCompat;

    private View mTrackedChild;

    private int mTrackedChildPrevPosition;

    private int mTrackedChildPrevTop;

    public int currentClickPosition = 0;

    private boolean enableSwipDimiss = false;//是否支持配合DragLinearLayout实现侧滑删除

    private boolean isScrolling;

    public ScrollListView(Context context) {
        this(context, null);
    }

    public ScrollListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //禁止多点触控
        setOnScrollListener(this);
        setMotionEventSplittingEnabled(false);
        mGestureDetectorCompat = new GestureDetectorCompat(context, new XScrollDetector());
    }


    private class XScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);

        if (listener == null) {

            return;
        }

        boolean firstVisible = true;


        if (getChildAt(0) != null) {

            if (getPositionForView(getChildAt(0)) != 0) {

                firstVisible = false;
            } else {

                firstVisible = true;

            }

            listener.onScrollChange(l, firstVisible, oldl, oldt);

        }
    }


    public void setOnMyScrollChangeListener(OnMyScrollChangeListener listener) {

        this.listener = listener;

    }

    public interface OnMyScrollChangeListener {

        void onScrollChange(int l, boolean firstVisible, int oldl, int oldt);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                isScrolling = true;
                break;
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                isScrolling = false;
                break;
            case OnScrollListener.SCROLL_STATE_FLING:
                isScrolling = false;
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //当支持操作时,手指落下时，停止listview的滚动  避免侧滑跟上下滑动的冲突
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && enableSwipDimiss) {
            currentClickPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
            closeMenuIfNeeded();
            if (!isScrolling)
                dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
        }
        return super.dispatchTouchEvent(ev);
    }

    float x, y;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!enableSwipDimiss)
            return super.onInterceptTouchEvent(ev);

        final int action = ev.getAction();
        boolean intercept;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                y = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                intercept = Math.abs(ev.getY() - y) > Math.abs(ev.getX() - x);
                return intercept;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void closeMenuIfNeeded() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            LinearLayout childAt = (LinearLayout) getChildAt(i);

            DragLinearLayout dragMenu = findDragLayout(childAt);
            if (dragMenu != null && dragMenu.isOpenOrFling()) {
                dragMenu.close();
            }
        }
    }

    private DragLinearLayout findDragLayout(LinearLayout childLL) {

        DragLinearLayout dragMenu = null;

        if (childLL instanceof DragLinearLayout) {

            dragMenu = (DragLinearLayout) childLL;

            return dragMenu;
        } else {
            int count = childLL.getChildCount();

            for (int i = 0; i < count; i++) {
                View childView = childLL.getChildAt(i);
                if (childView instanceof DragLinearLayout) {
                    dragMenu = (DragLinearLayout) childView;
                    return dragMenu;
                }
            }
            return dragMenu;
        }

    }


    public void setSwipe2Dimiss(boolean enableSwipDimiss) {
        this.enableSwipDimiss = enableSwipDimiss;
    }
}
