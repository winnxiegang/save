package com.android.banana.commlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by zhouyi on 2016/6/30 18:01.
 */
public class TouchyGridView extends GridView{

    // Depending on how you're creating this View,
    // you might need to specify additional constructors.
    public TouchyGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private OnNoItemClickListener listener;
    public interface OnNoItemClickListener
    {
        public void onNoItemClick();
    }

    public void setOnNoItemClickListener(OnNoItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        // The pointToPosition() method returns -1 if the touch event
        // occurs outside of a child View.
        // Change the MotionEvent action as needed. Here we use ACTION_DOWN
        // as a simple, naive indication of a click.
        if (pointToPosition((int) event.getX(), (int) event.getY()) == -1
                && event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (listener != null)
            {
                listener.onNoItemClick();
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
