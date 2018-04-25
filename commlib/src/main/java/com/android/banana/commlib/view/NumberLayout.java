package com.android.banana.commlib.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 连续+1,+1动画
 * <p>
 * Created by lingjiu on 2017/11/11.
 */

public class NumberLayout extends RelativeLayout {

    private int mHeight;

    public NumberLayout(Context context) {
        this(context, null);
    }

    public NumberLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initAnim(final View view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0, -mHeight);
        set.playTogether(alpha, translationY);
        set.setDuration(1000);
        set.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
    }

    public void setText(CharSequence text) {
        TextView textView = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textView.setLayoutParams(params);
        textView.setText(text);
        addView(textView, params);
        initAnim(textView);
    }
}
