package com.android.banana.commlib.liveScore.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.android.banana.commlib.R;

/**
 * Created by qiaomu on 2017/8/2.
 */

public class AnimView extends FrameLayout {
    public final String TAG = getClass().toString();
    private Bitmap mCacheBitmap;
    private Canvas mCacheCanvas;//双缓冲
    private int BITMAP_SOURCE_ID = R.drawable.bg_ft_ground;

    private long mAnimationDuration = 3000L;
    private ValueAnimator mValueAnimator;
    private Paint mPathPaint;
    private Paint mDashPaint;
    private Paint mArrowPaint;
    private Paint mShadowPaint;

    private Bitmap original;

    public AnimView(Context context) {
        this(context, null);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }


    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public int dp2px(float dpValue) {
        final float fontScale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * fontScale + 0.5f);
    }

    public int xp2px(float dpValue) {
        return (int) (dpValue * getWidth());
    }

    public int yp2px(float dpValue) {
        return (int) (dpValue * getHeight());
    }

    public Canvas getCanvas() {
        return mCacheCanvas;
    }

    public ValueAnimator getValueAnimator() {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofFloat(0, 1);
            mValueAnimator.setDuration(mAnimationDuration);
        } else {
            mValueAnimator = mValueAnimator.clone();
        }
        return mValueAnimator;
    }

    float lastAnimatedFraction = 0F;

    public void startValueAnimation(final ValueAnimator.AnimatorUpdateListener updateListener) {
        lastAnimatedFraction = 0F;
        mValueAnimator.removeAllUpdateListeners();
        mValueAnimator.removeAllListeners();
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animatedFraction = animation.getAnimatedFraction();
                if (lastAnimatedFraction == animatedFraction) return;
                lastAnimatedFraction = animatedFraction;
                if (updateListener != null)
                    updateListener.onAnimationUpdate(animation);

            }
        });
        mValueAnimator.start();
    }

    public long getAnimationDuration() {
        return mAnimationDuration;
    }

    public void clearAnimation() {
        if (mValueAnimator != null && mValueAnimator.isRunning())
            mValueAnimator.cancel();
    }


    public Shader makeLinearGradientShader(float startx0, float starty0, float endx1, float endy1, float[] positions,
                                           int... colors) {
        Shader shader = new LinearGradient(startx0, starty0,
                endx1, endy1,
                colors, positions,
                Shader.TileMode.CLAMP);
        return shader;
    }

    public Paint getPaint() {
        if (mPathPaint == null) {
            mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        }
        return mPathPaint;
    }

    public Paint getDashPaint() {
        if (mDashPaint == null)
            mDashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (mDashPaint.getPathEffect() == null) {
            mDashPaint.setStyle(Paint.Style.STROKE);
            mDashPaint.setStrokeWidth(3);
            PathEffect effects = new DashPathEffect(new float[]{8, 10, 8, 10}, 1);
            mDashPaint.setColor(Color.YELLOW);
            mDashPaint.setPathEffect(effects);
        }
        return mDashPaint;
    }


    public Paint getArrowPaint() {
        if (mArrowPaint == null) {
            mArrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mArrowPaint.setStyle(Paint.Style.FILL);
            mArrowPaint.setColor(Color.YELLOW);
        }
        return mArrowPaint;
    }

    public Paint getShadowPaint() {
        if (mShadowPaint == null) {
            mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mShadowPaint.setStyle(Paint.Style.FILL);
            mShadowPaint.setColor(ContextCompat.getColor(getContext(), R.color.half_transparent_4statusbar));
        }

        return mShadowPaint;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null) {
            mValueAnimator.removeAllUpdateListeners();
            mValueAnimator.removeAllListeners();
            mValueAnimator.cancel();
        }
    }
}


