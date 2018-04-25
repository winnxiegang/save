package com.tencent.ilivesdk.view;

import android.content.Context;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.tencent.av.opengl.gesturedetectors.MoveGestureDetector;
import com.tencent.av.opengl.ui.GLView;
import com.tencent.av.opengl.ui.GLViewGroup;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLog;

/**
 * 视频显示控件组[统一显示]
 */
public class AVVideoGroup extends GLViewGroup implements GLView.OnTouchListener, MoveGestureDetector.OnMoveGestureListener {
    private static String TAG = "AVVideoGroup";

    private AVRootView mRootView;
    private GestureDetector mGestureDetector = null;    // 点击事件
    private MoveGestureDetector mMoveDetector = null;   // 拖动事件
    private AVVideoView mCurVideoView = null;
    private boolean mInit = false;

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            ILiveLog.v(TAG, "onSingleTapConfirmed->entered id:" + mCurVideoView.getIdentifier());
            if (null != mCurVideoView && null != mCurVideoView.getGestureListener()) {
                mCurVideoView.getGestureListener().onSingleTapConfirmed(event);
            }
            return true;
        }


        @Override
        public boolean onDoubleTap(MotionEvent event) {
            ILiveLog.v(TAG, "onDoubleTap->entered id:" + mCurVideoView.getIdentifier());
            if (null != mCurVideoView && null != mCurVideoView.getGestureListener()) {
                mCurVideoView.getGestureListener().onDoubleTap(event);
            }
            return super.onDoubleTap(event);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            ILiveLog.v(TAG, "onFling->entered id:" + mCurVideoView.getIdentifier());
            if (null != mCurVideoView && null != mCurVideoView.getGestureListener()) {
                mCurVideoView.getGestureListener().onFling(e1, e2, velocityX, velocityY);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void initSubViews() {
        if (!mInit && null != mRootView) {
            ILiveLog.d(TAG, "ILVB-AVVideoGroup|init sub views");
            AVVideoView[] mViews = mRootView.initVideoGroup();
            for (AVVideoView videoView : mViews) {
                addView(videoView);
            }
            mRootView.notifySubViewCreated();
            mInit = true;
        }
    }

    public void initAvRootView(Context context, AVRootView view) {
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-AVVideoGroup|initAvRootView");
        if (mRootView != view) {
            mRootView = view;
            mRootView.setContentPane(this);

            if (0 == mRootView.getWidth()) {
                mRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        initSubViews();
                    }
                });
            } else {
                initSubViews();
            }


            mGestureDetector = new GestureDetector(context, new GestureListener());
            mMoveDetector = new MoveGestureDetector(context, this);
            setOnTouchListener(this);
        } else {
            return;
        }
    }

    public void onDestroy() {
        mRootView = null;
        if (getChildCount() > 0) {
            removeAllView();
        }
        mInit = false;
    }

    // 覆盖方法
    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        if (mRootView != null)
            mRootView.layoutVideo(false);
    }

    @Override
    public boolean onTouch(GLView glView, MotionEvent motionEvent) {
        //Log.v(TAG, "onTouch->entered:"+glView);
        if (!AVVideoView.class.isInstance(glView)) { // 忽略非AVVideoView的事件
            return true;
        }

        mCurVideoView = (AVVideoView) glView;
        if (null != mGestureDetector) {
            mGestureDetector.onTouchEvent(motionEvent);
        }
        if (null != mMoveDetector) {
            mMoveDetector.onTouchEvent(motionEvent);
        }
        return true;
    }

    @Override
    public boolean onMove(MoveGestureDetector moveGestureDetector) {
        PointF delta = moveGestureDetector.getFocusDelta();
        int deltaX = (int) delta.x;
        int deltaY = (int) delta.y;

        //mCurVideoView.setOffset(-deltaY, -deltaX, false);
        if (null != mCurVideoView && mCurVideoView.isDragable()) {
            if (mCurVideoView.getPosTop()+deltaY > 0  && mCurVideoView.getPosTop()+deltaY+mCurVideoView.getPosHeight() < mRootView.getHeight()) {
                mCurVideoView.setPosTop(mCurVideoView.getPosTop() + deltaY);
            }
            if (mCurVideoView.getPosLeft()+deltaX > 0 && mCurVideoView.getPosLeft()+deltaX+mCurVideoView.getPosWidth() < mRootView.getWidth()) {
                mCurVideoView.setPosLeft(mCurVideoView.getPosLeft() + deltaX);
            }
            mCurVideoView.autoLayout();
        }
        return true;
    }

    @Override
    public boolean onMoveBegin(MoveGestureDetector moveGestureDetector) {
        return true;
    }

    @Override
    public void onMoveEnd(MoveGestureDetector moveGestureDetector) {
        PointF delta = moveGestureDetector.getFocusDelta();
        int deltaX = (int) delta.x;
        int deltaY = (int) delta.y;

        //mCurVideoView.setOffset(-deltaY, -deltaX, true);
        if (null != mCurVideoView && mCurVideoView.isDragable()) {
            mCurVideoView.setPosTop(mCurVideoView.getPosTop() + deltaY);
            mCurVideoView.setPosLeft(mCurVideoView.getPosLeft() + deltaX);
            mCurVideoView.autoLayout();
        }
    }
}
