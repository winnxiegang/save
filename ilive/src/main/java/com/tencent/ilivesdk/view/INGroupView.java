package com.tencent.ilivesdk.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.tencent.av.opengl.ui.GLView;
import com.tencent.av.opengl.ui.GLViewGroup;

/**
 * 视频显示控件组[分开显示]
 */
public class INGroupView extends GLViewGroup implements GLView.OnTouchListener{
    private final static String TAG = "ILVB-INGroupView";

    private boolean bInitEvent = false;
    private GestureDetector mGestureDetector = null;    // 点击事件
    private GestureDetector.SimpleOnGestureListener gestureListener = null;

    class InnerGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            gestureListener.onSingleTapConfirmed(event);
            return true;
        }


        @Override
        public boolean onDoubleTap(MotionEvent event) {
            gestureListener.onDoubleTap(event);
            return super.onDoubleTap(event);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            gestureListener.onFling(e1, e2, velocityX, velocityY);
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    /**
     * 设置击事件监听
     * @param context
     * @param listener
     */
    public void setGestureListener(Context context, GestureDetector.SimpleOnGestureListener listener){
        if (!bInitEvent){
            mGestureDetector = new GestureDetector(context, new InnerGestureListener());
            setOnTouchListener(this);
            bInitEvent = true;
        }
        gestureListener = listener;
    }

    @Override
    public boolean onTouch(GLView glView, MotionEvent motionEvent) {
        if (null != mGestureDetector){
            mGestureDetector.onTouchEvent(motionEvent);
        }
        return false;
    }
}
