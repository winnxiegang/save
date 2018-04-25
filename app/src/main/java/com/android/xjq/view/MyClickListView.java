package com.android.xjq.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

import com.android.library.Utils.LogUtils;

/**
 * Created by lingjiu on 2017/4/13.
 *
 * 可以设置listView整体的点击事件
 *
 */

public class MyClickListView extends ListView {

    private GestureDetector detector;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClickListener();
    }

    public MyClickListView(Context context) {
        super(context);
    }

    public MyClickListView(Context context, AttributeSet attrs) {
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

    class FlingListener extends GestureDetector.SimpleOnGestureListener {

        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (onClickListener!=null) {
                onClickListener.onClickListener();
            }

            return false;
        }

    }


}
