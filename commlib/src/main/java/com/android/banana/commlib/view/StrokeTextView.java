package com.android.banana.commlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Created by qiaomu on 2018/3/9.
 */

public class StrokeTextView extends android.support.v7.widget.AppCompatTextView {
    private TextPaint strokePaint;
    private int mStrokeWidth = 0;
    private int mStrokeColor = -1;

    public StrokeTextView(Context context) {
        this(context, null);
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        strokePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        TextPaint paint = getPaint();
        strokePaint.setTextSize(paint.getTextSize());
        strokePaint.setTypeface(paint.getTypeface());
        strokePaint.setFlags(paint.getFlags());
        strokePaint.setAlpha(paint.getAlpha());
        strokePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    public void onDraw(Canvas canvas) {


        // 复制原来TextViewg画笔中的一些参数
        // 自定义描边效果
        if (mStrokeColor != -1) {
            strokePaint.setColor(mStrokeColor);
            strokePaint.setStrokeWidth(mStrokeWidth);
            String text = getText().toString();
            //在文本底层画出带描边的文本
            canvas.drawText(text, (getWidth() - strokePaint.measureText(text)) / 2, getBaseline(), strokePaint);
        }
        super.onDraw(canvas);
    }

    public void setStrokeColor(int strokeColor) {

        mStrokeColor = strokeColor;
    }

    public void setStrokeWidth(int strokeWidth) {

        mStrokeWidth = strokeWidth;
    }
}
