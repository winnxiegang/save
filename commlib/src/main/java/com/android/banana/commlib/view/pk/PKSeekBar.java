package com.android.banana.commlib.view.pk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.android.banana.commlib.R;

/**
 * Created by ajiao on 2018\3\15 0015.
 */

public class PKSeekBar extends android.support.v7.widget.AppCompatSeekBar {

    public PKSeekBar(Context context) {
        super(context);
        init();
    }


    public PKSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PKSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setMax(100);
        //this.setThumbOffset(dip2px(getContext(), 20));
        this.setBackgroundResource(R.drawable.seek_bar_bg);
        //int padding = dip2px(getContext(),(float)3);
        //this.setPadding(0, padding, 0, padding);
        this.setProgressDrawable(getResources().getDrawable(R.drawable.pk_seek_bar_define_style));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //AnimationDrawable drawable = (AnimationDrawable)this.getThumb();
        //drawable.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
