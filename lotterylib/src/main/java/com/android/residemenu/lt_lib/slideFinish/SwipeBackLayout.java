package com.android.residemenu.lt_lib.slideFinish;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.android.residemenu.lt_lib.R;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaanming
 * @blog http://blog.csdn.net/xiaanming
 */
public class SwipeBackLayout extends FrameLayout {
    private static final String TAG = SwipeBackLayout.class.getSimpleName();
    private final int mEdgeSlop;
    //SildingFinishLayout布局
    private View mContentView;
    private int mTouchSlop;
    private int downX;
    private int downY;
    private int tempX;
    private Scroller mScroller;
    private int viewWidth;
    private boolean isSilding;
    //是否结束Activity
    private boolean isFinish;
    private Drawable mShadowDrawable;
    private Activity mActivity;
    private List<ViewPager> mViewPagers = new LinkedList<ViewPager>();
    private List<TabLayout> mTabLayouts = new LinkedList<>();
    /**
     * 是否支持阴影效果
     */
    private boolean isSupportShadow = true;

    /**
     * 设置是否只有侧边划关闭
     */
    private boolean isOnlyEdgeSlideFinish = false;


    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mEdgeSlop = ViewConfiguration.get(context).getScaledEdgeSlop();
        //滚动器
        mScroller = new Scroller(context);
        if (isSupportShadow) {

            mShadowDrawable = getResources().getDrawable(R.drawable.shadow_left);
        }
    }

    /**
     * 将对象Activity最外层另外套上一个SwipeBackLayout
     *
     * @param activity
     */
    public void attachToActivity(Activity activity) {
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.windowBackground});
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    private void setContentView(View decorChild) {
        mContentView = (View) decorChild.getParent();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //对Viewpager做额外的处理
        ViewPager mViewPager = getTouchViewPager(mViewPagers, ev);
        TabLayout tabLayout = getTouchTabLayout(mTabLayouts, ev);
        if (mViewPager != null && mViewPager.getCurrentItem() != 0) {
            return super.onInterceptTouchEvent(ev);
        }
        if (tabLayout != null) {
            return super.onInterceptTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                // 当监听到用有水平方向的移动的趋势并且不在水平方向上有移动的趋势时,拦截触摸事件
                if (moveX - downX > mTouchSlop
                        && Math.abs((int) ev.getRawY() - downY) < mTouchSlop && setOnlyEdgeSlideFinish()) {
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    private TabLayout getTouchTabLayout(List<TabLayout> tabLayouts, MotionEvent ev) {
        if (tabLayouts == null || tabLayouts.size() == 0) {
            return null;
        }
        float x = ev.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
        float y = ev.getRawY(); // 获取相对于屏幕左上角的 y 坐标值
        for (TabLayout v : tabLayouts) {
            RectF rect = calcViewScreenLocation(v);
            boolean isInViewRect = rect.contains(x, y);
            if(isInViewRect){
                return v;
            }
        }
        return null;
    }

    /**
     * 控制边缘侧滑关闭
     *
     * @return
     */
    private boolean setOnlyEdgeSlideFinish() {

        return isOnlyEdgeSlideFinish ? downX < mEdgeSlop * 5 : true;
    }

    public void setOnlyEdgeSlideFinish(boolean onlyEdgeSlideFinish) {
        isOnlyEdgeSlideFinish = onlyEdgeSlideFinish;
    }

    /**
     * pxToDp
     *
     * @param
     * @return
     */
    private int pxToDp(int px) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, mActivity.getApplicationContext().getResources().getDisplayMetrics()));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getRawX();
                int deltaX = tempX - moveX;
                tempX = moveX;
                if (moveX - downX > mTouchSlop
                        && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
                    isSilding = true;
                }

                if (moveX - downX >= 0 && isSilding) {
                    mContentView.scrollBy(deltaX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                isSilding = false;
                //滚动超过屏幕的一半,将view完全划出
                if (mContentView.getScrollX() <= -viewWidth / 2) {
                    isFinish = true;
                    scrollRight();
                } else {
                    scrollOrigin();
                    isFinish = false;
                }
                break;
        }

        return true;
    }

    /**
     * 在所有的子控件中找ViewPager
     *
     * @param mViewPagers
     * @param parent
     */
    private void getAlLViewPager(List<ViewPager> mViewPagers, ViewGroup parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ViewPager) {
                mViewPagers.add((ViewPager) child);
            } else if (child instanceof ViewGroup) {
                getAlLViewPager(mViewPagers, (ViewGroup) child);
            }
        }
    }


    /**
     * @param mViewPagers
     * @param ev
     * @return
     */
    private ViewPager getTouchViewPager(List<ViewPager> mViewPagers, MotionEvent ev) {
        if (mViewPagers == null || mViewPagers.size() == 0) {
            return null;
        }
        float x = ev.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
        float y = ev.getRawY(); // 获取相对于屏幕左上角的 y 坐标值

        for (ViewPager v : mViewPagers) {
            RectF rect = calcViewScreenLocation(v);
            boolean isInViewRect = rect.contains(x, y);
            if(isInViewRect){
                return v;
            }
        }
        return null;
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            viewWidth = this.getWidth();
            getAllTabLayout(mTabLayouts, this);
            getAlLViewPager(mViewPagers, this);
        }
    }

    private void getAllTabLayout(List<TabLayout> tabLayouts, ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof TabLayout) {
                tabLayouts.add((TabLayout) child);
            } else if (child instanceof ViewGroup) {
                getAllTabLayout(tabLayouts, (ViewGroup) child);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShadowDrawable != null && mContentView != null && !isFinish) {

            int left = mContentView.getLeft()
                    - mShadowDrawable.getIntrinsicWidth();
            int right = left + mShadowDrawable.getIntrinsicWidth();
            int top = mContentView.getTop();
            int bottom = mContentView.getBottom();

            mShadowDrawable.setBounds(left, top, right, bottom);
            mShadowDrawable.draw(canvas);
        }

    }


    /**
     * 滑动到右边至消失
     */
    private void scrollRight() {
        final int delta = (viewWidth + mContentView.getScrollX());

        mScroller.startScroll(mContentView.getScrollX(), 0, -delta + 1, 0,
                Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滑动到原来的位置
     */
    private void scrollOrigin() {
        int delta = mContentView.getScrollX();
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));
        postInvalidate();
    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished() && isFinish) {
                mActivity.finish();
            }
        }
    }
}
