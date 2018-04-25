package com.android.banana.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by qiaomu on 2017/7/5.
 * <p>
 * <p>
 * 1.
 * ---宽高貌似可以直接使用Wrap_content,match_parent,和固定大小,反正最终大小都是会根据文字大小而定。
 * <p>
 * <p>
 * 2.当在布局种设定 BadgeView宽高为固定dp时,走{@link BadgeView#drawCircleRect(Canvas, String, boolean)} ()}
 * 有个建议,宽高设定固定值时,{@link BadgeView#mNum}可能处于 5 ，15 ，155不同长度段,你可能需要设定个最大值,来保证显示的完整性
 * <p>
 * <p>
 * 3.当在布局种设定 BadgeView宽高为Wrap_content时,会根据{@link BadgeView#mNum}的大小来动态改变 BadgeView的宽高{@link BadgeView#getWidthMeasureSpec()}
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;绘制走{@link BadgeView#drawWrapContentMode(Canvas)}
 * <p>
 * <p>
 */

public class BadgeView extends AppCompatTextView {
    private Paint mPaint;
    private int mNum = 0;
    private int mRadiu = 8;
    private int mOverNum = 9;
    private int mOverNumPlus = 99;
    private String mOverNumStr = "99+";
    private boolean mExactlyMode = true;

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(getTextSize());
        mRadiu = dip2px(mRadiu);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {//wrap_content
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(2 * mRadiu, MeasureSpec.EXACTLY);
            widthMeasureSpec = getWidthMeasureSpec();
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
            mExactlyMode = false;
        }

    }


    private int getWidthMeasureSpec() {
        int widthMeasureSpec;
        if (mNum > mOverNumPlus) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(4 * mRadiu, MeasureSpec.EXACTLY);
        } else if (mNum > mOverNum) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(3 * mRadiu, MeasureSpec.EXACTLY);
        } else {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(2 * mRadiu, MeasureSpec.EXACTLY);
        }
        return widthMeasureSpec;
    }


    private int getBadgeWidth() {
        if (mNum > mOverNumPlus) {
            return Math.min(4 * mRadiu, getWidth());
        } else if (mNum > mOverNum) {
            return Math.min(3 * mRadiu, getWidth());
        } else {
            return Math.min(2 * mRadiu, getWidth());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mExactlyMode)
            drawExactlyMode(canvas);
        else
            drawWrapContentMode(canvas);

    }

    private void drawExactlyMode(Canvas canvas) {
        if (mNum <= 0)
            return;
        if (mNum > mOverNumPlus) {
            drawCircleRect(canvas, mOverNumStr, false);
        } else if (mNum > mOverNum) {
            drawCircleRect(canvas, String.valueOf(mNum), false);
        } else {
            drawCircleRect(canvas, String.valueOf(mNum), true);
        }
    }

    private void drawCircleRect(Canvas canvas, String drawStr, boolean circle) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float widthStr = mPaint.measureText(drawStr);
        float startX = width - getBadgeWidth();

        //画背景
        mPaint.setColor(Color.RED);
        if (circle) {
            canvas.drawCircle(startX, height / 2, height / 2, mPaint);
        } else {
            RectF rectF = new RectF(startX, dip2px(1), startX + getBadgeWidth(), height - dip2px(1));//-1dp 是因为我觉得高度有点胖
            canvas.drawRoundRect(rectF, mRadiu, mRadiu, mPaint);
        }

        //画文字
        mPaint.setColor(Color.WHITE);
        if (circle) {
            canvas.drawText(drawStr, startX - widthStr / 2, getBaseline(), mPaint);
        } else {
            canvas.drawText(drawStr, startX + getBadgeWidth() / 2 - widthStr / 2, getBaseline(), mPaint);
        }
    }


    private void drawWrapContentMode(Canvas canvas) {
        if (mNum <= 0) return;
        if (mNum > mOverNum) {
            String drawStr = mNum > mOverNumPlus ? mOverNumStr : String.valueOf(mNum);
            float widthStr = mPaint.measureText(drawStr);
            RectF rectF = new RectF(0, 0, getWidth(), getHeight());
            mPaint.setColor(Color.RED);
            canvas.drawRoundRect(rectF, mRadiu, mRadiu, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(drawStr, (getWidth() - widthStr) / 2, getBaseline() - dip2px(1.5f), mPaint);
        } else {
            String drawStr = String.valueOf(mNum);
            float widthStr = mPaint.measureText(drawStr);
            mPaint.setColor(Color.RED);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadiu, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(drawStr, (getWidth() - widthStr) / 2, getBaseline() - dip2px(1.5f), mPaint);
        }
    }

    private int dip2px(float dipValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setBadgeNum(int num) {
        if (num <= 0) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        this.mNum = num;
        requestLayout();
        invalidate();
    }
}
