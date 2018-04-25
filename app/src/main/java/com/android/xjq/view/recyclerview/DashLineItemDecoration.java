package com.android.xjq.view.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 虚线分割线
 * Created by lingjiu on 2018/3/1.
 * <p>
 * onDraw是在itemview绘制之前，onDrawOver是在itemview绘制之后。
 * <p>
 */

public class DashLineItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Path mPath;
    private PathEffect mEffects;

    public DashLineItemDecoration() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPath = new Path();
        //参数1：float[] intervals 代表实需线的宽度(实需实需实需...)
        //参数2：phase 代表左起第一条实线的偏移量(向左偏移)
        mEffects = new DashPathEffect(new float[]{15, 15, 15, 15}, 5);
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            //以下计算主要用来确定绘制的位置
            final int top = child.getBottom() + params.bottomMargin;
            //绘制虚线
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.WHITE);
            mPath.reset();
            mPath.moveTo(left, top);
            mPath.lineTo(right, top);
            mPaint.setPathEffect(mEffects);
            c.drawPath(mPath, mPaint);
        }
    }
}
