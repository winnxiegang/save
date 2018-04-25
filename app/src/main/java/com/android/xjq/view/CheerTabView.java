package com.android.xjq.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.android.xjq.R;

/**
 * Created by lingjiu on 2018/2/6.
 */

public class CheerTabView extends View {

    private Paint mPaint;
    private Paint textPaint;

    private Path mPath;

    private int width;
    private int height;

    private Bitmap coinBitmap;

    private String leftText = "0";
    private String rightText = "0";
    private String centText = "-0.5球";
    private int baseline;
    private int bitmapHeight;

    public CheerTabView(Context context) {
        super(context);
        init();
    }

    public CheerTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheerTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //设置助威数据
    public void setCheerInfo(double leftCheerData, double rightCheerData, String plate) {
        leftText = String.valueOf(leftCheerData);
        rightText = String.valueOf(rightCheerData);
        centText = plate;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        textPaint.setTextSize(height / 2);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        // 实例化路径
        mPath = new Path();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coinBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_silver_banana_coin);
        bitmapHeight = coinBitmap.getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.parseColor("#FF5151"));
        baseline = (int) ((getMeasuredHeight() - (textPaint.descent() - textPaint.ascent())) / 2 - textPaint.ascent());
        int centerTextWidth = (int) textPaint.measureText(centText);
        //绘制中间文字
        textPaint.setColor(Color.parseColor("#4BA339"));
        canvas.drawText(centText, width / 2 - centerTextWidth / 2, baseline, textPaint);
        // 绘制左半边
        mPath.moveTo((float) (height * Math.tan(Math.PI / 6)), 0);
        mPath.lineTo(0, height);
        int rightRectIndex = width / 2 - centerTextWidth / 3;
        mPath.lineTo(rightRectIndex, height);
        mPath.lineTo(rightRectIndex - (float) (height * Math.tan(Math.PI * 0.2)) / 2, height / 2);
        mPath.lineTo(rightRectIndex, 0);
        // 闭合曲线
        mPath.close();
        // 绘制路径
        canvas.drawPath(mPath, mPaint);

        mPath.reset();

        // 绘制右半边
        mPaint.setColor(Color.parseColor("#0389F0"));
        mPath.moveTo(width - (float) (height * Math.tan(Math.PI / 6)), 0);
        mPath.lineTo(width, height);
        int leftRectIndex = width / 2 + centerTextWidth / 3;
        mPath.lineTo(leftRectIndex, height);
        mPath.lineTo(leftRectIndex + (float) (height * Math.tan(Math.PI * 0.2)) / 2, height / 2);
        mPath.lineTo(leftRectIndex, 0);
        // 闭合曲线
        mPath.close();
        // 绘制路径
        canvas.drawPath(mPath, mPaint);

        int leftTextWidth = (int) textPaint.measureText(leftText);
        int rightTextWidth = (int) textPaint.measureText(rightText);
        textPaint.setColor(Color.WHITE);
        canvas.drawText(leftText, rightRectIndex / 2 - leftTextWidth / 2, baseline, textPaint);
        canvas.drawText(rightText, (width - leftRectIndex) / 2 + leftRectIndex - rightTextWidth / 2, baseline, textPaint);
        canvas.drawBitmap(coinBitmap, rightRectIndex / 2 + leftTextWidth / 2, height / 2 - bitmapHeight / 2, textPaint);
        canvas.drawBitmap(coinBitmap, (width - leftRectIndex) / 2 + leftRectIndex + rightTextWidth / 2, height / 2 - bitmapHeight / 2, textPaint);


    }
}
