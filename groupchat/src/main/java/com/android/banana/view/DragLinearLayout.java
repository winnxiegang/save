package com.android.banana.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;


/**
 * xml使用
 * <p>
 * <DragLinearLayout
 * <p>
 * <LinearLayout
 * id="@+id/contentLayout">
 * .......
 * </LinearLayout>
 * <p>
 * <LinearLayoutid="@+id/menuLayout"
 * ......
 * </LinearLayout>
 * <p>
 * DragLinearLayout/>
 * <p>
 * <p>
 * adpter中维护 列表的打开 关闭
 */

/**
 * Created by mrs
 */
public class DragLinearLayout extends LinearLayout {
    private View mContentLayout;
    private View mMenuLayout;

    private ViewDragHelper mViewDragHelper;

    private int mMenuLayoutWidth;
    private int mContentLayoutWidth;

    private ViewDragListener mViewDragListener;
    private boolean dragEnable = true;
    @status
    private int openStatus;
    private float lastLeft;

    public @interface status {
        int OPEN = 1;
        int CLOSE = -1;
        int FLING = 0;
    }

    public DragLinearLayout(Context context) {
        super(context);
        init();
    }

    public DragLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onFinishInflate() {
        mContentLayout = getChildAt(0);
        mMenuLayout = getChildAt(1);
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContentLayoutWidth = mContentLayout.getMeasuredWidth();
        int contentMeasuredHeight = mContentLayout.getMeasuredHeight();
        int menuHeightMeasureSpec = MeasureSpec.makeMeasureSpec(contentMeasuredHeight, MeasureSpec.EXACTLY);
        measureChild(mMenuLayout, widthMeasureSpec, menuHeightMeasureSpec);
        mMenuLayoutWidth = mMenuLayout.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (openStatus == status.OPEN)
            openImmi();
        else
            closeImmi();
    }

    /**
     * 设置监听
     */
    public void setOnViewDragListener(ViewDragListener viewDragListener) {
        this.mViewDragListener = viewDragListener;
    }


    public interface ViewDragListener {
        void onOpen();

        void onClose();

        void onDrag(float percent);
    }


    /**
     * 滑动时松手后以一定速率继续自动滑动下去并逐渐停止，
     * 类似于扔东西或者松手后自动滑动到指定位置
     */
    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 初始化
     */
    private void init() {
        //创建ViewDragHelper的实例，第一个参数是ViewGroup，传自己，
        // 第二个参数就是滑动灵敏度的意思,可以随意设置，第三个是回调
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    class DragHelperCallback extends ViewDragHelper.Callback {

        /**
         * 根据返回结果决定当前child是否可以拖拽
         *
         * @param child     当前被拖拽的view
         * @param pointerId 区分多点触摸的id
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return dragEnable;//mContentLayout == child;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            lastLeft = mContentLayout.getLeft();
            super.onViewCaptured(capturedChild, activePointerId);
        }

        /**
         * 返回拖拽的范围，不对拖拽进行真正的限制，仅仅决定了动画执行速度
         *
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return mContentLayoutWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mContentLayout) {
                if (left >= 0)
                    return 0;
                if (left < -mMenuLayoutWidth)
                    return -mMenuLayoutWidth;

            } else if (child == mMenuLayout) {
                if (left >= mContentLayoutWidth)
                    return mContentLayoutWidth;
                else if (left < mContentLayoutWidth - mMenuLayoutWidth)
                    return mContentLayoutWidth - mMenuLayoutWidth;
            }

            return left;
        }


        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            if (null != mViewDragListener) {
                float percent = Math.abs((float) left / (float) mContentLayoutWidth);
                mViewDragListener.onDrag(percent);
            }

            if (changedView == mContentLayout) {
                mMenuLayout.offsetLeftAndRight(dx);
            } else {
                mContentLayout.offsetLeftAndRight(dx);
            }

            judgeOpenStatus();
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            boolean isScroll = Math.abs(mContentLayout.getLeft() - lastLeft) > ViewConfiguration.get(getContext()).getScaledTouchSlop();
            if (isScroll && xvel == 0 && mContentLayout.getLeft() < -mMenuLayoutWidth * 0.5f) {
                open();
            } else if (xvel < 0) {
                open();
            } else {
                close();
            }
        }
    }

    public void openImmi() {
        mMenuLayout.layout(mContentLayoutWidth - mMenuLayoutWidth, 0, mContentLayoutWidth,
                mMenuLayout.getMeasuredHeight());
        mContentLayout.layout(-mMenuLayoutWidth, 0, mContentLayoutWidth - mMenuLayoutWidth,
                mContentLayout.getMeasuredHeight());

        if (null != mViewDragListener)
            mViewDragListener.onOpen();

    }


    /**
     * 打开
     */
    public void open() {
        if (mViewDragHelper.smoothSlideViewTo(mContentLayout, -mMenuLayoutWidth, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (null != mViewDragListener)
            mViewDragListener.onOpen();

    }

    public void closeImmi() {
        mMenuLayout.layout(mContentLayoutWidth, 0, mContentLayoutWidth + mMenuLayoutWidth,
                mMenuLayout.getMeasuredHeight());
        mContentLayout.layout(0, 0, mContentLayoutWidth, mContentLayout.getMeasuredHeight());

        if (null != mViewDragListener)
            mViewDragListener.onClose();

    }

    public boolean isOpenOrFling() {
        return openStatus != status.CLOSE;
    }

    /**
     * 关闭
     */
    public void close() {
        if (mViewDragHelper.smoothSlideViewTo(mContentLayout, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (null != mViewDragListener)
            mViewDragListener.onClose();

    }


    public boolean judgeOpenStatus() {
        if (mContentLayout.getLeft() <= -mMenuLayoutWidth) {
            openStatus = status.OPEN;
        } else if (mContentLayout.getLeft() > -mMenuLayoutWidth && mContentLayout.getLeft() < 0) {
            openStatus = status.FLING;
        } else if (mContentLayout.getLeft() >= 0) {
            openStatus = status.CLOSE;
        }
        return openStatus == status.OPEN;
    }

    public void setDragEnable(boolean dragEnable) {
        this.dragEnable = dragEnable;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!dragEnable)
            return super.onInterceptTouchEvent(ev);

        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (!dragEnable)
            return super.onTouchEvent(e);

        mViewDragHelper.processTouchEvent(e);
        return true;
    }

}
