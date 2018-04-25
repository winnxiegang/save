package com.android.xjq.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;

import com.android.xjq.R;

/**
 * Created by lingjiu on 2017/10/11.
 */

public class ImageViewWithRedPoint extends ImageView {
    private Paint mPaint;
    private int mRadius;
    private boolean mShowRedPoint;
    private int mGravity;

    public ImageViewWithRedPoint(Context context) {
        super(context);
        init(context);
    }

    public ImageViewWithRedPoint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageViewWithRedPoint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.RED);
        mRadius = context.getResources().getDimensionPixelSize(R.dimen.red_point_radius_small);//小红点半径
        mGravity = Gravity.RIGHT | Gravity.TOP;
    }

    public void setShowRedPoint(boolean showRedPoint) {
        mShowRedPoint = showRedPoint;
        invalidate();
    }

    public boolean isShowRedPoint() {
        return mShowRedPoint;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mShowRedPoint) {
            // 获取原图标的右上角坐标
            int cx = getWidth();
            int cy = getHeight();
            // 计算我们的小红点的坐标
            if ((Gravity.LEFT & mGravity) == Gravity.LEFT) {
                cx = mRadius;
            } else if ((Gravity.RIGHT & mGravity) == Gravity.RIGHT) {
                cx -= mRadius;
            }
            if ((Gravity.TOP & mGravity) == Gravity.TOP) {
                cy = mRadius;
            } else if ((Gravity.BOTTOM & mGravity) == Gravity.BOTTOM) {
                cy -= mRadius;
            }
            canvas.drawCircle(cx, cy, mRadius, mPaint);//绘制小红点
        }
    }
}
