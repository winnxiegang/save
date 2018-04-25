package com.android.xjq.view.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.android.library.Utils.LogUtils;

/**
 * Created by lingjiu on 2017/7/5.
 */

public class MyClickRecyclerView extends RecyclerView {
    private GestureDetector detector;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClickListener();
    }

    public MyClickRecyclerView(Context context) {
        super(context);
    }

    public MyClickRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        FlingListener flingListener = new FlingListener();
        detector = new GestureDetector(flingListener);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        LogUtils.e("onSingleTapUp","onInterceptTouchEvent");
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        LogUtils.e("onSingleTapUp","onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    class FlingListener extends GestureDetector.SimpleOnGestureListener {

        public boolean onSingleTapConfirmed(MotionEvent e) {
            LogUtils.e("onSingleTapUp","onSingleTapConfirmed");
            if (onClickListener!=null) {
                onClickListener.onClickListener();
            }

            return false;
        }

    }
}
