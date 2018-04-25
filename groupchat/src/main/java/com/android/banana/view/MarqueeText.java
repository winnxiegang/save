package com.android.banana.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import com.android.banana.R;
import com.android.library.Utils.LibAppUtil;


/**
 * Created by qiaomu on 2017/6/5.
 */


public class MarqueeText extends android.support.v7.widget.AppCompatTextView {
    private float marqueeTextHeight = 25;

    public MarqueeText(Context con) {
        this(con, null);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        marqueeTextHeight = LibAppUtil.dip2px(context, marqueeTextHeight);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeText);
        marqueeTextHeight = typedArray.getDimension(R.styleable.MarqueeText_real_height, marqueeTextHeight);

        Log.e("MarqueeText: ", marqueeTextHeight+"-");
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        // super.onFocusChanged(focused,direction,previouslyFocusedRect);
    }

    public void setMarqueeText(String marqueeText) {
        if (!TextUtils.isEmpty(marqueeText)) {//公告
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = (int) marqueeTextHeight;
            setLayoutParams(params);
            setText(marqueeText);
            setFocusable(true);
            requestFocus();
            setSelected(true);
        } else {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = 1;
            setLayoutParams(params);
        }
    }
}
