package com.android.banana.commlib.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by qiaomu on 2018/2/5.
 * 勋章 标签 都可以用
 * <p>
 * 默认是单选中，需要给定状态之间的图片，selector
 */

public class MedalLayout extends LinearLayout {

    private Context context;

    private int width;

    public MedalLayout(Context context) {
        this(context, null);
    }

    public MedalLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MedalLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void addMedal(@DrawableRes int resId) {
        if (resId <= 0)
            return;
        ImageView medalView = new ImageView(getContext());

        LinearLayout.LayoutParams lp = new LayoutParams(-2, dp2Px(20));
        lp.leftMargin = getChildCount() > 0 ? dp2Px(5) : 0;
        lp.gravity = Gravity.CENTER_VERTICAL;

        if (resId > 0)
            medalView.setImageDrawable(ContextCompat.getDrawable(getContext(), resId));

        medalView.setLayoutParams(lp);
        addView(medalView);

        width += lp.leftMargin;
        int w = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
        medalView.measure(w, h);
        width += medalView.getMeasuredWidth();
    }

    public void addMedal(@DrawableRes int resId, final OnClickListener clickListener) {
        ImageView medalView = new ImageView(getContext());

        LinearLayout.LayoutParams lp = new LayoutParams(-2, -2);
        lp.leftMargin = getChildCount() > 0 ? dp2Px(5) : 0;
        if (resId > 0)
            medalView.setImageDrawable(ContextCompat.getDrawable(getContext(), resId));

        medalView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(v);
                }
            }
        });
        addView(medalView, lp);

    }

    public int getFinalWidth() {
        return width;
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        width = 0;
    }

    private int dp2Px(int dpValue) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return (int) (metrics.density * dpValue + 0.5f);
    }
}
