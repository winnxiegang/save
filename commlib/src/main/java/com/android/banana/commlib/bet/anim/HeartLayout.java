/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.banana.commlib.bet.anim;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.banana.commlib.R;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;


/**
 * 飘心动画
 */
public class HeartLayout extends RelativeLayout {

    private AbstractPathAnimator mAnimator;
    private AttributeSet attrs = null;
    private int defStyleAttr = 0;
    private OnHearLayoutListener onHearLayoutListener;

    public void setOnHearLayoutListener(OnHearLayoutListener onHearLayoutListener) {
        this.onHearLayoutListener = onHearLayoutListener;
    }

    public interface OnHearLayoutListener {
        boolean onAddFavor();
    }

    public HeartLayout(Context context) {
        super(context);
    }

    public HeartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
    }

    public HeartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
    }

    private int mWidth;
    private int initX;

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private void init(View anchor, AttributeSet attrs, int defStyleAttr, boolean isVertical) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HeartLayout, defStyleAttr, 0);
        int pointx;
        if (isVertical) {
            pointx = initX;
        } else {
            pointx = initX + 1500;
        }
        LogUtils.e("HeartLayout", "initX=" + initX + "  ");
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        mAnimator = new PathAnimator(AbstractPathAnimator.Config.fromTypeArray(a, x, LibAppUtil.getScreenHeight(getContext()) - y, pointx, anchor.getWidth(), anchor.getHeight()));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取本身的宽高 这里要注意,测量之后才有宽高
        mWidth = getMeasuredWidth();
        initX = mWidth / 2;

    }

    public AbstractPathAnimator getAnimator() {
        return mAnimator;
    }

    public void setAnimator(AbstractPathAnimator animator) {
        clearAnimation();
        mAnimator = animator;
    }

    public void clearAnimation() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
        }
        removeAllViews();
    }

    public void addFavor(View anchor, String url, boolean isVertical) {
        HeartView heartView = new HeartView(getContext());
        heartView.setUrl(url);
        // heartView.setDrawable(drawableIds[random.nextInt(8)]);
        init(anchor, attrs, defStyleAttr, isVertical);
        mAnimator.start(heartView, this);

    }
}
