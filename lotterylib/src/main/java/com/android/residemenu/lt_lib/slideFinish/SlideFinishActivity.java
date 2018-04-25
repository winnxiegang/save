package com.android.residemenu.lt_lib.slideFinish;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.residemenu.lt_lib.R;

import java.lang.reflect.Field;

/**
 * Created by Jim on 15/9/14.
 */
public abstract class SlideFinishActivity extends FragmentActivity {

    private RelativeLayout mLeftView;
    private ImageView mShadow;
    private int mShadowMarginLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setWindowTransparent();
        initSlideFinish();
        super.onCreate(savedInstanceState);
    }

    private void setWindowTransparent() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * initialize
     */
    private void initSlideFinish() {
        PagerEnabledSlidingPaneLayout slidingPaneLayout = new PagerEnabledSlidingPaneLayout(this);
        try {
            /* Change the 'mOverhangSize' to 0 by reflection, because the default value was 32dp. */
            Field overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
            overHang.setAccessible(true);
            overHang.set(slidingPaneLayout, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        slidingPaneLayout.setPanelSlideListener(new MyPanelSlideListener());
        if(!isFadingRightView()) {
            slidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        }

        /* Add a transparent Relative Layout at left */
        mLeftView = new RelativeLayout(this);
        if(isFadingLeftView()) {
            mLeftView.setBackgroundColor(0xcccccccc);
        }
        mLeftView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        /* Add a shadow effect in Relative Layout */
        if(isSupportShadow()) {
            mShadow = new ImageView(this);
            Drawable shadowDrawable = getResources().getDrawable(R.drawable.shadow_left);
            mShadow.setImageDrawable(shadowDrawable);
            mShadow.setScaleType(ImageView.ScaleType.FIT_XY);
            int shadowWidth = DpToPx(this, 20);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(shadowWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            int ms = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            mShadow.setLayoutParams(rlp);
            mShadow.measure(ms, ms);
            rlp.leftMargin = -shadowWidth;
            mShadowMarginLeft = -shadowWidth;
            mLeftView.addView(mShadow, rlp);
        }
        slidingPaneLayout.addView(mLeftView, 0);

        /* Add the decor view to the right of SlidingPaneLayout */
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundColor(getResources().getColor(android.R.color.white));
        decor.removeView(decorChild);
        decor.addView(slidingPaneLayout);
        slidingPaneLayout.addView(decorChild, 1);
    }

    private class MyPanelSlideListener extends SlidingPaneLayout.SimplePanelSlideListener {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            if(isFadingLeftView()) {
                mLeftView.setAlpha(1 - slideOffset);
            }
            if(isSupportShadow()) {
                WindowManager mWindowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
                int mScreenW = mWindowManager.getDefaultDisplay().getWidth();
                int distance = (int) (mScreenW * slideOffset);
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mShadow.getLayoutParams();
                rlp.leftMargin = mShadowMarginLeft + distance;
                mShadow.setLayoutParams(rlp);
                if(slideOffset > 0.5) {
                    mShadow.setAlpha(2 - slideOffset * 2);
                }
            }
        }

        @Override
        public void onPanelOpened(View view) {
            finish();
        }
    }

    /**
     * Is this activity support fading the left view.
     * @return
     */
    protected boolean isFadingLeftView(){
        return false;
    }

    /**
     * Is this activity support fading the right view.
     * @return
     */
    protected boolean isFadingRightView(){
        return false;
    }

    /**
     * Is this activity support adding the shadow.
     * @return
     */
    protected boolean isSupportShadow(){
        return true;
    }

    private int DpToPx(Context context, float x) {
        int result = 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        result = (int) (x * scale + 0.5f);
        return result;
    }
}
