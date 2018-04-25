package com.android.xjq.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.android.xjq.R;

/**
 * Created by zhouyi on 2018/4/19.
 */

public class RoundRectLayout extends FrameLayout {

    public RoundRectLayout(@NonNull Context context) {
        super(context);
    }

    public RoundRectLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundRectLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RoundRectLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setBackground(getResources().getDrawable(R.drawable.shape_grey_radius_bg_dp20));
        View view = new View(getContext());
        LayoutParams llps = new LayoutParams(40, 20);
        view.setLayoutParams(llps);
        view.setBackground(getResources().getDrawable(R.drawable.shape_white_radius_bg_dp20));
        addView(view);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }
}
