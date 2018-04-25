package com.android.banana.commlib.liveScore.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by mrs on 2017/4/1.
 */

public class LinearLayoutParent extends LinearLayout {
    onSizeChangedListener listener;
    onTapListener ontouchListener;

    public LinearLayoutParent(Context context) {
        this(context, null);
    }

    public LinearLayoutParent(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayoutParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /*所有手势ACTION_DOWN ACTION_UP   动作都会先经过这里    ACTION_MOVE不一定*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mListener != null)
            mListener.onTouch(ev);

        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            if (ontouchListener != null)
                ontouchListener.onTouch(ev.getRawX(), ev.getRawY());
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh != 0) {
            if (listener != null)
                listener.onSizeChanged(h < oldh, Math.abs(oldh - h));
        }
    }

    public interface onSizeChangedListener {
        void onSizeChanged(boolean kbOpened, int kbHeight);
    }

    public interface onTapListener {
        void onTouch(float x, float y);
    }

    public void setonTapListener(onTapListener listener1) {
        this.ontouchListener = listener1;
    }

    public void setOnSizeChangedListener(onSizeChangedListener listener1) {
        this.listener = listener1;
    }

    private ondispatchtouchEventListener mListener;

    public void setOndispatchtouchEventListener(ondispatchtouchEventListener listener1) {
        this.mListener = listener1;
    }

    public interface ondispatchtouchEventListener {
        void onTouch(MotionEvent event);
    }
}
