package com.android.banana.commlib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by zhouyi on 2016/1/3 14:54.
 */
public class MyScrollView extends ScrollView {

    private OnMyScrollChangeListener listener;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        listener.onScrollChange(l, t, oldl, oldt);
    }


    public void setOnMyScrollChangeListener(OnMyScrollChangeListener listener) {
        this.listener = listener;
    }


    public static interface OnMyScrollChangeListener {
        public void onScrollChange(int l, int t, int oldl, int oldt);
    }
}
