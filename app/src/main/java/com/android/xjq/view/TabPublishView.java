package com.android.xjq.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.xjq.R;
import com.android.xjq.utils.live.BitmapBlurHelper;

/**
 * Created by qiaomu on 2018/1/29.
 */

public class TabPublishView extends FrameLayout implements View.OnClickListener {
    public static final int CLICK_INDEX_SUBJECT = 0;
    public static final int CLICK_INDEX_SPEAK = 1;

    private FrameLayout shareLayout;
    private ImageView shareIv;
    private View qqView, wxView;

    private boolean isShown, isAnimating;
    private int translateX, translateY;
    private int duration = 250;

    private Activity mActivity;
    private OnClickListener mListener;

    public TabPublishView(@NonNull Activity activity) {
        super(activity);
        mActivity = activity;
        LayoutInflater.from(activity).inflate(R.layout.tab_publish_view, this, true);
        initView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP)
            startAnim();
        return true;
    }

    private void initView() {
        shareIv = (ImageView) findViewById(R.id.share_iv);
        shareLayout = (FrameLayout) findViewById(R.id.share_layout);
        qqView = findViewById(R.id.layout_qq);
        wxView = findViewById(R.id.layout_wx);
        shareIv.setOnClickListener(this);

        qqView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    v.setTag(CLICK_INDEX_SUBJECT);
                    mListener.onClick(v);
                    hidePublishView();
                }

            }
        });
        wxView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    v.setTag(CLICK_INDEX_SPEAK);
                    mListener.onClick(v);
                    hidePublishView();
                }

            }
        });

        translateX = translateY = dip2px(80);
    }


    @Override
    public void onClick(View v) {
        if (isShown) {
            hidePublishView();
        } else {
            showPublishView();
        }
    }

    private void startAnim() {
        if (isAnimating)
            return;

        isAnimating = true;
        float viewTranslateX = isShown ? 0 : -translateX;
        float viewTranslateY = isShown ? 0 : -translateY;
        float scale = isShown ? 0f : 1f;
        float alpha = isShown ? 0.1f : 1f;

        qqView.setVisibility(View.VISIBLE);
        wxView.setVisibility(View.VISIBLE);
        shareLayout.animate().alpha(isShown ? 0f : 0.7f).setDuration(duration).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        qqView.animate().translationX(viewTranslateX).translationY(viewTranslateY).scaleX(scale).scaleY(scale).alpha(alpha).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();
        wxView.animate().translationX(-viewTranslateX).translationY(viewTranslateY).scaleX(scale).scaleY(scale).alpha(alpha).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();
        shareIv.animate().alpha(0f).setDuration(duration / 2).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                shareIv.setImageResource(isShown ? R.drawable.ic_close_x : R.drawable.ic_close_x);
                shareIv.animate().alpha(1f).setDuration(duration / 2).setListener(null).start();
            }
        });

        wxView.animate().setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                qqView.setVisibility(isShown ? GONE : VISIBLE);
                wxView.setVisibility(isShown ? GONE : VISIBLE);
                TabPublishView.this.setVisibility(isShown ? GONE : VISIBLE);
                isShown = !isShown;
                isAnimating = false;
            }
        });
    }

    public void showPublishView() {
        FrameLayout decorView = (FrameLayout) mActivity.getWindow().getDecorView();

        if (this.getParent() == null) {
            FrameLayout.LayoutParams layoutParams = (LayoutParams) shareLayout.getLayoutParams();
            layoutParams.bottomMargin = getVirtualButtonHeight();
            shareLayout.setLayoutParams(layoutParams);
            decorView.addView(this);
          /*  FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
            Log.e("showPublishView: ", getVirtualButtonHeight() + "--");
            params.bottomMargin = getVirtualButtonHeight();*/
        }

        TabPublishView.this.setVisibility(VISIBLE);
        setBackground(new BitmapDrawable(getViewBitmap(decorView)));

        startAnim();
    }

    //获取虚拟导航栏的高度
    private int getVirtualButtonHeight() {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);

        return 0;
    }


    private Bitmap getViewBitmap(ViewGroup decorView) {
        View childAt = decorView.getChildAt(0);
        //childAt.destroyDrawingCache();
        childAt.setDrawingCacheEnabled(true);
        childAt.buildDrawingCache();
        Bitmap drawingCache = childAt.getDrawingCache();

        Log.e("getViewBitmap: ", drawingCache.getWidth() + "--" + drawingCache.getHeight());
        Bitmap bitmap = Bitmap.createScaledBitmap(drawingCache, decorView.getWidth(), decorView.getHeight(), true);

        bitmap = BitmapBlurHelper.doBlur(mActivity, bitmap, 3);
        childAt.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能

        return bitmap;
    }

    public void hidePublishView() {
        if (this.getParent() != null)
            startAnim();
    }

    public void removePublishView() {
        FrameLayout decorView = (FrameLayout) mActivity.getWindow().getDecorView();
        if (this.getParent() == decorView)
            startAnim();
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }
}
