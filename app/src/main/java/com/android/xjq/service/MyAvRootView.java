package com.android.xjq.service;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tencent.ilivesdk.view.AVRootView;

/**
 * Created by lingjiu on 2017/3/9.
 */

public class MyAvRootView extends AVRootView {

    public MyAvRootView(Context context) {
        super(context);
    }

    public MyAvRootView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return false;
    }


}
