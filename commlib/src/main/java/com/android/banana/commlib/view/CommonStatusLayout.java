package com.android.banana.commlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;

import com.android.banana.commlib.R;


/**
 * Created by lingjiu on 2017/8/22.
 */

public class CommonStatusLayout extends FrameLayout {
    private ViewFlipper mPages;
    private View statusView;

    public CommonStatusLayout(Context context) {
        this(context, null);
    }

    public CommonStatusLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonStatusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        statusView = layoutInflater.inflate(R.layout.layout_common_status, null);
        mPages = ((ViewFlipper) statusView.findViewById(R.id.viewFlipper));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addView(statusView);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeView(statusView);
    }

    public void hideStatusView() {
        mPages.setVisibility(View.GONE);
    }

    public void showLoading() {
        mPages.bringToFront();
        if (mPages.getVisibility() == View.GONE) mPages.setVisibility(View.VISIBLE);
        mPages.setDisplayedChild(0);
    }

    public void showRetry() {
        mPages.bringToFront();
        if (mPages.getVisibility() == View.GONE) mPages.setVisibility(View.VISIBLE);
        mPages.setDisplayedChild(1);
    }

}
